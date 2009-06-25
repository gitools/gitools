package org.gitools.exporter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.gitools.model.decorator.ElementDecoration;
import org.gitools.model.figure.MatrixFigure;

import edu.upf.bg.GenericFormatter;

public class HtmlMatrixExporter {

	protected File basePath;
	protected String indexName;
	
	public HtmlMatrixExporter() {
		basePath = new File(System.getProperty("user.dir"));
		indexName = "index.html";
	}
	
	public File getBasePath() {
		return basePath;
	}
	
	public void setBasePath(File basePath) {
		this.basePath = basePath;
	}
	
	public String getIndexName() {
		return indexName;
	}
	
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	
	public void exportMatrixFigure(MatrixFigure figure) {
		TemplateEngine eng = new TemplateEngine();
		try {
			eng.loadTemplate("/vm/exporter/html/matrixfigure.vm");
			
			Map<String, Object> context = new HashMap<String, Object>();
			context.put("fmt", new GenericFormatter());
			context.put("figure", figure);
			context.put("matrix", figure.getMatrixView());
			context.put("cellDecoration", new ElementDecoration());
			eng.setContext(context);
			
			File file = new File(basePath, indexName);
			eng.render(file);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
