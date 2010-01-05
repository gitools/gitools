package org.gitools.heatmap.model;

import org.gitools.model.decorator.ElementDecoration;

public class HeatmapHeaderDecoration extends ElementDecoration {

	protected String url;
	
	public HeatmapHeaderDecoration() {
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
}
