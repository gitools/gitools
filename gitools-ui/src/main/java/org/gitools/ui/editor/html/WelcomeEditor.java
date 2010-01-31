package org.gitools.ui.editor.html;

import org.gitools.ui.platform.editor.HtmlEditor;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;

import org.gitools.ui._DEPRECATED.actions.file.NewEnrichmentAnalysisAction;
import org.gitools.ui.actions.file.OpenEnrichmentAnalysisAction;


public class WelcomeEditor extends HtmlEditor {

	private static final long serialVersionUID = 6851947500231401412L;

	public WelcomeEditor() {
		super("Welcome");
		
		try {
			URL url = getClass().getResource("/html/welcome.html");
			setPage(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void performUrlAction(String name) {
		if (name.equals("newEnrichmentAnalysis")) {
			new NewEnrichmentAnalysisAction()
				.actionPerformed(new ActionEvent(this, 0, name));
		}
		else if (name.equals("openAnalysis")) {
			new OpenEnrichmentAnalysisAction()
				.actionPerformed(new ActionEvent(this, 0, name));
		}
	}
}
