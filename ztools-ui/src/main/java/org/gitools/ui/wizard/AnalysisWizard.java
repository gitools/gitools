package org.gitools.ui.wizard;




public abstract class AnalysisWizard extends AbstractWizard{
		
	private static final long serialVersionUID = -3270597264001925514L;
	
	
	public static final String ANALYSIS_NAME = "analysis_name";
	public static final String ANALYSIS_WORKING_DIR = "analysis_working_dir";
	public static final String PROCESSORS = "processors";
	public static final String DATA_FILE = "data_file";
	public static final String BIN_CUTOFF_CONDITION = "bin_cutoff_condition";
	public static final String BIN_CUTOFF_VALUE = "bin_cutoff_value";
	public static final String MODULE_FILE = "module_file";
	public static final String OMIT_NON_MAPPED_ITMEMS = "omit_non_mapped_items";
	public static final String MIN = "min";
	public static final String MAX = "max";
	public static final String SAMPLE_SIZE = "sample_size";
	public static final String STAT_TEST = "stat_test";
	public static final String DISABLED = "disabled";
	public static final String WIZARD_WORKING_DIR = "wizard_working_dir";
	
	public enum Condition { 
		EQ("eq","equal"), 
		GE("ge","greater or equal"),
		LE("le","lower or equal"),
		GT("gt","greater than"),
		LT("lt","lower than");
	
		private String title;
		private String arg;
		
		private Condition(String arg, String title) {
			this.title = title;
			this.arg = arg;
		}
		
		@Override
		public String toString() {
			return title;
		}
		
		public String toCommandLineArgument() {
			return arg;
		}
		
	}
	
	public enum StatTest {
		ZSCORE_MEAN("zscore-mean","Z-Score Mean"),
		ZSCORE_MEDIAN("zscore-median","Z-Score Median"),
		BINOMIAL_EXACT("binomial-exact","Binomial Exact"),
		BINOMIAL_NORMAL("binomial-normal","Binomial Normal"),
		BINOMIAL_POISSON("binomial-poisson","Binomial Poisson"),
		FISHER("fisher","Fisher"),
		/*HYPEROGEOM("hypergeom","Hypergeometric"),
		CHI_SQR("chi-sqr","Chi-Square")*/;		
		
		private String title;
		private String arg;
		
		private StatTest(String arg, String title) {
			
			this.title = title;
			this.arg = arg;
		}
		
		@Override
		public String toString() {
			return title;
		}
		
		public String toCommandLineArgument() {
			return arg;
		}
	}
	
	public AnalysisWizard() {
		super();
	}
}
