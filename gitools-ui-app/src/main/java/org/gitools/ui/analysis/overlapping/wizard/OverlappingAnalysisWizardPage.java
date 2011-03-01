/*
 *  Copyright 2010 Universitat Pompeu Fabra.
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
 * OverlappingPage.java
 *
 * Created on 25-mar-2010, 17:40:59
 */

package org.gitools.ui.analysis.overlapping.wizard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.event.DocumentEvent;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.ui.IconNames;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.gitools.ui.utils.DocumentChangeListener;

public class OverlappingAnalysisWizardPage extends AbstractWizardPage {
	private List<IElementAttribute> attrs;

	private static class AttrOption {
		private String name;
		private IElementAttribute attr;

		public AttrOption(String name) {
			this.name = name;
		}

		public AttrOption(IElementAttribute attr) {
			this.attr = attr;
		}

		public IElementAttribute getAttr() {
			return attr;
		}

		@Override
		public String toString() {
			return attr != null ? attr.getName() : name;
		}
	}

    public OverlappingAnalysisWizardPage() {
		super();

		initComponents();

		setTitle("Configure overlapping options");

		setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_METHOD, 96));

		setComplete(true);

		replaceEmptyValuesCheck.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				replaceValueField.setEnabled(replaceEmptyValuesCheck.isSelected());
			}
		});
		replaceValueField.getDocument().addDocumentListener(new DocumentChangeListener() {

			@Override
			protected void update(DocumentEvent e) {

				boolean valid = isValidNumber(replaceValueField.getText());

				boolean completed = !replaceEmptyValuesCheck.isSelected() || valid;

				setComplete(completed);

				if (!valid) {

					setStatus(MessageStatus.ERROR);

					setMessage("Invalid replacement for empty values, it should be a real number");

				} else

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
        attributeLabel = new javax.swing.JLabel();
        attributeCb = new javax.swing.JComboBox();
        replaceEmptyValuesCheck = new javax.swing.JCheckBox();
        replaceValueField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        applyToColumnsRb = new javax.swing.JRadioButton();
        applyToRowsRb = new javax.swing.JRadioButton();

        attributeLabel.setText("Take values from");

        replaceEmptyValuesCheck.setText("Replace empty values by");
        replaceEmptyValuesCheck.setVerticalAlignment(javax.swing.SwingConstants.TOP);

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
                        .addComponent(attributeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(attributeCb, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(replaceEmptyValuesCheck)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(replaceValueField, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2)
                    .addComponent(applyToColumnsRb)
                    .addComponent(applyToRowsRb))
                .addContainerGap(100, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(attributeLabel)
                    .addComponent(attributeCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(replaceEmptyValuesCheck)
                    .addComponent(replaceValueField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(applyToColumnsRb)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(applyToRowsRb)
                .addContainerGap(116, Short.MAX_VALUE))
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


	public void setAttributes(List<IElementAttribute> attrs) {
		this.attrs = attrs;

		if (attrs != null) {
			AttrOption[] pvalueAttrs = new AttrOption[attrs.size()];

			for (int i = 0; i < attrs.size(); i++)
				pvalueAttrs[i] = new AttrOption(attrs.get(i));

			attributeCb.setModel(new DefaultComboBoxModel(pvalueAttrs));
			attributeCb.setSelectedIndex(0);
			attributeCb.setEnabled(true);
			attributeCb.setVisible(true);
			attributeLabel.setVisible(true);
		}
		else
			dissableAttrCb();
	}

	private void dissableAttrCb() {
		attributeCb.setModel(new DefaultComboBoxModel());
		attributeCb.setEnabled(false);
		attributeCb.setVisible(false);
		attributeLabel.setVisible(false);
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup applyGroup;
    private javax.swing.JRadioButton applyToColumnsRb;
    private javax.swing.JRadioButton applyToRowsRb;
    private javax.swing.JComboBox attributeCb;
    private javax.swing.JLabel attributeLabel;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JCheckBox replaceEmptyValuesCheck;
    private javax.swing.JTextField replaceValueField;
    // End of variables declaration//GEN-END:variables

	public int getAttributeIndex() {
		return attributeCb.getSelectedIndex();
	}

	public boolean isReplaceNanValuesEnabled() {
		return replaceEmptyValuesCheck.isSelected();
	}

	public double getReplaceNanValue() {
		return Double.parseDouble(replaceValueField.getText());
	}

	public boolean isTransposeEnabled() {
		return applyToRowsRb.isSelected();
	}

	public void setReplaceNanValue(double value) {
		replaceValueField.setText(Double.toString(value));
	}

	public void setTransposeEnabled(boolean enabled) {
		applyToRowsRb.setSelected(enabled);
	}
	
	public void setReplaceNanValuesEnabled(boolean enabled) {
		replaceEmptyValuesCheck.setSelected(enabled);
	}

}