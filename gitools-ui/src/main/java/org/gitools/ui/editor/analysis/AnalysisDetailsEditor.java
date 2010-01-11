package org.gitools.ui.editor.analysis;

import java.awt.BorderLayout;

import org.apache.velocity.VelocityContext;
import org.gitools.model.Analysis;
import org.gitools.ui.platform.editor.AbstractEditor;
import org.gitools.ui.platform.panel.TemplatePane;

import edu.upf.bg.formatter.GenericFormatter;

public class AnalysisDetailsEditor extends AbstractEditor {

	private static final long serialVersionUID = 8258025724628410016L;

	private Analysis analysis;
	
	private TemplatePane templatePane;

	public AnalysisDetailsEditor(Analysis analysis) {
		this.analysis = analysis;
		
		createComponents();
	}
	
	private void createComponents() {
		templatePane = new TemplatePane();
		try {
			templatePane.setTemplate("/vm/analysis/enrichment.vm");
			
			VelocityContext context = new VelocityContext();
			context.put("fmt", new GenericFormatter());
			context.put("analysis", analysis);
			
			templatePane.setContext(context);
			templatePane.render();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setLayout(new BorderLayout());
		add(templatePane);
	}

	@Override
	public Object getModel() {
		return analysis;
	}

	@Override
	public void refresh() {
	}
}
