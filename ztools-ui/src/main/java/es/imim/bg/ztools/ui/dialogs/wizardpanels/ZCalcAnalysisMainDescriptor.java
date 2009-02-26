package es.imim.bg.ztools.ui.dialogs.wizardpanels;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.dialogs.AnalysisWizard;
import es.imim.bg.ztools.ui.dialogs.AnalysisWizardPanelDescriptor;
import es.imim.bg.ztools.ui.model.WizardDataModel;
import es.imim.bg.ztools.ui.utils.Options;


public class ZCalcAnalysisMainDescriptor extends AnalysisWizardPanelDescriptor {
    
    public static final String IDENTIFIER = "MAIN_PANEL";
    static ZCalcAnalysisMainPanel mainPanel = new ZCalcAnalysisMainPanel();
    final JButton chooserButton;
    final JTextField workDirField;
    final JTextField analysisNameField;

    WizardDataModel dataModel;
    
    public ZCalcAnalysisMainDescriptor(AnalysisWizard aw, Object BackPanelDescriptor, Object NextPanelDescriptor) {    	
        super(IDENTIFIER, mainPanel, BackPanelDescriptor, NextPanelDescriptor);
        this.dataModel = aw.getWizardDataModel();
        
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
				setWizardWorkingDir(selectedFile.getAbsolutePath());
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
