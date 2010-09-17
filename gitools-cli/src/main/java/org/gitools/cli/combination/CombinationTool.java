/*
 *  Copyright 2010 cperez.
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

package org.gitools.cli.combination;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import edu.upf.bg.progressmonitor.NullProgressMonitor;
import edu.upf.bg.progressmonitor.StreamProgressMonitor;
import edu.upf.bg.tools.ToolDescriptor;
import edu.upf.bg.tools.exception.ToolException;
import edu.upf.bg.tools.exception.ToolValidationException;
import java.io.PrintStream;
import org.gitools.analysis.combination.CombinationAnalysis;
import org.gitools.analysis.combination.CombinationCommand;
import org.gitools.cli.AnalysisArguments;
import org.gitools.cli.AnalysisTool;
import org.gitools.persistence.FileSuffixes;
import org.gitools.persistence.MimeTypes;
import org.gitools.threads.ThreadManager;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;


public class CombinationTool extends AnalysisTool {

	public static class CombinationArguments extends AnalysisArguments {
		@Option(name = "-df", aliases = "-data-format", metaVar = "<format>",
				usage = "Data file format (MIME type or file extension).")
		public String dataMime;

		@Option(name = "-d", aliases = "-data", metaVar = "<file>",
				usage = "File with data to be processed.")
		public String dataFile;

		@Option(name = "-cf", aliases = "-columns-format", metaVar = "<format>",
				usage = "Columns file format (MIME type or file extension).")
		public String columnsMime;

		@Option(name = "-c", aliases = "-columns", metaVar = "<file>",
				usage = "File specifying how to group columns to combine.\nAny modules file can be used.\n(default: all columns combined)")
		public String columnsFile;

		@Option(name = "-r", aliases = "-rows",
				usage = "Apply to rows, by default it is applied to columns.")
		public boolean applyToRows = false;

		@Option(name = "-pn", aliases = "-pvalue-name", metaVar = "<name>",
				usage = "Attribute name having the weight.\nUse only one -wn or -wi, but not both.\n(default: use the first one)")
		public String weigthName;

		/*@Option(name = "-pi", aliases = "-pvalue-index", metaVar = "<index>",
				usage = "Attribute index having the pvalue. (default: 0)")
		public int weightIndex = 0;*/

		@Option(name = "-pn", aliases = "-pvalue-name", metaVar = "<name>",
				usage = "Attribute name having the pvalue.\nUse only one -pn or -pi, but not both.\n(default: use the first one)")
		public String pvalueName;

		/*@Option(name = "-pi", aliases = "-pvalue-index", metaVar = "<index>",
				usage = "Attribute index having the pvalue. (default: 0)")
		public int pvalueIndex = 0;*/
	}

	@Override
	public void validate(Object argsObject) throws ToolException {

		super.validate(argsObject);

		CombinationArguments args = (CombinationArguments) argsObject;

		if (args.dataFile == null)
        	throw new ToolValidationException("Data file should be specified.");
	}

	@Override
	public void run(Object argsObject) throws ToolException {

		CombinationArguments args = (CombinationArguments) argsObject;

		CombinationAnalysis analysis = new CombinationAnalysis();
		prepareGeneralAnalysisAttributes(analysis, args);
		analysis.setTransposeData(args.applyToRows);
		analysis.setSizeAttrName(args.weigthName);
		analysis.setPvalueAttrName(args.pvalueName);

		String dataMime = mimeFromFormat(args.dataMime, args.dataFile, MimeTypes.DOUBLE_MATRIX);
		String columnsMime = mimeFromFormat(args.columnsMime, args.columnsFile, MimeTypes.MODULES_2C_MAP);

		CombinationCommand cmd = new CombinationCommand(
        		analysis,
				dataMime, args.dataFile,
				columnsMime, args.columnsFile,
				args.workdir, args.analysisName + "." + FileSuffixes.COMBINATION);

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

		printDataFormats(outputStream);
		outputStream.println();

		printModulesFormats(outputStream);
		outputStream.println();
	}
}
