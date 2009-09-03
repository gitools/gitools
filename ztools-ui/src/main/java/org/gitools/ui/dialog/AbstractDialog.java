package org.gitools.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;

import org.gitools.ui.dialog.DialogHeaderPanel.Status;

public abstract class AbstractDialog extends JDialog {

	private static final long serialVersionUID = 5886096207448862426L;

	DialogHeaderPanel hdrPanel;
	
	protected JComponent container;
	
	public AbstractDialog(
			Window owner,
			String title, String header, 
			String message, Status status,
			Icon logo) {
	
		super(owner, title);
		
		createComponents(header, message, status, logo);
		
		setLocationRelativeTo(owner);
		setMinimumSize(new Dimension(300, 260));
	}
	
	public AbstractDialog(Window owner, String title, Icon icon) {
		this(owner, title, "", "", Status.normal, icon);
	}

	public void open() {
		setVisible(true);
	}
	
	protected JComponent getContainer() {
		return container;
	}
	
	protected void setContainer(JComponent container) {
		this.container = container;
	}
	
	protected void createComponents(
			String header, 
			String message, 
			Status status,
			Icon logo) {
		
		hdrPanel = new DialogHeaderPanel(
				header, message, status, logo);
		
		container = createContainer();
		
		final DialogButtonsPanel buttonsPanel = 
			new DialogButtonsPanel(
					createButtons());
		
		setLayout(new BorderLayout());
		add(hdrPanel, BorderLayout.NORTH);
		if (container != null)
			add(container, BorderLayout.CENTER);
		add(buttonsPanel, BorderLayout.SOUTH);
	}

	public DialogHeaderPanel getHeaderPanel() {
		return hdrPanel;
	}
	
	protected abstract JComponent createContainer();
	
	protected abstract List<JButton> createButtons();
}
