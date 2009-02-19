package es.imim.bg.ztools.ui.dialogs.wizardpanels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

public class ZCalcAnalysisMainPanel extends JPanel {

	private static final long serialVersionUID = 4868634835041548193L;

	private JLabel blankSpace;
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JLabel jLabel3;
	private JLabel jLabel4;
	private JLabel jLabel5;
	private JLabel jLabel6;
	private JLabel jLabel7;

	private JComboBox jComboBox1;

	private JTextField jTextField1;
	private JTextField jTextField2;

	private JButton jButton1;

	private JLabel welcomeTitle;
	private JPanel contentPanel;

	private JLabel iconLabel;
	private ImageIcon icon;

	public ZCalcAnalysisMainPanel() {

		iconLabel = new JLabel();
		contentPanel = getContentPanel();
		contentPanel.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));

		// icon = getImageIcon();

		setLayout(new java.awt.BorderLayout());

		if (icon != null)
			iconLabel.setIcon(icon);

		iconLabel.setBorder(new EtchedBorder(EtchedBorder.RAISED));

		add(iconLabel, BorderLayout.WEST);

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
		jLabel4 = new JLabel();
		jLabel5 = new JLabel();
		jLabel6 = new JLabel();
		jLabel7 = new JLabel();

		jComboBox1 = new JComboBox();
		jComboBox1.setMaximumSize(new Dimension(250, 30));

		jTextField1 = new JTextField();
		jTextField1.setMaximumSize(new Dimension(340, 30));
		jTextField2 = new JTextField();
		jTextField2.setMaximumSize(new Dimension(230, 30));

		jButton1 = new JButton("choose Dir..");

		contentPanel1.setLayout(new java.awt.BorderLayout());

		welcomeTitle.setText("Welcome to the ZCalc Analysis Wizard!");
		welcomeTitle.setFont(new java.awt.Font(
				welcomeTitle.getFont().getName(), Font.BOLD, 18));
		contentPanel1.add(welcomeTitle, java.awt.BorderLayout.NORTH);

		jPanel1.setLayout(new java.awt.GridLayout(0, 1));

		jPanel1.add(blankSpace);
		jLabel1
				.setText("You're about to employ a ZCalc Statistical Analysis for your data.");
		jPanel1.add(jLabel1);
		jLabel2
				.setText("But first of all, please indicate a name for your analysis and how  ");
		jPanel1.add(jLabel2);
		jLabel3
				.setText("many processors you want to make use of, in case you're working ");
		jPanel1.add(jLabel3);
		jLabel4.setText("with a multiprocessor machine");
		jPanel1.add(jLabel4);
		jPanel1.add(blankSpace);

		jPanel2.setLayout(new BoxLayout(jPanel2, BoxLayout.PAGE_AXIS));
		jPanel2.getLayout();

		jPanel3.setLayout(new BoxLayout(jPanel3, BoxLayout.LINE_AXIS));
		jLabel5.setText("Analysis Name: ");
		jPanel3.add(jLabel5);
		jTextField1.setToolTipText("Choose a Name for your Analysis");
		jPanel3.add(jTextField1);
		jPanel2.add(jPanel3);

		jPanel4.setLayout(new BoxLayout(jPanel4, BoxLayout.LINE_AXIS));
		jLabel6.setText("Working Directory:");
		jPanel4.add(jLabel6);
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
		jLabel7.setText("Number of processors to use: ");
		jPanel5.add(jLabel7);
		jPanel5.add(jComboBox1);
		jPanel2.add(jPanel5);

		contentPanel1.add(jPanel1, java.awt.BorderLayout.CENTER);
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

	/*
	 * private ImageIcon getImageIcon() { return new
	 * ImageIcon((URL)getResource("clouds.jpg")); }
	 * 
	 * private Object getResource(String key) {
	 * 
	 * URL url = null; String name = key;
	 * 
	 * if (name != null) {
	 * 
	 * try { Class c = Class.forName("com.nexes.test.Main"); url =
	 * c.getResource(name); } catch (ClassNotFoundException cnfe) {
	 * System.err.println("Unable to find Main class"); } return url; } else
	 * return null;
	 * 
	 * }
	 */

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}

}
