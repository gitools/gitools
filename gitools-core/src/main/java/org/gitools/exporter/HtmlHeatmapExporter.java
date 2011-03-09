/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.exporter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.gitools.model.decorator.ElementDecoration;
import org.gitools.heatmap.Heatmap;

import edu.upf.bg.formatter.GenericFormatter;

public class HtmlHeatmapExporter extends AbstractHtmlExporter {
	
	public HtmlHeatmapExporter() {
		super();
	}
	
	public void exportHeatmap(Heatmap figure) {
        
		File templatePath = getTemplatePath();
		if (templatePath == null)
			throw new RuntimeException("Unable to locate templates path !");
		
		try {
			copy(new File(templatePath, "media"), basePath);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		TemplateEngine eng = new TemplateEngine();
		eng.setFileLoaderPath(templatePath);
		eng.init();
		
		try {
			Map<String, Object> context = new HashMap<String, Object>();
			context.put("fmt", new GenericFormatter());
			context.put("figure", figure);
			context.put("matrix", figure.getMatrixView());
			context.put("cellDecoration", new ElementDecoration());
			eng.setContext(context);
			
			File file = new File(basePath, indexName);
			//eng.loadTemplate("/vm/exporter/html/matrixfigure.vm");
			eng.loadTemplate("matrixfigure.vm");
			eng.render(file);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
