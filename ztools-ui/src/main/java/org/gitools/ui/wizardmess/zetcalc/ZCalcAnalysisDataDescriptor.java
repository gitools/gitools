package org.gitools.ui.wizardmess.zetcalc;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.*;

import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.utils.Options;
import org.gitools.ui.wizardmess.AbstractWizard;
import org.gitools.ui.wizardmess.AnalysisWizard;
import org.gitools.ui.wizardmess.AnalysisWizardPanelDescriptor;
import org.gitools.ui.wizardmess.WizardDataModel;
import org.gitools.ui.wizardmess.AnalysisWizard.Condition;

public class ZCalcAnalysisDataDescriptor extends AnalysisWizardPanelDescriptor {

	public static final String IDENTIFIER = "DATA_PANEL";

	final ZCalcAnalysisDataPanel dataPanel;
	WizardDataModel dataModel;
	final JButton chooserButton;
	final JTextField fileNameField;
	final JComboBox binCutoffConditionBox;
	final JTextField binCutoffField;
    final JCheckBox omitNonMappedItemsBox;


	public ZCalcAnalysisDataDescriptor(
			AbstractWizard aw, Object BackPanelDescriptor, Object NextPanelDescriptor) {
		
		super(IDENTIFIER, new ZCalcAnalysisDataPanel(), BackPanelDescriptor,
				NextPanelDescriptor);

		this.dataPanel = (ZCalcAnalysisDataPanel) getPanelComponent();
		this.dataModel = aw.getWizardDataModel();

		chooserButton = dataPanel.getChooserButton();
		fileNameField = dataPanel.getFileNameField();
		binCutoffConditionBox = dataPanel.getBinCutoffConditionBox();
		binCutoffField = dataPanel.getBinCutoffValueField();
		omitNonMappedItemsBox = dataPanel.getOmitNonMappedItemsBox();

		chooserButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File selectedFile = selectFile();
				if (selectedFile != null) {
					fileNameField.setText(selectedFile.toString());
					setWizardWorkingDir(selectedFile.getAbsolutePath());
				}
				setNextButtonAccordingToInputs();
			}
		});

		binCutoffConditionBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setBinCutOffFieldAccordingToInputs();
				setNextButtonAccordingToInputs();
			}

		});

		KeyListener keyListener = new KeyListener() {
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

		fileNameField.addKeyListener(keyListener);
		binCutoffField.addKeyListener(keyListener);
	}

	protected void setWizardWorkingDir(String path) {
		dataModel.setValue(AnalysisWizard.WIZARD_WORKING_DIR, path);
	}

	public void aboutToDisplayPanel() {
		setNextButtonAccordingToInputs();
		setBinCutOffFieldAccordingToInputs();
	}

	public void aboutToHidePanel() {
		dataModel.setValue(AnalysisWizard.DATA_FILE, fileNameField.getText());
		dataModel.setValue(AnalysisWizard.OMIT_NON_MAPPED_ITMEMS, 
								omitNonMappedItemsBox.isSelected());
		String conditionItemString = binCutoffConditionBox.getSelectedItem().toString();
		if (conditionItemString.equals(dataPanel.BIN_CUTOFF_DISABLED))
			dataModel.setValue(
					AnalysisWizard.BIN_CUTOFF_CONDITION,
					binCutoffConditionBox.getSelectedItem().toString());
		else {
			Condition[] conditions = Condition.values();
			Condition c = conditions[binCutoffConditionBox.getSelectedIndex() - 1];
			dataModel.setValue(
					AnalysisWizard.BIN_CUTOFF_CONDITION, 
					c.toCommandLineArgument());
			dataModel.setValue(
					AnalysisWizard.BIN_CUTOFF_VALUE, 
					binCutoffField.getText());
		}
	}

	private void setNextButtonAccordingToInputs() {
		if (verifyFileName() && binCutoffDisabled())
			getWizard().setNextFinishButtonEnabled(true);
		else if (verifyFileName() && !binCutoffDisabled()
				&& isNumeric(binCutoffField.getText()))
			getWizard().setNextFinishButtonEnabled(true);
		else {
			getWizard().setNextFinishButtonEnabled(false);
		}
	}

	private boolean isNumeric(String text) {
		try {
			Double.parseDouble(text);
		} catch (Exception e) {
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

	protected boolean verifyFileName() {
		File file = new File(fileNameField.getText());
		if (file.exists() && file.isFile()) {
			return true;
		} else
			return false;
	}

	private boolean binCutoffDisabled() {
		return binCutoffConditionBox.getSelectedItem().toString().equals(
				dataPanel.BIN_CUTOFF_DISABLED);
	}

	private File selectFile() {
		JFileChooser fileChooser = new JFileChooser(
				Options.instance().getLastDataPath());

		fileChooser.setDialogTitle("Select file");

		int retval = fileChooser.showOpenDialog(AppFrame.instance());
		if (retval == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			Options.instance().setLastDataPath(selectedFile.getParent());
			return selectedFile;
		}

		return null;
	}

}
