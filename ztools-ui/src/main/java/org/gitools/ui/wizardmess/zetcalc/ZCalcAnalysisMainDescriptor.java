package org.gitools.ui.wizardmess.zetcalc;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

import org.gitools.ui.AppFrame;
import org.gitools.ui.utils.Options;
import org.gitools.ui.wizardmess.AbstractWizard;
import org.gitools.ui.wizardmess.AnalysisWizard;
import org.gitools.ui.wizardmess.AnalysisWizardPanelDescriptor;
import org.gitools.ui.wizardmess.WizardDataModel;



public class ZCalcAnalysisMainDescriptor extends AnalysisWizardPanelDescriptor {
    
    public static final String IDENTIFIER = "MAIN_PANEL";
    final ZCalcAnalysisMainPanel mainPanel;
    final JButton chooserButton;
    final JTextField workDirField;
    final JTextField analysisNameField;

    WizardDataModel dataModel;
    
    public ZCalcAnalysisMainDescriptor(
    		AbstractWizard aw, Object BackPanelDescriptor, Object NextPanelDescriptor) {
    	
        super(IDENTIFIER, new ZCalcAnalysisMainPanel(), BackPanelDescriptor, NextPanelDescriptor);
        this.dataModel = aw.getWizardDataModel();
        this.mainPanel = (ZCalcAnalysisMainPanel) getPanelComponent();
    	analysisNameField = mainPanel.getAnalysisNameField();
    	
    	KeyListener keyListener = new KeyListener(){
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
    	};
    	
    	analysisNameField.addKeyListener(keyListener);
    	
    	workDirField = mainPanel.getWorkDirField();
    	workDirField.setText(Options.instance().getLastWorkPath());
    	
    	chooserButton = mainPanel.getChooserButton();
    	
    	workDirField.addKeyListener(keyListener);
    	
        chooserButton.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				File selectedFile = selectDir();
				if (selectedFile != null) {
					workDirField.setText(selectedFile.toString());
					setWizardWorkingDir(selectedFile.getAbsolutePath());
				}
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

    
    protected boolean verifyFileName() {
		File dir = new File(workDirField.getText());
		if (dir.exists() && dir.isDirectory()) {
			return true;
		}
		else 
			return false;
	}


	protected void setWizardWorkingDir(String path) {
		dataModel.setValue(AnalysisWizard.WIZARD_WORKING_DIR, path);		
	}


	public void aboutToDisplayPanel() {
        setNextButtonAccordingToInputs();
    }
    
    public void aboutToHidePanel() {
    	dataModel.setValue(AnalysisWizard.ANALYSIS_NAME, analysisNameField.getText());
    	dataModel.setValue(AnalysisWizard.ANALYSIS_WORKING_DIR, workDirField.getText());
    	dataModel.setValue(AnalysisWizard.PROCESSORS, mainPanel.getProcessorComboBox().getSelectedItem().toString());
    	setWizardWorkingDir(workDirField.getText());
    }    
    
    private void setNextButtonAccordingToInputs() {
         if (analysisNameFieldOK() && verifyFileName())
            getWizard().setNextFinishButtonEnabled(true);
         else
            getWizard().setNextFinishButtonEnabled(false);        
    
    }
    
    private boolean analysisNameFieldOK() {
    	return analysisNameField.getText().matches("[\\d\\w_-]+");
    }
    
	private File selectDir() {
		JFileChooser fileChooser = new JFileChooser(
				Options.instance().getLastWorkPath());
		
		fileChooser.setDialogTitle("Select working path");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		int retval = fileChooser.showOpenDialog(AppFrame.instance());
		if (retval == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			Options.instance().setLastWorkPath(selectedFile.getAbsolutePath());
			return selectedFile;
		}
		
		return null;
	}
    
}
