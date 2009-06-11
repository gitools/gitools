package org.gitools.ui.wizardmess.zetcalc;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JTextArea;

import org.gitools.ui.wizardmess.AbstractWizard;
import org.gitools.ui.wizardmess.AnalysisWizardPanelDescriptor;
import org.gitools.ui.wizardmess.WizardDataModel;



public class ZCalcAnalysisStatsHelpDescriptor extends AnalysisWizardPanelDescriptor {
    
    public static final String IDENTIFIER = "STATSHELP_PANEL";
    final ZCalcAnalysisStatsHelpPanel statsHelpPanel;
    WizardDataModel dataModel;
    
    public ZCalcAnalysisStatsHelpDescriptor(AbstractWizard aw, Object BackPanelDescriptor, Object NextPanelDescriptor) {    	
        super(IDENTIFIER, new ZCalcAnalysisStatsHelpPanel(), BackPanelDescriptor, NextPanelDescriptor);
        this.statsHelpPanel = (ZCalcAnalysisStatsHelpPanel) getPanelComponent();
        this.dataModel = aw.getWizardDataModel();
        setNextButtonAccordingToInputs();
        
        
        MouseListener urlClickListener = new MouseListener(){
        	
			@Override
			public void mouseClicked(MouseEvent e) { }

			@Override
			public void mouseEntered(MouseEvent e) { }

			@Override
			public void mouseExited(MouseEvent e) {	}

			@Override
			public void mousePressed(MouseEvent e) { }

			@Override
			public void mouseReleased(MouseEvent e) {
				JTextArea jta = (JTextArea) e.getComponent();
				String url = jta.getText();
				try {
					java.awt.Desktop.getDesktop().browse(new URI(url));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
        	
        };
        
        JTextArea[] jTextAreas = statsHelpPanel.getTextAreas();
        for (int i = 0; i < jTextAreas.length; i++) {
        	if (i%4 == 0) {
        		jTextAreas[i].addMouseListener(urlClickListener);
        	}
        }
        
    }
       
    private void setNextButtonAccordingToInputs() {
    	//TODO: for some reason, setWizard returns null
        //getWizard().setNextFinishButtonEnabled(false);       
   }

    
}
