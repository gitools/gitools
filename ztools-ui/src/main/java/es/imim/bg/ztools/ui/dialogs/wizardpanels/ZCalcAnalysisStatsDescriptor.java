package es.imim.bg.ztools.ui.dialogs.wizardpanels;

import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.dialogs.AnalysisWizard;
import es.imim.bg.ztools.ui.dialogs.AnalysisWizardPanelDescriptor;
import es.imim.bg.ztools.ui.utils.Options;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.*;


public class ZCalcAnalysisStatsDescriptor extends AnalysisWizardPanelDescriptor {
    
    public static final String IDENTIFIER = "STATS_PANEL";
    static ZCalcAnalysisStatsPanel statsPanel = new ZCalcAnalysisStatsPanel();
    AnalysisWizard aw;
    final JButton choserButton;
    final JTextField fileNameField;
    final JTextField minField;
    final JTextField maxField;

    
    public ZCalcAnalysisStatsDescriptor(AnalysisWizard aw, Object BackPanelDescriptor, Object NextPanelDescriptor) {    	
        super(IDENTIFIER, statsPanel, BackPanelDescriptor, NextPanelDescriptor);
        
        this.aw = aw;
        
        choserButton = statsPanel.getChoserButton();
        fileNameField = statsPanel.getFileNameField();
        maxField = statsPanel.getMaximumField();
        minField = statsPanel.getMinimumField();
        
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
    	aw.setValue(aw.MODULE_FILE, fileNameField.getText());
    	if (!minField.getText().isEmpty())
    		aw.setValue(aw.MIN, minField.getText());
    	if (!maxField.getText().isEmpty())
    		aw.setValue(aw.MAX, maxField.getText());
    }    
    
    private void setNextButtonAccordingToInputs() {
         if (!fileNameField.getText().isEmpty() && checkMinMaxInput())
            getWizard().setNextFinishButtonEnabled(true);
         else
            getWizard().setNextFinishButtonEnabled(false);        
    
    }
    
    private boolean checkMinMaxInput() {
    	boolean everythingOK = true;
    	
    	int min = getInteger(statsPanel.MIN_DEFAULT); //-1 when empty
    	int max = getInteger(statsPanel.MAX_DEFAULT); //-1 when empty
    	
    	
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
				Options.instance().getLastPath());
		
		fileChooser.setDialogTitle("Select the data file");
		
		int retval = fileChooser.showOpenDialog(AppFrame.instance());
		if (retval == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			return selectedFile;
		}		
		return null;
	}
}
