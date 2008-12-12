package es.imim.bg.ztools.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Color;
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

import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.IconNames;
import es.imim.bg.ztools.ui.utils.IconUtils;

public class FilterRowsByNameDialog extends JDialog {
	
	private static final long serialVersionUID = 4201760423693544699L;
	private String appName;
	private String appVersion;
	
	public FilterRowsByNameDialog(JFrame owner) {
		super(owner);
		
		appName = AppFrame.instance().getAppName();
		appVersion = AppFrame.instance().getAppVersion();
		
		setModal(true);
		setTitle("About " + appName);
		
		createComponents();
		
		getContentPane().setBackground(Color.WHITE);
		pack();
	}

	private void createComponents() {
		JLabel imageLabel = new JLabel(
				IconUtils.getIconResource(IconNames.aboutLogo));
		imageLabel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		
		
		Checkbox checkbox = new Checkbox("use regular Expression");
		
		JTextPane listPane = new JTextPane();

		JButton loadBtn = new JButton("Load");
		loadBtn.setMargin(new Insets(0, 30, 0, 30));
		loadBtn.addActionListener(new ActionListener() {
			//@Override
			public void actionPerformed(ActionEvent e) {
				closeDialog();
			}
		});
		
		JButton acceptBtn = new JButton("OK");
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
		btnPanel.add(loadBtn, BorderLayout.EAST);
		btnPanel.add(acceptBtn, BorderLayout.EAST);

		
		JPanel contPanel = new JPanel();
		contPanel.setLayout(new BorderLayout());
		contPanel.add(btnPanel, BorderLayout.NORTH);
		contPanel.add(checkbox, BorderLayout.SOUTH);
		contPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		
		setLayout(new BorderLayout());
		add(listPane, BorderLayout.WEST);
		add(contPanel, BorderLayout.CENTER);
	}

	protected void closeDialog() {
		setVisible(false);
	}
}
