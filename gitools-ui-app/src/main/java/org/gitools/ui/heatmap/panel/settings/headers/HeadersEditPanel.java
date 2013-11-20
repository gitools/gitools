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
package org.gitools.ui.heatmap.panel.settings.headers;

import com.alee.laf.table.WebTable;
import com.jgoodies.binding.adapter.AbstractTableAdapter;
import com.jgoodies.binding.list.SelectionInList;
import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.heatmap.HeatmapDimension;
import org.gitools.core.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.core.heatmap.header.HeatmapDecoratorHeader;
import org.gitools.core.heatmap.header.HeatmapHeader;
import org.gitools.core.heatmap.header.HeatmapTextLabelsHeader;
import org.gitools.ui.IconNames;
import org.gitools.ui.actions.EditActions;
import org.gitools.ui.heatmap.header.wizard.coloredlabels.ColoredLabelsHeaderWizard;
import org.gitools.ui.heatmap.header.wizard.heatmapheader.DecoratorHeaderWizard;
import org.gitools.ui.heatmap.header.wizard.textlabels.TextLabelsHeaderWizard;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.wizard.IWizard;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.utils.collections.ReverseList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class HeadersEditPanel extends JDialog {
    private static final String[] COLUMN_NAMES = {"", "Title", "Description", "General link", "Value link", "Size", "Color", "Background", "Visible", "", ""};
    private static final Class[] COLUMN_CLASS = {Icon.class, String.class, String.class, String.class, String.class, Integer.class, Color.class, Color.class, Boolean.class, String.class, String.class};
    private static final Icon DRAG_AND_DROP_ICON = new ImageIcon(HeadersEditPanel.class.getResource(IconNames.drag));

    private JPanel contentPane;
    private JButton buttonOK;
    private JTable headersTable;
    private JButton addNewButton;


    private Heatmap heatmap;
    private HeatmapDimension dimension;

    public HeadersEditPanel(Heatmap heatmap, HeatmapDimension dimension) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        this.heatmap = heatmap;
        this.dimension = dimension;

        List<HeatmapHeader> headers;
        if (dimension == heatmap.getColumns()) {
            headers = new ReverseList<>(dimension.getHeaders());
        } else {
            headers = dimension.getHeaders();
        }

        headersTable.setPreferredScrollableViewportSize(new Dimension(800, 400));
        headersTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        headersTable.setDragEnabled(true);
        headersTable.setDropMode(DropMode.INSERT_ROWS);
        headersTable.setTransferHandler(new TableDragAndDropHandler(headersTable));
        headersTable.setModel(
                new HeadersTableModel(
                        new SelectionInList<>(headers)
                )
        );
        headersTable.setDefaultEditor(Color.class, new ColorCellEditor());
        headersTable.setDefaultRenderer(Color.class, new ColorCellRenderer());

        Action delete = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                JTable table = (JTable) e.getSource();
                int modelRow = Integer.valueOf(e.getActionCommand());
                ((HeadersTableModel) table.getModel()).removeRow(modelRow);
            }
        };
        ButtonColumn deleteColumn = new ButtonColumn(headersTable, delete, 9);

        Action edit = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                JTable table = (JTable) e.getSource();
                int modelRow = Integer.valueOf(e.getActionCommand());
                HeadersEditPanel.this.setVisible(false);
                ((HeadersTableModel) table.getModel()).editRow(modelRow);
                HeadersEditPanel.this.dimension.updateHeaders();
                HeadersEditPanel.this.setVisible(true);
            }
        };
        ButtonColumn editColumn = new ButtonColumn(headersTable, edit, 10);


        // Custom editor

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                close();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                close();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        addNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HeadersEditPanel.this.setVisible(false);
                ((HeadersTableModel) headersTable.getModel()).addNewHeader();
                HeadersEditPanel.this.setVisible(true);
            }
        });
    }

    private void close() {
        dimension.updateHeaders();
        dispose();
    }

    private void createUIComponents() {
        headersTable = new WebTable();
    }

    private class HeadersTableModel extends AbstractTableAdapter<HeatmapHeader> implements Reorderable {

        private SelectionInList<HeatmapHeader> listModel;

        public HeadersTableModel(SelectionInList<HeatmapHeader> listModel) {
            super(listModel, COLUMN_NAMES);
            this.listModel = listModel;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return COLUMN_CLASS[columnIndex];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            HeatmapHeader header = getRow(rowIndex);

            switch (columnIndex) {
                case 0:
                    return DRAG_AND_DROP_ICON;
                case 1:
                    return header.getTitle();
                case 2:
                    return header.getDescription();
                case 3:
                    return header.getDescriptionUrl();
                case 4:
                    return header.getValueUrl();
                case 5:
                    return header.getSize();
                case 6:
                    return header.getLabelColor();
                case 7:
                    return header.getBackgroundColor();
                case 8:
                    return header.isVisible();
                case 9:
                    return "Delete";
                case 10:
                    return "Edit";
                default:
                    return header.toString();
            }
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            HeatmapHeader header = getRow(rowIndex);

            switch (columnIndex) {
                case 1:
                    header.setTitle((String) aValue);
                    break;
                case 2:
                    header.setDescription((String) aValue);
                    break;
                case 3:
                    header.setDescriptionUrl((String) aValue);
                    break;
                case 4:
                    header.setValueUrl((String) aValue);
                    break;
                case 5:
                    header.setSize((Integer) aValue);
                    break;
                case 6:
                    header.setLabelColor((Color) aValue);
                    break;
                case 7:
                    header.setBackgroundColor((Color) aValue);
                    break;
                case 8:
                    header.setVisible((Boolean) aValue);
                    break;
            }

            dimension.updateHeaders();
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex > 0;
        }

        @Override
        public void reorder(int fromIndex, int toIndex) {

            HeatmapHeader from = getRow(fromIndex);
            listModel.getList().add(toIndex, from);

            int removeIndex = fromIndex;
            if (toIndex < fromIndex) {
                removeIndex++;
            }
            listModel.getList().remove(removeIndex);
            dimension.updateHeaders();
        }

        public void removeRow(int modelRow) {
            listModel.getList().remove(modelRow);
            dimension.updateHeaders();
            fireTableDataChanged();
        }

        public void editRow(int modelRow) {
            actionHeaderEdit(heatmap, dimension, listModel.getList().get(modelRow));
            fireTableDataChanged();
        }

        public void addNewHeader() {
            addHeader(heatmap, dimension);
            fireTableDataChanged();
        }
    }

    public void addHeader(Heatmap header, HeatmapDimension dimension) {
        if (dimension == HeadersEditPanel.this.heatmap.getColumns())
            EditActions.addColumnHeader.actionPerformed(new ActionEvent(this, 1, ""));
        else
            EditActions.addRowHeader.actionPerformed(new ActionEvent(this, 1, ""));
    }

    private static void actionHeaderEdit(Heatmap hm, HeatmapDimension hdim, HeatmapHeader h) {
        Class<? extends HeatmapHeader> cls = h.getClass();
        IWizard wizard = null;

        if (HeatmapTextLabelsHeader.class.equals(cls))
            wizard = new TextLabelsHeaderWizard(hdim, (HeatmapTextLabelsHeader) h);
        else if (HeatmapColoredLabelsHeader.class.equals(cls)) {
            ColoredLabelsHeaderWizard wiz = new ColoredLabelsHeaderWizard(hdim, (HeatmapColoredLabelsHeader) h);
            wiz.setEditionMode(true);
            wizard = wiz;
        } else if (HeatmapDecoratorHeader.class.equals(cls)) {
            wizard = new DecoratorHeaderWizard((HeatmapDecoratorHeader) h);
        }

        if (wizard == null)
            return;

        WizardDialog wdlg = new WizardDialog(AppFrame.get(), wizard);
        wdlg.setTitle("Edit header");
        wdlg.setVisible(true);
    }

}
