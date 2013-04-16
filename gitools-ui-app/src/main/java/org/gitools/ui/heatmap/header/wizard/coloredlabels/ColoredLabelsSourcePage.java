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
package org.gitools.ui.heatmap.header.wizard.coloredlabels;

import org.gitools.clustering.method.annotations.AnnPatClusteringMethod;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.matrix.model.matrix.AnnotationMatrix;
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.gitools.ui.utils.DocumentChangeListener;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.*;

/**
 * @noinspection ALL
 */
public class ColoredLabelsSourcePage extends AbstractWizardPage
{

    private final HeatmapDimension hdim;
    private final AnnPatClusteringMethod method;

    public ColoredLabelsSourcePage(@NotNull HeatmapDimension hdim, AnnPatClusteringMethod method)
    {
        this.hdim = hdim;
        this.method = method;

        initComponents();

        ChangeListener optListener = new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                sourceChanged();
            }
        };

        annOpt.addChangeListener(optListener);
        patOpt.addChangeListener(optListener);

        AnnotationMatrix am = hdim.getAnnotations();
        if (am != null && am.getColumns().size() > 0)
        {
            annOpt.setSelected(true);
            DefaultListModel model = new DefaultListModel();
            for (int i = 0; i < am.getColumns().size(); i++)
                model.addElement(am.internalColumnLabel(i));
            annList.setModel(model);
            annList.setSelectedIndex(0);
        }
        else
        {
            patOpt.setSelected(true);
            annOpt.setEnabled(false);
        }

        annList.addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                updateCompleted();
            }
        });

        annSepCb.setModel(new DefaultComboBoxModel(new String[]{", ", "-", " | ", " / ", " > ", "::"}));
        annSepCb.setSelectedIndex(0);

        patText.getDocument().addDocumentListener(new DocumentChangeListener()
        {
            @Override
            protected void update(DocumentEvent e)
            {
                updateCompleted();
            }
        });

        setTitle("Annotations selection");
        setComplete(true);
    }

    private void updateCompleted()
    {
        boolean completed = annOpt.isSelected() && annList.getSelectedIndices().length > 0 || patOpt.isSelected() && patText.getDocument().getLength() > 0;

        setComplete(completed);
    }

    private void sourceChanged()
    {
        boolean annSel = annOpt.isSelected();
        annList.setEnabled(annSel);
        annSepLabel.setEnabled(annSel);
        annSepCb.setEnabled(annSel);
        patText.setEnabled(patOpt.isSelected());
    }

    @Override
    public void updateModel()
    {
        super.updateModel();

        method.setPattern(getPattern());
    }

    String getPattern()
    {
        if (patOpt.isSelected())
        {
            return patText.getText();
        }

        AnnotationMatrix am = hdim.getAnnotations();
        StringBuilder sb = new StringBuilder();
        int[] indices = annList.getSelectedIndices();

        sb.append("${");
        sb.append(am.internalColumnLabel(indices[0]));
        sb.append("}");
        for (int i = 1; i < indices.length; i++)
        {
            sb.append(annSepCb.getSelectedItem());
            sb.append("${");
            sb.append(am.internalColumnLabel(indices[i]));
            sb.append("}");
        }

        return sb.toString();
    }

    public String getClusterTitle()
    {
        if (patOpt.isSelected())
        {
            return patText.getText();
        }

        AnnotationMatrix am = hdim.getAnnotations();
        StringBuilder sb = new StringBuilder();
        int[] indices = annList.getSelectedIndices();

        sb.append(am.internalColumnLabel(indices[0]));
        for (int i = 1; i < indices.length; i++)
        {
            sb.append(annSepCb.getSelectedItem());
            sb.append(am.internalColumnLabel(indices[i]));
        }

        return sb.toString();
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

        optGroup = new javax.swing.ButtonGroup();
        annOpt = new javax.swing.JRadioButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        annList = new javax.swing.JList();
        patOpt = new javax.swing.JRadioButton();
        patText = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        annSepLabel = new javax.swing.JLabel();
        annSepCb = new javax.swing.JComboBox();

        optGroup.add(annOpt);
        annOpt.setText("Create labels by selecting a set of annotations");

        jScrollPane1.setViewportView(annList);

        optGroup.add(patOpt);
        patOpt.setText("Create labels by using a pattern");

        patText.setText("${id}");

        jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getSize() - 2f));
        jLabel1.setText("Press Ctrl key while selecting for multiple annotations");

        annSepLabel.setText("Separator");

        annSepCb.setEditable(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(24, 24, 24).addComponent(patText, javax.swing.GroupLayout.PREFERRED_SIZE, 544, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addGap(24, 24, 24).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 544, Short.MAX_VALUE).addComponent(jLabel1).addGroup(layout.createSequentialGroup().addComponent(annSepLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(annSepCb, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(annOpt)).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(patOpt))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(annOpt).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel1).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(annSepLabel).addComponent(annSepCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addComponent(patOpt).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(patText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList annList;
    private javax.swing.JRadioButton annOpt;
    private javax.swing.JComboBox annSepCb;
    private javax.swing.JLabel annSepLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.ButtonGroup optGroup;
    private javax.swing.JRadioButton patOpt;
    private javax.swing.JTextField patText;
    // End of variables declaration//GEN-END:variables

}
