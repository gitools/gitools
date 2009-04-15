package es.imim.bg.ztools.ui.wizard.zetcalc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import es.imim.bg.ztools.ui.wizard.AnalysisWizard.StatTest;

public class ZCalcAnalysisStatsPanel extends JPanel {
	
	private static final long serialVersionUID = 4868634835041548193L;
    public final String DEFAULT_SAMPLE_SIZE = "10000";
		
	private JLabel blankSpace;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    
	private JTextArea jTextArea1;
    
    private JComboBox jComboBox1;
    
    private JTextField jTextField1;
    
    private JLabel panelTitle;
    private JPanel contentPanel;

	public ZCalcAnalysisStatsPanel(){
		
        contentPanel = getContentPanel();
        contentPanel.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
        setLayout(new java.awt.BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
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
        
		jTextArea1 = new JTextArea();
        
        jComboBox1 = new JComboBox();
        
        jTextField1 = new JTextField();
        
        contentPanel1.setLayout(new java.awt.BorderLayout());

        panelTitle.setText("Statistics");
        panelTitle.setFont(new java.awt.Font(panelTitle.getFont().getName(), Font.BOLD, 18));
        contentPanel1.add(panelTitle, java.awt.BorderLayout.NORTH);

        jPanel1.setLayout(new BorderLayout());
        jPanel2.setLayout(new BoxLayout(jPanel2, BoxLayout.PAGE_AXIS));
        
        jTextArea1.setText(
        		"Choose the statistical test you want to apply and in case you chose z-score test the number " +
        		"of samples to use when bootstraping. The default value for the " +
        		"sample set size is " + DEFAULT_SAMPLE_SIZE);
		jTextArea1.setOpaque(false);
		jTextArea1.setLineWrap(true);
		jTextArea1.setWrapStyleWord(true);
		jTextArea1.setEditable(false);
		jPanel2.add(jTextArea1);
		blankSpace.setText(" ");
        jPanel2.add(blankSpace); 
                
        jPanel3.setLayout(new BoxLayout(jPanel3, BoxLayout.LINE_AXIS)); 
        jLabel1.setText("Satistical test ");
        jPanel3.add(jLabel1);
        jLabel3.setText(": ");
        jPanel3.add(jLabel3);
        StatTest[] stattests =  StatTest.values();
        for (int i = 0; i < stattests.length; i++)
            jComboBox1.addItem(stattests[i].toString());
        jPanel3.add(jComboBox1);
        jLabel2.setText("<html>(Help)</html>");
        jLabel2.setForeground(Color.BLUE);
        jPanel3.add(jLabel2);
        jPanel2.add(jPanel3);

        jPanel4.setLayout(new BoxLayout(jPanel4, BoxLayout.LINE_AXIS));
        
        jLabel4.setText("Sample size : ");
        jPanel4.add(jLabel4);
        jTextField1.setToolTipText("Default: " + DEFAULT_SAMPLE_SIZE);
        jPanel4.add(jTextField1);
        jPanel2.add(jPanel4);      

        jPanel1.add(jPanel2, BorderLayout.NORTH);
        contentPanel1.add(jPanel1, java.awt.BorderLayout.CENTER);

        return contentPanel1;
        
    }
	
	public JLabel getHelpLabel(){
		return jLabel2;
	}
	
	public JTextField getSampleSizeField(){
		return jTextField1;
	}
	
	public JComboBox getStatTestBox(){
		return jComboBox1;
	}
}


