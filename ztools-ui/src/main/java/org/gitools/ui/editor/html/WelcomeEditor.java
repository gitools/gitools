package org.gitools.ui.editor.html;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;

import org.gitools.ui.actions.file.NewEnrichmentAnalysisAction;
import org.gitools.ui.actions.file.OpenAnalysisAction;


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
		if (name.equals("newZetCalcAnalysis")) {
			new NewEnrichmentAnalysisAction()
				.actionPerformed(new ActionEvent(this, 0, name));
		}
		else if (name.equals("openAnalysis")) {
			new OpenAnalysisAction()
				.actionPerformed(new ActionEvent(this, 0, name));
		}
	}
}
