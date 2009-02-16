package es.imim.bg.ztools.ui.dialogs.wizardpanels;

import com.nexes.wizard.*;

import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.dialogs.AnalysisWizard;
import es.imim.bg.ztools.ui.utils.Options;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Map;

import javax.swing.*;


public class ZCalcAnalysisDataDescriptor extends WizardPanelDescriptor {
    
    public static final String IDENTIFIER = "DATA_PANEL";
    private static final ZCalcAnalysisDataPanel dataPanel = new ZCalcAnalysisDataPanel();
    final JButton choserButton;
    final JTextField fileNameField;
    
    public ZCalcAnalysisDataDescriptor(AnalysisWizard aw) {
        super(IDENTIFIER, dataPanel);
                
        choserButton = dataPanel.getChoserButton();
        fileNameField = dataPanel.getFileNameField();
        
        choserButton.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				File selectedFile = selectFile();
				if (selectedFile != null)
					fileNameField.setText(selectedFile.toString());
				setNextButtonAccordingToInputs();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			@Override
			public void mouseExited(MouseEvent e) {
			}
			@Override
			public void mousePressed(MouseEvent e) {
			}
			@Override
			public void mouseReleased(MouseEvent e) {
			}
        	
        });
    }
    
    public Object getNextPanelDescriptor() {
        return FINISH;
    }
    
    public Object getBackPanelDescriptor() {
        return ZCalcAnalysisMainDescriptor.IDENTIFIER;
    }  
    
    public void aboutToDisplayPanel() {
    	setNextButtonAccordingToInputs();
    }
    
    private void setNextButtonAccordingToInputs() {
        if (!fileNameField.getText().isEmpty())
           getWizard().setNextFinishButtonEnabled(true);
        else
           getWizard().setNextFinishButtonEnabled(false);        
   
   }
    
	private File selectFile() {
		JFileChooser fileChooser = new JFileChooser(
				Options.instance().getLastPath());
		
		fileChooser.setDialogTitle("Select the data file");
		
		int retval = fileChooser.showOpenDialog(AppFrame.instance());
		if (retval == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			if (verifyDataFile(selectedFile))
				return selectedFile;
		}
		
		return null;
		}

	private boolean verifyDataFile(File selectedFile) {
		// TODO Auto-generated method stub
		return true;
	}
    
}
