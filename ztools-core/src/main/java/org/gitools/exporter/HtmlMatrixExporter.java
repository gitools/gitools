package org.gitools.exporter;

import java.io.File;

import org.gitools.model.figure.MatrixFigure;

public class HtmlMatrixExporter {

	protected File basePath;
	protected String indexName;
	
	public HtmlMatrixExporter() {
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
		
	}
}
