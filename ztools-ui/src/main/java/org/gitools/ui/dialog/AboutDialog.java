package org.gitools.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import org.gitools.ui.IconNames;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.utils.IconUtils;


public class AboutDialog extends JDialog {

	private static final long serialVersionUID = -5869809986725283792L;
	
	private String appName;
	private String appVersion;
	
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
		imageLabel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		
		JTextPane infoPane = new JTextPane();
		infoPane.setFocusable(false);
		//infoPane.setBackground(Color.WHITE);
		infoPane.setContentType("text/html");
		//final JScrollPane scrollPane = new JScrollPane(infoPane);
		//scrollPane.setBorder(BorderFactory.createEmptyBorder());
		//scrollPane.setBorder(
		//		BorderFactory.createEmptyBorder(8, 8, 8, 8));
		
		//FIXME: It should load this text from a resource
		infoPane.setText("<h1>" + appName + "</h1>" +
						"<p>Version " + appVersion + "</p>" +
						"<p>Biomedical Genomics Laboratory at GRIB<br>" +
						"Parc de Recerca Biomedica de Barcelona (PRBB)</p>" +
						"<p>Authors:<ul>" +
						"<li>Nuria Lopez-Bigas</li>" + 
						"<li>Christian Perez-Llamas</li>" +
						"<li>Michael Schroeder</li></ul></p>" +
						"<p>Thanks to:<ul>" +
						"<li>Sonja Haenzelmann</li>" +
						"<li>Khademul Islam</li>" +
						"<li>Gunnes Gundem</li></ul></p>");
		
		JButton acceptBtn = new JButton("Accept");
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
		contPanel.add(infoPane, BorderLayout.CENTER);
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
