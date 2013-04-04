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
package org.gitools.cli.correlation;

import org.gitools.analysis.correlation.CorrelationAnalysis;
import org.gitools.analysis.correlation.CorrelationCommand;
import org.gitools.analysis.correlation.methods.PearsonCorrelationMethod;
import org.gitools.cli.AnalysisArguments;
import org.gitools.cli.AnalysisTool;
import org.gitools.matrix.model.DoubleMatrix;
import org.gitools.persistence.IResourceFormat;
import org.gitools.persistence.formats.analysis.CorrelationAnalysisFormat;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CorrelationTool extends AnalysisTool
{

    public static class CorrelationArguments extends AnalysisArguments
    {
        @Option(name = "-df", aliases = "-data-format", metaVar = "<format>",
                usage = "Data file format (reference file extension).")
        public String dataFormat;

        @Option(name = "-d", aliases = "-data", metaVar = "<file>",
                usage = "File with data to be processed.")
        public String dataFile;

        @Option(name = "-r", aliases = "-rows",
                usage = "Apply to rows, by default it is applied to columns.")
        public boolean applyToRows = false;

        @Option(name = "-m", aliases = "-method", metaVar = "<method>",
                usage = "Correlation method to use. (default: pearson)")
        public String method = PearsonCorrelationMethod.ID;

        @NotNull
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
    public void validate(Object argsObject) throws ToolException
    {

        super.validate(argsObject);

        CorrelationArguments args = (CorrelationArguments) argsObject;

        if (args.dataFile == null)
        {
            throw new ToolValidationException("Data file should be specified.");
        }

        if (args.method == null)
        {
            throw new ToolValidationException("The method should be specified.");
        }

        methodProperties = parseProperties(args.methodConf);
    }

    @Override
    public void run(Object argsObject) throws ToolException
    {

        CorrelationArguments args = (CorrelationArguments) argsObject;

        CorrelationAnalysis analysis = new CorrelationAnalysis();
        prepareGeneralAnalysisAttributes(analysis, args);
        analysis.setMethod(args.method);
        analysis.setMethodProperties(methodProperties);
        analysis.setTransposeData(args.applyToRows);
        analysis.setReplaceNanValue(args.replaceValue);

        IResourceFormat dataFormat = getResourceFormat(args.dataFormat, args.dataFile, DoubleMatrix.class);

        CorrelationCommand cmd = new CorrelationCommand(
                analysis,
                dataFormat, args.dataFile,
                args.workdir, args.analysisName + "." + CorrelationAnalysisFormat.EXTENSION);

        IProgressMonitor monitor = !args.quiet ?
                new StreamProgressMonitor(System.out, args.verbose, args.debug)
                : new NullProgressMonitor();

        ThreadManager.setNumThreads(args.maxProcs);

        try
        {
            cmd.run(monitor);
        } catch (Exception e)
        {
            throw new ToolException(e);
        } finally
        {
            ThreadManager.shutdown(monitor);
        }
    }

    @Override
    public void printUsage(@NotNull PrintStream outputStream, String appName, ToolDescriptor toolDesc, CmdLineParser parser)
    {
        super.printUsage(outputStream, appName, toolDesc, parser);

        outputStream.println();

        printMethods(outputStream);
        outputStream.println();

        printDataFormats(outputStream);
        outputStream.println();
    }

    private void printMethods(@NotNull PrintStream o)
    {
        o.println("Available correlation methods:");
        o.println(String.format(LIST_S_FMT, "pearson", "Pearson's correlation"));
        o.println(String.format(LIST_S_FMT, "spearman", "Spearman's rank correlation"));
    }
}