/*
 * #%L
 * gitools-cli
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.cli.comparison;

import org.gitools.core.analysis.groupcomparison.GroupComparisonCommand;
import org.gitools.core.analysis.groupcomparison.GroupComparisonAnalysis;
import org.gitools.cli.AnalysisArguments;
import org.gitools.cli.AnalysisTool;
import org.gitools.core.matrix.model.IMatrix;
import org.gitools.core.persistence.IResourceFormat;
import org.gitools.core.persistence.PersistenceException;
import org.gitools.core.persistence.formats.analysis.GroupComparisonAnalysisFormat;
import org.gitools.core.persistence.formats.matrix.TdmMatrixFormat;
import org.gitools.core.stats.test.MannWhitneyWilxoxonTest;
import org.gitools.core.stats.test.Test;
import org.gitools.core.stats.test.factory.TestFactory;
import org.gitools.utils.threads.ThreadManager;
import org.gitools.utils.csv.CSVReader;
import org.gitools.utils.fileutils.IOUtils;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.gitools.utils.progressmonitor.NullProgressMonitor;
import org.gitools.utils.progressmonitor.StreamProgressMonitor;
import org.gitools.utils.tools.ToolDescriptor;
import org.gitools.utils.tools.exception.ToolException;
import org.gitools.utils.tools.exception.ToolValidationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.io.PrintStream;
import java.io.Reader;
import java.util.Arrays;
import java.util.Properties;
import java.util.zip.DataFormatException;

/**
 * @noinspection ALL
 */
public class ComparisonTool extends AnalysisTool {

    private String groups;

    public static class ComparisonArguments extends AnalysisArguments {
        @Option(name = "-df", aliases = "-data-format", metaVar = "<format>",
                usage = "Data file format (reference extension).")
        public String dataFormat;

        /**
         * @noinspection UnusedDeclaration
         */
        @Option(name = "-d", aliases = "-data", metaVar = "<file>",
                usage = "File with data to be processed.")
        public String dataFile;

        @Option(name = "-g", aliases = "-grouping", metaVar = "<grouping>",
                usage = "By what criteria the groups are defined: 'value' (cut-offs) or 'label' (column labels)")
        public String grouping;

        @Option(name = "-gl", aliases = "-group-labels", metaVar = "<GROUP1,GROUP2>",
                usage = "For each group, pass a file with a list of row ids. Example: -gl group1.txt,group2.txt")
        public String groupLabels;

        @Option(name = "-gc", aliases = "-group-cutoffs", metaVar = "<'DIM' COMPARATOR VALUE,'DIM' COMPARATOR VALUE>",
                usage = "For each group, define a cutoff like this: 'Data Dimension' Comparator Value. \n" +
                        "Example: \"'Copy Number' agt 1\",\"'Copy Number' eq 0\"\n" +
                        "lt (less than), le (less equal than),\n" +
                        "eq (equal), ne (not equal),\n" +
                        "gt (greatar than), ge (greater equal than)," +
                        "alt (abs less than), ale (abs less equal than),\n" +
                        "aeq (abs equal), ane (abs not equal),\n" +
                        "agt (abs greatar than), age (abs greater equal than)")
        public String groupCutoffs;

        @NotNull
        @Option(name = "-gd", aliases = "-group-descriptions", metaVar = "<DESC 1,DESC 2>",
                usage = "Add a short description for each group seperated by commas. If not \n" +
                        "supplied, an automatically generated description will be added \n" +
                        "Example: \"With disease,Without disease\"")
        public final String groupDescriptions = "";

        @NotNull
        @Option(name = "-mtc", metaVar = "<name>",
                usage = "Multiple test correxction method.\n" + "Available: bonferroni, bh. (default: bh)")
        public final String mtc = "bh";

        @Option(name = "-an", aliases = "-attr-name", metaVar = "<name>",
                usage = "Attribute name of the data matrix to take values for comparison from.")
        public String attrName;

        @Option(name = "-ai", aliases = "-attr-index", metaVar = "<index>",
                usage = "Attribute index of the data matrix to take values for comparison. (default: 0)")
        public int attrIndex = 0;

    }

    @NotNull
    protected Properties methodProperties = new Properties();

