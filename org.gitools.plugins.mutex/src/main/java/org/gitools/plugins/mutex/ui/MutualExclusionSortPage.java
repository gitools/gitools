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

import com.google.common.base.Function;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.matrix.filter.PatternFunction;
import org.gitools.ui.platform.settings.Settings;
import org.gitools.ui.core.utils.DocumentChangeListener;
import org.gitools.ui.core.utils.FileChooserUtils;
import org.gitools.ui.core.pages.common.PatternSourcePage;
import org.gitools.ui.core.Application;
import org.gitools.ui.platform.dialog.ExceptionDialog;
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.gitools.ui.platform.wizard.PageDialog;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Predicates.in;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;


public class MutualExclusionSortPage extends AbstractWizardPage {

    private JPanel rootPanel;
    private JTextField patternsField;
    private JButton patternButton;
    private JTextArea patternsArea;
    private JButton loadButton;
    private JButton saveButton;
    private JButton pasteSelectedButton;
    private JButton pasteUnselectedButton;
    private JCheckBox useRegexCheck;
    private JCheckBox performTest;


    private final Heatmap hm;
    private final MatrixDimensionKey dimensionKey;
    private String pattern;

    public MutualExclusionSortPage(Heatmap hm, MatrixDimensionKey dimensionKey) {
        this.hm = hm;
        this.dimensionKey = dimensionKey;


        patternsArea.getDocument().addDocumentListener(new DocumentChangeListener() {
            @Override
            protected void update(DocumentEvent e) {
                saveButton.setEnabled(patternsArea.getDocument().getLength() > 0);
            }
        });

        pattern = "${id}";
        patternsField.setText("id");

        setTitle("Sort by mutual exclusion");
        setMessage("Puts the selected rows/columns at the top of the matrix and " + "sorts them by their mutual exclusion.");
        setComplete(true);

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadBtnAction();
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveBtnAction();
            }
        });
        pasteSelectedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setValues(getSelected());
            }
        });
        pasteUnselectedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setValues(getUnselected());
            }
        });
        patternButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePatternAction();
            }
        });
    }

    private void saveBtnAction() {
        try {
            File file = FileChooserUtils.selectFile("Select file name ...", Settings.get().getLastFilterPath(), FileChooserUtils.MODE_SAVE).getFile();

            if (file == null) {
                return;
            }

            Settings.get().setLastFilterPath(file.getParent());

            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.append(patternsArea.getText()).append('\n');
            bw.close();
        } catch (Exception ex) {
            ExceptionDialog edlg = new ExceptionDialog(Application.get(), ex);
            edlg.setVisible(true);
        }
    }

    public boolean performTest() {
        return performTest.isSelected();
    }

    @Override
    public JComponent createControls() {
        return rootPanel;
    }

    private void loadBtnAction() {
        try {
            File file = FileChooserUtils.selectFile("Select the file containing values", Settings.get().getLastFilterPath(), FileChooserUtils.MODE_OPEN).getFile();

            if (file == null) {
                return;
            }

            Settings.get().setLastFilterPath(file.getParent());

            patternsArea.setText(readNamesFromFile(file));
        } catch (IOException ex) {
            ExceptionDialog edlg = new ExceptionDialog(Application.get(), ex);
            edlg.setVisible(true);
        }
    }

    String readNamesFromFile(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty()) {
                sb.append(line).append('\n');
            }
        }

        return sb.toString();
    }

    private void changePatternAction() {
        PatternSourcePage page = new PatternSourcePage(hm.getDimension(dimensionKey), true);
        PageDialog dlg = new PageDialog(Application.get(), page);
        dlg.setVisible(true);
        if (dlg.isCancelled()) {
            return;
        }

        pattern = page.getPattern();
        patternsField.setText(page.getPatternTitle());
    }



    public MatrixDimensionKey getDimension() {
        return  dimensionKey;
    }

    public String getPattern() {
        return pattern;
    }


    private ArrayList<String> getSelected() {

        HeatmapDimension dimension = hm.getDimension(getDimension());
        Function<String, String> patternFunction = new PatternFunction(getPattern(), dimension.getAnnotations());

        return newArrayList(transform(dimension.getSelected(), patternFunction));
    }


    private ArrayList<String> getUnselected() {
        HeatmapDimension dimension = hm.getDimension(getDimension());
        Function<String, String> patternFunction = new PatternFunction(getPattern(), dimension.getAnnotations());
        Iterable<String> unselected = filter(dimension, not(in(dimension.getSelected())));
        return newArrayList(transform(unselected, patternFunction));
    }


    public Set<String> getValues() {
        Set<String> values = new HashSet<>();
        StringReader sr = new StringReader(patternsArea.getText());
        BufferedReader br = new BufferedReader(sr);
        String line;
        try {
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    values.add(line);
                }
            }
        } catch (IOException ex) {
            ExceptionDialog dlg = new ExceptionDialog(Application.get(), ex);
            dlg.setVisible(true);
        }

        return values;
    }

    void setValues(List<String> values) {
        for (String value : values) {
            patternsArea.append(value + "\n");
        }

    }

    public boolean isUseRegexChecked() {
        return useRegexCheck.isSelected();
    }


}
