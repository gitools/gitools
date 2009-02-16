package es.imim.bg.ztools.ui.dialogs;



import java.util.Set;

import com.nexes.wizard.*;

import es.imim.bg.ztools.ui.dialogs.wizardpanels.ZCalcAnalysisDataDescriptor;
import es.imim.bg.ztools.ui.dialogs.wizardpanels.ZCalcAnalysisMainDescriptor;

import javax.swing.*;


public class ZCalcAnalysisWizardDialog extends AnalysisWizard {
	
	public ZCalcAnalysisWizardDialog(JFrame owner) {
		super();
	    Wizard wizard = new Wizard(owner);
	    wizard.getDialog().setTitle("ZCalc Analysis Dialog");	    
	    	    
	    WizardPanelDescriptor mainDescriptor = new ZCalcAnalysisMainDescriptor(this);
	    wizard.registerWizardPanel(ZCalcAnalysisMainDescriptor.IDENTIFIER, mainDescriptor);
	    wizard.setCurrentPanel(ZCalcAnalysisMainDescriptor.IDENTIFIER);
	    
	    WizardPanelDescriptor dataDescriptor = new ZCalcAnalysisDataDescriptor(this);
	    wizard.registerWizardPanel(ZCalcAnalysisDataDescriptor.IDENTIFIER, dataDescriptor);
	    
	    /*WizardPanelDescriptor descriptor1 = new TestPanel1Descriptor();
	    wizard.registerWizardPanel(TestPanel1Descriptor.IDENTIFIER, descriptor1);*/
	    
	    //wizard.setCurrentPanel(TestPanel1Descriptor.IDENTIFIER);
	    
	    int ret = wizard.showModalDialog();
	    
	    System.out.println("Dialog return code is (0=Finish,1=Cancel,2=Error): " + ret);
	    Set<String> keyset = dataModel.keySet();
	    Object[] keys = keyset.toArray();
	    for (int i = 0; i < dataModel.size(); i++){
	    	System.out.println(keys[i].toString() + ": " + dataModel.get(keys[i].toString()) );
	    }
	    
	    //System.out.println("Second panel selection is: " + 
	        //(((TestPanel2)descriptor2.getPanelComponent()).getRadioButtonSelected()));
	    
	    //System.exit(0);	
	    }
	

}


