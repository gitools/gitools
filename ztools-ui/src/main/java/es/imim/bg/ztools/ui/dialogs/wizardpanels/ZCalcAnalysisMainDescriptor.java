package es.imim.bg.ztools.ui.dialogs.wizardpanels;

import com.nexes.wizard.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Map;

import javax.swing.*;


public class ZCalcAnalysisMainDescriptor extends WizardPanelDescriptor {
    
    public static final String IDENTIFIER = "MAIN_PANEL";
    static ZCalcAnalysisMainPanel mainPanel = new ZCalcAnalysisMainPanel();

    
    public ZCalcAnalysisMainDescriptor(Map<String, String> dataModel) {    	
        super(IDENTIFIER, mainPanel);
    }
    
    public Object getNextPanelDescriptor() {
        return ZCalcAnalysisDataDescriptor.IDENTIFIER;
    }
    
    public Object getBackPanelDescriptor() {
        return null;
    }  
    
    public void aboutToDisplayPanel() {
        setNextButtonAccordingToNameField();
    }    

    public void actionPerformed(ActionEvent e) {
        setNextButtonAccordingToNameField();
    }
            
    
    private void setNextButtonAccordingToNameField() {
         if (mainPanel.textFieldNotEmpty())
            getWizard().setNextFinishButtonEnabled(true);
         else
            getWizard().setNextFinishButtonEnabled(false);           
    
    }
    
}
