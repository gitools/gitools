package es.imim.bg.ztools.ui.wizards;

import java.awt.Component;

import com.nexes.wizard.WizardPanelDescriptor;

public class AnalysisWizardPanelDescriptor extends WizardPanelDescriptor {
	
    private Object NEXT_PANEL_DESCRIPTOR = FINISH;
    private Object BACK_PANEL_DESCRIPTOR = null;

	public AnalysisWizardPanelDescriptor(
								Object id, 
								Component panel, 
								Object BackPanelDescriptor, 
								Object NextPanelDescriptor) {
		super(id, panel);
		if (NextPanelDescriptor != null)
			NEXT_PANEL_DESCRIPTOR = NextPanelDescriptor;
		BACK_PANEL_DESCRIPTOR = BackPanelDescriptor;
	}
	
	public AnalysisWizardPanelDescriptor(Object id, Component panel){
		super(id, panel);
	}
	
	public AnalysisWizardPanelDescriptor(){
		super();
	}
	
	 /**
     * Change the panel that follows
     */   
	public void setNextPanelDescriptor(String Descriptor) {
		NEXT_PANEL_DESCRIPTOR = Descriptor;
	}
	
	 /**
     * Change the panel that precedes
     */   
	public void setBackPanelDescriptor(String Descriptor) {
		BACK_PANEL_DESCRIPTOR = Descriptor;
	}
	
	@Override
    public Object getNextPanelDescriptor() {
        return NEXT_PANEL_DESCRIPTOR;
    }
    
	@Override
    public Object getBackPanelDescriptor() {
        return BACK_PANEL_DESCRIPTOR;
    }    

}
