package es.imim.bg.ztools.ui.dialogs.wizardpanels;

import com.nexes.wizard.*;

import es.imim.bg.ztools.model.DataMatrix;
import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.dialogs.AnalysisWizard;
import es.imim.bg.ztools.ui.dialogs.AnalysisWizardPanelDescriptor;
import es.imim.bg.ztools.ui.dialogs.ZCalcAnalysisWizardDialog;
import es.imim.bg.ztools.ui.utils.Options;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.EventListener;
import java.util.Map;

import javax.swing.*;
import javax.swing.event.EventListenerList;


public class ZCalcAnalysisMainDescriptor extends AnalysisWizardPanelDescriptor {
    
    public static final String IDENTIFIER = "MAIN_PANEL";
    static ZCalcAnalysisMainPanel mainPanel = new ZCalcAnalysisMainPanel();
    final JButton chooserButton;
    final JTextField workDirField;
    final JTextField analysisNameField;

    AnalysisWizard aw;
    
    public ZCalcAnalysisMainDescriptor(AnalysisWizard aw, Object BackPanelDescriptor, Object NextPanelDescriptor) {    	
        super(IDENTIFIER, mainPanel, BackPanelDescriptor, NextPanelDescriptor);
        this.aw = aw;
        
    	analysisNameField = mainPanel.getAnalysisNameField();
    	analysisNameField.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				setNextButtonAccordingToInputs();
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
        });
    	
    	workDirField = mainPanel.getWorkDirField();
    	chooserButton = mainPanel.getChooserButton();
    	
        chooserButton.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				File selectedFile = selectDir();
				if (selectedFile != null)
					workDirField.setText(selectedFile.toString());
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
    	
    }

    
    public void aboutToDisplayPanel() {
        setNextButtonAccordingToInputs();
    }
    
    public void aboutToHidePanel() {
    	aw.setValue(aw.ANALYSIS_NAME, analysisNameField.getText());
    	aw.setValue(aw.WORKING_DIR, workDirField.getText());
    	aw.setValue(aw.PROCESSORS, mainPanel.getProcessorComboBox().getSelectedItem().toString());
    }    
    
    private void setNextButtonAccordingToInputs() {
         if (!analysisNameField.getText().isEmpty() && !workDirField.getText().isEmpty())
            getWizard().setNextFinishButtonEnabled(true);
         else
            getWizard().setNextFinishButtonEnabled(false);        
    
    }
    
	private File selectDir() {
		JFileChooser fileChooser = new JFileChooser(
				Options.instance().getLastPath());
		
		fileChooser.setDialogTitle("Select the data file");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		int retval = fileChooser.showOpenDialog(AppFrame.instance());
		if (retval == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			return selectedFile;
		}
		
		return null;
	}
    
}
