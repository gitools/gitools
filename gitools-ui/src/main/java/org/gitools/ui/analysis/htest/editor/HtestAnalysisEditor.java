package org.gitools.ui.analysis.htest.editor;

import org.gitools.ui.analysis.htest.editor.actions.NewDataHeatmapFromHtestAnalysisAction;
import org.gitools.ui.analysis.htest.editor.actions.NewResultsHeatmapFromHtestAnalysisAction;
import java.awt.BorderLayout;

import org.apache.velocity.VelocityContext;
import org.gitools.ui.platform.editor.AbstractEditor;
import org.gitools.ui.platform.panel.TemplatePane;

import edu.upf.bg.formatter.GenericFormatter;
import org.gitools.analysis.htest.HtestAnalysis;
import org.gitools.ui.platform.actions.ActionSet;
import org.gitools.ui.platform.actions.ActionSetUtils;
import org.gitools.ui.platform.actions.BaseAction;

public class HtestAnalysisEditor extends AbstractEditor {

	private static final long serialVersionUID = 8258025724628410016L;

	public static final ActionSet toolBar = new ActionSet(new BaseAction[] {
		new NewDataHeatmapFromHtestAnalysisAction(),
		new NewResultsHeatmapFromHtestAnalysisAction()
	});

	private HtestAnalysis analysis;
	
	private TemplatePane templatePane;

	public HtestAnalysisEditor(HtestAnalysis analysis) {
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
		add(ActionSetUtils.createToolBar(toolBar), BorderLayout.NORTH);
		add(templatePane, BorderLayout.CENTER);
	}

	@Override
	public Object getModel() {
		return analysis;
	}
}
