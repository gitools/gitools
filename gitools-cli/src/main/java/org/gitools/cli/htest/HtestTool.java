package org.gitools.cli.htest;

import edu.upf.bg.cutoffcmp.CutoffCmp;
import edu.upf.bg.tools.exception.ToolException;
import edu.upf.bg.tools.exception.ToolValidationException;
import edu.upf.bg.tools.impl.AbstractTool;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.gitools.analysis.htest.HtestAnalysis;

import org.gitools.cli.GitoolsArguments;
import org.gitools.model.Attribute;
import org.gitools.model.ToolConfig;
import org.gitools.persistence.MimeTypes;
import org.kohsuke.args4j.Option;

public abstract class HtestTool extends AbstractTool {

	public static class HtestArguments extends GitoolsArguments {
		@Option(name = "-N", aliases = "-name", metaVar = "<name>",
				usage = "Analysis name. A folder with this name will be created\n" +
				"in the workdir. (default: unnamed).")
		public String analysisName = "unnamed";

		@Option(name = "-T", aliases="-title", metaVar = "<title>",
				usage = "Set the analysis title. (default: analysis name).")
		public String analysisTitle;

		@Option(name = "-notes", metaVar = "<notes>",
				usage = "Set analysis description and notes.")
		public String analysisNotes;

		@Option(name = "-A", aliases="-attribute", metaVar = "<name=value>",
				usage = "Define an analysis attribute.")
		public List<String> analysisAttributes = new ArrayList<String>(0);

		@Option(name = "-t", aliases = "-test", metaVar = "<name>",
				usage = "Statistical test to use:\nzscore-mean, zscore-median,\n" +
				"binomial, binomial-exact, binomial-normal,\n" +
				"binomial-poisson, fisher, hypergeom, chi-sqr\n" +
				"(default: zscore-mean).")
		public String testName;

		@Option(name = "-tc", aliases = "-test-conf", metaVar = "<param=value>",
				usage = "Define the value for a test configuration parameter." +
				" This allows to configure the behaviour of the test.\n" +
				" Available configuration parameters:")
		public List<String> testConf = new ArrayList<String>(0);
	
		@Option(name = "-mtc", metaVar = "<name>",
				usage = "Multiple test correxction method.\n" +
				"Available: bonferroni, bh. (default: bh)")
		public String mtc = "bh";

		@Option(name = "-dm", aliases = "-data-mime", metaVar = "<file>",
				usage = "Data file format (MIME). Available formats:\n" +
				MimeTypes.DOUBLE_MATRIX + "\n" +
				MimeTypes.DOUBLE_BINARY_MATRIX + "\n" +
				MimeTypes.GENE_MATRIX + "\n" +
				MimeTypes.GENE_MATRIX_TRANSPOSED)
		public String dataMime = MimeTypes.DOUBLE_MATRIX;

		@Option(name = "-d", aliases = "-data", metaVar = "<file>",
				usage = "File with data to be processed.")
		public String dataFile;

		@Option(name = "-w", aliases = "-workdir", metaVar = "<dir>",
				usage = "Working directory (default: current dir).")
		public String workdir = System.getProperty("user.dir");

		/*@Option(name = "-f", aliases = "-fmt", metaVar = "<format>",
				usage = "Output format:\ncsv, rexml (default: csv).")
		public String outputFormat = "csv";*/

		@Option(name = "-b", aliases = "-bin-cutoff-filt",
				usage = "Binary cutoff filter. Available conditions:\n" +
						"lt (less than), le (less equal than),\n" +
						"eq (equal), ne (not equal),\n" +
						"gt (greatar than), ge (greater equal than)," +
						"alt (abs less than), ale (abs less equal than),\n" +
						"aeq (abs equal), ane (abs not equal),\n" +
						"agt (abs greatar than), age (abs greater equal than)",
				metaVar = "<CONDITION,CUTOFF>")
		public String binCutoff;
	}

	//protected BinaryCutoffParser binaryCutoffParser;
	
	protected List<Attribute> analysisAttributes = new ArrayList<Attribute>(0);

	protected boolean binaryCutoffEnabled = false;
	protected CutoffCmp binaryCutoffCmp;
	protected double binaryCutoffValue;

	protected ToolConfig testConfig;
	protected List<String[]> testConfigParams = new ArrayList<String[]>(0);
	
	@Override
	public void validate(Object argsObject) throws ToolException {

		super.validate(argsObject);
		
		HtestArguments args = (HtestArguments) argsObject;

		if (args.analysisTitle == null)
			args.analysisTitle = args.analysisName;

		for (String attr : args.analysisAttributes) {
			final String[] a = attr.split("=", 2);
			if (a.length != 2)
				throw new ToolValidationException("Malformed analysis attribute: " + attr);
			analysisAttributes.add(new Attribute(a[0], a[1]));
		}

		if (args.testName == null)
        	throw new ToolValidationException("Test name has to be specified.");

		for (String conf : args.testConf) {
			final String[] c = conf.split("=", 2);
			if (c.length != 2)
				throw new ToolValidationException("Malformed test configuration parameter: " + conf);
			testConfigParams.add(c);
		}

		if (args.dataFile == null)
        	throw new ToolValidationException("Data file has to be specified.");
		
		if (args.binCutoff != null) {
			Pattern pat = Pattern.compile("^(lt|le|eq|gt|ge|alt|ale|aeq|agt|age)\\,(.+)$");
			args.binCutoff = args.binCutoff.toLowerCase();
			Matcher mat = pat.matcher(args.binCutoff);
			if (!mat.matches())
				throw new ToolValidationException("Invalid parameters for binary cutoff filter: " + args.binCutoff);
			
			try {
				binaryCutoffEnabled = true;
				binaryCutoffCmp = CutoffCmp.getFromName(mat.group(1));
				binaryCutoffValue = Double.parseDouble(mat.group(2));
			}
			catch (NumberFormatException e) {
				throw new ToolValidationException("Invalid cutoff: " + mat.group(2));
			}
		}
	}

	protected void prepareAnalysis(HtestAnalysis analysis, HtestArguments args) {
		analysis.setTitle(args.analysisTitle);
		analysis.setDescription(args.analysisNotes);
		analysis.setAttributes(analysisAttributes);

		analysis.setBinaryCutoffEnabled(binaryCutoffEnabled);
		analysis.setBinaryCutoffCmp(binaryCutoffCmp);
		analysis.setBinaryCutoffValue(binaryCutoffValue);

		for (String[] c : testConfigParams)
			testConfig.put(c[0], c[1]);

		analysis.setTestConfig(testConfig);

		analysis.setMtc(args.mtc);
	}
}
