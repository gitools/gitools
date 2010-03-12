package org.gitools.ui.editor.html;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Map;
import org.gitools.ui.actions.file.NewEnrichmentAnalysisAction;

import org.gitools.ui.actions.file.OpenEnrichmentAnalysisAction;
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
		if (name.equals("newEnrichmentAnalysis")) {
			new NewEnrichmentAnalysisAction()
				.actionPerformed(new ActionEvent(this, 0, name));
		}
		else if (name.equals("openAnalysis")) {
			new OpenEnrichmentAnalysisAction()
				.actionPerformed(new ActionEvent(this, 0, name));
		}
	}

	@Override
	public void doVisible() {
		AppFrame.instance().setLeftPanelVisible(false);
	}
}
