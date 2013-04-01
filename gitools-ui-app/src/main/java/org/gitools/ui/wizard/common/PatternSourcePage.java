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
package org.gitools.ui.wizard.common;

import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.gitools.ui.utils.DocumentChangeListener;

import javax.swing.*;
import javax.swing.event.*;

public class PatternSourcePage extends AbstractWizardPage
{

    private AnnotationMatrix am;

    private boolean idOptVisible;

    public PatternSourcePage(boolean idOptVisible)
    {
        this(null, idOptVisible);
    }

    public PatternSourcePage(AnnotationMatrix am, boolean idOptVisible)
    {
        this.am = am;
        this.idOptVisible = idOptVisible;

        initComponents();

        ChangeListener optListener = new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                sourceChanged();
            }
        };

        idOpt.addChangeListener(optListener);
        annOpt.addChangeListener(optListener);
        patOpt.addChangeListener(optListener);

        annList.addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                annListChanged();
            }
        });

        annSepCb.setModel(new DefaultComboBoxModel(
                new String[]{", ", "-", " | ", " / ", " > ", "::"}));
        annSepCb.setSelectedIndex(0);

        patText.getDocument().addDocumentListener(new DocumentChangeListener()
        {
            @Override
            protected void update(DocumentEvent e)
            {
                updateComplete();
            }
        });

        setTitle("Annotations selection");
        setComplete(true);
    }

    @Override
    public void updateControls()
    {
        super.updateControls();

        if (am != null && am.getColumnCount() > 0)
        {
            annOpt.setSelected(true);
            DefaultListModel model = new DefaultListModel();
            if (idOptVisible)
            {
                model.addElement("id");
            }
            for (int i = 0; i < am.getColumnCount(); i++)
                model.addElement(am.getColumnLabel(i));
            annList.setModel(model);
            annList.setSelectedIndex(0);
        }
        else
        {
            if (idOptVisible)
            {
                idOpt.setSelected(true);
            }
            else
            {
                patOpt.setSelected(true);
            }
            annOpt.setEnabled(false);
        }
    }

    private void updateComplete()
    {
        setComplete(
                annOpt.isSelected() && annList.getSelectedIndices().length > 0
                        || patOpt.isSelected() && patText.getDocument().getLength() > 0);
    }

    private void sourceChanged()
    {
        boolean annSel = annOpt.isSelected();
        annList.setEnabled(annSel);
        annSepLabel.setEnabled(annSel);
        annSepCb.setEnabled(annSel);
        patText.setEnabled(patOpt.isSelected());
    }

    private void annListChanged()
    {
        patText.setText(getPattern());
        updateComplete();
    }

    public String getPattern()
    {
        if (idOpt.isSelected())
        {
            return "${id}";
        }
        else if (patOpt.isSelected())
        {
            return patText.getText();
        }

        StringBuilder sb = new StringBuilder();
        Object[] values = annList.getSelectedValues();
        if (values.length == 0)
        {
            return "";
        }

        sb.append("${");
        sb.append(values[0]);
        sb.append("}");
        for (int i = 1; i < values.length; i++)
        {
            sb.append(annSepCb.getSelectedItem());
            sb.append("${");
            sb.append(values[i]);
            sb.append("}");
        }

        return sb.toString();
    }

    public String getPatternTitle()
    {
        if (idOpt.isSelected())
        {
            return "id";
        }
        else if (patOpt.isSelected())
        {
            return patText.getText();
        }

        StringBuilder sb = new StringBuilder();
        Object[] values = annList.getSelectedValues();

        sb.append(values[0]);
        for (int i = 1; i < values.length; i++)
        {
            sb.append(annSepCb.getSelectedItem());
            sb.append(values[i]);
        }

        return sb.toString();
    }

    public void setAnnotationMatrix(AnnotationMatrix am)
    {
        this.am = am;
        updateControls();
    }

    public boolean isIdOptVisible()
    {
        return idOptVisible;
    }

    public void setIdOptVisible(boolean idOptVisible)
    {
        this.idOptVisible = idOptVisible;
        idOpt.setVisible(idOptVisible);
        invalidate();
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
        idOpt = new javax.swing.JRadioButton();

        optGroup.add(annOpt);
        annOpt.setText("Annotations");

        jScrollPane1.setViewportView(annList);

        optGroup.add(patOpt);
        patOpt.setText("Pattern");

        patText.setText("${id}");

        jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getSize() - 2f));
        jLabel1.setText("Press Ctrl key to select for multiple annotations");

        annSepLabel.setText("Separator");

        annSepCb.setEditable(true);

        optGroup.add(idOpt);
        idOpt.setText("Identifiers");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(24, 24, 24)
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(24, 24, 24)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel1)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(annSepLabel)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(annSepCb, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(patOpt))
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(idOpt))
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(annOpt))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(24, 24, 24)
                                                .addComponent(patText, javax.swing.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(idOpt)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(annOpt)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(annSepLabel)
                                        .addComponent(annSepCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(patOpt)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(patText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList annList;
    private javax.swing.JRadioButton annOpt;
    private javax.swing.JComboBox annSepCb;
    private javax.swing.JLabel annSepLabel;
    private javax.swing.JRadioButton idOpt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.ButtonGroup optGroup;
    private javax.swing.JRadioButton patOpt;
    private javax.swing.JTextField patText;
    // End of variables declaration//GEN-END:variables

}
