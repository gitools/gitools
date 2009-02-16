package es.imim.bg.ztools.ui.dialogs.wizardpanels;

import com.nexes.wizard.*;

import es.imim.bg.ztools.model.DataMatrix;
import es.imim.bg.ztools.ui.dialogs.AnalysisWizard;
import es.imim.bg.ztools.ui.dialogs.ZCalcAnalysisWizardDialog;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.EventListener;
import java.util.Map;

import javax.swing.*;
import javax.swing.event.EventListenerList;


public class ZCalcAnalysisMainDescriptor extends WizardPanelDescriptor {
    
    public static final String IDENTIFIER = "MAIN_PANEL";
    static ZCalcAnalysisMainPanel mainPanel = new ZCalcAnalysisMainPanel();
    AnalysisWizard aw;
    
    public ZCalcAnalysisMainDescriptor(AnalysisWizard aw) {    	
        super(IDENTIFIER, mainPanel);
        
        this.aw = aw;
        
    	JTextField nameTextField = mainPanel.getNameTextField();
    	nameTextField.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				setNextButtonAccordingToNameField();
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
        });
    }
    
    public Object getNextPanelDescriptor() {
        return ZCalcAnalysisDataDescriptor.IDENTIFIER;
    }
    
    public Object getBackPanelDescriptor() {
        return null;
    }  
    
    public void aboutToDisplayPanel() {
        setNextButtonAccordingToNameField();
    }
    
    public void aboutToHidePanel() {
    	aw.setValue(aw.ANALYSIS_NAME, mainPanel.getNameTextField().getText());
    	aw.setValue(aw.PROCESSORS, mainPanel.getProcessorComboBox().getSelectedItem().toString());
    }    
    
    private void setNextButtonAccordingToNameField() {
         if (mainPanel.textFieldNotEmpty())
            getWizard().setNextFinishButtonEnabled(true);
         else
            getWizard().setNextFinishButtonEnabled(false);        
    
    }
    
}
