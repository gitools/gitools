package es.imim.bg.ztools.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import es.imim.bg.ztools.ui.AppFrame;

public class AboutDialog extends JDialog {

	private static final long serialVersionUID = -5869809986725283792L;
	
	private String appName;
	private String appVersion;
	
	public AboutDialog(AppFrame owner) {
		super(owner);
		
		appName = AppFrame.instance().getAppName();
		appVersion = AppFrame.instance().getAppVersion();
		
		setModal(true);
		setTitle("About " + appName);
		
		createComponents();
		
		pack();
	}

	private void createComponents() {
		JPanel contPanel = new JPanel();
		contPanel.setLayout(new BorderLayout());
		contPanel.add(new JLabel(appName + " " + appVersion));
		
		JButton acceptBtn = new JButton("OK");
		acceptBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closeDialog();
			}
		});
		
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new BorderLayout());
		btnPanel.add(acceptBtn, BorderLayout.EAST);
		
		setLayout(new BorderLayout());
		add(contPanel, BorderLayout.CENTER);
		add(btnPanel, BorderLayout.SOUTH);
	}

	protected void closeDialog() {
		setVisible(false);
	}
}
