package es.imim.bg.ztools.ui.dialogs.wizardpanels;

import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.dialogs.AnalysisWizard;
import es.imim.bg.ztools.ui.dialogs.AnalysisWizardPanelDescriptor;
import es.imim.bg.ztools.ui.utils.Options;


import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.*;

import com.nexes.wizard.Wizard;
import com.nexes.wizard.WizardModel;
import com.nexes.wizard.WizardPanelDescriptor;


public class ZCalcAnalysisStatsHelpDescriptor extends AnalysisWizardPanelDescriptor {
    
    public static final String IDENTIFIER = "STATSHELP_PANEL";
    static ZCalcAnalysisStatsHelpPanel statsHelpPanel = new ZCalcAnalysisStatsHelpPanel();
    AnalysisWizard aw;
    
    public ZCalcAnalysisStatsHelpDescriptor(AnalysisWizard aw, Object BackPanelDescriptor, Object NextPanelDescriptor) {    	
        super(IDENTIFIER, statsHelpPanel, BackPanelDescriptor, NextPanelDescriptor);
        
        this.aw = aw;
        //getWizard().setNextFinishButtonEnabled(false); 
        setNextButtonAccordingToInputs();
    }
       
    private void setNextButtonAccordingToInputs() {
    	//TODO: for some reason, setWizard returns null
        //getWizard().setNextFinishButtonEnabled(true);       
   }

    
}
