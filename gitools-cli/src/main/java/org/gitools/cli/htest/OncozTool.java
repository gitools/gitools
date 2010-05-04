package org.gitools.cli.htest;

import org.kohsuke.args4j.Option;

import edu.upf.bg.progressmonitor.NullProgressMonitor;
import edu.upf.bg.progressmonitor.IProgressMonitor;
import edu.upf.bg.progressmonitor.StreamProgressMonitor;
import edu.upf.bg.tools.exception.ToolException;
import edu.upf.bg.tools.exception.ToolValidationException;
import org.gitools.analysis.htest.oncozet.OncodriveAnalysis;

import org.gitools.analysis.htest.oncozet.OncodriveCommand;
import org.gitools.model.ToolConfig;
import org.gitools.persistence.FileSuffixes;
import org.gitools.stats.test.factory.TestFactory;
import org.gitools.threads.ThreadManager;

public class OncozTool extends HtestTool {

	public static class OncozArguments extends HtestArguments {
		@Option(name = "-sf", aliases = "-sets-format", metaVar = "<format>",
				usage = "Column sets file format (MIME type or file extension).")
		public String setsMime;

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
		
		OncozArguments args = (OncozArguments) argsObject;

		testConfig = TestFactory.createToolConfig(
				ToolConfig.ONCODRIVER, args.testName);

		if (testConfig == null)
			throw new ToolValidationException("Unknown test: " + args.testName);
		
		if (args.minSetSize < 1)
        	args.minSetSize = 1;
        if (args.maxSetSize < args.minSetSize)
        	args.maxSetSize = args.minSetSize;
	}
	
	@Override
	public void run(Object argsObject) throws ToolException {
		
		OncozArguments args = (OncozArguments) argsObject;

		OncodriveAnalysis analysis = new OncodriveAnalysis();
		prepareAnalysis(analysis, args);

		analysis.setMinColumnsSize(args.minSetSize);
		analysis.setMaxColumnsSize(args.maxSetSize);

		OncodriveCommand cmd = new OncodriveCommand(
        		analysis, args.dataMime, args.dataFile,
				args.populationFile,
				args.setsMime, args.setsFile,
				args.workdir, args.analysisName + "." + FileSuffixes.ONCODRIVER);
        
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
