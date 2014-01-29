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
package org.gitools.cli.overlapping;

import org.gitools.analysis.overlapping.OverlappingAnalysis;
import org.gitools.analysis.overlapping.OverlappingCommand;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.resource.IResourceFormat;
import org.gitools.cli.AnalysisArguments;
import org.gitools.cli.AnalysisTool;
import org.gitools.persistence.ResourceReference;
import org.gitools.persistence.formats.analysis.OverlappingAnalysisFormat;
import org.gitools.persistence.locators.UrlResourceLocator;
import org.gitools.utils.cutoffcmp.CutoffCmp;
import org.gitools.utils.progressmonitor.NullProgressMonitor;
import org.gitools.utils.progressmonitor.StreamProgressMonitor;
import org.gitools.utils.threads.ThreadManager;
import org.gitools.utils.tools.exception.ToolException;
import org.gitools.utils.tools.exception.ToolValidationException;
import org.kohsuke.args4j.Option;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @noinspection ALL
 */
public class OverlappingTool extends AnalysisTool {

    public static class OverlappingArguments extends AnalysisArguments {
        @Option(name = "-df", aliases = "-data-format", metaVar = "<format>",
                usage = "Data file format (Reference file extension).")
        public String dataFormat;

        @Option(name = "-d", aliases = "-data", metaVar = "<file>",
                usage = "File with data to be processed.")
        public String dataFile;

        @Option(name = "-r", aliases = "-rows",
                usage = "Apply to rows, by default it is applied to columns.")
        public final boolean applyToRows = false;

        /**
         * @noinspection UnusedDeclaration
         */
        @Option(name = "-ev", aliases = "-empty-values", metaVar = "<value>",
                usage = "Replace empty values by <value>.\n" + "If not specified empty values will be discarded.")
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

    private boolean binaryCutoffEnabled = false;
    private CutoffCmp binaryCutoffCmp;
    private double binaryCutoffValue;

    @Override
    public void validate(Object argsObject) throws ToolException {

        super.validate(argsObject);

        OverlappingArguments args = (OverlappingArguments) argsObject;

        if (args.dataFile == null) {
            throw new ToolValidationException("Data file should be specified.");
        }

        if (args.binCutoff != null) {
            Pattern pat = Pattern.compile("^([a-zA-Z]+),(.+)$");
            args.binCutoff = args.binCutoff.toLowerCase();
            Matcher mat = pat.matcher(args.binCutoff);
            if (!mat.matches()) {
                throw new ToolValidationException("Invalid parameters for binary cutoff filter: " + args.binCutoff);
            }

            try {
                binaryCutoffEnabled = true;
                binaryCutoffCmp = CutoffCmp.getFromName(mat.group(1));
                if (binaryCutoffCmp == null) {
                    throw new ToolException("Invalid condition: " + mat.group(1));
                }
                binaryCutoffValue = Double.parseDouble(mat.group(2));
            } catch (NumberFormatException e) {
                throw new ToolValidationException("Invalid cutoff: " + mat.group(2));
            }
        }
    }

    @Override
    public void run(Object argsObject) throws ToolException {

        OverlappingArguments args = (OverlappingArguments) argsObject;

        IProgressMonitor monitor = !args.quiet ? new StreamProgressMonitor(System.out, args.verbose, args.debug) : new NullProgressMonitor();

        ThreadManager.setNumThreads(args.maxProcs);

        try {

            IResourceFormat resourceFormat = getResourceFormat(args.dataFormat, args.dataFile, IMatrix.class);

            OverlappingAnalysis analysis = new OverlappingAnalysis();
            prepareGeneralAnalysisAttributes(analysis, args);
            analysis.setTransposeData(args.applyToRows);
            analysis.setReplaceNanValue(args.replaceValue);
            analysis.setSourceData(new ResourceReference<IMatrix>(new UrlResourceLocator(args.dataFile), resourceFormat));
            analysis.setBinaryCutoffEnabled(binaryCutoffEnabled);
            analysis.setBinaryCutoffCmp(binaryCutoffCmp);
            analysis.setBinaryCutoffValue(binaryCutoffValue);

            OverlappingCommand cmd = new OverlappingCommand(analysis, args.workdir, args.analysisName + "." + OverlappingAnalysisFormat.EXTENSION);


            cmd.run(monitor);
        } catch (Exception e) {
            throw new ToolException(e);
        } finally {
            ThreadManager.shutdown(monitor);
        }
    }
}
