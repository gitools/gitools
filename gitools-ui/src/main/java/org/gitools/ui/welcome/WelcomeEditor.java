package org.gitools.ui.welcome;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.gitools.ui.actions.file.ImportBiomartModulesAction;
import org.gitools.ui.actions.file.ImportBiomartTableAction;
import org.gitools.ui.actions.file.ImportIntogenMatrixAction;
import org.gitools.ui.actions.file.ImportIntogenOncomodulesAction;
import org.gitools.ui.actions.file.NewCorrelationAnalysisAction;
import org.gitools.ui.actions.file.NewEnrichmentAnalysisAction;
import org.gitools.ui.actions.file.NewOncodriveAnalysisAction;

import org.gitools.ui.actions.file.OpenAnalysisAction;
import org.gitools.ui.dialog.UnimplementedDialog;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.Html4Editor;
import org.slf4j.LoggerFactory;


public class WelcomeEditor extends Html4Editor {

	private static final long serialVersionUID = 6851947500231401412L;

	public WelcomeEditor() {
		super("Welcome");

		try {
			URL url = getClass().getResource("/html/welcome.html");
			navigate(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void performUrlAction(String name, Map<String, String> params) {
		if (name.equals("goHome")) {
			try {
				Desktop.getDesktop().browse(new URI("http://www.gitools.org"));
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		else if (name.equals("importBiomart")) {
			BiomartTypeDialog dlg = new BiomartTypeDialog(AppFrame.instance());
			dlg.setVisible(true);
			if (!dlg.isCancelled()) {
				switch (dlg.getSelection()) {
					case BiomartTypeDialog.TABLE:
						new ImportBiomartTableAction()
								.actionPerformed(new ActionEvent(this, 0, name));
						break;

					case BiomartTypeDialog.MODULES:
						new ImportBiomartModulesAction()
								.actionPerformed(new ActionEvent(this, 0, name));
						break;
				}
			}
		}
		else if (name.equals("importIntogen")) {
			IntogenTypeDialog dlg = new IntogenTypeDialog(AppFrame.instance());
			dlg.setVisible(true);
			if (!dlg.isCancelled()) {
				switch (dlg.getSelection()) {
					case IntogenTypeDialog.MATRIX:
						new ImportIntogenMatrixAction()
								.actionPerformed(new ActionEvent(this, 0, name));
						break;

					case IntogenTypeDialog.ONCOMODULES:
						new ImportIntogenOncomodulesAction()
								.actionPerformed(new ActionEvent(this, 0, name));
						break;
				}
			}
		}
		else if (name.equals("analysis")) {
			Map<String, Class<? extends BaseAction>> actions = new HashMap<String, Class<? extends BaseAction>>();
			actions.put("Enrichment", NewEnrichmentAnalysisAction.class);
			actions.put("Oncodrive", NewOncodriveAnalysisAction.class);
			actions.put("Correlations", NewCorrelationAnalysisAction.class);
			
			String ref = params.get("ref");
			Class<? extends BaseAction> actionClass = actions.get(ref);
			if (actionClass != null) {
				try {
					ActionEvent event = new ActionEvent(this, 0, name);
					actionClass.newInstance().actionPerformed(event);
				} catch (Exception ex) {
					LoggerFactory.getLogger(WelcomeEditor.class).debug(null, ex);
				}
			}
			else {
				UnimplementedDialog dlg = new UnimplementedDialog(AppFrame.instance());
				dlg.setVisible(true);
			}
		}
		else if (name.equals("example")) {
			LoggerFactory.getLogger(WelcomeEditor.class).debug("example: " + params);
		}
		else if (name.equals("downloadExamples")) {
			DownloadExamplesDialog dlg = new DownloadExamplesDialog(AppFrame.instance());
			dlg.setVisible(true);
		}
		if (name.equals("openAnalysis")) {
			new OpenAnalysisAction()
				.actionPerformed(new ActionEvent(this, 0, name));
		}
		else if (name.equals("dataMatrices")
				|| name.equals("dataModules")
				|| name.equals("dataTables")) {
				DataHelpDialog dlg = new DataHelpDialog(AppFrame.instance());
				dlg.setVisible(true);
		}
	}

	@Override
	public void doVisible() {
		AppFrame.instance().setLeftPanelVisible(false);
	}
}
