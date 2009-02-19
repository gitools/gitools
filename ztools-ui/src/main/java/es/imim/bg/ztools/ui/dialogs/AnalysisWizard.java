package es.imim.bg.ztools.ui.dialogs;

import java.util.HashMap;
import java.util.Map;


public abstract class AnalysisWizard {
		
	private static final long serialVersionUID = -3270597264001925514L;
	
	
	static Map<String, String> dataModel;
	public final String ANALYSIS_NAME = "analysis_name";
	public final String WORKING_DIR = "working_dir";
	public final String PROCESSORS = "processors";
	public final String DATA_FILE = "data_file";
	public final String BIN_CUTOFF_CONDITION = "bin_cutoff_condition";
	public final String BIN_CUTOFF_VALUE = "bin_cutoff_value";
	public final String MODULE_FILE = "module_file";
	public final String MIN = "min";
	public final String MAX = "max";
	public final String SAMPLE_SIZE = "sample_size";
	public final String STAT_TEST = "stat_test";
	
	public enum Condition { 
		EQ("eq","equal"), 
		NE("ne","not equal"), 
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
		BINOMINAL("binominal","Binominal"),
		BINOMINAL_EXACT("binominal-exact","Binominal Exact"),
		BINOMINAL_NORMAL("binominal-normal","Binominal Normal"),
		BINOMINAL_POISSON("binominal-poisson","Binominal Poisson"),
		FISHER("fisher","Fisher"),
		HYPEROGEOM("hypergeom","Hypergeometric"),
		CHI_SQR("chi-sqr","Chi-Square");		
		
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
		dataModel = new HashMap<String, String>();
	}
	
	public void setValue(String key, String value){
		dataModel.put(key, value);
	}
	
	public String getValue(String key) {
		return dataModel.get(key);
	}
	
	public void removeValue(String key) {
		dataModel.remove(key);
	}
	
	public void clear(){
		dataModel.clear();
	}
}
