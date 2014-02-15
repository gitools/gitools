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
import org.gitools.utils.text.MatrixReaderProfile;
import org.gitools.utils.text.ReaderProfileValidationException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SelectMatrixColumnsPage extends AbstractWizardPage implements IFileImportStep {

    private FlatTextReader reader;

    private JPanel mainPanel;
    private JList<FileHeader> valuesHeaderList;
    private JList<FileHeader> ignoredHeaderList;
    private JTextPane previewPane;
    private JSpinner columnIdsSpinner;
    private JSpinner rowIdsSpinner;
    private JButton previewRowIdsButton;
    private JButton previewColumnIdsButton;
    private List<FileHeader> allheaders;
    private List<List<FileField>> preview;
    private FileHeader lastSelected;
    private static String ID = "id";
    private static String COLUMN = "column";
    private String lastPreview;
    private FileHeader rowIdsHeader;
    private boolean rowIdsHeaderWasIgnored = true;

    public SelectMatrixColumnsPage() {

        rowIdsSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                updateRowIdHeader();

                updateControls();

                if (lastPreview == null) {
                    return;
                }

                if (lastPreview.equals(ID)) {
                    updateRowIdPreview();
                } else {
                    updateColumnPreview(lastSelected);
                }
            }
        });
        columnIdsSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                updateControls();

                if (lastPreview == null) {
                    return;
                }

                if (lastPreview.equals(ID)) {
                    updateColumnIdPreview();
                } else {
                    updateColumnPreview(lastSelected);
                }
            }
        });

        previewColumnIdsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateColumnIdPreview();
            }
        });
        previewRowIdsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateRowIdPreview();
            }
        });
    }

    private void updateRowIdHeader() {
        FileHeader newHeader = allheaders.get((int) rowIdsSpinner.getValue() - 1);
        FileHeader oldHeader = rowIdsHeader;

        if (oldHeader == null)
            return;

        //add old header where it belongs
        DefaultListModel restoreListModel;
        if (rowIdsHeaderWasIgnored) {
            restoreListModel = (DefaultListModel) ignoredHeaderList.getModel();
        } else {
            restoreListModel = (DefaultListModel) valuesHeaderList.getModel();
        }
        restoreListModel.add(0, oldHeader);


        //remove old header
        DefaultListModel removeListModel = (DefaultListModel) valuesHeaderList.getModel();
        if (removeListModel.contains(newHeader)) {
            rowIdsHeaderWasIgnored = false;
        } else {
            removeListModel = (DefaultListModel) ignoredHeaderList.getModel();
            rowIdsHeaderWasIgnored = true;
        }
        removeListModel.removeElement(newHeader);
        rowIdsHeader = newHeader;


    }

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

                    int i = theList.getLeadSelectionIndex();
                    updateColumnPreview((FileHeader) theList.getModel().getElementAt(i));
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

    private void updateRowIdPreview() {


        int rowPos = (int) rowIdsSpinner.getModel().getValue() - 1;
        int colPos = (int) columnIdsSpinner.getModel().getValue() - 1;
        StringBuilder html = new StringBuilder("");

        html.append("<html>" +
                PreviewHTMLs.CSS +
                "<body><table>");
        html.append("<tr><th>Row Ids</th><tr>");
        for (List<FileField> line : preview) {
            FileField f = line.get(rowPos);
            if (f.getLine() > colPos) {
                html.append("<tr><td>" + f.getLabel() + "</td></tr>");
            }
        }
        html.append("</table></body></html>");

        previewPane.setText(html.toString());
        previewPane.setCaretPosition(0);
        lastPreview = this.ID;

    }

    private void updateColumnIdPreview() {


        int rowPos = (int) rowIdsSpinner.getModel().getValue() - 1;
        StringBuilder html = new StringBuilder("");

        html.append("<html>" +
                PreviewHTMLs.CSS +
                "<body><table>");
        html.append("<tr><th>Column Ids</th><tr>");
        for (int i = 0; i < valuesHeaderList.getModel().getSize(); i++) {
            html.append("<tr><td>" + valuesHeaderList.getModel().getElementAt(i).getLabel() + "</td></tr>");
        }

        html.append("</table></body></html>");

        previewPane.setText(html.toString());
        previewPane.setCaretPosition(0);
        lastPreview = this.ID;


    }

    private void updateColumnPreview(FileHeader selectedValue) {


        if (selectedValue == null)
            return;

        StringBuilder html = new StringBuilder("");

        html.append("<html>" +
                PreviewHTMLs.CSS +
                "<body><table>");
        html.append("<tr><th>" + selectedValue.getLabel() + "</th><tr>");
        for (List<FileField> line : preview) {
            FileField f = line.get(selectedValue.getPos());
            if (f.getLine() < (int) rowIdsSpinner.getModel().getValue())
                continue;
            html.append("<tr><td>" + line.get(selectedValue.getPos()).getLabel() + "</td></tr>");
        }
        html.append("</table></body></html>");

        previewPane.setText(html.toString());
        previewPane.setCaretPosition(0);
        lastPreview = this.COLUMN;

    }

    @Override
    public JComponent createControls() {
        return mainPanel;
    }

    @Override
    public void updateControls() {
        setMessage(MessageStatus.INFO, "Drag and Drop the file headers to choose which columns to use for the heatmap");

        if ((int) rowIdsSpinner.getValue() < 1) {
            setMessage(MessageStatus.ERROR, "Heatmap row id must be a number bigger than 0");
            setComplete(false);
            return;
        } else if ((int) columnIdsSpinner.getValue() < 1) {
            setMessage(MessageStatus.ERROR, "Heatmap column id must be a number bigger than 0");
            setComplete(false);
            return;
        } else if (valuesHeaderList.getModel().getSize() == 0) {
            setMessage(MessageStatus.ERROR, "At least one column for heatmap values needed");
            setComplete(false);
            return;
        }

        setComplete(true);
    }

    private void fillComponents() {
        if (allheaders == null) {
            this.preview = reader.getPreview();
            this.allheaders = reader.getHeaders();

            rowIdsSpinner.setValue(1);
            columnIdsSpinner.setValue(1);

            DefaultListModel<FileHeader> values = new DefaultListModel<FileHeader>();
            DefaultListModel<FileHeader> ignored = new DefaultListModel<FileHeader>();

            for (FileHeader header : allheaders) {
                if (header.getPos() != (int) rowIdsSpinner.getValue() - 1) {
                    values.addElement(header);
                } else {
                    rowIdsHeader = header;
                    rowIdsHeaderWasIgnored = true;
                }
            }

            ListItemTransferHandler transferHandler = new ListItemTransferHandler();

            initJList(valuesHeaderList, transferHandler, values, false, false);
            initJList(ignoredHeaderList, transferHandler, ignored, false, true);
        }
    }

    @Override
    public void finish() throws ReaderProfileValidationException {

        MatrixReaderProfile profile = (MatrixReaderProfile) reader.getReaderProfile();

        profile.setRowIdsPosition((int) rowIdsSpinner.getValue() - 1);
        profile.setColumnIdsPosition((int) columnIdsSpinner.getValue() - 1);
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
        fillComponents();
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
