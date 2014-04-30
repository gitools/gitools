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

import org.gitools.ui.platform.icons.IconNames;
import org.gitools.ui.app.fileimport.wizard.text.reader.FlatTextImporter;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.gitools.utils.progressmonitor.NullProgressMonitor;
import org.gitools.utils.readers.FileField;
import org.gitools.utils.readers.FileHeader;
import org.gitools.utils.readers.MatrixReaderProfile;
import org.gitools.utils.readers.profile.Separator;
import org.gitools.utils.readers.profile.TableReaderProfile;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import static org.gitools.utils.readers.profile.ReaderProfile.MATRIX;
import static org.gitools.utils.readers.profile.ReaderProfile.TABLE;

public class SelectDataLayoutPage extends AbstractWizardPage {

    private FlatTextImporter reader;

    private JPanel mainPanel;

    private JComboBox separatorCombo;
    private JRadioButton tableRadioButton;
    private JRadioButton matrixRadioButton;
    private JTextPane dataFormatTextPane;
    private JTextPane preview;
    private JScrollPane previewScrollPane;

    private DefaultComboBoxModel separatorsModel;


    public SelectDataLayoutPage(FlatTextImporter flatTextImporter) {
        this.reader = flatTextImporter;

        separatorsModel = new DefaultComboBoxModel(Separator.values());
        separatorCombo.setModel(separatorsModel);
        separatorCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reader.getReaderProfile().setSeparator((Separator) separatorsModel.getSelectedItem());
                updateParsing();
            }
        });

        matrixRadioButton.setSelected(reader.getReaderProfile().getLayout().equals(MATRIX));
        tableRadioButton.setSelected(reader.getReaderProfile().getLayout().equals(TABLE));
        ActionListener layoutListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tableRadioButton.isSelected()) {
                    reader.setReaderProfile(TableReaderProfile.fromProfile(reader.getReaderProfile()));
                } else if (matrixRadioButton.isSelected()) {
                    reader.setReaderProfile(MatrixReaderProfile.fromProfile(reader.getReaderProfile()));
                }
                updateControls();
            }
        };
        tableRadioButton.addActionListener(layoutListener);
        matrixRadioButton.addActionListener(layoutListener);
        dataFormatTextPane.setContentType("text/html");
        setComplete(true);

        separatorsModel.setSelectedItem(reader.getReaderProfile().getSeparator());
    }

    @Override
    public JComponent createControls() {
        return mainPanel;
    }

    @Override
    public void updateControls() {

        try {
            updateParsing();

            if (tableRadioButton.isSelected()) {
                dataFormatTextPane.setText("<html><body>" +
                        "Each line is a heatmap cell: Two fields describing Column and Row id.<br>" +
                        "The other fields are data points, therefore <b>multiple per cell</b>.<br>" +
                        "<img max-width=\"300px\" src=\"" + IconNames.DATA_FORMAT_TABLE.toString() + "\">" +
                        "</body></html>");
            } else if (matrixRadioButton.isSelected()) {
                dataFormatTextPane.setText("<html><body>" +
                        "The first row and column of the file are the Column and Row ids <br>" +
                        "<b>Only one data point per cell is possible</b>.<br>" +
                        "<img max-width=\"300px\" src=\"" + IconNames.DATA_FORMAT_MATRIX.toString() + "\">" +
                        "</body></html>");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void updateParsing() {

        reader.loadHead(NullProgressMonitor.get());
        List<FileHeader> allHeaders = reader.getFileHeaders();

        StringBuilder htmlTable = getHTMLTable();
        preview.setText(htmlTable.toString());

        preview.setCaretPosition(0);


        if (allHeaders.size() > 2) {
            setComplete(true);
            setMessage(MessageStatus.INFO, "Select Data Layout.");
        } else {
            setMessage(MessageStatus.ERROR, "Only " + allHeaders.size() + " columns detected.");
            setComplete(false);
        }
        separatorsModel.setSelectedItem(reader.getReaderProfile().getSeparator());

    }

    private StringBuilder getHTMLTable() {
        StringBuilder htmlTable = new StringBuilder("");
        //header
        htmlTable.append("<html><head>");
        htmlTable.append(PreviewHTMLs.CSS);
        htmlTable.append("</head><body><table><tr>");
        for (FileHeader header : reader.getFileHeaders()) {
            htmlTable.append("<th>" + header.getLabel() + "</th>");
        }
        htmlTable.append("</tr>");


        // preview

        boolean matrix = reader.getReaderProfile().getLayout().equals(MATRIX);
        for (List<FileField> line : reader.getPreview()) {
            htmlTable.append("<tr>");
            for (FileField field : line) {
                String celltype = (matrix && field.getPos() == 0) ? "th" : "td";
                htmlTable.append("<" + celltype + ">");
                htmlTable.append(field.getLabel());
                htmlTable.append("</" + celltype + ">");
            }
            htmlTable.append("</tr>");
        }


        //end
        htmlTable.append("</html></body></table");
        return htmlTable;
    }

    public FlatTextImporter getReader() {
        return reader;
    }

    public void setReader(FlatTextImporter reader) {
        this.reader = reader;
    }


}
