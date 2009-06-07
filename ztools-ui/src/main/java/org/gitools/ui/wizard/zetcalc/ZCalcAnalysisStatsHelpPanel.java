package org.gitools.ui.wizard.zetcalc;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import org.gitools.ui.wizard.AnalysisWizard.StatTest;


public class ZCalcAnalysisStatsHelpPanel extends JPanel {
	
	private static final long serialVersionUID = 4868634835041548193L;
    public final String DEFAULT_SAMPLE_SIZE = "10000";
		
    private JTextArea[] jTextAreas;

    
    private JLabel panelTitle;
    private JPanel contentPanel;

	public ZCalcAnalysisStatsHelpPanel(){
		
        contentPanel = getContentPanel();
        contentPanel.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
        setLayout(new java.awt.BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
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
        	jTextAreas[i].setWrapStyleWord(true);
        	//set titles bold
        	if (i < 5 || i % 4 == 1)
        		jTextAreas[i].setFont(boldFont);
        	else
        		jTextAreas[i].setFont(normalFont);
        }

        panelTitle = new JLabel();
       
        contentPanel1.setLayout(new BorderLayout());

        panelTitle.setText("Statistics Help");
        panelTitle.setFont(new java.awt.Font(panelTitle.getFont().getName(), Font.BOLD, 18));
        contentPanel1.add(panelTitle, BorderLayout.NORTH);

        GridBagLayout helpContentLayout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        int gridCols = 4;
        //int gridRows = 9;
        jPanel1.setLayout(helpContentLayout);
        
        
        jTextAreas = fillfields(jTextAreas);
        
       
        for (int i = 1; i < 37; i++) {
        	c.fill = GridBagConstraints.NONE;
        	c.gridx = (i-1) % gridCols;
        	c.gridy = (int) (i-1)/gridCols;
        	c.weighty = 1;
        	c.weightx = 1;
        	c.insets = new Insets(10,10,10,10);
        	jPanel1.add(jTextAreas[i], c);
        }
        
        JScrollPane scrollPane = new JScrollPane(jPanel1);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        contentPanel1.add(scrollPane, BorderLayout.CENTER);

        return contentPanel1;
        
    }


	private JTextArea[] fillfields(JTextArea[] textAreas) {
        jTextAreas[1].setText("Name");
        jTextAreas[2].setText("Description");
        jTextAreas[3].setText("Requirements");
        jTextAreas[4].setText("Reference");

        jTextAreas[9].setText(StatTest.ZSCORE_MEAN.toString());
        jTextAreas[10].setText("Z-Score using bootstrapping of means");
        jTextAreas[11].setText("n >= 20, where n is the module size");
        jTextAreas[12].setText("http://en.wikipedia.org/wiki/Bootstrapping_%28statistics%29");
        
        jTextAreas[13].setText(StatTest.ZSCORE_MEDIAN.toString());
        jTextAreas[14].setText("Z-Score using bootstrapping of medians");
        jTextAreas[15].setText("n >= 20, where n is the module size");
        jTextAreas[16].setText("http://en.wikipedia.org/wiki/Bootstrapping_%28statistics%29");

        jTextAreas[17].setText(StatTest.BINOMIAL_EXACT.toString());
        jTextAreas[18].setText("Binomial distribution B(n, p) test");
        jTextAreas[19].setText("n >= 20, where n is the module size");
        jTextAreas[20].setText("http://en.wikipedia.org/wiki/Binomial_distribution");

        jTextAreas[21].setText(StatTest.BINOMIAL_NORMAL.toString());
        jTextAreas[22].setText("Z-Score from a Binomial distribution B(n, p) using Normal distribution aproximation N(np, sqrt(np(1-p)))");
        jTextAreas[23].setText("np > 5 and n(1-p) > 5, where n is the module size and p is the probability of 1.0 occurrence");
        jTextAreas[24].setText("http://en.wikipedia.org/wiki/Binomial_distribution");

        jTextAreas[25].setText(StatTest.BINOMIAL_POISSON.toString());
        jTextAreas[26].setText("P-Value from Binomial distribution B(n, p) using Poisson distribution aproximation Pois(obs, np)");
        jTextAreas[27].setText("(n >= 20 and p <= 0.05) or (n >= 100 and np <= 10), where n is the module size, obs is the observed frecuency and p is the probability of 1.0 occurrence");
        jTextAreas[28].setText("http://en.wikipedia.org/wiki/Binomial_distribution");
        
        jTextAreas[33].setText(StatTest.FISHER.toString());
        jTextAreas[34].setText("Fisher's Exact Test");
        jTextAreas[35].setText("When the expected value < 10");
        jTextAreas[36].setText("http://en.wikipedia.org/wiki/Fisher's_exact_test");
		return jTextAreas;
	}
	
	public JTextArea[] getTextAreas() {
		return this.jTextAreas;
	}


}

