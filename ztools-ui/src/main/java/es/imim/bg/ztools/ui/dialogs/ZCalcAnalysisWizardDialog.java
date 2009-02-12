package es.imim.bg.ztools.ui.dialogs;


import java.util.HashMap;
import java.util.Map;

import com.nexes.wizard.*;

import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.dialogs.wizardpanels.ZCalcAnalysisMainDescriptor;
import es.imim.bg.ztools.ui.dialogs.wizardpanels.ZCalcAnalysisMainPanel;

import javax.swing.*;


public class ZCalcAnalysisWizardDialog {
	
	static Map<String, String> executionDataModel = new HashMap<String, String>();
	

	public ZCalcAnalysisWizardDialog(JFrame owner) {
	    Wizard wizard = new Wizard(owner);
	    wizard.getDialog().setTitle("ZCalc Analysis Dialog");
	    executionDataModel = new HashMap<String, String>();
	    
	    
	    
	    WizardPanelDescriptor mainDescriptor = new ZCalcAnalysisMainDescriptor(executionDataModel);
	    wizard.registerWizardPanel(ZCalcAnalysisMainDescriptor.IDENTIFIER, mainDescriptor);
	    wizard.setCurrentPanel(ZCalcAnalysisMainDescriptor.IDENTIFIER);
	    
	    /*WizardPanelDescriptor descriptor1 = new TestPanel1Descriptor();
	    wizard.registerWizardPanel(TestPanel1Descriptor.IDENTIFIER, descriptor1);*/
	    
	    //wizard.setCurrentPanel(TestPanel1Descriptor.IDENTIFIER);
	    
	    int ret = wizard.showModalDialog();
	    
	    System.out.println("Dialog return code is (0=Finish,1=Cancel,2=Error): " + ret);
	    //System.out.println("Second panel selection is: " + 
	        //(((TestPanel2)descriptor2.getPanelComponent()).getRadioButtonSelected()));
	    
	    //System.exit(0);	
	    }
	

}


