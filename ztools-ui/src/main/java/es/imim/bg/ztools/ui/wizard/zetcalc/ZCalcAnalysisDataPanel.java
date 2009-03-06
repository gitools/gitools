package es.imim.bg.ztools.ui.wizard.zetcalc;

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
import javax.swing.border.EmptyBorder;

import es.imim.bg.ztools.ui.wizard.AnalysisWizard;
import es.imim.bg.ztools.ui.wizard.AnalysisWizard.Condition;

public class ZCalcAnalysisDataPanel extends JPanel {
	
	private static final long serialVersionUID = 4868634835041548193L;
	
	public String BIN_CUTOFF_DISABLED;
	
	private JLabel blankSpace;
    private JLabel jLabel1;
    private JLabel jLabel2;

	private JTextArea jTextArea1;
    
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

		jTextArea1 = new JTextArea();
        
        jComboBox1 = new JComboBox();
        
        jTextField1 = new JTextField();
        jTextField2 = new JTextField();

        jButton1 = new JButton("choose File..");
        
        contentPanel1.setLayout(new java.awt.BorderLayout());

        panelTitle.setText("Data");
        panelTitle.setFont(new java.awt.Font(panelTitle.getFont().getName(), Font.BOLD, 18));
        contentPanel1.add(panelTitle, java.awt.BorderLayout.NORTH);

        jPanel1.setLayout(new BorderLayout());
        jPanel2.setLayout(new BoxLayout(jPanel2, BoxLayout.PAGE_AXIS));

        
        jTextArea1.setText(
        		"Please choose the file with the data to analyise. In case " +
        		"it contains continous data, indicate by what criteria you " +
        		"want to convert it into binary data.");
		jTextArea1.setOpaque(false);
		jTextArea1.setLineWrap(true);
		jTextArea1.setWrapStyleWord(true);
		jTextArea1.setEditable(false);
		jPanel2.add(jTextArea1);
		blankSpace.setText(" ");
        jPanel2.add(blankSpace); 
                
        jPanel3.setLayout(new BoxLayout(jPanel3, BoxLayout.LINE_AXIS)); 
        jLabel1.setText("Data Table File: ");
        jTextField1.setToolTipText("Choose the data file");
        jPanel3.add(jLabel1);
        jPanel3.add(jTextField1);
        jPanel3.add(jButton1);
        jPanel2.add(jPanel3);
        
        jPanel4.setLayout(new BoxLayout(jPanel4, BoxLayout.LINE_AXIS));
        Condition[] conditions =  Condition.values();
        jComboBox1.addItem(BIN_CUTOFF_DISABLED);
        for (int i = 0; i < conditions.length; i++)
            jComboBox1.addItem(conditions[i].toString());
        jLabel2.setText("Binary cut-off : ");
        jTextField2.setToolTipText("Enter a numerical cut-off value");
        jPanel4.add(jLabel2);
        jPanel4.add(jComboBox1);
        jPanel4.add(jTextField2);
        jPanel2.add(jPanel4);

        jPanel1.add(jPanel2, BorderLayout.NORTH); //prevent expanding in north
        contentPanel1.add(jPanel1, BorderLayout.CENTER);

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


