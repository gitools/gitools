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

import org.gitools.ui.app.IconNames;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.gitools.utils.progressmonitor.NullProgressMonitor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import static org.gitools.utils.text.ReaderProfile.SEPARATORS;

public class SelectDataLayoutPage extends AbstractWizardPage {

    private FlatTextReader reader;

    private JPanel mainPanel;

    private JComboBox separatorCombo;
    private JRadioButton tableRadioButton;
    private JRadioButton matrixRadioButton;
    private JTextPane dataFormatTextPane;
    private JTextPane preview;

    private DefaultComboBoxModel separatorsModel;


    public SelectDataLayoutPage(FlatTextReader flatTextReader) {
        this.reader = flatTextReader;

        separatorsModel = new DefaultComboBoxModel(SEPARATORS);
        separatorCombo.setModel(separatorsModel);
        separatorCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reader.getReaderProfile().setSeparator((String) separatorsModel.getSelectedItem());
                updateParsing();
            }
        });

        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: set correct profile (Table / tab)
                updateControls();
            }
        };
        tableRadioButton.addActionListener(listener);
        matrixRadioButton.addActionListener(listener);
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

        reader.run(NullProgressMonitor.get());
        List<FlatTextHeader> allHeaders = reader.getHeaders();

        StringBuilder table = new StringBuilder("");
        table.append("<html><head>" +
                "<style>" +
                " th {\n" +
                "    border-collapse: collapse;\n" +
                "    border: 2px solid black;\n" +
                "    white-space: nowrap;\n" +
                "  }\n" +
                " td {\n" +
                "    border-collapse: collapse;\n" +
                "    border: 1px solid black;\n" +
                "    white-space: nowrap;\n" +
                "  }" +
                "</style>" +
                "</head><body><table><tr>");
        for (FlatTextHeader header : allHeaders) {
            table.append("<th>" + header.getLabel() + "</th>");
        }
        table.append("<tr></html></body></table");
        preview.setText(table.toString());


        if (allHeaders.size() > 3) {
            setComplete(true);
            setMessage(MessageStatus.INFO, "Select Data Layout.");
        } else {
            setMessage(MessageStatus.ERROR, "Only " + allHeaders.size() + " columns detected.");
            setComplete(false);
        }
        separatorsModel.setSelectedItem(reader.getReaderProfile().getSeparator());

    }

    public FlatTextReader getReader() {
        return reader;
    }

    public void setReader(FlatTextReader reader) {
        this.reader = reader;
    }


}
