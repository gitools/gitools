package es.imim.bg.ztools.ui.wizards;

import java.awt.Dialog;
import java.awt.Frame;

import com.nexes.wizard.Wizard;

public class AbstractWizard extends Wizard {

	protected static WizardDataModel dataModel;

	public AbstractWizard() {
		super();
		dataModel = new WizardDataModel();
	}

	public AbstractWizard(Dialog owner) {
		super(owner);
	}

	public AbstractWizard(Frame owner) {
		super(owner);
	}

	public WizardDataModel getWizardDataModel() {
		return dataModel;
	}

}