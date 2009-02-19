package es.imim.bg.ztools.ui.dialogs.wizardpanels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.TextArea;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.net.URL;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.dialogs.AnalysisWizard.Condition;
import es.imim.bg.ztools.ui.dialogs.AnalysisWizard.StatTest;
import es.imim.bg.ztools.ui.utils.Options;

public class ZCalcAnalysisStatsHelpPanel extends JPanel {
	
	private static final long serialVersionUID = 4868634835041548193L;
    public final String DEFAULT_SAMPLE_SIZE = "10000";
		
  /*  private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JLabel jLabel9;
    private JLabel jLabel10;
    private JLabel jLabel11;
    private JLabel jLabel12;
    private JLabel jLabel13;
    private JLabel jLabel14;
    private JLabel jLabel15;
    private JLabel jLabel16;
    private JLabel jLabel17;
    private JLabel jLabel18;
    private JLabel jLabel19;
    private JLabel jLabel20;
    private JLabel jLabel21;
    private JLabel jLabel22;
    private JLabel jLabel23;
    private JLabel jLabel24;
    private JLabel jLabel25;
    private JLabel jLabel26;
    private JLabel jLabel27;
    private JLabel jLabel28;
    private JLabel jLabel29;
    private JLabel jLabel30;
    private JLabel jLabel31;
    private JLabel jLabel32;*/
    
    private JTextArea[] jTextAreas;

    
    private JLabel panelTitle;
    private JPanel contentPanel;
    
    private JLabel iconLabel;
    private ImageIcon icon;

