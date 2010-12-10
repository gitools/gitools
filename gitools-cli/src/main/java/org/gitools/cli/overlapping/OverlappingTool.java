/*
 *  Copyright 2010 Universitat Pompeu Fabra.
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

package org.gitools.cli.overlapping;

import edu.upf.bg.cutoffcmp.CutoffCmp;
import edu.upf.bg.progressmonitor.IProgressMonitor;
import edu.upf.bg.progressmonitor.NullProgressMonitor;
import edu.upf.bg.progressmonitor.StreamProgressMonitor;
import edu.upf.bg.tools.exception.ToolException;
import edu.upf.bg.tools.exception.ToolValidationException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.gitools.analysis.overlapping.OverlappingAnalysis;
import org.gitools.analysis.overlapping.OverlappingCommand;
import org.gitools.cli.AnalysisArguments;
import org.gitools.cli.AnalysisTool;
import org.gitools.model.ResourceRef;
import org.gitools.persistence.FileSuffixes;
import org.gitools.persistence.MimeTypes;
import org.gitools.threads.ThreadManager;
import org.kohsuke.args4j.Option;

public class OverlappingTool extends AnalysisTool {

	public static class OverlappingArguments extends AnalysisArguments {
		@Option(name = "-df", aliases = "-data-format", metaVar = "<format>",
				usage = "Data file format (MIME type or file extension).")
		public String dataMime;

		@Option(name = "-d", aliases = "-data", metaVar = "<file>",
				usage = "File with data to be processed.")
		public String dataFile;

		@Option(name = "-r", aliases = "-rows",
				usage = "Apply to rows, by default it is applied to columns.")
		public boolean applyToRows = false;

		@Option(name = "-ev", aliases = "-empty-values", metaVar = "<value>",
				usage = "Replace empty values by <value>.\n" +
				"If not specified empty values will be discarded.")
		public Double replaceValue;

		@Option(name = "-an", aliases = "-attr-name", metaVar = "<name>",
				usage = "Attribute name of the data matrix to use.")
		public String attrName;

		@Option(name = "-ai", aliases = "-attr-index", metaVar = "<index>",
				usage = "Attribute index of the data matrix to use. (default: 0)")
		public int attrIndex = 0;

		@Option(name = "-b", aliases = "-bin-cutoff-filt",
				usage = "Binary cutoff filter. Available conditions:\n" +
						"lt (less than), le (less equal than),\n" +
						"eq (equal), ne (not equal),\n" +
						"gt (greatar than), ge (greater equal than)," +
						"alt (abs less than), ale (abs less equal than),\n" +
						"aeq (abs equal), ane (abs not equal),\n" +
						"agt (abs greatar than), age (abs greater equal than)",
				metaVar = "<CONDITION,CUTOFF>")
		public String binCutoff;
	}

	protected boolean binaryCutoffEnabled = false;
	protected CutoffCmp binaryCutoffCmp;
	protected double binaryCutoffValue;

	@Override
	public void validate(Object argsObject) throws ToolException {

		super.validate(argsObject);

		OverlappingArguments args = (OverlappingArguments) argsObject;

		if (args.dataFile == null)
        	throw new ToolValidationException("Data file should be specified.");

		if (args.binCutoff != null) {
			Pattern pat = Pattern.compile("^([a-zA-Z]+),(.+)$");
			args.binCutoff = args.binCutoff.toLowerCase();
			Matcher mat = pat.matcher(args.binCutoff);
			if (!mat.matches())
				throw new ToolValidationException("Invalid parameters for binary cutoff filter: " + args.binCutoff);

			try {
				binaryCutoffEnabled = true;
				binaryCutoffCmp = CutoffCmp.getFromName(mat.group(1));
				if (binaryCutoffCmp == null)
					throw new ToolException("Invalid condition: " + mat.group(1));
				binaryCutoffValue = Double.parseDouble(mat.group(2));
			}
			catch (NumberFormatException e) {
				throw new ToolValidationException("Invalid cutoff: " + mat.group(2));
			}
		}
	}

	@Override
	public void run(Object argsObject) throws ToolException {
		OverlappingArguments args = (OverlappingArguments) argsObject;

		String dataMime = mimeFromFormat(args.dataMime, args.dataFile, MimeTypes.DOUBLE_MATRIX);

		OverlappingAnalysis analysis = new OverlappingAnalysis();
		prepareGeneralAnalysisAttributes(analysis, args);
		analysis.setTransposeData(args.applyToRows);
		analysis.setReplaceNanValue(args.replaceValue);
		analysis.setSourceDataResource(new ResourceRef(dataMime, args.dataFile));
		analysis.setBinaryCutoffEnabled(binaryCutoffEnabled);
		analysis.setBinaryCutoffCmp(binaryCutoffCmp);
		analysis.setBinaryCutoffValue(binaryCutoffValue);

		OverlappingCommand cmd = new OverlappingCommand(
        		analysis,
				args.workdir, args.analysisName + "." + FileSuffixes.OVERLAPPING);

        IProgressMonitor monitor = !args.quiet ?
			new StreamProgressMonitor(System.out, args.verbose, args.debug)
			: new NullProgressMonitor();

		ThreadManager.setNumThreads(args.maxProcs);

        try {
			cmd.run(monitor);
		} catch (Exception e) {
			throw new ToolException(e);
		}
		finally {
			ThreadManager.shutdown(monitor);
		}
	}
}
