package es.imim.bg.ztools.ui.dialogs.wizardpanels;

import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.dialogs.AnalysisWizard;
import es.imim.bg.ztools.ui.dialogs.AnalysisWizardPanelDescriptor;
import es.imim.bg.ztools.ui.utils.Options;


import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.*;

import com.nexes.wizard.Wizard;
import com.nexes.wizard.WizardModel;
import com.nexes.wizard.WizardPanelDescriptor;


public class ZCalcAnalysisStatsDescriptor extends AnalysisWizardPanelDescriptor {
    
    public static final String IDENTIFIER = "STATS_PANEL";
    static ZCalcAnalysisStatsPanel statsPanel = new ZCalcAnalysisStatsPanel();
    AnalysisWizard aw;
    final JTextField sampleSizeField;
    final JComboBox statTestBox;
    final JLabel helpLabel;

    
    public ZCalcAnalysisStatsDescriptor(AnalysisWizard aw, Object BackPanelDescriptor, Object NextPanelDescriptor) {    	
        super(IDENTIFIER, statsPanel, BackPanelDescriptor, NextPanelDescriptor);
        
        this.aw = aw;
        
        sampleSizeField = statsPanel.getSampleSizeField();
        statTestBox = statsPanel.getStatTestBox();
        helpLabel = statsPanel.getHelpLabel();
        final String helpLabelText = helpLabel.getText();

        
        helpLabel.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				changeToHelpPanel();
				setNextButtonAccordingToInputs();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				helpLabel.setText("<html><u>"+ helpLabelText +"</u></html>");
			}
			
			@Override
			public void mouseExited(MouseEvent e) { 
				helpLabel.setText("<html>"+ helpLabelText +"</html>");
			}
			
			@Override
			public void mousePressed(MouseEvent e) { }
			
			@Override
			public void mouseReleased(MouseEvent e) { }
        });
    }
            
    
    protected void changeToHelpPanel() {
		System.out.println("help clicked");
		Wizard wizard = getWizard();
        Object descriptor = ZCalcAnalysisStatsHelpDescriptor.IDENTIFIER;
        wizard.setCurrentPanel(descriptor);
	}


	public void aboutToDisplayPanel() {
    	setNextButtonAccordingToInputs();
    }
    
    public void aboutToHidePanel() {
    	aw.setValue(aw.MODULE_FILE, sampleSizeField.getText());
    }    
    
    private void setNextButtonAccordingToInputs() {
         if (sampleSizeInputIsOK())
            getWizard().setNextFinishButtonEnabled(true);
         else
            getWizard().setNextFinishButtonEnabled(false);        
    }
    
    
    private boolean sampleSizeInputIsOK() {
    	String input = sampleSizeField.getText();
		if (!input.isEmpty() && isInteger(input))
				return true;
		else if (input.isEmpty())
			return true;
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
    
}
