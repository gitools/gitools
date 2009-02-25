package es.imim.bg.ztools.ui.dialogs.wizardpanels;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.nexes.wizard.Wizard;

import es.imim.bg.ztools.ui.dialogs.AnalysisWizard;
import es.imim.bg.ztools.ui.dialogs.AnalysisWizardPanelDescriptor;
import es.imim.bg.ztools.ui.dialogs.AnalysisWizard.Condition;
import es.imim.bg.ztools.ui.dialogs.AnalysisWizard.StatTest;


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
        final String helpLabelText = helpLabel.getText().replaceAll("\\<.*?\\>", "");
        
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
		Wizard wizard = getWizard();
        Object descriptor = ZCalcAnalysisStatsHelpDescriptor.IDENTIFIER;
        wizard.setCurrentPanel(descriptor);
	}


	public void aboutToDisplayPanel() {
    	setNextButtonAccordingToInputs();
    }
    
    public void aboutToHidePanel() {
    	if (!sampleSizeField.getText().isEmpty()) {
    		aw.setValue(AnalysisWizard.SAMPLE_SIZE, sampleSizeField.getText());
    	}
    	
    	StatTest[] statTests = StatTest.values();
    	StatTest st = statTests[statTestBox.getSelectedIndex()];
    	aw.setValue(AnalysisWizard.STAT_TEST, st.toCommandLineArgument());
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
