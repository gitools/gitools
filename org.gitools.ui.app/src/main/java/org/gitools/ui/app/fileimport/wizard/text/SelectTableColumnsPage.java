package org.gitools.ui.app.fileimport.wizard.text;

/*
 * #%L
 * gitools-ui-app
 * %%
 * Copyright (C) 2013 Biomedical Genomics Lab
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

import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.gitools.utils.text.FileField;
import org.gitools.utils.text.FileHeader;
import org.gitools.utils.text.ReaderProfileValidationException;
import org.gitools.utils.text.TableReaderProfile;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class SelectTableColumnsPage extends AbstractWizardPage implements IFileImportStep {

    private FlatTextReader reader;

    private JPanel mainPanel;
    private JList<FileHeader> valuesHeaderList;
    private JList<FileHeader> rowsHeaderList;
    private JList<FileHeader> columnsHeaderLists;
    private JList<FileHeader> ignoredHeaderList;
    private JTextPane previewPane;
    private List<FileHeader> allheaders;
    private List<List<FileField>> preview;

    private void initJList(JList<FileHeader> list, ListItemTransferHandler transferHandler, DefaultListModel<FileHeader> dataModel, boolean bold, boolean smallText) {

        list.setModel(dataModel);
        list.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setDropMode(DropMode.INSERT);
        list.setDragEnabled(true);
        list.setTransferHandler(transferHandler);
        list.setCellRenderer(new HeaderCellRenderer(bold, smallText));

        // listener
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    JList theList = (JList) e.getSource();

                    updateControls();

                    if (theList.getSelectedValue() == null)
                        return;

                    if (theList.equals(rowsHeaderList)) {
                        updateIdPreview(rowsHeaderList);
                    } else if (theList.equals(columnsHeaderLists)) {
                        updateIdPreview(columnsHeaderLists);
                    } else {
                        int i = theList.getLeadSelectionIndex();
                        updateColumnPreview((FileHeader) theList.getModel().getElementAt(i));
                    }
                }
            }
        });


        //Disable row Cut, Copy, Paste
        ActionMap map = list.getActionMap();
        AbstractAction dummy = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { /* Dummy action */ }
        };
        map.put(TransferHandler.getCutAction().getValue(Action.NAME), dummy);
        map.put(TransferHandler.getCopyAction().getValue(Action.NAME), dummy);
        map.put(TransferHandler.getPasteAction().getValue(Action.NAME), dummy);
    }

    private void updateIdPreview(JList<FileHeader> headerList) {

        String fieldGlue = ((TableReaderProfile) reader.getReaderProfile()).getFieldGlue();

        Integer[] pos = new Integer[headerList.getModel().getSize()];
        StringBuilder headerString = new StringBuilder("");
        for (int i = 0; i < pos.length; i++) {
            FileHeader h = headerList.getModel().getElementAt(i);
            pos[i] = h.getPos();
            if (i > 0) {
                headerString.append(fieldGlue);
            }
            headerString.append(h.getLabel());
        }


        StringBuilder html = new StringBuilder("");

        html.append("<html>" +
                PreviewHTMLs.CSS +
                "<body><table>");
        html.append("<tr><th>" + headerString.toString() + "</th><tr>");
        for (List<FileField> line : preview) {

            StringBuilder fieldString = new StringBuilder("");
            for (Integer i : pos) {
                if (fieldString.length() > 0) fieldString.append(fieldGlue);
                fieldString.append(line.get(i).getLabel());
            }

            html.append("<tr><td>" + fieldString.toString() + "</td></tr>");
        }
        html.append("</table></body></html>");

        previewPane.setText(html.toString());
        previewPane.setCaretPosition(0);

    }

    private void updateColumnPreview(FileHeader selectedValue) {

        StringBuilder html = new StringBuilder("");

        html.append("<html>" +
                PreviewHTMLs.CSS +
                "<body><table>");
        html.append("<tr><th>" + selectedValue.getLabel() + "</th><tr>");
        for (List<FileField> line : preview) {
            html.append("<tr><td>" + line.get(selectedValue.getPos()).getLabel() + "</td></tr>");
        }
        html.append("</table></body></html>");

        previewPane.setText(html.toString());
        previewPane.setCaretPosition(0);

    }

    @Override
    public JComponent createControls() {
        return mainPanel;
    }

    @Override
    public void updateControls() {
        setMessage(MessageStatus.INFO, "Drag and Drop the file headers to choose which columns to use for the heatmap");

        if (rowsHeaderList.getModel().getSize() == 0) {
            setMessage(MessageStatus.ERROR, "At least one column for heatmap rows needed");
            setComplete(false);
            return;
        } else if (columnsHeaderLists.getModel().getSize() == 0) {
            setMessage(MessageStatus.ERROR, "At least one column for heatmap columns needed");
            setComplete(false);
            return;
        } else if (valuesHeaderList.getModel().getSize() == 0) {
            setMessage(MessageStatus.ERROR, "At least one column for heatmap values needed");
            setComplete(false);
            return;
        }

        setComplete(true);
    }

    private void fillLists() {
        if (allheaders == null) {
            this.preview = reader.getPreview();
            this.allheaders = reader.getFileHeaders();
            DefaultListModel<FileHeader> values = new DefaultListModel<FileHeader>();
            DefaultListModel<FileHeader> ignored = new DefaultListModel<FileHeader>();
            DefaultListModel<FileHeader> rows = new DefaultListModel<FileHeader>();
            DefaultListModel<FileHeader> columns = new DefaultListModel<FileHeader>();

            for (FileHeader header : allheaders) {
                if (columns.getSize() == 0) {
                    columns.addElement(header);
                } else if (rows.getSize() == 0) {
                    rows.addElement(header);
                } else {
                    try {
                        Double v = Double.valueOf(preview.get(0).get(header.getPos()).getLabel());
                        values.addElement(header);
                    } catch (NumberFormatException e) {
                        ignored.addElement(header);
                    }
                }
            }

            ListItemTransferHandler transferHandler = new ListItemTransferHandler();

            initJList(rowsHeaderList, transferHandler, rows, true, false);
            initJList(columnsHeaderLists, transferHandler, columns, true, false);
            initJList(valuesHeaderList, transferHandler, values, false, false);
            initJList(ignoredHeaderList, transferHandler, ignored, false, true);
        }
    }

    @Override
    public void finish() throws ReaderProfileValidationException {

        TableReaderProfile profile = (TableReaderProfile) reader.getReaderProfile();

        profile.setHeatmapRowsIds(getPositions(rowsHeaderList));
        profile.setHeatmapColumnsIds(getPositions(columnsHeaderLists));
        profile.setDataColumns(getPositions(valuesHeaderList));
        profile.setIgnoredColumns(getPositions(ignoredHeaderList));

        reader.getReaderProfile().validate(allheaders);
    }

    private int[] getPositions(JList<FileHeader> headerList) {
        int[] indices = new int[headerList.getModel().getSize()];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = headerList.getModel().getElementAt(i).getPos();
        }
        return indices;
    }

    @Override
    public FlatTextReader getReader() {
        return reader;
    }

    public void setReader(FlatTextReader reader) {
        this.reader = reader;
        fillLists();
    }


    class HeaderCellRenderer extends DefaultListCellRenderer {


        private boolean bold;
        private boolean smallFont;

        HeaderCellRenderer(boolean bold, boolean smallFont) {
            this.bold = bold;
            this.smallFont = smallFont;
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

            FileHeader h = (FileHeader) value;
            String newValue = h.getLabel() + " (column " + h.getPos() + 1 + ")";

            Component c = super.getListCellRendererComponent(list, newValue, index, isSelected, cellHasFocus);
            if (bold) {
                c.setFont(c.getFont().deriveFont(Font.BOLD));
            }

            if (smallFont) {
                c.setFont(c.getFont().deriveFont(11f));
            } else {
                c.setFont(c.getFont().deriveFont(14f));
            }
            return c;
        }
    }


}
