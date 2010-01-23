package org.gitools.cli;

import edu.upf.bg.tools.args.BaseArguments;
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
	
	@Option(name = "-p", aliases = "-max-procs", usage = "Maximum number of parallel processors allowed\n(default: all available processors).", metaVar = "<n>")
	public int maxProcs = ThreadManager.getAvailableProcessors();
	
}
