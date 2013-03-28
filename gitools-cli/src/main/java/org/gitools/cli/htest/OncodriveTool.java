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
import org.gitools.persistence._DEPRECATED.FileSuffixes;
import org.gitools.persistence._DEPRECATED.MimeTypes;
import org.gitools.stats.test.factory.TestFactory;
import org.gitools.threads.ThreadManager;

public class OncodriveTool extends HtestTool {

	public static class OncodriveArguments extends HtestArguments {
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
		
		OncodriveArguments args = (OncodriveArguments) argsObject;

		testConfig = TestFactory.createToolConfig(
				ToolConfig.ONCODRIVE, args.testName);

		if (testConfig == null)
			throw new ToolValidationException("Unknown test: " + args.testName);
		
		if (args.minSetSize < 1)
        	args.minSetSize = 1;
        if (args.maxSetSize < args.minSetSize)
        	args.maxSetSize = args.minSetSize;
	}
	
	@Override
	public void run(Object argsObject) throws ToolException {
		
		OncodriveArguments args = (OncodriveArguments) argsObject;

		OncodriveAnalysis analysis = new OncodriveAnalysis();
		prepareAnalysis(analysis, args);

		analysis.setMinModuleSize(args.minSetSize);
		analysis.setMaxModuleSize(args.maxSetSize);

		String dataMime = mimeFromFormat(args.dataMime, args.dataFile, MimeTypes.DOUBLE_MATRIX);

		String setsMime = mimeFromFormat(args.setsMime, args.setsFile, MimeTypes.MODULES_2C_MAP);

		OncodriveCommand cmd = new OncodriveCommand(
        		analysis, dataMime, args.dataFile,
                args.valueIndex,
				args.populationFile,
				populationDefaultValue,
				setsMime, args.setsFile,
				args.workdir, args.analysisName + "." + FileSuffixes.ONCODRIVE);
        
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
