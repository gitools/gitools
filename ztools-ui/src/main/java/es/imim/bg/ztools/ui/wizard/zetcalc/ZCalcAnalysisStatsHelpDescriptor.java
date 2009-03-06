package es.imim.bg.ztools.ui.wizard.zetcalc;

import es.imim.bg.ztools.ui.wizard.AbstractWizard;
import es.imim.bg.ztools.ui.wizard.AnalysisWizardPanelDescriptor;
import es.imim.bg.ztools.ui.wizard.WizardDataModel;


public class ZCalcAnalysisStatsHelpDescriptor extends AnalysisWizardPanelDescriptor {
    
    public static final String IDENTIFIER = "STATSHELP_PANEL";
    final ZCalcAnalysisStatsHelpPanel statsHelpPanel;
    WizardDataModel dataModel;
    
    public ZCalcAnalysisStatsHelpDescriptor(AbstractWizard aw, Object BackPanelDescriptor, Object NextPanelDescriptor) {    	
        super(IDENTIFIER, new ZCalcAnalysisStatsHelpPanel(), BackPanelDescriptor, NextPanelDescriptor);
        this.statsHelpPanel = (ZCalcAnalysisStatsHelpPanel) getPanelComponent();
        this.dataModel = aw.getWizardDataModel();
        setNextButtonAccordingToInputs();
    }
       
    private void setNextButtonAccordingToInputs() {
    	//TODO: for some reason, setWizard returns null
        //getWizard().setNextFinishButtonEnabled(false);       
   }

    
}