    @Override
    public void validate(Object argsObject) throws ToolException {

        super.validate(argsObject);

        ComparisonArguments args = (ComparisonArguments) argsObject;

        if (args.dataFile == null) {
            throw new ToolValidationException("Data file should be specified.");
        }

        if (args.grouping == null) {
            throw new ToolValidationException("A grouping method should be defined. Options: [" + GroupComparisonCommand.GROUP_BY_VALUE + "," + GroupComparisonCommand.GROUP_BY_LABELS + "]");
        } else if (!(args.grouping.equals(GroupComparisonCommand.GROUP_BY_VALUE) || args.grouping.equals(GroupComparisonCommand.GROUP_BY_LABELS))) {
            throw new ToolValidationException("Unknown grouping method: " + args.grouping + ". Choose from [" + GroupComparisonCommand.GROUP_BY_VALUE + "," + GroupComparisonCommand.GROUP_BY_LABELS + "]");
        }

        if (args.grouping.equals(GroupComparisonCommand.GROUP_BY_LABELS) && args.groupLabels == null) {
            throw new ToolValidationException("The <grouping> by labels requires the -gl option to be set");
        } else if (args.grouping.equals(GroupComparisonCommand.GROUP_BY_VALUE) && args.groupCutoffs == null) {
            throw new ToolValidationException("The <grouping> by value requires the -gc option to be set");
        }

        this.groups = (args.grouping.equals(GroupComparisonCommand.GROUP_BY_VALUE) ? args.groupCutoffs : args.groupLabels);

        if (args.attrName != null) {
            TdmMatrixFormat obp = new TdmMatrixFormat();
            String[] headers = new String[0];
            try {
                headers = readHeader(new File(args.dataFile));
            } catch (PersistenceException e) {
                throw new ToolValidationException("Data file could not be opened");
            }
            args.attrIndex = Arrays.asList(headers).indexOf(args.attrName);
            if (args.attrIndex < 0) {
                throw new ToolValidationException("Specified attribute index not found: " + args.attrName);
            }
        }
    }

    @Nullable
    private static String[] readHeader(File file) throws PersistenceException {

        String[] matrixHeaders = null;
        try {
            Reader reader = IOUtils.openReader(file);

            CSVReader parser = new CSVReader(reader);

            String[] line = parser.readNext();

            // read header
            if (line.length < 3) {
                throw new DataFormatException("At least 3 columns expected.");
            }

            int numAttributes = line.length - 2;
            matrixHeaders = new String[numAttributes];
            System.arraycopy(line, 2, matrixHeaders, 0, numAttributes);
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
        return matrixHeaders;
    }

    @Override
    public void run(Object argsObject) throws ToolException {

        ComparisonArguments args = (ComparisonArguments) argsObject;

        GroupComparisonAnalysis analysis = new GroupComparisonAnalysis();
        prepareGeneralAnalysisAttributes(analysis, args);
        //analysis.setMethodProperties(methodProperties);

        Test t = new MannWhitneyWilxoxonTest();
        analysis.setMtc(args.mtc);
        analysis.setToolConfig(TestFactory.createToolConfig("group comparison", t.getName()));

        IResourceFormat dataFormat = getResourceFormat(args.dataFormat, args.dataFile, IMatrix.class);

        analysis.setAttributeIndex(args.attrIndex);

        String[] groupDescriptions = args.groupDescriptions.split(",");


        GroupComparisonCommand cmd = new GroupComparisonCommand(analysis, dataFormat, args.dataFile, args.workdir, args.analysisName + "." + GroupComparisonAnalysisFormat.EXTENSION, args.grouping, this.groups, groupDescriptions);

        IProgressMonitor monitor = !args.quiet ? new StreamProgressMonitor(System.out, args.verbose, args.debug) : new NullProgressMonitor();

        ThreadManager.setNumThreads(args.maxProcs);

        try {
            cmd.run(monitor);
        } catch (Exception e) {
            throw new ToolException(e);
        } finally {
            ThreadManager.shutdown(monitor);
        }
    }

    @Override
    public void printUsage(@NotNull PrintStream outputStream, String appName, ToolDescriptor toolDesc, CmdLineParser parser) {
        super.printUsage(outputStream, appName, toolDesc, parser);

        outputStream.println();

        printMethods(outputStream);
        outputStream.println();

        printDataFormats(outputStream);
        outputStream.println();
    }

    private void printMethods(@NotNull PrintStream o) {
        o.println("Available comparison methods:");
        o.println(String.format(LIST_S_FMT, "mann-whitney-wilcoxon", "Mann-Whitney-Wilcoxon"));
    }
}