package es.imim.bg.ztools.ui.dialogs.wizardpanels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.net.URL;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

public class ZCalcAnalysisMainPanel extends JPanel {
	
    private JLabel blankSpace;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JLabel jLabel9;
    
    private JTextField jtextField1;

    private JLabel welcomeTitle;
    private JPanel contentPanel;
    
    private JLabel iconLabel;
    private ImageIcon icon;

	public ZCalcAnalysisMainPanel(Map<String, String> dataModel){
		
        iconLabel = new JLabel();
        contentPanel = getContentPanel();
        contentPanel.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));

        //icon = getImageIcon();

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
        
        welcomeTitle = new JLabel();
        blankSpace = new JLabel();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        jLabel4 = new JLabel();
        jLabel5 = new JLabel();
        jLabel7 = new JLabel();
        jLabel6 = new JLabel();
        jLabel8 = new JLabel();
        jLabel9 = new JLabel();
        
        jtextField1 = new JTextField();

        contentPanel1.setLayout(new java.awt.BorderLayout());

        welcomeTitle.setText("Welcome to the ZCalc Analysis Wizard!");
        welcomeTitle.setFont(new java.awt.Font(welcomeTitle.getFont().getName(), Font.BOLD, 18));
        contentPanel1.add(welcomeTitle, java.awt.BorderLayout.NORTH);

        jPanel1.setLayout(new java.awt.GridLayout(0, 1));

        jPanel1.add(blankSpace);
        jLabel1.setText("You're about to employ a ZCalc Statistical Analysis for your data.");
        jPanel1.add(jLabel1);
        jLabel2.setText("But first of all, please indicate a name for your analysis and how  ");
        jPanel1.add(jLabel2);
        jLabel3.setText("many processors you want to make use of, in case you're working ");
        jPanel1.add(jLabel3);
        jLabel4.setText("with a multiprocessor machine");
        jPanel1.add(jLabel4);
        jPanel1.add(blankSpace);        
        
        jPanel2.setLayout(new BoxLayout(jPanel2, BoxLayout.LINE_AXIS));
        
        jLabel5.setText("Analysis Name: ");
        jPanel2.add(jLabel5);
        jtextField1.setToolTipText("Choose a Name for your Analysis");
        jPanel2.add(jtextField1);
        
        int processors =  java.lang.Runtime.getRuntime().availableProcessors();
        

        /*jLabel7.setText("'Finish' button, and it depends on the user's entries as to how they ");
        jPanel1.add(jLabel7);
        jLabel6.setText("traverse the path). That's not the case with this example, however.");
        jPanel1.add(jLabel6);
        jPanel1.add(jLabel8);
        jLabel9.setText("Press the 'Next' button to continue....");
        jPanel1.add(jLabel9);*/

        contentPanel1.add(jPanel1, java.awt.BorderLayout.CENTER);
        contentPanel1.add(jPanel2, BorderLayout.SOUTH);

        return contentPanel1;
        
    }

   /* private ImageIcon getImageIcon() {
        return new ImageIcon((URL)getResource("clouds.jpg"));
    }*/
    
    private Object getResource(String key) {

        URL url = null;
        String name = key;

        if (name != null) {

            try {
                Class c = Class.forName("com.nexes.test.Main");
                url = c.getResource(name);
            } catch (ClassNotFoundException cnfe) {
                System.err.println("Unable to find Main class");
            }
            return url;
        } else
            return null;

    }
		
}


