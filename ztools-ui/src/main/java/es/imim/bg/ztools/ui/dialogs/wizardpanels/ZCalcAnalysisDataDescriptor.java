package es.imim.bg.ztools.ui.dialogs.wizardpanels;

import com.nexes.wizard.*;

import java.awt.*;
import java.util.Map;

import javax.swing.*;


public class ZCalcAnalysisDataDescriptor extends WizardPanelDescriptor {
    
    public static final String IDENTIFIER = "MAIN_PANEL";
    
    public ZCalcAnalysisDataDescriptor(Map<String, String> dataModel) {
        super(IDENTIFIER, new ZCalcAnalysisMainPanel());
    }
    
    public Object getNextPanelDescriptor() {
        return FINISH;
    }
    
    public Object getBackPanelDescriptor() {
        return null;
    }  
    
}
