package org.gitools.ui.editor.analysis;

import java.awt.BorderLayout;

import org.gitools.ui.editor.AbstractEditor;
import org.gitools.ui.panels.TemplatePane;

import org.gitools.model.Analysis;

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
