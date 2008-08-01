package es.imim.bg.ztools.zcalc.report.velocity;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.SAXException;

public class ReportModelLoader {

	public Map<String, Object> load(Map<String, Object> model, String reportPath) throws SAXException, IOException, ParserConfigurationException, JDOMException {

		SAXBuilder builder = new SAXBuilder("org.apache.xerces.parsers.SAXParser");
		Document root = builder.build(reportPath);
		
		model.put("root", root.getRootElement());
		
		return model;
	}
}
