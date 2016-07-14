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
package org.gitools.ui.core.pages.common;

import com.alee.utils.SwingUtils;
import org.gitools.api.ApplicationContext;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.resource.ResourceReference;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.heatmap.header.HeatmapTextLabelsHeader;
import org.gitools.matrix.format.AnnotationMatrixFormat;
import org.gitools.matrix.model.matrix.AnnotationMatrix;
import org.gitools.persistence.locators.UrlResourceLocator;
import org.gitools.ui.core.utils.DocumentChangeListener;
import org.gitools.ui.core.utils.FileChooserUtils;
import org.gitools.ui.core.utils.LogUtils;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.icons.IconNames;
import org.gitools.ui.platform.settings.Settings;
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AnnotationEditPage extends AbstractWizardPage {

    private final AnnotationMatrix annotationMatrix;
    private JPanel root;
    private JList annList;
    private JTextField annotationSearchField;
    private JScrollPane jScrollPane1;
    private JButton loadAnnotations;
    private JButton removeSelected;
    private JButton fromOtherHeatmapButton;
    private JTextField annTitleField;
    private JButton applyTitleChange;
    private JTable previewTable;
    private JTextField descriptionField;
    private JButton applyDescriptionButton;
    private JScrollPane previewScrollPane;
    private JLabel edit_titledesc;

    private HeatmapDimension hdim;

    private List<AnnotationOption> annotation;
    private DefaultTableModel tableModel;


    public AnnotationEditPage(HeatmapDimension hdim) {
        this.hdim = hdim;
        annotationMatrix = hdim.getAnnotations();

        setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.annotation512, 96));
        tableModel = new DefaultTableModel() {


            @Override
            public String getColumnName(int column) {
                if (column == 0) {
                    return "Id";
                } else if (column > 0 && column < this.getColumnCount()) {
                    return ((AnnotationOption) annList.getSelectedValuesList().get(column - 1)).getKey();
                }
                return super.getColumnName(column);
            }

            @Override
            public int getRowCount() {
                return annotationMatrix.getIdentifiers().size();
            }

            @Override
            public int getColumnCount() {
                return annList.getSelectedValuesList().size() + 1;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                if (rowIndex < 0 || columnIndex < 0) {
                    return null;
                }
                String id = annotationMatrix.getIdentifiers().getLabel(rowIndex);
                if (columnIndex == 0) {
                    return id;
                } else {
                    String key;
                    try {
                        key = ((AnnotationOption) annList.getSelectedValuesList().get(columnIndex - 1)).getKey();
                    } catch (IndexOutOfBoundsException e) {
                        return null;
                    }
                    return annotationMatrix.getAnnotation(id, key);
                }
            }
        };
        previewTable.setModel(tableModel);
        previewTable.setRowSelectionAllowed(false);


        initComponents();


        fromOtherHeatmapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //todo: addFromOtherHeatmap();

            }
        });
        fromOtherHeatmapButton.setEnabled(false);
        fromOtherHeatmapButton.setVisible(false);


        annList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (e.getValueIsAdjusting()) {
                    return;
                }
                annListSelectionChanged();
                updateControls();
            }
        });
        setTitle("Annotations selection");
        setComplete(true);
    }


    private void annListSelectionChanged() {
        tableModel.setColumnCount(annList.getSelectedValuesList().size() + 1);

        SwingUtils.invokeLater(new Runnable() {
            @Override
            public void run() {
                previewTable.updateUI();
                AnnotationOption selectedValue = (AnnotationOption) annList.getSelectedValue();
                if (selectedValue == null) {
                    return;
                }
                if (annList.getSelectedIndices().length == 1) {
                    edit_titledesc.setText("<html>Edit <i>'" + selectedValue.getKey() + "'</i> title and description</html>");
                    annTitleField.setEnabled(true);
                    descriptionField.setEnabled(true);
                    annTitleField.setText(selectedValue.getKey());
                    descriptionField.setText(selectedValue.getDescription());
                } else {
                    edit_titledesc.setText("");
                    annTitleField.setEnabled(false);
                    descriptionField.setEnabled(false);
                }
            }
        });

    }

    class FilterCellRenderer extends DefaultListCellRenderer {

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            AnnotationOption annOpt = (AnnotationOption) value;
            String needle = annotationSearchField.getText().toLowerCase();
            boolean match;
            match = annotationSearchField.getText().isEmpty();

            if (!match) {
                match = annOpt.getKey().toLowerCase().contains(needle) |
                        annOpt.getDescription().toLowerCase().contains(needle);
            }

            if (match) {// <= put your logic here
                c.setFont(c.getFont().deriveFont(Font.ITALIC, 14.0f));
                c.setForeground(Color.BLACK);
            } else {
                c.setFont(c.getFont().deriveFont(Font.ITALIC, 9.0f));
                c.setForeground(Color.LIGHT_GRAY);
            }
            return c;
        }
    }


    @Override
    public void updateControls() {

        SwingUtils.invokeLater(new Runnable() {
            @Override
            public void run() {
                AnnotationOption ann = (AnnotationOption) annList.getSelectedValue();
                if (ann != null && !annTitleField.getText().equals(ann.getKey()) && isNewKey() &&
                        annList.getSelectedIndices().length == 1) {
                    annTitleField.setFont(annTitleField.getFont().deriveFont(Font.BOLD));
                    applyTitleChange.setEnabled(true);
                } else {
                    annTitleField.setFont(annTitleField.getFont().deriveFont(Font.PLAIN));
                    applyTitleChange.setEnabled(false);
                }

                if (ann != null && !descriptionField.getText().equals(ann.getDescription()) &&
                        annList.getSelectedIndices().length == 1) {
                    descriptionField.setFont(descriptionField.getFont().deriveFont(Font.BOLD));
                    applyDescriptionButton.setEnabled(true);
                } else {
                    descriptionField.setFont(descriptionField.getFont().deriveFont(Font.PLAIN));
                    applyDescriptionButton.setEnabled(false);
                }
            }
        });


    }

    private boolean isNewKey() {
        String newKey = annTitleField.getText();
        int selectedIdx = annList.getSelectedIndex();
        int elements = annList.getModel().getSize();
        for (int i = 0; i < elements; i++) {
            if (i == selectedIdx) {
                continue;
            }

            AnnotationOption ao = (AnnotationOption) annList.getModel().getElementAt(i);
            if (newKey.toLowerCase().equals(ao.getKey().toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    private void removeSelectedAnnotations() {
        String inUse = "id";
        for (HeatmapHeader hh : hdim.getHeaders()) {
            inUse = inUse + ", " + hh.getAnnotationPattern();
        }

        StringBuilder cannotRemove = new StringBuilder();
        boolean stuffHasBeenRemoved = false;

        for (Object option : annList.getSelectedValuesList()) {
            String label = ((AnnotationOption) option).getKey();
            if (!inUse.contains(label)) {
                hdim.getAnnotations().removeAnnotations(label);
                stuffHasBeenRemoved = true;
            } else {
                cannotRemove.append(label);
                cannotRemove.append(", ");
            }
        }

        if (stuffHasBeenRemoved) {
            initComponents();
        }
        annList.setSelectedIndices(new int[0]);
        if (cannotRemove.length() > 0) {
            setMessage(MessageStatus.WARN, "<html>Could not remove some annotations because they are being used in a header: " +
                    "<b>" + cannotRemove.toString() + "</b></html>");
        }
    }


    private void updateComplete() {
        setComplete(annList.getSelectedIndices().length > 0);
    }


    public String[] getSelectedValues() {
        int[] indices = annList.getSelectedIndices();
        String[] values = new String[indices.length];

        for (int i = 0; i < indices.length; i++) {
            int index = indices[i];
            if (index == 0) {
                values[i] = "id";
            } else {
                values[i] = annotation.get(index - 1).getKey();
            }
        }

        return values;
    }

    public void setHeatmapDimension(HeatmapDimension heatmapDimension) {
        this.hdim = heatmapDimension;
        updateControls();
    }


    private void filterAnnotationsBox(KeyEvent evt) {
        annList.repaint();
    }

    public HeatmapTextLabelsHeader.LabelSource getLabelSource() {
        if (annList.getSelectedIndex() == 0) {
            return HeatmapTextLabelsHeader.LabelSource.ID;
        } else {
            return HeatmapTextLabelsHeader.LabelSource.PATTERN;
        }
    }

    public String getAnnotationName() {
        if (annList.getSelectedIndex() > 0) {
            return annotation.get(annList.getSelectedIndex() - 1).getKey();
        } else {
            return "";
        }
    }


    private void initComponents() {

        jScrollPane1.setViewportView(annList);
        previewScrollPane.setViewportView(previewTable);

        loadAnnotations.setText("Load from file");
        loadAnnotations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadAnnotationsActionPerformed(evt);
            }
        });

        annotationSearchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                annotationSearchFieldKeyReleased(evt);
            }
        });

        removeSelected.setText("remove Selected");
        removeSelected.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeSelectedAnnotations();
            }
        });

        DocumentChangeListener docListener = new DocumentChangeListener() {
            @Override
            protected void update(DocumentEvent e) {
                updateControls();
            }
        };
        annTitleField.getDocument().addDocumentListener(docListener);
        descriptionField.getDocument().addDocumentListener(docListener);

        applyTitleChange.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeAnnotationKey();
            }
        });
        applyDescriptionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeDescription();
            }
        });

        if (hdim != null && hdim.getAnnotations() != null && !hdim.getAnnotations().getLabels().isEmpty()) {

            DefaultListModel<AnnotationOption> model = new DefaultListModel<>();
            FilterCellRenderer cellRenderer = new FilterCellRenderer();

            // show all annotations except hierarchical clusters..
            annotation = new ArrayList<>();
            for (String key : hdim.getAnnotations().getLabels()) {
                if (key.matches(".* L[0-9]{1,2}$")) {
                    continue;
                }
                String description = hdim.getAnnotations().getAnnotationMetadata(AnnotationMatrix.METADATA_DESCRIPTION, key);
                annotation.add(new AnnotationOption(key, description));
            }

            Collections.sort(annotation, new Comparator<AnnotationOption>() {
                @Override
                public int compare(AnnotationOption o1, AnnotationOption o2) {
                    return o1.toString().toUpperCase().compareTo(o2.toString().toUpperCase());
                }
            });

            for (AnnotationOption annotationOption : annotation) {
                model.addElement(annotationOption);
            }

            annList.setModel(model);
            annList.setSelectedIndex(0);
            annList.setCellRenderer(cellRenderer);
            setMessage(MessageStatus.INFO, "View, Load and Remove annotations");
        }

    }

    private void changeDescription() {
        int idx = annList.getSelectedIndex();
        AnnotationOption anno = (AnnotationOption) annList.getSelectedValue();
        annotationMatrix.setAnnotationMetadata(AnnotationMatrix.METADATA_DESCRIPTION, anno.getKey(), descriptionField.getText());
        initComponents();
        annList.setSelectedIndex(idx);
    }

    private void changeAnnotationKey() {
        AnnotationOption anno = (AnnotationOption) annList.getSelectedValue();
        IMatrixLayer<String> oldLayer = annotationMatrix.getLayers().get(anno.getKey());

        String newKey = annTitleField.getText();
        for (String id : annotationMatrix.getIdentifiers()) {
            annotationMatrix.setAnnotation(id, newKey, annotationMatrix.get(oldLayer, id));
        }
        initComponents();

    }

    private void loadAnnotationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadAnnotationsActionPerformed
        try {
            File file = FileChooserUtils.selectFile("Open annotationMatrix file", Settings.get().getLastAnnotationPath(), FileChooserUtils.MODE_OPEN).getFile();

            if (file != null) {
                hdim.addAnnotations(new ResourceReference<>(new UrlResourceLocator(file), ApplicationContext.getPersistenceManager().getFormat(AnnotationMatrixFormat.EXTENSION, AnnotationMatrix.class)).get());
                Settings.get().setLastAnnotationPath(file.getParent());
                updateControls();
            }
        } catch (Exception ex) {
            LogUtils.logException(ex, LoggerFactory.getLogger(getClass()));
        }
    }

    private void annotationSearchFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_annotationSearchFieldKeyReleased
        filterAnnotationsBox(evt);
    }


    public JPanel getPanel() {
        return root;
    }

}
