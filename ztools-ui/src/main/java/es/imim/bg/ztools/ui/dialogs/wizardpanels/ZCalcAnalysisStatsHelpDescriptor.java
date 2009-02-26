package es.imim.bg.ztools.ui.dialogs.wizardpanels;

import es.imim.bg.ztools.ui.dialogs.AnalysisWizard;
import es.imim.bg.ztools.ui.dialogs.AnalysisWizardPanelDescriptor;
import es.imim.bg.ztools.ui.model.WizardDataModel;


public class ZCalcAnalysisStatsHelpDescriptor extends AnalysisWizardPanelDescriptor {
    
    public static final String IDENTIFIER = "STATSHELP_PANEL";
    static ZCalcAnalysisStatsHelpPanel statsHelpPanel = new ZCalcAnalysisStatsHelpPanel();
    WizardDataModel dataModel;
    
    public ZCalcAnalysisStatsHelpDescriptor(AnalysisWizard aw, Object BackPanelDescriptor, Object NextPanelDescriptor) {    	
        super(IDENTIFIER, statsHelpPanel, BackPanelDescriptor, NextPanelDescriptor);
        
        this.dataModel = aw.getWizardDataModel();
        setNextButtonAccordingToInputs();
    }
       
    private void setNextButtonAccordingToInputs() {
    	//TODO: for some reason, setWizard returns null
        //getWizard().setNextFinishButtonEnabled(false);       
   }

    
}
