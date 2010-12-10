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

package org.gitools.cli.correlation;

import org.kohsuke.args4j.Option;

import edu.upf.bg.progressmonitor.NullProgressMonitor;
import edu.upf.bg.progressmonitor.IProgressMonitor;
import edu.upf.bg.progressmonitor.StreamProgressMonitor;
import edu.upf.bg.tools.ToolDescriptor;
import edu.upf.bg.tools.exception.ToolException;
import edu.upf.bg.tools.exception.ToolValidationException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.gitools.analysis.correlation.CorrelationAnalysis;
import org.gitools.analysis.correlation.CorrelationCommand;
import org.gitools.analysis.correlation.methods.PearsonCorrelationMethod;
import org.gitools.cli.AnalysisArguments;
import org.gitools.cli.AnalysisTool;
import org.gitools.persistence.FileSuffixes;
import org.gitools.persistence.MimeTypes;
import org.gitools.threads.ThreadManager;
import org.kohsuke.args4j.CmdLineParser;

public class CorrelationTool extends AnalysisTool {

	public static class CorrelationArguments extends AnalysisArguments {
		@Option(name = "-df", aliases = "-data-format", metaVar = "<format>",
				usage = "Data file format (MIME type or file extension).")
		public String dataMime;

		@Option(name = "-d", aliases = "-data", metaVar = "<file>",
				usage = "File with data to be processed.")
		public String dataFile;

		@Option(name = "-r", aliases = "-rows",
				usage = "Apply to rows, by default it is applied to columns.")
		public boolean applyToRows = false;

		@Option(name = "-m", aliases = "-method", metaVar = "<method>",
				usage = "Correlation method to use. (default: pearson)")
		public String method = PearsonCorrelationMethod.ID;

		@Option(name = "-M", aliases = "-method-conf", metaVar = "<param=value>",
				usage = "Define a method configuration parameter.\n" +
				" This allows to configure the behaviour of the method.")
		public List<String> methodConf = new ArrayList<String>(0);

		@Option(name = "-ev", aliases = "-empty-values", metaVar = "<value>",
				usage = "Replace empty values by <value>.\n" +
				"If not specified pairs with empty values\n" +
				"will be discarded.")
		public Double replaceValue;

		@Option(name = "-an", aliases = "-attr-name", metaVar = "<name>",
				usage = "Attribute name of the data matrix to use.")
		public String attrName;

		@Option(name = "-ai", aliases = "-attr-index", metaVar = "<index>",
				usage = "Attribute index of the data matrix to use. (default: 0)")
		public int attrIndex = 0;
	}

	protected Properties methodProperties = new Properties();

	@Override
	public void validate(Object argsObject) throws ToolException {
		
		super.validate(argsObject);
		
		CorrelationArguments args = (CorrelationArguments) argsObject;

		if (args.dataFile == null)
        	throw new ToolValidationException("Data file should be specified.");

		if (args.method == null)
        	throw new ToolValidationException("The method should be specified.");

		methodProperties = parseProperties(args.methodConf);
	}
	
	@Override
	public void run(Object argsObject) throws ToolException {
		
		CorrelationArguments args = (CorrelationArguments) argsObject;

		CorrelationAnalysis analysis = new CorrelationAnalysis();
		prepareGeneralAnalysisAttributes(analysis, args);
		analysis.setMethod(args.method);
		analysis.setMethodProperties(methodProperties);
		analysis.setTransposeData(args.applyToRows);
		analysis.setReplaceNanValue(args.replaceValue);

		String dataMime = mimeFromFormat(args.dataMime, args.dataFile, MimeTypes.DOUBLE_MATRIX);

		CorrelationCommand cmd = new CorrelationCommand(
        		analysis,
				dataMime, args.dataFile,
				args.workdir, args.analysisName + "." + FileSuffixes.CORRELATIONS);
        
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

	@Override
	public void printUsage(PrintStream outputStream, String appName, ToolDescriptor toolDesc, CmdLineParser parser) {
		super.printUsage(outputStream, appName, toolDesc, parser);

		outputStream.println();

		printMethods(outputStream);
		outputStream.println();

		printDataFormats(outputStream);
		outputStream.println();
	}

	private void printMethods(PrintStream o) {
		o.println("Available correlation methods:");
		o.println(String.format(LIST_S_FMT, "pearson", "Pearson's correlation"));
		o.println(String.format(LIST_S_FMT, "spearman", "Spearman's rank correlation"));
	}
}