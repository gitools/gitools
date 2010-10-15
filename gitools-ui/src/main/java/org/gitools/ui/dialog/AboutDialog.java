package org.gitools.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import org.apache.velocity.VelocityContext;

import org.gitools.ui.IconNames;
import org.gitools.ui.platform.panel.TemplatePanel;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.IconUtils;


public class AboutDialog extends JDialog {

	private static final long serialVersionUID = -5869809986725283792L;
	
	private String appName;
	private String appVersion;

	private TemplatePanel creditsPane;
	
	public AboutDialog(JFrame owner) {
		super(owner);
		
		appName = AppFrame.getAppName();
		appVersion = AppFrame.getAppVersion();
		
		setModal(true);
		setTitle("About " + appName);
		
		createComponents();
		
		//getContentPane().setBackground(Color.WHITE);

		pack();
	}

	private void createComponents() {
		JLabel imageLabel = new JLabel(
				IconUtils.getIconResource(IconNames.aboutLogo));
		imageLabel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 0));
		imageLabel.setVerticalAlignment(SwingConstants.TOP);

		creditsPane = new TemplatePanel();
		creditsPane.setFocusable(false);
		Dimension dim = new Dimension(550, 374);
		creditsPane.setPreferredSize(dim);
		creditsPane.setMaximumSize(dim);
		try {
			creditsPane.setTemplateFromResource("/vm/about.vm");
			VelocityContext context = new VelocityContext();
			context.put("appName", appName);
			context.put("appVersion", appVersion);
			creditsPane.setContext(context);
			creditsPane.render();
		}
		catch (Exception ex) {
			System.err.println("Unexpected error creating credits pane.");
		}
		
		JButton acceptBtn = new JButton("Close");
		acceptBtn.setMargin(new Insets(0, 30, 0, 30));
		acceptBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closeDialog();
			}
		});
		
		JPanel btnPanel = new JPanel();
		btnPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		btnPanel.setLayout(new BorderLayout());
		btnPanel.add(acceptBtn, BorderLayout.EAST);

		JPanel contPanel = new JPanel();
		contPanel.setLayout(new BorderLayout());
		contPanel.add(creditsPane, BorderLayout.CENTER);
		contPanel.add(btnPanel, BorderLayout.SOUTH);
		contPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		
		setLayout(new BorderLayout());
		add(imageLabel, BorderLayout.WEST);
		add(contPanel, BorderLayout.CENTER);
	}

	protected void closeDialog() {
		setVisible(false);
	}
}
