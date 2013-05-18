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
package org.gitools.ui.heatmap.header.wizard.textlabels;

import org.gitools.core.heatmap.HeatmapDimension;
import org.gitools.core.heatmap.header.HeatmapTextLabelsHeader;
import org.gitools.core.matrix.model.matrix.AnnotationMatrix;
import org.gitools.core.matrix.model.IAnnotations;
import org.gitools.core.persistence.PersistenceManager;
import org.gitools.core.persistence.ResourceReference;
import org.gitools.core.persistence._DEPRECATED.FileSuffixes;
import org.gitools.core.persistence.locators.UrlResourceLocator;
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.utils.FileChooserUtils;
import org.gitools.ui.utils.LogUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class TextLabelsSourcePage extends AbstractWizardPage {

    private final HeatmapDimension hdim;
    private final HeatmapTextLabelsHeader header;

    public TextLabelsSourcePage(HeatmapDimension hdim, HeatmapTextLabelsHeader header) {
        this.hdim = hdim;
        this.header = header;

        initComponents();

        ActionListener optionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                optionsChanged();
            }
        };

        idOpt.addActionListener(optionListener);
        annOpt.addActionListener(optionListener);
        patOpt.addActionListener(optionListener);

        setTitle("Select the contents of the header");
        setComplete(true);
    }

    @Override
    public void updateControls() {
        super.updateControls();

        switch (header.getLabelSource()) {
            case ID:
                idOpt.setSelected(true);
                break;
            case ANNOTATION:
                annOpt.setSelected(true);
                break;
            case PATTERN:
                patOpt.setSelected(true);
                break;
        }

        IAnnotations am = hdim.getAnnotations();
        if (am != null && !am.getLabels().isEmpty()) {
            DefaultListModel model = new DefaultListModel();
            for (String key : am.getLabels()) {
                String description = am.getAnnotationMetadata("description", key);
                String text = key + (description == null? "" : " - " +description);
                model.addElement(text);
            }

            annList.setModel(model);
            annList.setSelectedValue(header.getLabelAnnotation(), true);

        }

        pattText.setText(header.getLabelPattern());

        optionsChanged();
    }

    @Override
    public void updateModel() {
        super.updateModel();

        header.setLabelSource(getLabelSource());
        header.setLabelAnnotation(getAnnotation());
        header.setLabelPattern(getPattern());

        header.setDescription(getAnnotationMetadata("description"));
        header.setDescriptionUrl(getAnnotationMetadata("description-url"));
        header.setValueUrl(getAnnotationMetadata("value-url"));

    }

    public String getAnnotationMetadata(String key) {
        return hdim.getAnnotations().getAnnotationMetadata(key, getAnnotation());
    }

    @NotNull
    HeatmapTextLabelsHeader.LabelSource getLabelSource() {
        if (idOpt.isSelected()) {
            return HeatmapTextLabelsHeader.LabelSource.ID;
        } else if (annOpt.isSelected()) {
            return HeatmapTextLabelsHeader.LabelSource.ANNOTATION;
        } else if (patOpt.isSelected()) {
            return HeatmapTextLabelsHeader.LabelSource.PATTERN;
        }
        return HeatmapTextLabelsHeader.LabelSource.ID;
    }

    private void optionsChanged() {
        annList.setEnabled(annOpt.isSelected());
        pattText.setEnabled(patOpt.isSelected());
    }

    @NotNull
    String getAnnotation() {
        if (annList.getSelectedIndex() != -1) {
            return hdim.getAnnotations().getLabels().get(annList.getSelectedIndex());
        } else {
            return "";
        }
    }

    String getPattern() {
        return pattText.getText();
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

        optGroup = new javax.swing.ButtonGroup();
        idOpt = new javax.swing.JRadioButton();
        annOpt = new javax.swing.JRadioButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        annList = new javax.swing.JList();
        patOpt = new javax.swing.JRadioButton();
        pattText = new javax.swing.JTextField();
        loadAnnotations = new javax.swing.JButton();

        optGroup.add(idOpt);
        idOpt.setText("The ID");

        optGroup.add(annOpt);
        annOpt.setText("An annotation");

        annList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        annList.setEnabled(false);
        jScrollPane1.setViewportView(annList);

        optGroup.add(patOpt);
        patOpt.setText("A pattern");

        pattText.setText("${id}");
        pattText.setEnabled(false);

        loadAnnotations.setText("Add annotations from file...");
        loadAnnotations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadAnnotationsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addGap(24, 24, 24)
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(idOpt)
                                                        .addComponent(annOpt))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(loadAnnotations))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(12, 12, 12)
                                                                .addComponent(pattText, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE))
                                                        .addComponent(patOpt))))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(idOpt)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(annOpt))
                                        .addComponent(loadAnnotations, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(patOpt)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pattText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void loadAnnotationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadAnnotationsActionPerformed
        try {
            File file = FileChooserUtils.selectFile("Open annotations file", Settings.getDefault().getLastAnnotationPath(), FileChooserUtils.MODE_OPEN);

            if (file != null) {
                hdim.addAnnotations(new ResourceReference<>(new UrlResourceLocator(file), PersistenceManager.get().getFormat(FileSuffixes.ANNOTATION_MATRIX, AnnotationMatrix.class)).get());
                updateControls();
                //annFile.setText(file.getName());
            }
        } catch (Exception ex) {
            LogUtils.logException(ex, LoggerFactory.getLogger(getClass()));
        }
    }//GEN-LAST:event_loadAnnotationsActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList annList;
    private javax.swing.JRadioButton annOpt;
    private javax.swing.JRadioButton idOpt;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton loadAnnotations;
    private javax.swing.ButtonGroup optGroup;
    private javax.swing.JRadioButton patOpt;
    private javax.swing.JTextField pattText;
    // End of variables declaration//GEN-END:variables

}
