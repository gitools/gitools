package es.imim.bg.ztools.ui.views.analysis;

import es.imim.bg.ztools.ui.model.AnalysisModel;
import es.imim.bg.ztools.ui.model.ResultsModel;

public class AnalysisInfoRender {

	protected AnalysisModel model;
	
	protected String colName;
	protected String rowName;
	protected String[] values;
	
	public AnalysisInfoRender() {
	}
	
	public AnalysisInfoRender(AnalysisModel model) {
		this.model = model;
	}

	public void setModel(AnalysisModel model) {
		this.model = model;
	}
	
	public void setColName(String colName) {
		this.colName = colName;
	}
	
	public void setRowName(String rowName) {
		this.rowName = rowName;
	}
	
	public void setValues(String[] values) {
		this.values = values;
	}
	
	@Override
	public String toString() {
		//Analysis a = model.getAnalysis();
		ResultsModel rmodel = model.getResultsModel();
		String[] paramNames = rmodel.getParamNames();
		
		StringBuilder sb = new StringBuilder();
		
		// Render parameters & values
		if (values != null) {
			sb.append("<p><b>Column:</b><br>");
			sb.append(colName);
			sb.append("</p>");
			sb.append("<p><b>Row:</b><br>");
			sb.append(rowName);
			sb.append("</p>");
			for (int i = 0; i < paramNames.length; i++) {
				final String paramName = paramNames[i];
				sb.append("<p><b>");
				sb.append(paramName);
				sb.append(":</b><br>");
				sb.append(values[i]);
				sb.append("</p>");
			}
		}
		
		return sb.toString();
	}
}
