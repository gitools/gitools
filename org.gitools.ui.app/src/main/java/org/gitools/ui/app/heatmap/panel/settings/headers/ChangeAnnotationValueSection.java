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

import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.matrix.filter.PatternFunction;
import org.gitools.matrix.model.matrix.AnnotationMatrix;
import org.gitools.ui.core.utils.DocumentChangeListener;
import org.gitools.ui.platform.settings.ISettingsSection;
import org.gitools.utils.textpattern.TextPattern;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangeAnnotationValueSection implements ISettingsSection {
    private final HeatmapHeader heatmapHeader;
    private final List<String> selected;
    private final AnnotationMatrix annotations;
    private final PatternFunction patternFunction;

    private JPanel root;
    private JTable currentValuesTable;
    private JPanel changePanel;
    private Map<String, JTextField> inputMap;

    public ChangeAnnotationValueSection(final HeatmapHeader heatmapHeader, final List<String> selected) {
        this.heatmapHeader = heatmapHeader;
        this.annotations = heatmapHeader.getHeatmapDimension().getAnnotations();
        this.patternFunction = new PatternFunction(heatmapHeader.getAnnotationPattern(), annotations);
        this.selected = selected;

        inputMap = new HashMap<>();

        changePanel.removeAll();
        changePanel.setLayout(new GridLayout(0,2));
        for (TextPattern.VariableToken variableToken : patternFunction.getPattern().getVariableTokens()) {
            addChangeInput(variableToken.getVariableName());
        }


        heatmapHeader.getHeatmapDimension();

        currentValuesTable.setRowSelectionAllowed(false);
        currentValuesTable.setModel(new AbstractTableModel() {

            String[] columns = {"Id", "Current annotation", "Changed"};

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
                return 3;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                if (columnIndex == -1 || rowIndex == -1) {
                    return null;
                }
                else if (columnIndex == 0) {
                    return selected.get(rowIndex);
                } else if (columnIndex == 1) {
                    try {
                        return patternFunction.apply(selected.get(rowIndex));
                    } catch (NullPointerException e) {
                        return "";
                    }
                } else if (columnIndex == 2){
                    return getNewValue(selected.get(rowIndex));
                }

                return null;
            }
        });
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 2 && !value.equals("")) {
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
        currentValuesTable.getColumnModel().getColumn(2).setCellRenderer(renderer);


    }

    private String getNewValue(String id) {
        StringBuilder sb = new StringBuilder();
        for (TextPattern.Token token : patternFunction.getPattern().getTokens()) {
            if (inputMap.containsKey(token.toString())) {
                sb.append(inputMap.get(token.toString()).getText());
            } else {
                sb.append(token.toString());
            }
        }
        return sb.toString();

    }


    private void addChangeInput(String name) {

        changePanel.add(new JLabel("<html><body>Change <b>" + name + "</b> annotation to:</body></html>"));
        JTextField input = new JTextField("");
        input.getDocument().addDocumentListener(new DocumentChangeListener() {
            @Override
            protected void update(DocumentEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        currentValuesTable.updateUI();
                    }
                });
            }
        });


        changePanel.add(input);
        inputMap.put(name, input);


        changePanel.revalidate();
        changePanel.repaint();
    }

    @Override
    public String getName() {
        return "Edit annotation values" ;
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

    public List<TextPattern.VariableToken> getAnnotationKeys() {
        return patternFunction.getPattern().getVariableTokens();
    }

    public List<String> getSelected() {
        return selected;
    }

    public AnnotationMatrix getAnnotations() {
        return annotations;
    }

    public Map<String, String> getInputMap() {
        Map<String, String> m = new HashMap<>();
        for (String key : inputMap.keySet()) {
            m.put(key, inputMap.get(key).getText());
        }
        return m;
    }
}
