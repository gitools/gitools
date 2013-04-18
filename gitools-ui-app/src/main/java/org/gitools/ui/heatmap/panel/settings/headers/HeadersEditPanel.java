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
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.header.*;
import org.gitools.ui.IconNames;
import org.gitools.ui.heatmap.header.AddHeaderPage;
import org.gitools.ui.heatmap.header.wizard.coloredlabels.ColoredLabelsHeaderWizard;
import org.gitools.ui.heatmap.header.wizard.coloredlabels.HierarchicalColoredLabelsHeaderWizard;
import org.gitools.ui.heatmap.header.wizard.heatmapheader.AggregatedHeatmapHeaderWizard;
import org.gitools.ui.heatmap.header.wizard.textlabels.TextLabelsHeaderWizard;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.wizard.IWizard;
import org.gitools.ui.platform.wizard.PageDialog;
import org.gitools.ui.platform.wizard.WizardDialog;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;

public class HeadersEditPanel extends JDialog {
    private static final String[] COLUMN_NAMES = {"", "Title", "Size", "Color", "Background", "Visible", "", ""};
    private static final Class[] COLUMN_CLASS = {Icon.class, String.class, Integer.class, Color.class, Color.class, Boolean.class, String.class, String.class};
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

        headersTable.setPreferredScrollableViewportSize(new Dimension(600, 300));
        headersTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        headersTable.setDragEnabled(true);
        headersTable.setDropMode(DropMode.INSERT_ROWS);
        headersTable.setTransferHandler(new TableDragAndDropHandler(headersTable));
        headersTable.setModel(
                new HeadersTableModel(
                        new SelectionInList<HeatmapHeader>(dimension.getHeaders())
                )
        );
        headersTable.setDefaultEditor(Color.class, new ColorCellEditor());
        headersTable.setDefaultRenderer(Color.class, new ColorCellRenderer());
        TableColumn spinner = headersTable.getColumnModel().getColumn(2);
        spinner.setCellEditor(new SpinnerCellEditor(new SpinnerNumberModel(0, 0, 400, 1)));

        Action delete = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                JTable table = (JTable) e.getSource();
                int modelRow = Integer.valueOf(e.getActionCommand());
                ((HeadersTableModel) table.getModel()).removeRow(modelRow);
            }
        };
        ButtonColumn deleteColumn = new ButtonColumn(headersTable, delete, 6);

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
        ButtonColumn editColumn = new ButtonColumn(headersTable, edit, 7);


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
                    return header.getSize();
                case 3:
                    return header.getLabelColor();
                case 4:
                    return header.getBackgroundColor();
                case 5:
                    return header.isVisible();
                case 6:
                    return "Delete";
                case 7:
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
                    header.setSize((Integer) aValue);
                    break;
                case 3:
                    header.setLabelColor((Color) aValue);
                    break;
                case 4:
                    header.setBackgroundColor((Color) aValue);
                    break;
                case 5:
                    header.setVisible((Boolean) aValue);
                    break;
            }

            dimension.updateHeaders();
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex > 1;
        }

        @Override
        public void reorder(int fromIndex, int toIndex) {
            if (toIndex == listModel.getList().size()) {
                toIndex = listModel.getList().size() - 1;
            }

            HeatmapHeader from = getRow(fromIndex);
            HeatmapHeader to = getRow(toIndex);

            listModel.getList().set(toIndex, from);
            listModel.getList().set(fromIndex, to);

            dimension.updateHeaders();
        }

        public void removeRow(int modelRow) {
            listModel.getList().remove(modelRow);
            dimension.updateHeaders();
            fireTableDataChanged();
        }

        public void editRow(int modelRow) {
            actionHeaderEdit(heatmap, dimension, dimension.getHeaders().get(modelRow));
            fireTableDataChanged();
        }

        public void addNewHeader() {
            actionNewHeaders(HeadersEditPanel.this.heatmap, HeadersEditPanel.this.dimension);
            fireTableDataChanged();
        }
    }

    public static void actionNewHeaders(Heatmap heatmap, HeatmapDimension heatmapDimension) {
        AddHeaderPage headerPage = new AddHeaderPage();
        PageDialog tdlg = new PageDialog(AppFrame.get(), headerPage);
        tdlg.setTitle("Header type selection");
        tdlg.setVisible(true);
        if (tdlg.isCancelled())
            return;

        HeatmapHeader header = null;
        IWizard wizard = null;
        Class<? extends HeatmapHeader> cls = headerPage.getHeaderClass();
        String headerTitle = headerPage.getHeaderTitle();

        if (cls.equals(HeatmapTextLabelsHeader.class)) {
            HeatmapTextLabelsHeader h = new HeatmapTextLabelsHeader(heatmapDimension);
            wizard = new TextLabelsHeaderWizard(heatmapDimension, h);
            header = h;
        } else if (cls.equals(HeatmapColoredLabelsHeader.class)) {
            HeatmapColoredLabelsHeader h = new HeatmapColoredLabelsHeader(heatmapDimension);
            wizard = new ColoredLabelsHeaderWizard(heatmapDimension, h);
            header = h;
        } else if (cls.equals(HeatmapDataHeatmapHeader.class)) {
            HeatmapDataHeatmapHeader h = new HeatmapDataHeatmapHeader(heatmapDimension);
            wizard = new AggregatedHeatmapHeaderWizard(heatmap, h, heatmapDimension == heatmap.getRows());
            header = h;

            if (headerTitle.equals(AddHeaderPage.ANNOTATION_HEATMAP)) {
                ((AggregatedHeatmapHeaderWizard) wizard).setDataSource(
                        AggregatedHeatmapHeaderWizard.DataSourceEnum.annotation);
            } else {
                ((AggregatedHeatmapHeaderWizard) wizard).setDataSource(
                        AggregatedHeatmapHeaderWizard.DataSourceEnum.aggregatedData);
            }

        }

        WizardDialog wdlg = new WizardDialog(AppFrame.get(), wizard);
        wdlg.setTitle("Add header");
        wdlg.setVisible(true);
        if (wdlg.isCancelled())
            return;

        heatmapDimension.addHeader(header);

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
        } else if (HeatmapHierarchicalColoredLabelsHeader.class.equals(cls)) {
            wizard = new HierarchicalColoredLabelsHeaderWizard(
                    hm, hdim, (HeatmapHierarchicalColoredLabelsHeader) h);
        } else if (HeatmapDataHeatmapHeader.class.equals(cls)) {
            AggregatedHeatmapHeaderWizard wiz = new AggregatedHeatmapHeaderWizard(hm, (HeatmapDataHeatmapHeader) h, hm.getRows() == hdim);
            wiz.setEditionMode(true);
            wizard = wiz;
        }

        if (wizard == null)
            return;

        WizardDialog wdlg = new WizardDialog(AppFrame.get(), wizard);
        wdlg.setTitle("Edit header");
        wdlg.setVisible(true);
    }

}
