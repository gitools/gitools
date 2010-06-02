package org.gitools.ui.analysis.htest.editor;

import org.gitools.ui.analysis.htest.editor.actions.NewDataHeatmapFromHtestAnalysisAction;
import org.gitools.ui.analysis.htest.editor.actions.NewResultsHeatmapFromHtestAnalysisAction;
import java.awt.BorderLayout;

import org.apache.velocity.VelocityContext;
import org.gitools.ui.platform.editor.AbstractEditor;
import org.gitools.ui.platform.panel.TemplatePane;

import edu.upf.bg.formatter.GenericFormatter;
import java.util.HashMap;
import java.util.Map;
import org.gitools.analysis.htest.HtestAnalysis;
import org.gitools.analysis.htest.enrichment.EnrichmentAnalysis;
import org.gitools.analysis.htest.oncozet.OncodriveAnalysis;
import org.gitools.ui.platform.actions.ActionSet;
import org.gitools.ui.platform.actions.ActionSetUtils;
import org.gitools.ui.platform.actions.BaseAction;

public class HtestAnalysisEditor extends AbstractEditor {

	private static final long serialVersionUID = 8258025724628410016L;

	public static final ActionSet toolBar = new ActionSet(new BaseAction[] {
		new NewDataHeatmapFromHtestAnalysisAction(),
		new NewResultsHeatmapFromHtestAnalysisAction()
	});

	private static Map<Class<? extends HtestAnalysis>, String> templateMap = new HashMap<Class<? extends HtestAnalysis>, String>();
	static {
		templateMap.put(EnrichmentAnalysis.class, "/vm/analysis/enrichment.vm");
		templateMap.put(OncodriveAnalysis.class, "/vm/analysis/oncodrive.vm");
	}

	private HtestAnalysis analysis;

	private TemplatePane templatePane;

	public HtestAnalysisEditor(HtestAnalysis analysis) {
		this.analysis = analysis;

		createComponents();
	}

	private void createComponents() {
		templatePane = new TemplatePane();
		try {
			templatePane.setTemplate(
					templateMap.get(analysis.getClass()));

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
