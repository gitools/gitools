package es.imim.bg.ztools.ui.dialogs;



import java.awt.Dimension;
import java.util.Set;

import com.nexes.wizard.*;

import es.imim.bg.ztools.ui.dialogs.wizardpanels.ZCalcAnalysisDataDescriptor;
import es.imim.bg.ztools.ui.dialogs.wizardpanels.ZCalcAnalysisMainDescriptor;
import es.imim.bg.ztools.ui.dialogs.wizardpanels.ZCalcAnalysisModuleDescriptor;
import es.imim.bg.ztools.ui.dialogs.wizardpanels.ZCalcAnalysisModulePanel;
import es.imim.bg.ztools.ui.dialogs.wizardpanels.ZCalcAnalysisStatsDescriptor;

import javax.swing.*;


public class ZCalcAnalysisWizardDialog extends AnalysisWizard {
	
	public ZCalcAnalysisWizardDialog(JFrame owner) {
		super();
	    Wizard wizard = new Wizard(owner);
	    wizard.getDialog().setTitle("ZCalc Analysis Dialog");
	    Dimension d = new Dimension(600,400);
	    wizard.getDialog().setSize(d);
	    wizard.getDialog().setMaximumSize(d);
	    wizard.getDialog().setMinimumSize(d);
	    wizard.getDialog().setResizable(false);
	    	    
	    WizardPanelDescriptor mainDescriptor = new ZCalcAnalysisMainDescriptor(
										    		this, 
										    		null, 
										    		ZCalcAnalysisDataDescriptor.IDENTIFIER);
	    wizard.registerWizardPanel(ZCalcAnalysisMainDescriptor.IDENTIFIER, mainDescriptor);
	    wizard.setCurrentPanel(ZCalcAnalysisMainDescriptor.IDENTIFIER);
	    
	    WizardPanelDescriptor dataDescriptor = new ZCalcAnalysisDataDescriptor(
	    											this,
	    											ZCalcAnalysisMainDescriptor.IDENTIFIER,
	    											ZCalcAnalysisModuleDescriptor.IDENTIFIER);
	    wizard.registerWizardPanel(ZCalcAnalysisDataDescriptor.IDENTIFIER, dataDescriptor);
	    
	    WizardPanelDescriptor moduleDescriptor = new ZCalcAnalysisModuleDescriptor(
	    											this,
	    											ZCalcAnalysisDataDescriptor.IDENTIFIER,
	    											ZCalcAnalysisStatsDescriptor.IDENTIFIER);
	    wizard.registerWizardPanel(ZCalcAnalysisModuleDescriptor.IDENTIFIER, moduleDescriptor);
	    	    
	    WizardPanelDescriptor statsDescriptor = new ZCalcAnalysisStatsDescriptor(
	    											this,
	    											ZCalcAnalysisModuleDescriptor.IDENTIFIER,
	    											null);
	    wizard.registerWizardPanel(ZCalcAnalysisStatsDescriptor.IDENTIFIER, statsDescriptor);
	    
	    int ret = wizard.showModalDialog();
	    
	    System.out.println("Dialog return code is (0=Finish,1=Cancel,2=Error): " + ret);
	    Set<String> keyset = dataModel.keySet();
	    Object[] keys = keyset.toArray();
	    for (int i = 0; i < dataModel.size(); i++){
	    	System.out.println(keys[i].toString() + ":\t" + dataModel.get(keys[i].toString()) );
	    }
	    	    
	}

}


