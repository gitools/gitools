package es.imim.bg.ztools.cli;

import org.kohsuke.args4j.Option;

public class AnalysisTool extends BasicArguments {

	@Option(name = "-N", aliases = "-name", usage = "Analysis name (default: unnamed).", metaVar = "<name>")
	public String analysisName = "unnamed";

	@Option(name = "-t", aliases = "-test", usage = "Statistical test to use:\nzscore-mean, zscore-median,\nbinomial, binomial-exact, binomial-normal,\nbinomial-poisson, fisher, hypergeom, chi-sqr\n(default: zscore-mean).", metaVar = "<name>")
	public String testName = "zscore-mean";

	@Option(name = "-s", aliases = "-num-samples", usage = "Number of samples to take when randomizing\n(default: 10000).", metaVar = "<n>")
	public int samplingNumSamples = 10000;

	@Option(name = "-d", aliases = "-data", usage = "File with data to be processed.", metaVar = "<file>")
	public String dataFile;
	
	@Option(name = "-w", aliases = "-workdir", usage = "Working directory (default: current dir).", metaVar = "<dir>")
	public String workdir = System.getProperty("user.dir");

	@Option(name = "-f", aliases = "-out-fmt", usage = "Output format:\ncsv, rexml (default: csv).", metaVar = "<format>")
	public String outputFormat = "csv";
	
	protected void processArgs(AnalysisTool args) 
			throws RequiredArgumentException {
		
		if (args.dataFile == null)
        	throw new RequiredArgumentException("Data file has to be specified.");
	}
}
