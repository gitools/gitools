package es.imim.bg.ztools.cli.zcalc;

import static org.kohsuke.args4j.ExampleMode.ALL;

import java.io.IOException;
import java.io.PrintStream;
import java.util.zip.DataFormatException;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import es.imim.bg.progressmonitor.NullProgressMonitor;
import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.progressmonitor.StreamProgressMonitor;
import es.imim.bg.ztools.threads.ThreadManager;
import es.imim.bg.ztools.zcalc.ZCalcCommand;

public class Main {

	private static final String appName = Main.class.getPackage().getImplementationTitle();
	private static final String versionString = Main.class.getPackage().getImplementationVersion();
	
	@Option(name = "-h", aliases = "-help", usage = "Print this message.")
	public boolean help = false;

	@Option(name = "-version", usage = "Print the version information and exit.")
	public boolean version = false;

	@Option(name = "-quiet", usage = "Don't print any information.")
	public boolean quiet = false;

	@Option(name = "-v", aliases = "-verbose", usage = "Print extra information.")
	public boolean verbose = false;

	@Option(name = "-debug", usage = "Print debug level information.")
	public boolean debug = false;

	@Option(name = "-N", aliases = "-name", usage = "Analysis name (default: unnamed).", metaVar = "<name>")
	public String analysisName = "unnamed";

	@Option(name = "-t", aliases = "-test", usage = "Statistical test to use:\nzscore-mean, zscore-median,\nbinomial, binomial-exact, binomial-normal,\nbinomial-poisson, fisher, hypergeom, chi-sqr\n(default: zscore-mean).", metaVar = "<name>")
	public String testName = "zscore-mean";

	@Option(name = "-s", aliases = "-num-samples", usage = "Number of samples to take when randomizing\n(default: 10000).", metaVar = "<n>")
	public int samplingNumSamples = 10000;

	@Option(name = "-d", aliases = "-data", usage = "File with data to be processed.", metaVar = "<file>")
	public String dataFile;

	@Option(name = "-c", aliases = "-class", usage = "File with mappings between items and modules.", metaVar = "<file>")
	public String groupsFile;

	@Option(name = "-min", aliases = "-min-mod-size", usage = "Discard all modules that have\nless items than <min> (default: 20)", metaVar = "<min>")
	private int minModuleSize = 20;

	@Option(name = "-max", aliases = "-max-mod-size", usage = "Discard all modules that have\nmore items than <max> (default: no limit)", metaVar = "<max>")
	private int maxModuleSize = Integer.MAX_VALUE;

	@Option(name = "-w", aliases = "-workdir", usage = "Working directory (default: current dir).", metaVar = "<dir>")
	public String workdir = System.getProperty("user.dir");

	@Option(name = "-f", aliases = "-out-fmt", usage = "Output format:\ncsv, rexml (default: csv).", metaVar = "<format>")
	public String outputFormat = "csv";

	@Option(name = "-results-by-module", usage = "Generated csv is arranged by module\ninstead of condition.")
	public boolean resultsByModule = false;
	
	@Option(name = "-p", aliases = "-max-procs", usage = "Maximum number of parallel processors allowed\n(default: all available processors).", metaVar = "<n>")
	public int maxProcs = ThreadManager.getAvailableProcessors();
	
	public static void main(String[] args) {
		int code = 0;
		Main main = null;
		try {
			main = new Main();
			main.run(args);
		} catch (Exception e) {
			System.err.println();
			
			if (main != null && main.debug)
				e.printStackTrace();
			else
				System.err.println("ERROR: " + e.toString());
			
			code = -1;
		}
		
		ThreadManager.shutdown(new NullProgressMonitor());
		
		System.out.println();
		System.exit(code);
	}
	
	public void run(String[] args) throws IOException, DataFormatException, InterruptedException {
		CmdLineParser parser = new CmdLineParser(this);

        parser.setUsageWidth(80);

        try {
            parser.parseArgument(args);
        } catch( CmdLineException e ) {
        	System.err.println(e.getMessage());
            printUsage(System.err, parser);
            System.exit(-1);
        }
        
        if (version)
        	printVersion();
        
        if (help) {
        	printUsage(System.out, parser);
        	System.exit(0);
        }
        else if (dataFile == null)
        	requiredArgument("Data file has to be specified.", parser);
        else if (groupsFile == null)
        	requiredArgument("Groups file has to be specified.", parser);
        
        if (minModuleSize < 1)
        	minModuleSize = 1;
        if (maxModuleSize < minModuleSize)
        	maxModuleSize = minModuleSize;
        
        ThreadManager.setNumThreads(maxProcs);
        
        ZCalcCommand cmd = new ZCalcCommand(
        		analysisName, testName, samplingNumSamples, 
        		dataFile, groupsFile, 
        		minModuleSize, maxModuleSize,
        		workdir, outputFormat, !resultsByModule);
        
        ProgressMonitor monitor = !quiet ? 
			new StreamProgressMonitor(System.out, verbose, debug)
			: new NullProgressMonitor();
        
        cmd.run(monitor);
	}

	private void printVersion() {
		System.out.println(appName + " version " + versionString);
		System.out.println("Written by Christian Perez Llamas <christian.perez@upf.edu>");
		//System.out.println();
	}

	private void requiredArgument(String msg, CmdLineParser parser) {
		System.err.println(msg);
		printUsage(System.err, parser);
		System.exit(-1);
	}

	private void printUsage(PrintStream out, CmdLineParser parser) {
        System.err.println("Usage: zcalc [options]");
        parser.printUsage(System.err);
        System.err.println();

        // print option sample. This is useful some time
        System.err.println("  Example: zcalc -m fisher -d datafile -c classfile" + parser.printExample(ALL));
	}
}