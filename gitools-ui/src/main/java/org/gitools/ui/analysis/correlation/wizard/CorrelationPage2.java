/*
 *  Copyright 2010 chris.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

/*
 * CorrelationPage.java
 *
 * Created on 25-mar-2010, 17:40:59
 */

package org.gitools.ui.analysis.correlation.wizard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.event.DocumentEvent;
import org.gitools.ui.IconNames;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.gitools.ui.utils.DocumentChangeListener;

public class CorrelationPage2 extends AbstractWizardPage {

    public CorrelationPage2() {
		super();

		initComponents();

		setTitle("Correlation method");

		setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_METHOD, 96));

		setComplete(true);
		
		replaceEmptyValuesCheck.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				replaceValueField.setEnabled(replaceEmptyValuesCheck.isSelected());
			}
		});

		replaceValueField.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override protected void update(DocumentEvent e) {
				boolean valid = isValidNumber(replaceValueField.getText());
				boolean completed = !replaceEmptyValuesCheck.isSelected() || valid;
				setComplete(completed);

				if (!valid) {
					setStatus(MessageStatus.ERROR);
					setMessage("Invalid replacement for empty values, it should be a real number");
				}
				else
					setMessage(MessageStatus.INFO, "");
			}
		});
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        applyGroup = new javax.swing.ButtonGroup();
        replaceEmptyValuesCheck = new javax.swing.JCheckBox();
        replaceValueField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        applyToColumnsRb = new javax.swing.JRadioButton();
        applyToRowsRb = new javax.swing.JRadioButton();

        replaceEmptyValuesCheck.setText("Replace empty values by");

        replaceValueField.setText("0");
        replaceValueField.setEnabled(false);

        jLabel2.setText("Apply to:");

        applyGroup.add(applyToColumnsRb);
        applyToColumnsRb.setSelected(true);
        applyToColumnsRb.setText("Columns");

        applyGroup.add(applyToRowsRb);
        applyToRowsRb.setText("Rows");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(replaceEmptyValuesCheck)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(replaceValueField, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2)
                    .addComponent(applyToColumnsRb)
                    .addComponent(applyToRowsRb))
                .addContainerGap(152, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(replaceEmptyValuesCheck)
                    .addComponent(replaceValueField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(applyToColumnsRb)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(applyToRowsRb)
                .addContainerGap(161, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

	@Override
	public JComponent createControls() {
		return this;
	}

	private boolean isValidNumber(String text) {
		try {
			Double.parseDouble(text);
		}
		catch (NumberFormatException ex) {
			return false;
		}
		return true;
	}


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup applyGroup;
    private javax.swing.JRadioButton applyToColumnsRb;
    private javax.swing.JRadioButton applyToRowsRb;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JCheckBox replaceEmptyValuesCheck;
    private javax.swing.JTextField replaceValueField;
    // End of variables declaration//GEN-END:variables

	public boolean isReplaceNanValuesEnabled() {
		return replaceEmptyValuesCheck.isSelected();
	}

	public double getReplaceNanValue() {
		return Double.parseDouble(replaceValueField.getText());
	}

	public boolean isTransposeEnabled() {
		return applyToRowsRb.isSelected();
	}
}