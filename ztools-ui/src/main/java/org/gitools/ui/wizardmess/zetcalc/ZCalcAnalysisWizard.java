package org.gitools.ui.wizardmess.zetcalc;



import java.awt.Dimension;

import javax.swing.JFrame;

import org.gitools.ui.wizardmess.AnalysisWizard;

import com.nexes.wizard.WizardPanelDescriptor;

import org.gitools.commands.ZCalcCommand;
import org.gitools.datafilters.BinaryCutoffFilter;
import org.gitools.stats.test.factory.ZscoreTestFactory;


public class ZCalcAnalysisWizard extends AnalysisWizard {
	
	ZCalcCommand command; 
	
	public ZCalcAnalysisWizard(JFrame owner){
		super();
		getDialog().setTitle("ZetCalc Analysis...");
		Dimension d = new Dimension(600,400);
		getDialog().setSize(d);
		getDialog().setMinimumSize(d);
		getDialog().setResizable(true);

	    	    
	    WizardPanelDescriptor mainDescriptor = new ZCalcAnalysisMainDescriptor(
										    		this, 
										    		null, 
										    		ZCalcAnalysisDataDescriptor.IDENTIFIER);
	    registerWizardPanel(ZCalcAnalysisMainDescriptor.IDENTIFIER, mainDescriptor);
	    
	    WizardPanelDescriptor dataDescriptor = new ZCalcAnalysisDataDescriptor(
	    											this,
	    											ZCalcAnalysisMainDescriptor.IDENTIFIER,
	    											ZCalcAnalysisModuleDescriptor.IDENTIFIER);
	    registerWizardPanel(ZCalcAnalysisDataDescriptor.IDENTIFIER, dataDescriptor);
	    
	    WizardPanelDescriptor moduleDescriptor = new ZCalcAnalysisModuleDescriptor(
	    											this,
	    											ZCalcAnalysisDataDescriptor.IDENTIFIER,
	    											ZCalcAnalysisStatsDescriptor.IDENTIFIER);
	    registerWizardPanel(ZCalcAnalysisModuleDescriptor.IDENTIFIER, moduleDescriptor);
	    	    
	    WizardPanelDescriptor statsDescriptor = new ZCalcAnalysisStatsDescriptor(
	    											this,
	    											ZCalcAnalysisModuleDescriptor.IDENTIFIER,
	    											null);
	    registerWizardPanel(ZCalcAnalysisStatsDescriptor.IDENTIFIER, statsDescriptor);
	    
	    WizardPanelDescriptor statsHelpDescriptor = new ZCalcAnalysisStatsHelpDescriptor(
													this,
													ZCalcAnalysisStatsDescriptor.IDENTIFIER,
													ZCalcAnalysisStatsDescriptor.IDENTIFIER);
	    registerWizardPanel(ZCalcAnalysisStatsHelpDescriptor.IDENTIFIER, statsHelpDescriptor);
	    
	    setCurrentPanel(ZCalcAnalysisMainDescriptor.IDENTIFIER);
	    int ret = showModalDialog();

	    if (ret == 0) {
		    //Preparing the Analysis command
		    BinaryCutoffFilter binaryCutoffFilter = null;
		    if (!dataModel.getValue(BIN_CUTOFF_CONDITION).equals(DISABLED)) {
				String cond = (String) dataModel.getValue(BIN_CUTOFF_CONDITION);
				double cutoff = Double.parseDouble(dataModel.getValue(BIN_CUTOFF_VALUE).toString());
				
				if ("lt".equals(cond))
					binaryCutoffFilter = new BinaryCutoffFilter(cutoff, BinaryCutoffFilter.LT);
				else if ("le".equals(cond))
					binaryCutoffFilter = new BinaryCutoffFilter(cutoff, BinaryCutoffFilter.LE);
				else if ("eq".equals(cond))
					binaryCutoffFilter = new BinaryCutoffFilter(cutoff, BinaryCutoffFilter.EQ);
				else if ("gt".equals(cond))
					binaryCutoffFilter = new BinaryCutoffFilter(cutoff, BinaryCutoffFilter.GT);
				else if ("ge".equals(cond))
					binaryCutoffFilter = new BinaryCutoffFilter(cutoff, BinaryCutoffFilter.GE);
		    }
	
			String outputFormat = "csv";
			
			Integer sampleSize;
			if (dataModel.getValue(SAMPLE_SIZE) == null){
				//TODO: make default sample size test specific
				sampleSize = ZscoreTestFactory.DEFAULT_NUM_SAMPLES;
			}
			else {
				sampleSize = Integer.parseInt((String) dataModel.getValue(SAMPLE_SIZE));
			}
			
			Integer minModuleSize;
			if (dataModel.getValue(MIN) == null) {
				//TODO: find a solution for default values for both the wizard and the command line
				minModuleSize = 20;
			}
			else {
				minModuleSize = Integer.parseInt((String) dataModel.getValue(MIN));
			}
			
			Integer maxModuleSize;
			if (dataModel.getValue(MAX) == null) {
				//TODO: find a solution for default values for both the wizard and the command line
				maxModuleSize = Integer.MAX_VALUE;
			}
			else {
				maxModuleSize = Integer.parseInt((String) dataModel.getValue(MAX));
			}
			
			boolean omitNonMappedItems = Boolean.parseBoolean(
					dataModel.getValue(OMIT_NON_MAPPED_ITMEMS).toString());
			
		    command = new ZCalcCommand(
		    							(String) dataModel.getValue(ANALYSIS_NAME),
		    							(String) dataModel.getValue(STAT_TEST),
		    							sampleSize,
		    							(String) dataModel.getValue(DATA_FILE),
		    							binaryCutoffFilter,
		    							(String) dataModel.getValue(MODULE_FILE),
		    							minModuleSize,
		    							maxModuleSize,
		    							!omitNonMappedItems, 
		    							(String) dataModel.getValue(ANALYSIS_WORKING_DIR),
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
	
	@Override
	public void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}
}


