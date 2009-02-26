package es.imim.bg.ztools.ui.dialogs.wizardpanels;

import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.dialogs.AnalysisWizard;
import es.imim.bg.ztools.ui.dialogs.AnalysisWizardPanelDescriptor;
import es.imim.bg.ztools.ui.model.WizardDataModel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.*;


public class ZCalcAnalysisModuleDescriptor extends AnalysisWizardPanelDescriptor {
    
    public static final String IDENTIFIER = "MODULE_PANEL";
    static ZCalcAnalysisModulePanel modulePanel = new ZCalcAnalysisModulePanel();
    WizardDataModel dataModel;
    final JButton choserButton;
    final JTextField fileNameField;
    final JTextField minField;
    final JTextField maxField;

    
    public ZCalcAnalysisModuleDescriptor(AnalysisWizard aw, Object BackPanelDescriptor, Object NextPanelDescriptor) {    	
        super(IDENTIFIER, modulePanel, BackPanelDescriptor, NextPanelDescriptor);
        
        this.dataModel = aw.getWizardDataModel();
        
        choserButton = modulePanel.getChoserButton();
        fileNameField = modulePanel.getFileNameField();
        maxField = modulePanel.getMaximumField();
        minField = modulePanel.getMinimumField();
        
        choserButton.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				File selectedFile = selectFile();
				if (selectedFile != null)
					fileNameField.setText(selectedFile.toString());
				setNextButtonAccordingToInputs();
			}
			@Override
			public void mouseEntered(MouseEvent e) { }
			
			@Override
			public void mouseExited(MouseEvent e) { }
			
			@Override
			public void mousePressed(MouseEvent e) { }
			
			@Override
			public void mouseReleased(MouseEvent e) { }
        });
        
        KeyListener minMaxKeyListener = new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) { }

			@Override
			public void keyReleased(KeyEvent e) {
				setNextButtonAccordingToInputs();
			}

			@Override
			public void keyTyped(KeyEvent e) { }
        };
        
        minField.addKeyListener(minMaxKeyListener);
        maxField.addKeyListener(minMaxKeyListener);
        
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
         if (!fileNameField.getText().isEmpty() && checkMinMaxInput())
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
				(String) dataModel.getValue(AnalysisWizard.WIZARD_WORKING_DIR));
		
		fileChooser.setDialogTitle("Select the data file");
		
		int retval = fileChooser.showOpenDialog(AppFrame.instance());
		if (retval == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			return selectedFile;
		}		
		return null;
	}
}
