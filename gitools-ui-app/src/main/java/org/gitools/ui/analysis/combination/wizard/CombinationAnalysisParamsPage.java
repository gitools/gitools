/*
 * #%L
 * gitools-ui-app
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.ui.analysis.combination.wizard;

import org.gitools.core.matrix.model.IMatrixLayer;
import org.gitools.core.matrix.model.IMatrixLayers;
import org.gitools.ui.IconNames;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class CombinationAnalysisParamsPage extends AbstractWizardPage {

    @Nullable
    private IMatrixLayers attrs;

    private static class AttrOption {
        private String name;
        private IMatrixLayer attr;

        public AttrOption(String name) {
            this.name = name;
        }

        public AttrOption(IMatrixLayer attr) {
            this.attr = attr;
        }

        public IMatrixLayer getAttr() {
            return attr;
        }

        @Override
        public String toString() {
            return attr != null ? attr.getName() : name;
        }
    }

    private String preferredSizeAttr;
    private String preferredPvalueAttr;

    public CombinationAnalysisParamsPage() {
        initComponents();

        dissableAttrCb();

        setTitle("Configure combination options");
        setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_METHOD, 96));

        setComplete(true);
    }

    private void dissableAttrCb() {
        sizeAttrCb.setModel(new DefaultComboBoxModel());
        sizeAttrLabel.setEnabled(false);
        sizeAttrCb.setEnabled(false);
        pvalueAttrCb.setModel(new DefaultComboBoxModel());
        pvalueAttrLabel.setEnabled(false);
        pvalueAttrCb.setEnabled(false);
    }

    public void setAttributes(@Nullable IMatrixLayers<? extends IMatrixLayer> attrs) {
        this.attrs = attrs;

        if (attrs != null) {
            AttrOption[] sizeAttrs = new AttrOption[attrs.size() + 1];
            sizeAttrs[0] = new AttrOption("All columns with the same weight");
            for (int i = 0; i < attrs.size(); i++)
                sizeAttrs[i + 1] = new AttrOption(attrs.get(i));
            sizeAttrCb.setModel(new DefaultComboBoxModel(sizeAttrs));

            AttrOption[] pvalueAttrs = new AttrOption[attrs.size()];
            for (int i = 0; i < attrs.size(); i++)
                pvalueAttrs[i] = new AttrOption(attrs.get(i));
            pvalueAttrCb.setModel(new DefaultComboBoxModel(pvalueAttrs));

            int sizeIndex = -1;
            int pvalueIndex = -1;
            int i = 0;
            for (IMatrixLayer a : attrs) {
                String aid = a.getId();
                if (sizeIndex == -1 && (aid.equals(preferredSizeAttr) || aid.matches("^(n|N)$"))) {
                    sizeIndex = i;
                }

                if (pvalueIndex == -1 && (aid.equals(preferredPvalueAttr) || aid.matches("^(right-|.*)p-value$"))) {
                    pvalueIndex = i;
                }

                i++;
            }

            sizeIndex = sizeIndex == -1 ? 0 : sizeIndex + 1;
            pvalueIndex = pvalueIndex == -1 ? 0 : pvalueIndex + 1;
            sizeAttrCb.setSelectedIndex(sizeIndex);
            pvalueAttrCb.setSelectedIndex(pvalueIndex);
            sizeAttrLabel.setEnabled(true);
            sizeAttrCb.setEnabled(true);
            pvalueAttrLabel.setEnabled(true);
            pvalueAttrCb.setEnabled(true);
        } else {
            dissableAttrCb();
        }
    }

    public void setPreferredSizeAttr(String preferredSizeAttr) {
        this.preferredSizeAttr = preferredSizeAttr;
    }

    public void setPreferredPvalueAttr(String preferredPvalueAttr) {
        this.preferredPvalueAttr = preferredPvalueAttr;
    }

    @Nullable
    public IMatrixLayer getSizeAttribute() {
        AttrOption option = (AttrOption) sizeAttrCb.getSelectedItem();
        return option != null ? option.getAttr() : null;
    }

    @Nullable
    public IMatrixLayer getPvalueAttribute() {
        AttrOption option = (AttrOption) pvalueAttrCb.getSelectedItem();
        return option != null ? option.getAttr() : null;
    }

    public boolean isTransposeEnabled() {
        return applyToRowsRb.isSelected();
    }

    public void setTransposeEnabled(boolean transpose) {
        applyToColumnsRb.setSelected(!transpose);
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        applyButtonGroup = new javax.swing.ButtonGroup();
        sizeAttrLabel = new javax.swing.JLabel();
        sizeAttrCb = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        applyToColumnsRb = new javax.swing.JRadioButton();
        applyToRowsRb = new javax.swing.JRadioButton();
        pvalueAttrLabel = new javax.swing.JLabel();
        pvalueAttrCb = new javax.swing.JComboBox();

        sizeAttrLabel.setText("Size attribute");

        jLabel2.setText("Apply to:");

        applyButtonGroup.add(applyToColumnsRb);
        applyToColumnsRb.setSelected(true);
        applyToColumnsRb.setText("Columns");

        applyButtonGroup.add(applyToRowsRb);
        applyToRowsRb.setText("Rows");

        pvalueAttrLabel.setText("P-value attribute");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(sizeAttrLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(pvalueAttrLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(sizeAttrCb, 0, 304, Short.MAX_VALUE).addComponent(pvalueAttrCb, javax.swing.GroupLayout.Alignment.LEADING, 0, 304, Short.MAX_VALUE))).addComponent(jLabel2).addComponent(applyToColumnsRb).addComponent(applyToRowsRb)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(sizeAttrLabel).addComponent(sizeAttrCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(pvalueAttrLabel).addComponent(pvalueAttrCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(applyToColumnsRb).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(applyToRowsRb).addContainerGap(125, Short.MAX_VALUE)));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup applyButtonGroup;
    private javax.swing.JRadioButton applyToColumnsRb;
    private javax.swing.JRadioButton applyToRowsRb;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JComboBox pvalueAttrCb;
    private javax.swing.JLabel pvalueAttrLabel;
    private javax.swing.JComboBox sizeAttrCb;
    private javax.swing.JLabel sizeAttrLabel;
    // End of variables declaration//GEN-END:variables

}
