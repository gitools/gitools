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

import org.gitools.analysis.htest.enrichment.EnrichmentAnalysis;
import org.gitools.analysis.htest.enrichment.EnrichmentCommand;
import org.gitools.model.ToolConfig;
import org.gitools.persistence._DEPRECATED.FileSuffixes;
import org.gitools.persistence._DEPRECATED.MimeTypes;
import org.gitools.stats.test.factory.TestFactory;
import org.gitools.threads.ThreadManager;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.gitools.utils.progressmonitor.NullProgressMonitor;
import org.gitools.utils.progressmonitor.StreamProgressMonitor;
import org.gitools.utils.tools.exception.ToolException;
import org.gitools.utils.tools.exception.ToolValidationException;
import org.kohsuke.args4j.Option;

public class EnrichmentTool extends HtestTool
{

    public static class EnrichmentArguments extends HtestArguments
    {
        @Option(name = "-mf", aliases = "-modules-format", metaVar = "<format>",
                usage = "Modules file format (MIME type or file extension).")
        public String modulesMime;

        @Option(name = "-m", aliases = "-modules", metaVar = "<file>",
                usage = "File with mappings between items and modules.")
        public String modulesFile;

        @Option(name = "-min", aliases = "-min-mod-size", metaVar = "<min>",
                usage = "Discard all modules that have\nless items than <min> (default: 20)")
        private int minModuleSize = 20;

        @Option(name = "-max", aliases = "-max-mod-size", metaVar = "<max>",
                usage = "Discard all modules that have\nmore items than <max> (default: no limit)")
        private int maxModuleSize = Integer.MAX_VALUE;

        @Option(name = "-omi", aliases = "-only-mapped-items",
                usage = "Consider only items having a mapping for the background.\n" +
                        "(default: all items in data file will be considered)")
        private boolean discardNonMappedRows = false;
    }

    @Override
    public void validate(Object argsObject) throws ToolException
    {

        super.validate(argsObject);

        EnrichmentArguments args = (EnrichmentArguments) argsObject;

        testConfig = TestFactory.createToolConfig(
                ToolConfig.ENRICHMENT, args.testName);

        if (testConfig == null)
        {
            throw new ToolValidationException("Unknown test: " + args.testName);
        }

        if (args.modulesFile == null)
        {
            throw new ToolValidationException("Groups file has to be specified.");
        }

        if (args.minModuleSize < 1)
        {
            args.minModuleSize = 1;
        }
        if (args.maxModuleSize < args.minModuleSize)
        {
            args.maxModuleSize = args.minModuleSize;
        }
    }

    @Override
    public void run(Object argsObject) throws ToolException
    {

        EnrichmentArguments args = (EnrichmentArguments) argsObject;

        EnrichmentAnalysis analysis = new EnrichmentAnalysis();
        prepareAnalysis(analysis, args);

        analysis.setMinModuleSize(args.minModuleSize);
        analysis.setMaxModuleSize(args.maxModuleSize);

        analysis.setDiscardNonMappedRows(args.discardNonMappedRows);

        String dataMime = mimeFromFormat(args.dataMime, args.dataFile, MimeTypes.DOUBLE_MATRIX);

        String modulesMime = mimeFromFormat(args.modulesMime, args.modulesFile, MimeTypes.MODULES_2C_MAP);

        EnrichmentCommand cmd = new EnrichmentCommand(
                analysis,
                dataMime, args.dataFile,
                args.valueIndex,
                args.populationFile,
                populationDefaultValue,
                modulesMime, args.modulesFile,
                args.workdir, args.analysisName + "." + FileSuffixes.ENRICHMENT);

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
}