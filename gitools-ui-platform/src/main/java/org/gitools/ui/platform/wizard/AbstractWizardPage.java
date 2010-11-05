package org.gitools.ui.platform.wizard;

import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.help.HelpContext;

public abstract class AbstractWizardPage extends JPanel implements IWizardPage {

	private static final long serialVersionUID = -4330234851091328953L;

	private String id;
	
	private IWizard wizard;
	
	private boolean pageComplete;
	
	private String title = "";

	private Icon logo;

	private MessageStatus status = MessageStatus.INFO;

	private String message = "";

	private HelpContext helpContext;

	private List<IWizardPageUpdateListener> listeners = new ArrayList<IWizardPageUpdateListener>();
	
	public AbstractWizardPage() {
		this(null);
	}
	
	public AbstractWizardPage(String id) {
		this.id = id != null ? id : this.getClass().getCanonicalName();
		this.pageComplete = false;
		//this.helpContext = new HelpContext(this.getClass());
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public IWizard getWizard() {
		return wizard;
	}
	
	@Override
	public void setWizard(IWizard wizard) {
		this.wizard = wizard;
	}
	
	@Override
	public boolean isComplete() {
		return pageComplete;
	}
	
	public void setComplete(boolean complete) {
		this.pageComplete = complete;
		fireUpdated();
	}

	@Override
	public JComponent createControls() {
		return this;
	}
	
	@Override
	public void updateControls() {
		// do nothing
	}

	@Override
	public void updateModel() {
		// do nothing
	}
	
	@Override
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
		fireUpdated();
	}

	@Override
	public Icon getLogo() {
		return logo;
	}

	public void setLogo(Icon logo) {
		this.logo = logo;
	}

	@Override
	public MessageStatus getStatus() {
		return this.status;
	}

	public void setStatus(MessageStatus status) {
		this.status = status;
		fireUpdated();
	}

	@Override
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
		fireUpdated();
	}

	@Override
	public HelpContext getHelpContext() {
		return helpContext;
	}

	@Override
	public void addPageUpdateListener(IWizardPageUpdateListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removePageUpdateListener(IWizardPageUpdateListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void setHelpContext(HelpContext helpContext) {
		this.helpContext = helpContext;
	}

	public void setMessage(MessageStatus status, String message) {
		this.status = status;
		this.message = message;
		fireUpdated();
	}

	protected void fireUpdated() {
		for (IWizardPageUpdateListener l : listeners)
			l.pageUpdated(this);
	}
}
