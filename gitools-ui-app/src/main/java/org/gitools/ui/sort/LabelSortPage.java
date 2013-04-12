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
package org.gitools.ui.sort;

import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.sort.ValueSortCriteria.SortDirection;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.gitools.ui.platform.wizard.PageDialog;
import org.gitools.ui.utils.DocumentChangeListener;
import org.gitools.ui.wizard.common.PatternSourcePage;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LabelSortPage extends AbstractWizardPage
{

    private final Heatmap hm;

    private String rowsPat;
    private String colsPat;

    public LabelSortPage(Heatmap hm)
    {
        this.hm = hm;

        initComponents();

        rowsLabelFld.setText("id");
        rowsPat = "${id}";

        colsLabelFld.setText("id");
        colsPat = "${id}";

        ActionListener dimListener = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                dimChanged();
            }
        };

        rowsChk.addActionListener(dimListener);
        colsChk.addActionListener(dimListener);

        rowsLabelFld.getDocument().addDocumentListener(new DocumentChangeListener()
        {
            @Override
            protected void update(DocumentEvent e)
            {
                updateComplete();
            }
        });

        rowsLabelBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                selectRowsPattern();
            }
        });

        rowsDirCb.setModel(new DefaultComboBoxModel(SortDirection.values()));

        colsLabelFld.getDocument().addDocumentListener(new DocumentChangeListener()
        {
            @Override
            protected void update(DocumentEvent e)
            {
                updateComplete();
            }
        });

        colsLabelBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                selectColsPattern();
            }
        });

        colsDirCb.setModel(new DefaultComboBoxModel(SortDirection.values()));

        setTitle("Sort by label");
        updateComplete();
    }

    @Override
    public void updateControls()
    {
        dimChanged();
    }

    private void updateComplete()
    {
        setComplete((rowsChk.isSelected() || colsChk.isSelected()) && !((rowsChk.isSelected() && rowsLabelFld.getText().isEmpty()) || (colsChk.isSelected() && colsLabelFld.getText().isEmpty())));
    }

    private void dimChanged()
    {
        boolean rs = rowsChk.isSelected();
        rowsLabelFld.setEnabled(rs);
        rowsLabelBtn.setEnabled(rs);
        rowsDirCb.setEnabled(rs);
        numericRowsCb.setEnabled(rs);

        boolean cs = colsChk.isSelected();
        colsLabelFld.setEnabled(cs);
        colsLabelBtn.setEnabled(cs);
        colsDirCb.setEnabled(cs);
        numericColumnsCb.setEnabled(cs);

        updateComplete();
    }

    private void selectRowsPattern()
    {
        PatternSourcePage page = new PatternSourcePage(hm.getRows().getAnnotations(), true);
        PageDialog dlg = new PageDialog(AppFrame.get(), page);
        dlg.setVisible(true);
        if (dlg.isCancelled())
        {
            return;
        }

        rowsPat = page.getPattern();
        rowsLabelFld.setText(page.getPatternTitle());
    }

    private void selectColsPattern()
    {
        PatternSourcePage page = new PatternSourcePage(hm.getColumns().getAnnotations(), true);
        PageDialog dlg = new PageDialog(AppFrame.get(), page);
        dlg.setVisible(true);
        if (dlg.isCancelled())
        {
            return;
        }

        colsPat = page.getPattern();
        colsLabelFld.setText(page.getPatternTitle());
    }

    public boolean isApplyToRowsSelected()
    {
        return rowsChk.isSelected();
    }

    public String getRowsPattern()
    {
        return rowsPat;
    }

    @NotNull
    public SortDirection getRowsDirection()
    {
        return (SortDirection) rowsDirCb.getSelectedItem();
    }

    public boolean getRowsNumeric()
    {
        return numericRowsCb.isSelected() && rowsChk.isSelected();
    }

    public boolean isApplyToColumnsSelected()
    {
        return colsChk.isSelected();
    }

    public String getColumnsPattern()
    {
        return colsPat;
    }

    @NotNull
    public SortDirection getColumnsDirection()
    {
        return (SortDirection) colsDirCb.getSelectedItem();
    }

    public boolean getColumnsNumeric()
    {
        return numericColumnsCb.isSelected() && colsChk.isSelected();
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jLabel1 = new javax.swing.JLabel();
        rowsDirCb = new javax.swing.JComboBox();
        rowsChk = new javax.swing.JCheckBox();
        rowsLabelFld = new javax.swing.JTextField();
        rowsLabelBtn = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        colsChk = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        colsLabelFld = new javax.swing.JTextField();
        colsLabelBtn = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        colsDirCb = new javax.swing.JComboBox();
        numericColumnsCb = new javax.swing.JCheckBox();
        numericRowsCb = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        jLabel1.setText("Direction");

        rowsChk.setSelected(true);
        rowsChk.setText("Rows");

        rowsLabelFld.setEditable(false);

        rowsLabelBtn.setText("Change...");

        jLabel2.setText("Label");

        colsChk.setText("Columns");

        jLabel3.setText("Label");

        colsLabelFld.setEditable(false);

        colsLabelBtn.setText("Change...");

        jLabel4.setText("Direction");

        numericColumnsCb.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                numericColumnsCbActionPerformed(evt);
            }
        });

        numericRowsCb.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        numericRowsCb.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                numericRowsCbActionPerformed(evt);
            }
        });

        jLabel5.setText("Numeric");

        jLabel6.setText("Numeric");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(12, 12, 12).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1).addComponent(jLabel2).addComponent(jLabel6)).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(rowsLabelFld, javax.swing.GroupLayout.DEFAULT_SIZE, 367, Short.MAX_VALUE).addComponent(rowsDirCb, javax.swing.GroupLayout.Alignment.LEADING, 0, 367, Short.MAX_VALUE))).addGroup(layout.createSequentialGroup().addComponent(rowsChk).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 397, Short.MAX_VALUE))).addGap(26, 26, 26).addComponent(rowsLabelBtn)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(colsChk).addGroup(layout.createSequentialGroup().addGap(11, 11, 11).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel4).addComponent(jLabel3).addComponent(jLabel5)).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(numericColumnsCb, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(colsLabelFld, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE).addComponent(colsDirCb, javax.swing.GroupLayout.Alignment.LEADING, 0, 368, Short.MAX_VALUE)).addComponent(numericRowsCb, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))))).addGap(26, 26, 26).addComponent(colsLabelBtn))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(rowsChk).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(rowsLabelFld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(rowsLabelBtn)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(rowsDirCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(layout.createSequentialGroup().addComponent(jLabel6).addGap(34, 34, 34).addComponent(colsChk).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)).addGroup(layout.createSequentialGroup().addComponent(numericRowsCb, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(63, 63, 63))).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(colsLabelFld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(colsLabelBtn)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel4).addComponent(colsDirCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(layout.createSequentialGroup().addComponent(jLabel5).addGap(34, 34, 34)).addGroup(layout.createSequentialGroup().addComponent(numericColumnsCb, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(29, 29, 29)))));
    }// </editor-fold>//GEN-END:initComponents

    private void numericRowsCbActionPerformed(java.awt.event.ActionEvent evt)
    {//GEN-FIRST:event_numericRowsCbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_numericRowsCbActionPerformed

    private void numericColumnsCbActionPerformed(java.awt.event.ActionEvent evt)
    {//GEN-FIRST:event_numericColumnsCbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_numericColumnsCbActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox colsChk;
    private javax.swing.JComboBox colsDirCb;
    private javax.swing.JButton colsLabelBtn;
    private javax.swing.JTextField colsLabelFld;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JCheckBox numericColumnsCb;
    private javax.swing.JCheckBox numericRowsCb;
    private javax.swing.JCheckBox rowsChk;
    private javax.swing.JComboBox rowsDirCb;
    private javax.swing.JButton rowsLabelBtn;
    private javax.swing.JTextField rowsLabelFld;
    // End of variables declaration//GEN-END:variables
}
