package org.gitools.ui.app.fileimport.wizard.csv;

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
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.gitools.utils.progressmonitor.NullProgressMonitor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SelectDataLayoutPage extends AbstractWizardPage {

    private CsvReader reader;

    private JPanel mainPanel;

    private JComboBox separatorCombo;
    private JRadioButton tableRadioButton;
    private JRadioButton matrixRadioButton;
    private JTextPane dataFormatTextPane;

    private DefaultComboBoxModel separator;


    public SelectDataLayoutPage() {
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateControls();
            }
        };
        tableRadioButton.addActionListener(listener);
        matrixRadioButton.addActionListener(listener);
        dataFormatTextPane.setContentType("text/html");

    }

    @Override
    public JComponent createControls() {
        return mainPanel;
    }

    @Override
    public void updateControls() {

        try {

            separator = new DefaultComboBoxModel(CsvReader.SEPARATORS);
            separatorCombo.setModel(separator);
            separatorCombo.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    reader.setSeparator((String) separator.getSelectedItem());
                    updateParsing();
                }
            });

            updateParsing();

            if (tableRadioButton.isSelected()) {
                dataFormatTextPane.setText("<html><body>" +
                        "Each line is a heatmap cell: Two fields describing Column and Row id.<br>" +
                        "The other fields are data points, therefore multiple per cell.<br>" +
                        "<img max-width=\"300px\" src=\"" + IconNames.DATA_FORMAT_TABLE.toString() + "\">" +
                        "</body></html>");
            } else if (matrixRadioButton.isSelected()) {
                dataFormatTextPane.setText("<html><body>" +
                        "The first row and column of the file are the Column and Row ids <br>" +
                        "Only one data point per cell is possible.<br>" +
                        "<img max-width=\"300px\" src=\"" + IconNames.DATA_FORMAT_MATRIX.toString() + "\">" +
                        "</body></html>");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void updateParsing() {

        reader.run(NullProgressMonitor.get());
        List<CsvHeader> allHeaders = reader.getHeaders();

        separator.setSelectedItem(reader.getSeparator());

    }

    @Override
    public boolean isComplete() {


        return false;
    }

    public CsvReader getReader() {
        return reader;
    }

    public void setReader(CsvReader reader) {
        this.reader = reader;
    }


}
