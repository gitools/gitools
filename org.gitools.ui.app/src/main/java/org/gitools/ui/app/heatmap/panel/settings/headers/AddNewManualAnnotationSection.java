/*
 * #%L
 * gitools-ui-app
 * %%
 * Copyright (C) 2013 - 2014 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.ui.app.heatmap.panel.settings.headers;


import org.gitools.matrix.model.matrix.AnnotationMatrix;
import org.gitools.ui.core.utils.DocumentChangeListener;
import org.gitools.ui.platform.settings.AbstractSettingsSection;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

public class AddNewManualAnnotationSection extends AbstractSettingsSection {
    private final List<String> selected;
    private final AnnotationMatrix annotations;

    private JPanel root;
    private JTable currentValuesTable;
    private JTextField annotationLabel;
    private JTextField annotationValue;
    private JLabel explanationLabel;

    public AddNewManualAnnotationSection(final AnnotationMatrix annotations, final List<String> selected) {
        this.annotations = annotations;
        this.selected = selected;
        this.explanationLabel.setText("<html><body><i>The annotation will automatically added as a colored header</i></body></html>");


        DocumentChangeListener docListener = new DocumentChangeListener() {
            @Override
            protected void update(DocumentEvent e) {

                setDirty(annotationLabel.getText().length() > 0 && validName() && annotationValue.getText().length() > 0);
            }
        };
        annotationValue.getDocument().addDocumentListener(docListener);
        annotationLabel.getDocument().addDocumentListener(docListener);

        currentValuesTable.setRowSelectionAllowed(false);
        currentValuesTable.setModel(new AbstractTableModel() {

            String[] columns = {"Id",  "New annotation"};

            @Override
            public String getColumnName(int column) {
                return columns[column];
            }

            @Override
            public int getRowCount() {
                return selected.size();
            }

            @Override
            public int getColumnCount() {
                return 2;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                if (columnIndex == -1 || rowIndex == -1) {
                    return null;
                }
                else if (columnIndex == 0) {
                    return selected.get(rowIndex);
                } else if (columnIndex == 1) {
                    return annotationValue.getText();
                }

                return null;
            }
        });
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 1 && !value.equals("")) {
                    Object changedValue = currentValuesTable.getValueAt(row, column);
                    Object oldValue = currentValuesTable.getValueAt(row, column - 1);
                    if (!changedValue.equals(oldValue)) {
                        c.setBackground(Color.YELLOW);
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                }
                return c;
            }
        };
        currentValuesTable.getColumnModel().getColumn(1).setCellRenderer(renderer);


    }

    private boolean validName() {
        boolean ok = true;
        for (String s : annotations.getLabels()) {
            if (s.toLowerCase().equals(annotationLabel.getText().toLowerCase())) {
                annotationLabel.setBackground(Color.red);
                return false;
            }
        }
        annotationLabel.setBackground(Color.white);
        return ok;
    }

    @Override
    public String getName() {
        return "Add new annotation" ;
    }

    @Override
    public JPanel getPanel() {
        return root;
    }

    private void createUIComponents() {
        currentValuesTable = new JTable() {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                return c;
            }
        };
    }

    public String getAnnotationLabel() {
        return annotationLabel.getText();
    }

    public String getAnnotationValue() {
        return annotationValue.getText();
    }

    public List<String> getSelected() {
        return selected;
    }

    public AnnotationMatrix getAnnotations() {
        return annotations;
    }

}
