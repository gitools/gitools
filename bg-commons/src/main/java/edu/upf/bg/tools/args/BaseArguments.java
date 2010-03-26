package edu.upf.bg.tools.args;

import java.util.List;

import org.kohsuke.args4j.Option;

public class BaseArguments {

	@Option(name = "-loglevel", aliases = "-ll", metaVar = "<PACKAGE=LEVEL>",
			usage = "Define the log level for the package.")
	public List<String> loglevel;
	
	@Option(name = "-help", aliases = "-h", usage = "Show available options.")
	public boolean help;
	
	@Option(name = "-err-log",
		usage = "Specify the file where error logs will be saved.\n" +
		"If it is '-' the standard error stream will be used.")
	public String errorLog;
}
