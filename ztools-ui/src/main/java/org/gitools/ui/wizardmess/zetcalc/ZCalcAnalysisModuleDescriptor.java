package org.gitools.ui.wizardmess.zetcalc;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.*;

import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.utils.Options;
import org.gitools.ui.wizardmess.AbstractWizard;
import org.gitools.ui.wizardmess.AnalysisWizard;
import org.gitools.ui.wizardmess.AnalysisWizardPanelDescriptor;
import org.gitools.ui.wizardmess.WizardDataModel;

public class ZCalcAnalysisModuleDescriptor extends AnalysisWizardPanelDescriptor {
    
    public static final String IDENTIFIER = "MODULE_PANEL";
    
    final ZCalcAnalysisModulePanel modulePanel;
    WizardDataModel dataModel;
    final JButton choserButton;
    final JTextField fileNameField;
    final JTextField minField;
    final JTextField maxField;

    public ZCalcAnalysisModuleDescriptor(
    		AbstractWizard aw, Object BackPanelDescriptor, Object NextPanelDescriptor) {
    	
        super(IDENTIFIER, new ZCalcAnalysisModulePanel(), BackPanelDescriptor, NextPanelDescriptor);
        this.modulePanel = (ZCalcAnalysisModulePanel) getPanelComponent();
        this.dataModel = aw.getWizardDataModel();
        
        choserButton = modulePanel.getChoserButton();
        fileNameField = modulePanel.getFileNameField();
        maxField = modulePanel.getMaximumField();
        minField = modulePanel.getMinimumField();
        
        choserButton.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		File selectedFile = selectFile();
				if (selectedFile != null)
					fileNameField.setText(selectedFile.toString());
				setNextButtonAccordingToInputs();
        	}
        });
        
        KeyListener keyListener = new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) { }

			@Override
			public void keyReleased(KeyEvent e) {
				setNextButtonAccordingToInputs();
			}

			@Override
			public void keyTyped(KeyEvent e) { }
        };
        
        minField.addKeyListener(keyListener);
        maxField.addKeyListener(keyListener);
        fileNameField.addKeyListener(keyListener);
        
    }
    
    public void aboutToDisplayPanel() {
    	setNextButtonAccordingToInputs();
    }
    
    public void aboutToHidePanel() {
    	dataModel.setValue(AnalysisWizard.MODULE_FILE, fileNameField.getText());
    	if (!minField.getText().isEmpty())
    		dataModel.setValue(AnalysisWizard.MIN, minField.getText());
    	if (!maxField.getText().isEmpty())
    		dataModel.setValue(AnalysisWizard.MAX, maxField.getText());
    }    
    
    private void setNextButtonAccordingToInputs() {
         if (verifyFileName() && checkMinMaxInput())
            getWizard().setNextFinishButtonEnabled(true);
         else
            getWizard().setNextFinishButtonEnabled(false);        
    }
    
    private boolean checkMinMaxInput() {
    	boolean everythingOK = true;
    	
    	int min = getInteger(modulePanel.MIN_DEFAULT); //-1 when empty
    	int max = getInteger(modulePanel.MAX_DEFAULT); //-1 when empty
    	
    	
    	// check min-value
    	if (!isInteger(minField.getText())) {
    		if (!minField.getText().isEmpty())
    			everythingOK = false;
    	}
        else 
        	min = getInteger(minField.getText());
    	
    	// check max-value
    	if (!isInteger(maxField.getText())) {
    		if (!maxField.getText().isEmpty())
    			everythingOK = false;
    	}
    	else 
    		max = getInteger(maxField.getText());  	
    	
    	// max should be at least as big as min
    	if (everythingOK)
    		if (min > max && min != -1 && max != -1)
    			everythingOK = false;
    	
    	//no negative values
    	
    	if(everythingOK)
    		if(min < -1 || max < -1)
    			everythingOK = false;
    	
    	return everythingOK;
    }
    
    protected boolean verifyFileName() {
		File file = new File(fileNameField.getText());
		if (file.exists() && file.isFile()) {
			return true;
		}
		else 
			return false;
	}
    
    private boolean isInteger(String text) {
		try {
			Integer.parseInt(text);
		}
		catch(Exception e) {
			return false;
		}
		return true;
	}
    
    private int getInteger(String text) {
    	
    	int i = -1; // => is no integer
    	
		try {
			i = Integer.parseInt(text);
		}
		catch(Exception e) {
		}
		return i;
	}
    
	private File selectFile() {
		JFileChooser fileChooser = new JFileChooser(
				Options.instance().getLastMapPath());
		
		fileChooser.setDialogTitle("Select file");
		
		int retval = fileChooser.showOpenDialog(AppFrame.instance());
		if (retval == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			Options.instance().setLastMapPath(selectedFile.getParent());
			return selectedFile;
		}		
		return null;
	}
}
