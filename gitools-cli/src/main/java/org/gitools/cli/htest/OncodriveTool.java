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
package org.gitools.cli.htest;

import org.gitools.analysis.htest.oncozet.OncodriveAnalysis;
import org.gitools.analysis.htest.oncozet.OncodriveCommand;
import org.gitools.matrix.model.matrix.DoubleMatrix;
import org.gitools.model.ModuleMap;
import org.gitools.model.ToolConfig;
import org.gitools.persistence.IResourceFormat;
import org.gitools.persistence.formats.analysis.OncodriveAnalysisFormat;
import org.gitools.stats.test.factory.TestFactory;
import org.gitools.threads.ThreadManager;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.gitools.utils.progressmonitor.NullProgressMonitor;
import org.gitools.utils.progressmonitor.StreamProgressMonitor;
import org.gitools.utils.tools.exception.ToolException;
import org.gitools.utils.tools.exception.ToolValidationException;
import org.kohsuke.args4j.Option;

/**
 * @noinspection ALL
 */
public class OncodriveTool extends HtestTool {

    public static class OncodriveArguments extends HtestArguments {
        @Option(name = "-sf", aliases = "-sets-format", metaVar = "<format>",
                usage = "Column sets file format (reference file extension).")
        public String setsFormat;

        @Option(name = "-s", aliases = {"-sets"}, metaVar = "<file>",
                usage = "File with mappings for column sets.")
        public String setsFile;

        @Option(name = "-min", aliases = "-min-set-size", metaVar = "<min>",
                usage = "Discard all column sets that have\nless columns than <min> (default: 20)")
        private int minSetSize = 20;

        @Option(name = "-max", aliases = "-max-set-size", metaVar = "<max>",
                usage = "Discard all column sets that have\nmore columns than <max> (default: no limit)")
        private int maxSetSize = Integer.MAX_VALUE;
    }

    @Override
    public void validate(Object argsObject) throws ToolException {

        super.validate(argsObject);

        OncodriveArguments args = (OncodriveArguments) argsObject;

        testConfig = TestFactory.createToolConfig(ToolConfig.ONCODRIVE, args.testName);

        if (testConfig == null) {
            throw new ToolValidationException("Unknown test: " + args.testName);
        }

        if (args.minSetSize < 1) {
            args.minSetSize = 1;
        }
        if (args.maxSetSize < args.minSetSize) {
            args.maxSetSize = args.minSetSize;
        }
    }

    @Override
    public void run(Object argsObject) throws ToolException {

        OncodriveArguments args = (OncodriveArguments) argsObject;

        OncodriveAnalysis analysis = new OncodriveAnalysis();
        prepareAnalysis(analysis, args);

        analysis.setMinModuleSize(args.minSetSize);
        analysis.setMaxModuleSize(args.maxSetSize);

        IResourceFormat dataMime = getResourceFormat(args.dataFormat, args.dataFile, DoubleMatrix.class);

        IResourceFormat setsMime = getResourceFormat(args.setsFormat, args.setsFile, ModuleMap.class);

        OncodriveCommand cmd = new OncodriveCommand(analysis, dataMime, args.dataFile, args.valueIndex, args.populationFile, populationDefaultValue, setsMime, args.setsFile, args.workdir, args.analysisName + "." + OncodriveAnalysisFormat.EXTENSION);

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
}
