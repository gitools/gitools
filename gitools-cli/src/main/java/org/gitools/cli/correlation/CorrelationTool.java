package org.gitools.cli.correlation;

import org.kohsuke.args4j.Option;

import edu.upf.bg.progressmonitor.NullProgressMonitor;
import edu.upf.bg.progressmonitor.IProgressMonitor;
import edu.upf.bg.progressmonitor.StreamProgressMonitor;
import edu.upf.bg.tools.ToolDescriptor;
import edu.upf.bg.tools.exception.ToolException;
import edu.upf.bg.tools.exception.ToolValidationException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.gitools.analysis.correlation.CorrelationAnalysis;
import org.gitools.analysis.correlation.CorrelationCommand;
import org.gitools.analysis.correlation.methods.PearsonCorrelationMethod;
import org.gitools.cli.AnalysisArguments;
import org.gitools.cli.AnalysisTool;
import org.gitools.persistence.FileSuffixes;
import org.gitools.persistence.MimeTypes;
import org.gitools.threads.ThreadManager;
import org.kohsuke.args4j.CmdLineParser;

public class CorrelationTool extends AnalysisTool {

	public static class CorrelationArguments extends AnalysisArguments {
		@Option(name = "-dm", aliases = "-data-mime", metaVar = "<MIME>",
				usage = "Data file format. (default: " + MimeTypes.DOUBLE_MATRIX + ")")
		public String dataMime = MimeTypes.DOUBLE_MATRIX;

		@Option(name = "-d", aliases = "-data", metaVar = "<file>",
				usage = "File with data to be processed.")
		public String dataFile;

		@Option(name = "-r", aliases = "-rows",
				usage = "Apply to rows, by default it is applied to columns.")
		public boolean applyToRows = false;

		@Option(name = "-m", aliases = "-method", metaVar = "<method>",
				usage = "Correlation method to use. (default: pearson)")
		public String method = PearsonCorrelationMethod.ID;

		@Option(name = "-M", aliases = "-method-conf", metaVar = "<param=value>",
				usage = "Define a method configuration parameter.\n" +
				" This allows to configure the behaviour of the method.")
		public List<String> methodConf = new ArrayList<String>(0);

		@Option(name = "-ev", aliases = "-empty-values", metaVar = "<value>",
				usage = "Replace empty values by <value>.\n" +
				"If not specified pairs with empty values\n" +
				"will be discarded.")
		public Double replaceValue;

		@Option(name = "-an", aliases = "-attr-name", metaVar = "<name>",
				usage = "Attribute name of the data matrix to use.")
		public String attrName;

		@Option(name = "-ai", aliases = "-attr-index", metaVar = "<index>",
				usage = "Attribute index of the data matrix to use. (default: 0)")
		public int attrIndex = 0;
	}

	protected Properties methodProperties = new Properties();

	@Override
	public void validate(Object argsObject) throws ToolException {
		
		super.validate(argsObject);
		
		CorrelationArguments args = (CorrelationArguments) argsObject;

		if (args.dataFile == null)
        	throw new ToolValidationException("Data file should be specified.");

		if (args.method == null)
        	throw new ToolValidationException("The method should be specified.");

		methodProperties = parseProperties(args.methodConf);
	}
	
	@Override
	public void run(Object argsObject) throws ToolException {
		
		CorrelationArguments args = (CorrelationArguments) argsObject;

		CorrelationAnalysis a = new CorrelationAnalysis();
		prepareGeneralAnalysisAttributes(a, args);
		a.setMethod(args.method);
		a.setMethodProperties(methodProperties);
		a.setTransposeData(args.applyToRows);
		a.setReplaceNanValue(args.replaceValue);

		CorrelationCommand cmd = new CorrelationCommand(
        		a, args.dataMime, args.dataFile,
				args.workdir, args.analysisName + "." + FileSuffixes.CORRELATION);
        
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

	@Override
	public void printUsage(PrintStream outputStream, String appName, ToolDescriptor toolDesc, CmdLineParser parser) {
		super.printUsage(outputStream, appName, toolDesc, parser);

		outputStream.println();

		printMethods(outputStream);
		outputStream.println();

		printDataFormats(outputStream);
		outputStream.println();
	}

	private void printMethods(PrintStream o) {
		o.println("Available correlation methods:");
		o.println(String.format(LIST_S_FMT, "pearson", "Pearson's correlation"));
		o.println(String.format(LIST_S_FMT, "spearman", "Spearman's rank correlation"));
	}
}