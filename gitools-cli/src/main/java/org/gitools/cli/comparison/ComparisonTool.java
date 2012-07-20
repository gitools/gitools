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

package org.gitools.cli.comparison;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import edu.upf.bg.progressmonitor.NullProgressMonitor;
import edu.upf.bg.progressmonitor.StreamProgressMonitor;
import edu.upf.bg.tools.ToolDescriptor;
import edu.upf.bg.tools.exception.ToolException;
import edu.upf.bg.tools.exception.ToolValidationException;
import org.apache.commons.lang.ArrayUtils;
import org.gitools.analysis.correlation.GroupComparisonCommand;
import org.gitools.analysis.groupcomparison.GroupComparisonAnalysis;
import org.gitools.cli.AnalysisArguments;
import org.gitools.cli.AnalysisTool;
import org.gitools.model.ToolConfig;
import org.gitools.persistence.FileSuffixes;
import org.gitools.persistence.MimeTypes;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.text.ObjectMatrixTextPersistence;
import org.gitools.stats.mtc.MTCFactory;
import org.gitools.stats.test.MannWhitneyWilxoxonTest;
import org.gitools.stats.test.Test;
import org.gitools.stats.test.factory.TestFactory;
import org.gitools.threads.ThreadManager;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.*;
import java.util.Arrays;
import java.util.Properties;

public class ComparisonTool extends AnalysisTool {

    protected String groups;

    public static class ComparisonArguments extends AnalysisArguments {
		@Option(name = "-df", aliases = "-data-format", metaVar = "<format>",
				usage = "Data file format (MIME type or file extension).")
		public String dataMime;

		@Option(name = "-d", aliases = "-data", metaVar = "<file>",
				usage = "File with data to be processed.")
		public String dataFile;

        @Option(name = "-g", aliases = "-grouping", metaVar = "<grouping>",
                usage = "By what criteria the groups are defined: 'value' (cut-offs) or 'label' (row)")
        public String grouping;

        @Option(name = "-gl", aliases = "-group-labels", metaVar = "<group-labels>",
                usage = "For each group, pass a file with a list of row ids. Example: -gl group1.txt,group2.txt")
         public String groupLabels;

        @Option(name = "-gc", aliases = "-group-cutoffs", metaVar = "<group-cutoffs>",
                usage = "For each group, define a cutoff (Data Dimension <= Value). \n" +
                        "Example: \"Copy Number abs>= 1\",\"Copy Number == 0\"")
        public String groupCutoffs;

        @Option(name = "-mtc", metaVar = "<name>",
                usage = "Multiple test correxction method.\n" +
                        "Available: bonferroni, bh. (default: bh)")
        public String mtc = "bh";

        @Option(name = "-an", aliases = "-attr-name", metaVar = "<name>",
                usage = "Attribute name of the data matrix to take values for comparison from.")
        public String attrName;

        @Option(name = "-ai", aliases = "-attr-index", metaVar = "<index>",
                usage = "Attribute index of the data matrix to take values for comparison. (default: 0)")
        public int attrIndex = 0;

	}

	protected Properties methodProperties = new Properties();

	@Override
	public void validate(Object argsObject) throws ToolException {
		
		super.validate(argsObject);
		
		ComparisonArguments args = (ComparisonArguments) argsObject;

		if (args.dataFile == null)
        	throw new ToolValidationException("Data file should be specified.");
        
        if (args.grouping == null)
            throw new ToolValidationException("A grouping method should be defined. Options: ["+ GroupComparisonCommand.GROUP_BY_VALUE+","+ GroupComparisonCommand.GROUP_BY_LABELS+"]");
        else if (! (args.grouping.equals(GroupComparisonCommand.GROUP_BY_VALUE) || args.grouping.equals(GroupComparisonCommand.GROUP_BY_LABELS) ) )
            throw new ToolValidationException("Unknown grouping method: "+ args.grouping +". Choose from ["+ GroupComparisonCommand.GROUP_BY_VALUE+","+ GroupComparisonCommand.GROUP_BY_LABELS+"]");
        
        if (args.grouping.equals(GroupComparisonCommand.GROUP_BY_LABELS) && args.groupLabels == null) {
            throw new ToolValidationException("The <grouping> by labels requires the -gl option to be set");
        } else if (args.grouping.equals(GroupComparisonCommand.GROUP_BY_VALUE) && args.groupCutoffs == null)  {
            throw new ToolValidationException("The <grouping> by value requires the -gc option to be set");
        }

        this.groups =  (args.grouping.equals(GroupComparisonCommand.GROUP_BY_VALUE) ? args.groupCutoffs : args.groupLabels);
        
        if (args.attrName != null) {
            ObjectMatrixTextPersistence obp =  new ObjectMatrixTextPersistence();
            String[] headers = new String[0];
            try {
                headers = obp.readHeader(new File(args.dataFile));
            } catch (PersistenceException e) {
                throw new ToolValidationException("Data file could not be opened");
            }
            args.attrIndex = Arrays.asList(headers).indexOf(args.attrName);
            if (args.attrIndex < 0)  {
                throw new ToolValidationException("Specified attribute index not found: " + args.attrName);
            }
        }
    }

    @Override
	public void run(Object argsObject) throws ToolException {
		
		ComparisonArguments args = (ComparisonArguments) argsObject;

        GroupComparisonAnalysis analysis = new GroupComparisonAnalysis();
		prepareGeneralAnalysisAttributes(analysis, args);
		//analysis.setMethodProperties(methodProperties);

        Test t = new MannWhitneyWilxoxonTest();
        analysis.setMtc(args.mtc);
        analysis.setToolConfig(TestFactory.createToolConfig("group comparison",t.getName()));

		String dataMime = mimeFromFormat(args.dataMime, args.dataFile, MimeTypes.DOUBLE_MATRIX);

        analysis.setAttributeIndex(args.attrIndex);
        
		GroupComparisonCommand cmd = new GroupComparisonCommand(
        		analysis,
				dataMime, args.dataFile,
				args.workdir, args.analysisName + "." + FileSuffixes.GROUP_COMPARISON,
                args.grouping, this.groups);
        
        IProgressMonitor monitor = !args.quiet ? 
			new StreamProgressMonitor(System.out, args.verbose, args.debug)
			: new NullProgressMonitor();

		ThreadManager.setNumThreads(args.maxProcs);

        try {
			cmd.run(monitor);
		} catch (Exception e) {
			throw new  ToolException(e);
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
		o.println("Available comparison methods:");
		o.println(String.format(LIST_S_FMT, "mann-whitney-wilcoxon", "Mann-Whitney-Wilcoxon"));
	}
}