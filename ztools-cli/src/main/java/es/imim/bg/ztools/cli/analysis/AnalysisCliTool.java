package es.imim.bg.ztools.cli.analysis;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.kohsuke.args4j.Option;

import es.imim.bg.ztools.cli.AbstractCliTool;
import es.imim.bg.ztools.cli.InvalidArgumentException;
import es.imim.bg.ztools.cli.RequiredArgumentException;
import es.imim.bg.ztools.datafilters.BinCutoffFilter;

public abstract class AnalysisCliTool extends AbstractCliTool {

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

	@Option(name = "-f", aliases = "-fmt", usage = "Output format:\ncsv, rexml (default: csv).", metaVar = "<format>")
	public String outputFormat = "csv";
	
	@Option(name = "-b", aliases = "-bin-cutoff-filt", 
			usage = "Binary cutoff filter:\nlt (less than), le (less equal than)," +
					"\neq (equal), gt (greatar than), ge (greater equal than).", metaVar = "<condition,cutoff>")
	public String binCutoff;
	
	protected BinCutoffFilter binCutoffFilter;
	
	@Override
	public void validateArguments(Object argsObject) 
			throws RequiredArgumentException, InvalidArgumentException {
		
		AnalysisCliTool args = (AnalysisCliTool) argsObject;
		
		if (args.dataFile == null)
        	throw new RequiredArgumentException("Data file has to be specified.");
		
		if (binCutoff != null) {
			Pattern pat = Pattern.compile("^(lt|le|eq|gt|ge)\\,(.+)$");
			binCutoff = binCutoff.toLowerCase();
			Matcher mat = pat.matcher(binCutoff);
			if (!mat.matches())
				throw new InvalidArgumentException("Invalid parameters for binary cutoff filter: " + binCutoff);
			
			try {
				final String cond = mat.group(1);
				final double cutoff = Double.parseDouble(mat.group(2));
				
				if ("lt".equals(cond))
					binCutoffFilter = new BinCutoffFilter(cutoff, BinCutoffFilter.LT);
				else if ("le".equals(cond))
					binCutoffFilter = new BinCutoffFilter(cutoff, BinCutoffFilter.LE);
				else if ("eq".equals(cond))
					binCutoffFilter = new BinCutoffFilter(cutoff, BinCutoffFilter.EQ);
				else if ("gt".equals(cond))
					binCutoffFilter = new BinCutoffFilter(cutoff, BinCutoffFilter.GT);
				else if ("ge".equals(cond))
					binCutoffFilter = new BinCutoffFilter(cutoff, BinCutoffFilter.GE);
			}
			catch (NumberFormatException e) {
				throw new InvalidArgumentException("Invalid cutoff: " + mat.group(2), e);
			}
		}
	}
}
