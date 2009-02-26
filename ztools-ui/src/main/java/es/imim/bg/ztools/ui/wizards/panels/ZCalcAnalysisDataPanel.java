package es.imim.bg.ztools.ui.wizards.panels;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import es.imim.bg.ztools.ui.wizards.AnalysisWizard;
import es.imim.bg.ztools.ui.wizards.AnalysisWizard.Condition;

public class ZCalcAnalysisDataPanel extends JPanel {
	
	private static final long serialVersionUID = 4868634835041548193L;
	
	public String BIN_CUTOFF_DISABLED;
	
	private JLabel blankSpace;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;

    
    private JComboBox jComboBox1;
    
    private JTextField jTextField1;
    private JTextField jTextField2;
    
    private JButton jButton1;

    
    private JLabel panelTitle;
    private JPanel contentPanel;
    

	public ZCalcAnalysisDataPanel(){
		
		BIN_CUTOFF_DISABLED = AnalysisWizard.DISABLED;
		
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

        
        jComboBox1 = new JComboBox();
        
        jTextField1 = new JTextField();
        jTextField2 = new JTextField();

        jButton1 = new JButton("choose File..");
        
        contentPanel1.setLayout(new java.awt.BorderLayout());

        panelTitle.setText("Data");
        panelTitle.setFont(new java.awt.Font(panelTitle.getFont().getName(), Font.BOLD, 18));
        contentPanel1.add(panelTitle, java.awt.BorderLayout.NORTH);

        jPanel1.setLayout(new java.awt.GridLayout(0, 1));

        jPanel1.add(blankSpace);
        jLabel1.setText("Please choose the file with the data to analyise. In case");
        jPanel1.add(jLabel1);
        jLabel2.setText("it contains continous data, indicate by what criteria you");
        jPanel1.add(jLabel2);
        jLabel3.setText("want to convert it into binary data. ");
        jPanel1.add(jLabel3);
        jPanel1.add(blankSpace); 
        
        jPanel2.setLayout(new BoxLayout(jPanel2, BoxLayout.PAGE_AXIS));
        
        jPanel3.setLayout(new BoxLayout(jPanel3, BoxLayout.LINE_AXIS)); 
        jLabel4.setText("Data Table File: ");
        jTextField1.setToolTipText("Choose the data file");
        jTextField1.setEditable(false);
        jPanel3.add(jLabel4);
        jPanel3.add(jTextField1);
        jPanel3.add(jButton1);
        jPanel2.add(jPanel3);
        
        jPanel4.setLayout(new BoxLayout(jPanel4, BoxLayout.LINE_AXIS));
        Condition[] conditions =  Condition.values();
        jComboBox1.addItem(BIN_CUTOFF_DISABLED);
        for (int i = 0; i < conditions.length; i++)
            jComboBox1.addItem(conditions[i].toString());
        jLabel5.setText("Binary cut-off : ");
        jTextField2.setToolTipText("Enter a numerical cut-off value");
        jPanel4.add(jLabel5);
        jPanel4.add(jComboBox1);
        jPanel4.add(jTextField2);
        jPanel2.add(blankSpace);
        jPanel2.add(jPanel4);

        contentPanel1.add(jPanel1, java.awt.BorderLayout.CENTER);
        contentPanel1.add(jPanel2, BorderLayout.SOUTH);

        return contentPanel1;
        
    }
	
	public JButton getChooserButton(){
		return jButton1;
	}
	
	public JTextField getFileNameField(){
		return jTextField1;
	}
	
	public JComboBox getBinCutoffConditionBox(){
		return jComboBox1;
	}
	
	public JTextField getBinCutoffValueField() {
		return jTextField2;
	}		
}


