package es.imim.bg.ztools.ui.dialogs.wizardpanels;

import com.nexes.wizard.*;

import java.awt.*;
import java.util.Map;

import javax.swing.*;


public class ZCalcAnalysisMainDescriptor extends WizardPanelDescriptor {
    
    public static final String IDENTIFIER = "MAIN_PANEL";
    
    public ZCalcAnalysisMainDescriptor(Map<String, String> dataModel) {
        super(IDENTIFIER, new ZCalcAnalysisMainPanel(dataModel));
    }
    
    public Object getNextPanelDescriptor() {
        return FINISH;
    }
    
    public Object getBackPanelDescriptor() {
        return null;
    }  
    
}
