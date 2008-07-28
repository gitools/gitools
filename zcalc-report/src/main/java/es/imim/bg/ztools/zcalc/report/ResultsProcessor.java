package es.imim.bg.ztools.zcalc.report;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class ResultsProcessor {
	
	private Configuration cfg;
	private Template temp;
	
	public ResultsProcessor(File templatePath) throws IOException {
				
		cfg = new Configuration();
		
		cfg.setDirectoryForTemplateLoading(templatePath);
		
		cfg.setObjectWrapper(new DefaultObjectWrapper());	

		temp = cfg.getTemplate("results.ftl");
	}

	public void process( 
			Map<String, Object> model,
			File outputPath,
			String outputFileName) 
				throws IOException, TemplateException {
		
		Writer out = new FileWriter(
				new File(outputPath, outputFileName));
		
		temp.process(model, out);
		
		out.flush();
		out.close();
	}
}