	public ZCalcAnalysisStatsHelpPanel(){
		
        iconLabel = new JLabel();
        contentPanel = getContentPanel();
        contentPanel.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));

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

        jTextAreas = new JTextArea[37];
        jTextAreas[0] = new JTextArea(); //dummy
        Font normalFont = new Font (jTextAreas[0].getFont().getName(), 
        							jTextAreas[0].getFont().getStyle(), 
        							12);
        Font boldFont = new Font (jTextAreas[0].getFont().getName(), 
        							Font.BOLD, 
        							12);
        for (int i = 1; i < 37; i++) {
        	jTextAreas[i] = new JTextArea();
        	jTextAreas[i].setOpaque(false);
        	jTextAreas[i].setLineWrap(true);
        	//set titles bold
        	if (i < 5 || i % 4 == 1)
        		jTextAreas[i].setFont(boldFont);
        	else
        		jTextAreas[i].setFont(normalFont);
        }

        panelTitle = new JLabel();
       
        contentPanel1.setLayout(new java.awt.BorderLayout());

        panelTitle.setText("Statistics Help");
        panelTitle.setFont(new java.awt.Font(panelTitle.getFont().getName(), Font.BOLD, 18));
        contentPanel1.add(panelTitle, java.awt.BorderLayout.NORTH);

        GridBagLayout helpContentLayout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        int gridCols = 4;
        int gridRows = 9;
        jPanel1.setLayout(helpContentLayout);
        
        
        jTextAreas = fillfields(jTextAreas);
        
       
        for (int i = 1; i < 37; i++) {
        	c.fill = GridBagConstraints.HORIZONTAL;
        	c.gridx = (i-1) % gridCols;
        	c.gridy = (int) (i-1)/gridCols;
        	System.out.println("adding " + c.gridx + "/" + c.gridy);
        	jPanel1.add(jTextAreas[i], c);
        }
        
        
        contentPanel1.add(jPanel1, java.awt.BorderLayout.CENTER);

        return contentPanel1;
        
    }


	private JTextArea[] fillfields(JTextArea[] textAreas) {
        jTextAreas[1].setText("Name");
        //jPanel1.add(jTextAreas[1]);
        jTextAreas[2].setText("Description");
        //jPanel1.add(jTextAreas[2]);
        jTextAreas[3].setText("Requirements");
        //jPanel1.add(jTextAreas[3]);
        jTextAreas[4].setText("Reference");
        //jPanel1.add(jTextAreas[4]);
        
        jTextAreas[5].setText(StatTest.CHI_SQR.toString());
        //jPanel1.add(jTextAreas[5]);
        jTextAreas[6].setText("?");
        //jPanel1.add(jTextAreas[6]);
        jTextAreas[7].setText("?");
        //jPanel1.add(jTextAreas[7]);
        jTextAreas[8].setText("?");
        //jPanel1.add(jTextAreas[8]);

        
        jTextAreas[9].setText(StatTest.ZSCORE_MEAN.toString());
        //jPanel1.add(jTextAreas[9]);
        jTextAreas[10].setText("Z-Score using bootstrapping of means");
        //jPanel1.add(jTextAreas[10]);
        jTextAreas[11].setText("n >= 20, where n is the module size");
        //jPanel1.add(jTextAreas[11]);
        jTextAreas[12].setText("http://en.wikipedia.org/wiki/Bootstrapping_(statistics)");
        //jPanel1.add(jTextAreas[12]);
        
        jTextAreas[13].setText(StatTest.ZSCORE_MEDIAN.toString());
        //jPanel1.add(jTextAreas[13]);
        jTextAreas[14].setText("Z-Score using bootstrapping of medians");
        //jPanel1.add(jTextAreas[14]);
        jTextAreas[15].setText("n >= 20, where n is the module size");
        //jPanel1.add(jTextAreas[15]);
        jTextAreas[16].setText("http://en.wikipedia.org/wiki/Bootstrapping_(statistics)");
        //jPanel1.add(jTextAreas[16]);


        jTextAreas[17].setText(StatTest.BINOMINAL_EXACT.toString());
        //jPanel1.add(jTextAreas[17]);
        jTextAreas[18].setText("Binomial distribution B(n, p) test");
        //jPanel1.add(jTextAreas[18]);
        jTextAreas[19].setText("n >= 20, where n is the module size");
        //jPanel1.add(jTextAreas[19]);
        jTextAreas[20].setText("http://en.wikipedia.org/wiki/Binomial_distribution");
        //jPanel1.add(jTextAreas[20]);


        jTextAreas[21].setText(StatTest.BINOMINAL_NORMAL.toString());
        //jPanel1.add(jTextAreas[21]);
        jTextAreas[22].setText("Z-Score from a Binomial distribution B(n, p) using Normal distribution aproximation N(np, sqrt(np(1-p)))");
        //jPanel1.add(jTextAreas[22]);
        jTextAreas[23].setText("np > 5 and n(1-p) > 5, where n is the module size and p is the probability of 1.0 occurrence");
        //jPanel1.add(jTextAreas[23]);
        jTextAreas[24].setText("http://en.wikipedia.org/wiki/Binomial_distribution");
        //jPanel1.add(jTextAreas[24]);



        jTextAreas[25].setText(StatTest.BINOMINAL_POISSON.toString());
        //jPanel1.add(jTextAreas[25]);
        jTextAreas[26].setText("P-Value from Binomial distribution B(n, p) using Poisson distribution aproximation Pois(obs, np)");
        //jPanel1.add(jTextAreas[26]);
        jTextAreas[27].setText("(n >= 20 and p <= 0.05) or (n >= 100 and np <= 10), where n is the module size, obs is the observed frecuency and p is the probability of 1.0 occurrence");
        //jPanel1.add(jTextAreas[27]);
        jTextAreas[28].setText("http://en.wikipedia.org/wiki/Binomial_distribution");
        //jPanel1.add(jTextAreas[28]);


        jTextAreas[29].setText(StatTest.BINOMINAL.toString());
        //jPanel1.add(jTextAreas[29]);
        jTextAreas[30].setText("Binomial test using the best approximation possible (exact, poisson or normal)");
        //jPanel1.add(jTextAreas[30]);
        jTextAreas[31].setText("Those for binomial-exact / binomial-normal / binomial-poisson");
        //jPanel1.add(jTextAreas[31]);
        jTextAreas[32].setText("http://en.wikipedia.org/wiki/Binomial_distribution");
        //jPanel1.add(jTextAreas[32]);
        
        jTextAreas[33].setText(StatTest.FISHER.toString());
        //jPanel1.add(jTextAreas[33]);
        jTextAreas[34].setText("Fisher's Exact Test");
        //jPanel1.add(jTextAreas[34]);
        jTextAreas[35].setText("When the expected value < 10");
        //jPanel1.add(jTextAreas[35]);
        jTextAreas[36].setText("http://en.wikipedia.org/wiki/Fisher's_exact_test");
        //jPanel1.add(jTextAreas[36]);
		return jTextAreas;
	}


}


