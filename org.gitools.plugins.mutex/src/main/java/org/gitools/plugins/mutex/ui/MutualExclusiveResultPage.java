/*
 * #%L
 * org.gitools.ui.app
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
package org.gitools.plugins.mutex.ui;

import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.heatmap.Bookmark;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.matrix.element.LayerAdapter;
import org.gitools.plugins.mutex.MutualExclusiveBookmark;
import org.gitools.plugins.mutex.MutualExclusivePlugin;
import org.gitools.plugins.mutex.analysis.MutualExclusiveResult;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.help.HelpContext;
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.gitools.utils.formatter.HeatmapTextFormatter;
import org.gitools.utils.formatter.ScientificHeatmapTextFormatter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.ArrayList;
import java.util.List;

public class MutualExclusiveResultPage extends AbstractWizardPage {
    private JPanel panel;
    private JTextField nameField;
    private JLabel rowNbLabel;
    private JLabel colNbLabel;
    private JTextArea descriptionTextArea;
    private JLabel layerLabel;
    private JLabel mutexPvalueLabel;
    private JLabel zscoreLabel;
    private JLabel signalLabel;
    private JLabel coverageLabel;
    private JLabel ratioLabel;
    private JLabel meanLabel;
    private JLabel varianceLabel;
    private JLabel coocPValueLabel;
    private boolean delete = false;
    private List<String> forbiddenNames;
    private MutualExclusiveBookmark bookmark;
    private MutualExclusiveResult result;
    boolean creating;
    private Heatmap heatmap;
    private String oldName;

    public Bookmark getBookmark() {
        return bookmark;
    }


    public MutualExclusiveResultPage(Heatmap heatmap, MutualExclusiveBookmark bookmark) {
        super();
        this.heatmap = heatmap;
        this.setTitle("Mutual Exclusive result");
        this.result = bookmark.getResult();
        this.bookmark = bookmark;
        this.oldName = bookmark.getName();

        if (bookmark.getDescription().equals("")) {
            StringBuilder sb = new StringBuilder("Sorted and tested:\n");
            List<String> tested = bookmark.getTestDimension().equals(MatrixDimensionKey.ROWS) ?
                    bookmark.getRows() : bookmark.getColumns();
            for (String t : tested) {
                sb.append(" - " + t + "\n");
            }
            bookmark.setDescription(sb.toString());
        }

        setMessage(MessageStatus.INFO, "Choose a name for the Bookmark");


        setHelpContext(new HelpContext("http://www.google.com/s?Gitools+mutual+exclusive"));


        //BOOKMARK NAME
        MutualExclusivePlugin plugin = (MutualExclusivePlugin) heatmap.getPluggedBoxes().get(MutualExclusivePlugin.ID);
        forbiddenNames = new ArrayList<>();
        forbiddenNames.add("");
        forbiddenNames.add("-");
        forbiddenNames.add(" ");
        for (String s : plugin.getKeys()) {
            forbiddenNames.add(s);
        }
        nameField.setText(bookmark.getName());
        nameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateModel();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateModel();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateModel();
            }
        });

        //Description
        descriptionTextArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateModel();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateModel();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateModel();
            }
        });

        descriptionTextArea.setText(bookmark.getDescription());

        assignLabels();
        updateControls();
        nameField.selectAll();
    }

    public boolean isDelete() {
        return delete;
    }


    @Override
    public void updateModel() {
        bookmark.setName(nameField.getText());

        bookmark.setDescription(descriptionTextArea.getText());

        updateControls();
    }

    @Override
    public void updateControls() {

        if (bookmark.getName().equals("new Bookmark") || bookmark.getName().equals("")) {
            setComplete(false);
            return;
        }
        if (!uniqueMutexName()) {
            setMessage(MessageStatus.WARN, "Please choose a unique name for the result or click cancel to discard");
            setComplete(false);
            return;
        } else {
            setMessage(MessageStatus.INFO, "Click 'OK' add result to heatmap or 'Cancel' to discard");
        }

        setComplete(true);
    }

    private String fill(String s) {
        return "<html><b>" + s + "</b></html>";
    }

    private String fill(Double d) {

        return "<html><b>" + HeatmapTextFormatter.TWO_DECIMALS.format(d) + "</b></html>";
    }

    private String fillPValue(Double d) {
        return "<html><b>" + ScientificHeatmapTextFormatter.INSTANCE.format(d) + "</b></html>";
    }

    private String fill(Integer i) {
        return "<html><b>" + Integer.toString(i) + "</b></html>";
    }


    private void assignLabels() {

        LayerAdapter<MutualExclusiveResult> adapter = new LayerAdapter<>(MutualExclusiveResult.class);

        String rows = bookmark.getRows() == null ? "-" : String.valueOf(bookmark.getRows().size());
        String cols = bookmark.getColumns() == null ? "-" : String.valueOf(bookmark.getColumns().size());
        rowNbLabel.setText(fill(rows));
        colNbLabel.setText(fill(cols));
        layerLabel.setText(fill(bookmark.getLayerId()));
        mutexPvalueLabel.setText(fillPValue(result.getMutexPvalue()));
        coocPValueLabel.setText(fillPValue(result.getCoocPvalue()));
        zscoreLabel.setText(fill(result.getZscore()));
        signalLabel.setText(fill(result.getSignal()));
        coverageLabel.setText(fill(result.getCoverage()));
        ratioLabel.setText(fill(result.getSignalCoverageRatio()));
        meanLabel.setText(fill(result.getExpectedMean()));
        varianceLabel.setText(fill(result.getExpectedVar()));
    }

    public boolean uniqueMutexName() {
        if (oldName == null || bookmark.getName().equals("")) {
            return false;
        }
        return (oldName.toLowerCase().equals(bookmark.getName().toLowerCase()) || !forbiddenNames.contains(bookmark.getName()));
    }

    @Override
    public JComponent createControls() {
        return panel;
    }


}
