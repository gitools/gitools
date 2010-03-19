package org.gitools.ui.welcome;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.net.URI;
import java.net.URL;
import java.util.Map;
import org.gitools.ui.actions.file.ImportBiomartModulesAction;
import org.gitools.ui.actions.file.ImportBiomartTableAction;
import org.gitools.ui.actions.file.ImportIntogenMatrixAction;
import org.gitools.ui.actions.file.ImportIntogenOncomodulesAction;
import org.gitools.ui.actions.file.NewEnrichmentAnalysisAction;

import org.gitools.ui.actions.file.OpenEnrichmentAnalysisAction;
import org.gitools.ui.dialog.UnimplementedDialog;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.editor.Html4Editor;


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
		if (name.equals("openAnalysis")) {
			new OpenEnrichmentAnalysisAction()
				.actionPerformed(new ActionEvent(this, 0, name));
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
		if (name.equals("analysisEnrichment")) {
			new NewEnrichmentAnalysisAction()
				.actionPerformed(new ActionEvent(this, 0, name));
		}
		else if (name.equals("analysisAlterations")
				|| name.equals("analysisCombinations")
				|| name.equals("analysisCorrelations")) {
			UnimplementedDialog dlg = new UnimplementedDialog(AppFrame.instance());
			dlg.setVisible(true);
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
