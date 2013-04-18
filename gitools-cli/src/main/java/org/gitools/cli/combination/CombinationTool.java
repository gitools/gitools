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
package org.gitools.cli.combination;

import org.gitools.analysis.combination.CombinationAnalysis;
import org.gitools.analysis.combination.CombinationCommand;
import org.gitools.cli.AnalysisArguments;
import org.gitools.cli.AnalysisTool;
import org.gitools.matrix.model.matrix.DoubleMatrix;
import org.gitools.model.ModuleMap;
import org.gitools.persistence.IResourceFormat;
import org.gitools.persistence._DEPRECATED.FileFormat;
import org.gitools.persistence._DEPRECATED.FileFormats;
import org.gitools.persistence.formats.analysis.CombinationAnalysisFormat;
import org.gitools.threads.ThreadManager;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.gitools.utils.progressmonitor.NullProgressMonitor;
import org.gitools.utils.progressmonitor.StreamProgressMonitor;
import org.gitools.utils.tools.ToolDescriptor;
import org.gitools.utils.tools.exception.ToolException;
import org.gitools.utils.tools.exception.ToolValidationException;
import org.jetbrains.annotations.NotNull;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.PrintStream;


/**
 * @noinspection ALL
 */
public class CombinationTool extends AnalysisTool {

    public static class CombinationArguments extends AnalysisArguments {
        @Option(name = "-df", aliases = "-data-format", metaVar = "<format>",
                usage = "Data file format (reference file extension).")
        public String dataFormat;

        @Option(name = "-d", aliases = "-data", metaVar = "<file>",
                usage = "File with data to be processed.")
        public String dataFile;

        @Option(name = "-cf", aliases = "-columns-format", metaVar = "<format>",
                usage = "Columns file format (reference file extension).")
        public String columnsFormat;

        @Option(name = "-c", aliases = "-columns", metaVar = "<file>",
                usage = "File specifying how to group columns to combine.\nAny modules file can be used.\n(default: all columns combined)")
        public String columnsFile;

        @Option(name = "-r", aliases = "-rows",
                usage = "Apply to rows, by default it is applied to columns.")
        public final boolean applyToRows = false;

        @Option(name = "-sn", aliases = "-size-name", metaVar = "<name>",
                usage = "Attribute name for size.\n(default: if not specified a constant value of 1 will be used)")
        public String sizeName;

        @Option(name = "-pn", aliases = "-pvalue-name", metaVar = "<name>",
                usage = "Attribute name having the pvalue.\n(default: use the first one)")
        public String pvalueName;

    }

    @Override
    public void validate(Object argsObject) throws ToolException {

        super.validate(argsObject);

        CombinationArguments args = (CombinationArguments) argsObject;

        if (args.dataFile == null) {
            throw new ToolValidationException("Data file should be specified.");
        }
    }

    @Override
    public void run(Object argsObject) throws ToolException {

        CombinationArguments args = (CombinationArguments) argsObject;

        CombinationAnalysis analysis = new CombinationAnalysis();
        prepareGeneralAnalysisAttributes(analysis, args);
        analysis.setTransposeData(args.applyToRows);
        analysis.setSizeAttrName(args.sizeName);
        analysis.setPvalueAttrName(args.pvalueName);

        IResourceFormat dataMime = getResourceFormat(args.dataFormat, args.dataFile, DoubleMatrix.class);
        IResourceFormat columnsMime = getResourceFormat(args.columnsFormat, args.columnsFile, ModuleMap.class);

        CombinationCommand cmd = new CombinationCommand(analysis, dataMime, args.dataFile, columnsMime, args.columnsFile, args.workdir, args.analysisName + "." + CombinationAnalysisFormat.EXTENSION);

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

        printDataFormats(outputStream);
        outputStream.println();

        printModulesFormats(outputStream);
        outputStream.println();
    }

    @Override
    protected void printDataFormats(@NotNull PrintStream out) {
        out.println("Supported data formats:");
        FileFormat[] formats = new FileFormat[]{FileFormats.MULTIVALUE_DATA_MATRIX, FileFormats.DOUBLE_MATRIX, FileFormats.DOUBLE_BINARY_MATRIX, FileFormats.GENE_MATRIX, FileFormats.GENE_MATRIX_TRANSPOSED, FileFormats.MODULES_2C_MAP};

        for (FileFormat f : formats)
            out.println(String.format(LIST_L_FMT, f.getExtension(), f.getTitle()));
    }
}
