package org.gitools.cli.htest;

import edu.upf.bg.cutoffcmp.CutoffCmp;
import edu.upf.bg.tools.ToolDescriptor;
import edu.upf.bg.tools.exception.ToolException;
import edu.upf.bg.tools.exception.ToolValidationException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.gitools.analysis.htest.HtestAnalysis;
import org.gitools.cli.AnalysisArguments;
import org.gitools.cli.AnalysisTool;

import org.gitools.model.ToolConfig;
import org.gitools.persistence.MimeTypes;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public abstract class HtestTool extends AnalysisTool {

	public static class HtestArguments extends AnalysisArguments {

		@Option(name = "-t", aliases = "-test", metaVar = "<name>",
				usage = "Statistical test to use.")
		public String testName;

		@Option(name = "-tc", aliases = "-test-conf", metaVar = "<param=value>",
				usage = "Define a test configuration parameter." +
				" This allows to configure the behaviour of the test.")
		public List<String> testConf = new ArrayList<String>(0);
	
		@Option(name = "-mtc", metaVar = "<name>",
				usage = "Multiple test correxction method.\n" +
				"Available: bonferroni, bh. (default: bh)")
		public String mtc = "bh";

		@Option(name = "-dm", aliases = "-data-mime", metaVar = "<MIME>",
				usage = "Data file format (MIME). (default: " +
				MimeTypes.DOUBLE_MATRIX + ")")
		public String dataMime = MimeTypes.DOUBLE_MATRIX;

		@Option(name = "-d", aliases = "-data", metaVar = "<file>",
				usage = "File with data to be processed.")
		public String dataFile;

		@Option(name = "-P", aliases = "-population", metaVar = "<file>",
				usage = "File with background population elements.")
		public String populationFile;

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

	protected boolean binaryCutoffEnabled = false;
	protected CutoffCmp binaryCutoffCmp;
	protected double binaryCutoffValue;

	protected ToolConfig testConfig;
	protected List<String[]> testConfigParams = new ArrayList<String[]>(0);
	
	@Override
	public void validate(Object argsObject) throws ToolException {

		super.validate(argsObject);
		
		HtestArguments args = (HtestArguments) argsObject;

		if (args.testName == null)
        	throw new ToolValidationException("Test name has to be specified.");

		//FIXME use testConfigParams = parseConfiguration(args.testConf);
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
		prepareGeneralAnalysisAttributes(analysis, args);

		analysis.setBinaryCutoffEnabled(binaryCutoffEnabled);
		analysis.setBinaryCutoffCmp(binaryCutoffCmp);
		analysis.setBinaryCutoffValue(binaryCutoffValue);

		for (String[] c : testConfigParams)
			testConfig.put(c[0], c[1]);

		analysis.setTestConfig(testConfig);

		analysis.setMtc(args.mtc);
	}

	@Override
	public void printUsage(PrintStream outputStream, String appName, ToolDescriptor toolDesc, CmdLineParser parser) {
		super.printUsage(outputStream, appName, toolDesc, parser);

		outputStream.println();

		printTests(outputStream);
		outputStream.println();

		printDataFormats(outputStream);
		outputStream.println();

		printModulesFormats(outputStream);
		outputStream.println();
	}

	private void printTests(PrintStream o) {
		o.println("Available tests:");
		o.println(String.format(LIST_S_FMT, "zscore", "Z-score test"));
		o.println(String.format(LIST_S_FMT, "binomial", "Binomial test"));
		o.println(String.format(LIST_S_FMT, "fisher", "Fisher's exact test"));
		//o.println(String.format(LIST_FMT, "chi-square", "Chi Square test"));
	}
}
