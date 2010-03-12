package org.gitools.ui.platform.wizard;

import javax.swing.Icon;
import javax.swing.JComponent;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.help.HelpContext;

public interface IWizardPage {

	String getId();
	
	void setId(String id);

	IWizard getWizard();
	
	void setWizard(IWizard wizard);

	boolean isComplete();

	JComponent createControls();
	
	void updateControls();

	String getTitle();

	Icon getLogo();

	MessageStatus getStatus();
	
	String getMessage();

	HelpContext getHelpContext();
}