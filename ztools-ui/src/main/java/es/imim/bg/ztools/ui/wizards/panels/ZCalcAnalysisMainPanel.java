package es.imim.bg.ztools.ui.wizards.panels;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import EDU.oswego.cs.dl.util.concurrent.FJTask.Wrap;

public class ZCalcAnalysisMainPanel extends JPanel {

	private static final long serialVersionUID = 4868634835041548193L;

	private JLabel blankSpace;
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JLabel jLabel3;
	
	private JTextArea jTextArea1;

	private JComboBox jComboBox1;

	private JTextField jTextField1;
	private JTextField jTextField2;

	private JButton jButton1;

	private JLabel welcomeTitle;
	private JPanel contentPanel;

	public ZCalcAnalysisMainPanel() {

		contentPanel = getContentPanel();
		contentPanel.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
		
		setLayout(new java.awt.BorderLayout());


		JPanel secondaryPanel = new JPanel();
		secondaryPanel.add(contentPanel, BorderLayout.NORTH);
		add(secondaryPanel, BorderLayout.CENTER);
	}

	private JPanel getContentPanel() {

		JPanel contentPanel1 = new JPanel();
		JPanel jPanel1 = new JPanel();
		JPanel jPanel2 = new JPanel();
		JPanel jPanel3 = new JPanel();
		JPanel jPanel4 = new JPanel();
		JPanel jPanel5 = new JPanel();
		
		welcomeTitle = new JLabel();
		blankSpace = new JLabel();
		jLabel1 = new JLabel();
		jLabel2 = new JLabel();
		jLabel3 = new JLabel();
		
		jTextArea1 = new JTextArea();
		
		jComboBox1 = new JComboBox();
		
		jTextField1 = new JTextField();
		jTextField2 = new JTextField();

		jButton1 = new JButton("choose Dir..");

		contentPanel1.setLayout(new java.awt.BorderLayout());
		
		welcomeTitle.setText("Welcome to the ZCalc Analysis Wizard!");
		welcomeTitle.setFont(new java.awt.Font(
				welcomeTitle.getFont().getName(), Font.BOLD, 18));
		contentPanel1.add(welcomeTitle, java.awt.BorderLayout.NORTH);

		jPanel1.setLayout(new BoxLayout(jPanel1, BoxLayout.PAGE_AXIS));

		jTextArea1.setText("You're about to employ a ZCalc Statistical Analysis for your data. " +
				"But first of all, please indicate a name for your analysis and how  " +
				"many processors you want to make use of, in case you're working " +
				"with a multiprocessor machine");
		jTextArea1.setOpaque(false);
		jTextArea1.setLineWrap(true);
		jTextArea1.setWrapStyleWord(true);
		jTextArea1.setEditable(false);
		jPanel1.add(jTextArea1);
		blankSpace.setText(" ");
		jPanel1.add(blankSpace);
		
		jPanel2.setLayout(new BoxLayout(jPanel2, BoxLayout.PAGE_AXIS));
		jPanel2.getLayout();

		jPanel3.setLayout(new BoxLayout(jPanel3, BoxLayout.LINE_AXIS));
		jLabel1.setText("Analysis Name: ");
		jPanel3.add(jLabel1);
		jTextField1.setToolTipText("Choose a Name for your Analysis");
		jPanel3.add(jTextField1);
		jPanel2.add(jPanel3);

		jPanel4.setLayout(new BoxLayout(jPanel4, BoxLayout.LINE_AXIS));
		jLabel2.setText("Working Directory:");
		jPanel4.add(jLabel2);
		jTextField2.setToolTipText("Choose the data file");
		jTextField2.setEditable(false);
		jPanel4.add(jTextField2);
		jPanel4.add(jButton1);
		jPanel2.add(jPanel4);

		jPanel5.setLayout(new BoxLayout(jPanel5, BoxLayout.LINE_AXIS));
		int processors = java.lang.Runtime.getRuntime().availableProcessors();
		for (int i = 1; i <= processors; i++)
			jComboBox1.addItem(i);
		jComboBox1.setSelectedIndex(jComboBox1.getItemCount() - 1);
		jLabel3.setText("Number of processors to use: ");
		jPanel5.add(jLabel3);
		jPanel5.add(jComboBox1);
		jPanel2.add(jPanel5);

		contentPanel1.add(jPanel1, BorderLayout.CENTER);
		contentPanel1.add(jPanel2, BorderLayout.SOUTH);


		return contentPanel1;

	}

	public boolean textFieldNotEmpty() {
		return !jTextField1.getText().isEmpty();
	}

	public JTextField getAnalysisNameField() {
		return jTextField1;
	}

	public JComboBox getProcessorComboBox() {
		return jComboBox1;
	}

	public JButton getChooserButton() {
		return jButton1;
	}

	public JTextField getWorkDirField() {
		return jTextField2;
	}
}
