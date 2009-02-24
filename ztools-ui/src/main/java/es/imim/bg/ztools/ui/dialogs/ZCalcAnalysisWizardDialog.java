package es.imim.bg.ztools.ui.dialogs;



import java.awt.Dimension;
import java.util.Set;

import javax.swing.JFrame;

import com.nexes.wizard.Wizard;
import com.nexes.wizard.WizardPanelDescriptor;

import es.imim.bg.ztools.commands.ZCalcCommand;
import es.imim.bg.ztools.datafilters.BinCutoffFilter;
import es.imim.bg.ztools.test.factory.ZscoreTestFactory;
import es.imim.bg.ztools.ui.dialogs.wizardpanels.ZCalcAnalysisDataDescriptor;
import es.imim.bg.ztools.ui.dialogs.wizardpanels.ZCalcAnalysisMainDescriptor;
import es.imim.bg.ztools.ui.dialogs.wizardpanels.ZCalcAnalysisModuleDescriptor;
import es.imim.bg.ztools.ui.dialogs.wizardpanels.ZCalcAnalysisStatsDescriptor;
import es.imim.bg.ztools.ui.dialogs.wizardpanels.ZCalcAnalysisStatsHelpDescriptor;


public class ZCalcAnalysisWizardDialog extends AnalysisWizard {
	
	ZCalcCommand command; 
	
	public ZCalcAnalysisWizardDialog(JFrame owner){
		super();
	    Wizard wizard = new Wizard(owner);
	    wizard.getDialog().setTitle("ZCalc Analysis Dialog");
	    Dimension d = new Dimension(600,400);
	    wizard.getDialog().setSize(d);
	    wizard.getDialog().setMinimumSize(d);

	    	    
	    WizardPanelDescriptor mainDescriptor = new ZCalcAnalysisMainDescriptor(
										    		this, 
										    		null, 
										    		ZCalcAnalysisDataDescriptor.IDENTIFIER);
	    wizard.registerWizardPanel(ZCalcAnalysisMainDescriptor.IDENTIFIER, mainDescriptor);
	    
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
	    
	    WizardPanelDescriptor statsHelpDescriptor = new ZCalcAnalysisStatsHelpDescriptor(
													this,
													ZCalcAnalysisStatsDescriptor.IDENTIFIER,
													ZCalcAnalysisStatsDescriptor.IDENTIFIER);
	    wizard.registerWizardPanel(ZCalcAnalysisStatsHelpDescriptor.IDENTIFIER, statsHelpDescriptor);
	    
	    
	    wizard.setCurrentPanel(ZCalcAnalysisMainDescriptor.IDENTIFIER);
	    int ret = wizard.showModalDialog();
	    
	    System.out.println("Dialog return code is (0=Finish,1=Cancel,2=Error): " + ret);
	    Set<String> keyset = dataModel.keySet();
	    Object[] keys = keyset.toArray();
	    for (int i = 0; i < dataModel.size(); i++){
	    	System.out.println(keys[i].toString() + ":\t" + dataModel.get(keys[i].toString()) );
	    }
	    
	    if (ret == 0) {
		    //Preparing the Analysis command
		    BinCutoffFilter binCutoffFilter = null;
		    if (!getValue(BIN_CUTOFF_CONDITION).equals(DISABLED)) {
				String cond = getValue(BIN_CUTOFF_CONDITION);
				double cutoff = Double.parseDouble(dataModel.get(BIN_CUTOFF_VALUE));
				
				if ("lt".equals(cond))
					binCutoffFilter = new BinCutoffFilter(cutoff, BinCutoffFilter.LT);
				else if ("le".equals(cond))
					binCutoffFilter = new BinCutoffFilter(cutoff, BinCutoffFilter.LE);
				else if ("eq".equals(cond))
					binCutoffFilter = new BinCutoffFilter(cutoff, BinCutoffFilter.EQ);
				else if ("gt".equals(cond))
					binCutoffFilter = new BinCutoffFilter(cutoff, BinCutoffFilter.GT);
				else if ("ge".equals(cond))
					binCutoffFilter = new BinCutoffFilter(cutoff, BinCutoffFilter.GE);
		    }
	
			String outputFormat = "csv";
			
			Integer sampleSize;
			if (getValue(SAMPLE_SIZE) == null){
				//TODO: make default sample size test specific
				sampleSize = ZscoreTestFactory.DEFAULT_NUM_SAMPLES;
			}
			else {
				sampleSize = Integer.parseInt(getValue(SAMPLE_SIZE));
			}
			
			Integer minModuleSize;
			if (getValue(MIN) == null) {
				//TODO: find a solution for default values for both the wizard and the command line
				minModuleSize = 20;
			}
			else {
				minModuleSize = Integer.parseInt(getValue(MIN));
			}
			
			Integer maxModuleSize;
			if (getValue(MAX) == null) {
				//TODO: find a solution for default values for both the wizard and the command line
				maxModuleSize = Integer.MAX_VALUE;
			}
			else {
				maxModuleSize = Integer.parseInt(getValue(MAX));
			}
			
		    command = new ZCalcCommand(
		    							getValue(ANALYSIS_NAME),
		    							getValue(STAT_TEST),
		    							sampleSize,
		    							getValue(DATA_FILE),
		    							binCutoffFilter,
		    							getValue(MODULE_FILE),
		    							minModuleSize,
		    							maxModuleSize,
		    							getValue(ANALYSIS_WORKING_DIR),
		    							outputFormat,
		    							true);
	    }
	    else {
	    	command = null;
	    }
	}
	
	public ZCalcCommand getCommand(){
		return command;
	}

}


