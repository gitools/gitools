/*
 *  Copyright 2010 chris.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.cli;

import edu.upf.bg.tools.exception.ToolException;
import edu.upf.bg.tools.exception.ToolValidationException;
import edu.upf.bg.tools.impl.AbstractTool;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.gitools.model.Analysis;
import org.gitools.model.Attribute;
import org.gitools.model.KeyValue;
import org.gitools.persistence.FileFormat;
import org.gitools.persistence.FileFormats;
import org.gitools.persistence.PersistenceManager;

public class AnalysisTool extends AbstractTool {

	protected static final String LIST_S_FMT = "\t* %-16s%s";
	protected static final String LIST_L_FMT = "\t* %-48s%s";

	protected List<Attribute> analysisAttributes = new ArrayList<Attribute>(0);
	
	@Override
	public void validate(Object argsObject) throws ToolException {
		super.validate(argsObject);

		AnalysisArguments args = (AnalysisArguments) argsObject;

		if (args.analysisTitle == null)
			args.analysisTitle = args.analysisName;

		for (String attr : args.analysisAttributes) {
			final String[] a = attr.split("=", 2);
			if (a.length != 2)
				throw new ToolValidationException("Malformed analysis attribute: " + attr);
			analysisAttributes.add(new Attribute(a[0], a[1]));
		}
	}

	protected String mimeFromFormat(String format, String fileName, String defaultMime) {
		String mime = null;
		if (fileName == null)
			return null;
		
		if (format != null) {
			// Try with file extension first
			mime = PersistenceManager.getDefault().getMimeFromFile("fake." + format);
			if (mime == null)
				mime = format; // it should be mime type then
			//TODO check valid mime
		}
		else
			mime = PersistenceManager.getDefault().getMimeFromFile(fileName);

		return mime != null ? mime : defaultMime;
	}

	protected List<KeyValue> parseConfiguration(List<String> config) throws ToolValidationException {
		List<KeyValue> kv = new ArrayList<KeyValue>(config.size());
		for (String conf : config) {
			final String[] c = conf.split("=", 2);
			if (c.length != 2)
				throw new ToolValidationException("Malformed configuration parameter: " + conf);
			kv.add(new KeyValue(c[0], c[1]));
		}
		return kv;
	}

	protected Properties parseProperties(List<String> config) throws ToolValidationException {
		Properties properties = new Properties();
		for (String conf : config) {
			final String[] c = conf.split("=", 2);
			if (c.length != 2)
				throw new ToolValidationException("Malformed configuration parameter: " + conf);
			properties.setProperty(c[0], c[1]);
		}
		return properties;
	}

	protected void prepareGeneralAnalysisAttributes(Analysis analysis, AnalysisArguments args) {
		analysis.setTitle(args.analysisTitle);
		analysis.setDescription(args.analysisNotes);
		analysis.setAttributes(analysisAttributes);
	}

	protected void printDataFormats(PrintStream out) {
		out.println("Supported data formats:");
		FileFormat[] formats = new FileFormat[] {
			FileFormats.DOUBLE_MATRIX,
			FileFormats.DOUBLE_BINARY_MATRIX,
			FileFormats.GENE_MATRIX,
			FileFormats.GENE_MATRIX_TRANSPOSED,
			FileFormats.MODULES_2C_MAP
		};

		for (FileFormat f : formats)
			out.println(String.format(LIST_L_FMT, f.getExtension() + " (" + f.getMime() + ")", f.getTitle()));
	}

	protected void printModulesFormats(PrintStream out) {
		out.println("Supported modules formats:");
		FileFormat[] formats = new FileFormat[] {
			FileFormats.GENE_MATRIX,
			FileFormats.GENE_MATRIX_TRANSPOSED,
			FileFormats.MODULES_2C_MAP,
			FileFormats.DOUBLE_MATRIX,
			FileFormats.DOUBLE_BINARY_MATRIX
		};

		for (FileFormat f : formats)
			out.println(String.format(LIST_L_FMT, f.getExtension() + " (" + f.getMime() + ")", f.getTitle()));
	}
}
