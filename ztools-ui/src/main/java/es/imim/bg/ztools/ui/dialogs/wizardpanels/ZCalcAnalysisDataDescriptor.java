package es.imim.bg.ztools.ui.dialogs.wizardpanels;

import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.dialogs.AnalysisWizard;
import es.imim.bg.ztools.ui.dialogs.AnalysisWizardPanelDescriptor;
import es.imim.bg.ztools.ui.dialogs.AnalysisWizard.Condition;
import es.imim.bg.ztools.ui.utils.Options;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.*;


public class ZCalcAnalysisDataDescriptor extends AnalysisWizardPanelDescriptor {
    
    public static final String IDENTIFIER = "DATA_PANEL";
    private static final ZCalcAnalysisDataPanel dataPanel = new ZCalcAnalysisDataPanel();
    AnalysisWizard aw;
    final JButton chooserButton;
    final JTextField fileNameField;
    final JComboBox binCutoffConditionBox;
    final JTextField binCutoffField;
    
    public ZCalcAnalysisDataDescriptor(AnalysisWizard aw, Object BackPanelDescriptor, Object NextPanelDescriptor) {    	
        super(IDENTIFIER, dataPanel, BackPanelDescriptor, NextPanelDescriptor);
        
        this.aw = aw;
                
        chooserButton = dataPanel.getChooserButton();
        fileNameField = dataPanel.getFileNameField();
        binCutoffConditionBox = dataPanel.getBinCutoffConditionBox();
        binCutoffField = dataPanel.getBinCutoffValueField();
        
        
        chooserButton.addMouseListener(new MouseListener(){
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
        
        binCutoffConditionBox.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				setBinCutOffFieldAccordingToInputs();
				setNextButtonAccordingToInputs();
			}
        	
        });
        
        binCutoffField.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) { }

			@Override
			public void keyReleased(KeyEvent e) {
				setNextButtonAccordingToInputs();
			}

			@Override
			public void keyTyped(KeyEvent e) { }
        });
    }
    
    public void aboutToDisplayPanel() {
    	setNextButtonAccordingToInputs();
    	setBinCutOffFieldAccordingToInputs();
    }
    
    public void aboutToHidePanel() {
    	aw.setValue(aw.DATA_FILE, fileNameField.getText());
    	String conditionItemString = binCutoffConditionBox.getSelectedItem().toString();
    	if (conditionItemString.equals(dataPanel.BIN_CUTOFF_DISABLED))
        	aw.setValue(aw.BIN_CUTOFF_CONDITION, binCutoffConditionBox.getSelectedItem().toString());
    	else {
    		Condition[] conditions = Condition.values();
	    	Condition c = conditions[binCutoffConditionBox.getSelectedIndex()-1];
	    	aw.setValue(aw.BIN_CUTOFF_CONDITION, c.toCommandLineArgument());
    		aw.setValue(aw.BIN_CUTOFF_VALUE, binCutoffField.getText());
    	}
    }    
    
    private void setNextButtonAccordingToInputs() {
    	if (!fileNameField.getText().isEmpty() && binCutoffDisabled())
           getWizard().setNextFinishButtonEnabled(true);
    	else if(!fileNameField.getText().isEmpty() 
    			&& !binCutoffDisabled()
    			&& isNumeric(binCutoffField.getText()))
            getWizard().setNextFinishButtonEnabled(true);
        else {
           getWizard().setNextFinishButtonEnabled(false);
        }
   }
    
    private boolean isNumeric(String text) {
		try {
			Double.parseDouble(text);
		}
		catch(Exception e) {
			return false;
		}
		return true;
	}

	private void setBinCutOffFieldAccordingToInputs() {
        if (binCutoffDisabled()) {
            binCutoffField.setEnabled(false);
        } else {
            binCutoffField.setEnabled(true);
        }
    }
    
    private boolean binCutoffDisabled() {
    	return binCutoffConditionBox.getSelectedItem().toString().equals(dataPanel.BIN_CUTOFF_DISABLED);
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
