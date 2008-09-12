package es.imim.bg.ztools.zcalc.report;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import freemarker.ext.dom.NodeModel;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class ReportModelLoader {

	public Map<String, Object> load(Map<String, Object> model, String reportPath) throws SAXException, IOException, ParserConfigurationException {

		model.put("report", NodeModel.parse(new File(reportPath)).getChildNodes());
		
		final AnalysisModelLoader analysisModelLoader = new AnalysisModelLoader();
		
		model.put("loadAnalysisDescription", new TemplateMethodModel() {
			@Override
			public Object exec(List args) throws TemplateModelException {
				if (args.size() < 1)
					throw new TemplateModelException("Wrong arguments, required: analysisPath");
				
				String analysisPath = args.get(0).toString();
				
				Map<String, Object> model = null;
				try {
					model = analysisModelLoader.loadDescription(analysisPath);
				} catch (Exception e) {
					throw new TemplateModelException("Error loading analysis " + analysisPath, e);
				}
				
				return new SimpleHash(model);
			}
		});
		
		model.put("loadAnalysisResults", new TemplateMethodModel() {
			@Override
			public Object exec(List args) throws TemplateModelException {
				if (args.size() < 1)
					throw new TemplateModelException("Wrong arguments, required: analysisPath");
				
				String analysisPath = args.get(0).toString();
				
				Map<String, Object> model = null;
				try {
					model = analysisModelLoader.loadResults(analysisPath);
				} catch (Exception e) {
					throw new TemplateModelException("Error loading results " + analysisPath, e);
				}
				
				return new SimpleHash(model);
			}
		});
		return model;
	}
}
