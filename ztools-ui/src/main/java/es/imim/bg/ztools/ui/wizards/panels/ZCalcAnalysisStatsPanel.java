package es.imim.bg.ztools.ui.wizards.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import es.imim.bg.ztools.ui.wizards.AnalysisWizard.StatTest;

public class ZCalcAnalysisStatsPanel extends JPanel {
	
	private static final long serialVersionUID = 4868634835041548193L;
    public final String DEFAULT_SAMPLE_SIZE = "10000";
		
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
    
    private JLabel panelTitle;
    private JPanel contentPanel;

	public ZCalcAnalysisStatsPanel(){
		
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

        panelTitle = new JLabel();
        blankSpace = new JLabel();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        jLabel4 = new JLabel();
        jLabel5 = new JLabel();
        jLabel6 = new JLabel();
        jLabel7 = new JLabel();
        
        jComboBox1 = new JComboBox();
        
        jTextField1 = new JTextField();
        
        contentPanel1.setLayout(new java.awt.BorderLayout());

        panelTitle.setText("Statistics");
        panelTitle.setFont(new java.awt.Font(panelTitle.getFont().getName(), Font.BOLD, 18));
        contentPanel1.add(panelTitle, java.awt.BorderLayout.NORTH);

        jPanel1.setLayout(new java.awt.GridLayout(0, 1));

        jPanel1.add(blankSpace);
        jLabel1.setText("Choose the statistical test you want to apply and the number");
        jPanel1.add(jLabel1);
        jLabel2.setText("of samples to use when randomizing. The default value for the");
        jPanel1.add(jLabel2);
        jLabel3.setText("sample set size is " + DEFAULT_SAMPLE_SIZE);
        jPanel1.add(jLabel3);

        jPanel1.add(blankSpace); 
        
        jPanel2.setLayout(new BoxLayout(jPanel2, BoxLayout.PAGE_AXIS));
        
        jPanel3.setLayout(new BoxLayout(jPanel3, BoxLayout.LINE_AXIS)); 
        jLabel4.setText("Satistical test ");
        jPanel3.add(jLabel4);
        jLabel6.setText(": ");
        jPanel3.add(jLabel6);
        StatTest[] stattests =  StatTest.values();
        for (int i = 0; i < stattests.length; i++)
            jComboBox1.addItem(stattests[i].toString());
        jPanel3.add(jComboBox1);
        jLabel5.setText("<html>(Help)</html>");
        jLabel5.setForeground(Color.BLUE);
        jPanel3.add(jLabel5);
        jPanel2.add(jPanel3);
        jPanel2.add(blankSpace);

        
        jPanel4.setLayout(new BoxLayout(jPanel4, BoxLayout.LINE_AXIS));
        
        jPanel2.add(blankSpace);
        jLabel7.setText("Sample size : ");
        jPanel4.add(jLabel7);
        jTextField1.setToolTipText("Default: " + DEFAULT_SAMPLE_SIZE);
        jPanel4.add(jTextField1);
        jPanel2.add(jPanel4);      

        contentPanel1.add(jPanel1, java.awt.BorderLayout.CENTER);
        contentPanel1.add(jPanel2, BorderLayout.SOUTH);

        return contentPanel1;
        
    }
	
	public JLabel getHelpLabel(){
		return jLabel5;
	}
	
	public JTextField getSampleSizeField(){
		return jTextField1;
	}
	
	public JComboBox getStatTestBox(){
		return jComboBox1;
	}
}


