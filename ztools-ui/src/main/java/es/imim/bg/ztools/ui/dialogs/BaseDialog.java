package es.imim.bg.ztools.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;


public abstract class BaseDialog extends JDialog{
	

	private static final long serialVersionUID = 183914906195441688L;
	
	String dialogTitle;
	String dialogSubtitle;
	
	public BaseDialog(JFrame owner, String windowTitle, String dialogTitle) {
		setModal(true);
		setLocationByPlatform(true);				
		setTitle(windowTitle);
		setDialogSubtitle(dialogTitle);
	}
		
	//Titles
	protected void setDialogTitle(String dialogTitle){
		this.dialogTitle = dialogTitle;
	}
	public void setDialogSubtitle(String dialogSubtitle){
		this.dialogTitle = dialogSubtitle;
	}
	abstract void setIcon();
	//Components
	abstract void addButton(JButton button);
	abstract void removeButton(JButton button);
	
	protected void createComponents(JComponent cntComponent){
		
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new BorderLayout());
		JTextField titleField = new JTextField();
		titleField.setText(dialogTitle);
		titlePanel.add(titleField, BorderLayout.CENTER);
		if (dialogSubtitle != null){
			JTextField subtitleField = new JTextField();
			subtitleField.setText(dialogSubtitle);
			Font subtitleFont = new Font(
					subtitleField.getFont().getName(),
					subtitleField.getFont().getStyle(),
					12);
			subtitleField.setFont(subtitleFont);
			titlePanel.add(subtitleField, BorderLayout.SOUTH);
		}
			
		
		
		setLayout(new BorderLayout());
		add(titlePanel, BorderLayout.NORTH);
		add(cntComponent, BorderLayout.CENTER);
	}
	
}
