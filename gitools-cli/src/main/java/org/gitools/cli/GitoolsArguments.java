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

package org.gitools.cli;

import org.gitools.utils.tools.args.BaseArguments;
import org.kohsuke.args4j.Option;

import org.gitools.threads.ThreadManager;

public class GitoolsArguments extends BaseArguments {

	@Option(name = "-version", usage = "Print the version information and exit.")
	public boolean version = false;

	@Option(name = "-quiet", usage = "Don't print any information.")
	public boolean quiet = false;

	@Option(name = "-v", aliases = "-verbose", usage = "Print extra information.")
	public boolean verbose = false;

	@Option(name = "-debug", usage = "Print debug level information.")
	public boolean debug = false;
	
	@Option(name = "-p", aliases = "-max-procs",
		usage = "Maximum number of parallel processors allowed.\n" +
		"(default: all available processors).", metaVar = "<n>")
	public int maxProcs = ThreadManager.getAvailableProcessors();
	
}
