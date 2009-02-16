package es.imim.bg.ztools.ui.dialogs;

import java.util.HashMap;
import java.util.Map;



public abstract class AnalysisWizard {
		
	private static final long serialVersionUID = -3270597264001925514L;
	
	public enum Condition { 
		EQ("equal"), 
		NE("not equal"), 
		GE("greater or equal"),
		LE("lower or equal"),
		GT("greater than"),
		LT("lower than");
	
		private String title;
		
		private Condition(String title) {
			this.title = title;
		}
		
		@Override
		public String toString() {
			return title;
		}
	}
	
	static Map<String, String> dataModel;
	public final String ANALYSIS_NAME = "analysis_name";
	public final String PROCESSORS = "processors";
	
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
}
