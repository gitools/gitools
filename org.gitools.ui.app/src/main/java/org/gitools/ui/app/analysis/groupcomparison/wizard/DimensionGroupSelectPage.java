package org.gitools.ui.app.analysis.groupcomparison.wizard;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.matrix.filter.FilterByLabelPredicate;
import org.gitools.matrix.filter.PatternFunction;
import org.gitools.ui.app.settings.Settings;
import org.gitools.ui.app.utils.DocumentChangeListener;
import org.gitools.ui.app.utils.FileChooserUtils;
import org.gitools.ui.app.wizard.common.PatternSourcePage;
import org.gitools.ui.platform.Application;
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

public class DimensionGroupSelectPage extends AbstractWizardPage {
    private JTextField colsPattFld;
    private JPanel panel1;
    private JButton changeButton;
    private JTextArea textArea;
    private JButton pasteUnselectedButton;
    private JButton loadButton;
    private JButton saveButton;
    private JButton pasteSelectedButton;
    private JScrollPane scrollPane;
    private JPanel buttonPanel;
    private JTextField groupName;


    private final HeatmapDimension dimension;
    private String pattern;

    public DimensionGroupSelectPage(HeatmapDimension dimension, String groupName) {
        this.dimension = dimension;
        this.groupName.setText(groupName);

        setComplete(false);
        textArea.getDocument().addDocumentListener(new DocumentChangeListener() {
            @Override
            protected void update(DocumentEvent e) {
                saveButton.setEnabled(textArea.getDocument().getLength() > 0);
                setComplete(textArea.getDocument().getLength() > 0);
            }
        });

        pattern = "${id}";
        colsPattFld.setText("id");

        setTitle("Group Comparison Analysis");
        setMessage("Choose the two column groups to compare");


        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectColsPattern();
            }
        });
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadFromFile();
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveToFile();
            }
        });
        pasteSelectedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> ids = getSelected();
                setValues(ids, textArea);
            }
        });
        pasteUnselectedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> ids = getUnselected();
                setValues(ids, textArea);
            }
        });
    }

    public String getGroupName() {
        return (groupName.getText());
    }

    private void saveToFile() {
        try {
            File file = FileChooserUtils.selectFile("Select file name ...", Settings.getDefault().getLastFilterPath(), FileChooserUtils.MODE_SAVE).getFile();

            if (file == null) {
                return;
            }

            Settings.getDefault().setLastFilterPath(file.getParent());

            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.append(textArea.getText()).append('\n');
            bw.close();
        } catch (Exception ex) {
            ExceptionDialog edlg = new ExceptionDialog(Application.get(), ex);
            edlg.setVisible(true);
        }
    }

    private void loadFromFile() {
        try {
            File file = FileChooserUtils.selectFile("Select the file containing values", Settings.getDefault().getLastFilterPath(), FileChooserUtils.MODE_OPEN).getFile();

            if (file == null) {
                return;
            }

            Settings.getDefault().setLastFilterPath(file.getParent());

            textArea.setText(readNamesFromFile(file));
        } catch (IOException ex) {
            ExceptionDialog edlg = new ExceptionDialog(Application.get(), ex);
            edlg.setVisible(true);
        }
    }

    private void selectColsPattern() {
        PatternSourcePage page = new PatternSourcePage(dimension, true);
        PageDialog dlg = new PageDialog(Application.get(), page);
        dlg.setVisible(true);
        if (dlg.isCancelled()) {
            return;
        }

        pattern = page.getPattern();
        colsPattFld.setText(page.getPatternTitle());
    }

    public String getPattern() {
        return pattern;
    }

    private String readNamesFromFile(File file) throws IOException {
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

    public Set<String> getGroup() {
        return getGroupIndices(textArea);
    }

    private Set<String> getGroupIndices(JTextArea patterns) {

        // Read pattern values
        Set<String> values = new HashSet<>();
        StringReader sr = new StringReader(patterns.getText());
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

        // Filter identifiers
        Predicate<String> filter = new FilterByLabelPredicate(
                new PatternFunction(pattern, dimension.getAnnotations()),
                values,
                false);

        return Sets.newHashSet(filter(dimension, filter));
    }

    private ArrayList<String> getSelected() {

        return newArrayList(
                transform(
                        dimension.getSelected(),
                        new PatternFunction(pattern, dimension.getAnnotations())
                )
        );
    }

    private ArrayList<String> getUnselected() {

        return newArrayList(
                transform(
                        filter(dimension, not(in(dimension.getSelected()))),
                        new PatternFunction(pattern, dimension.getAnnotations())
                )
        );

    }


    private void setValues(List<String> values, JTextArea patterns) {
        for (String value : values) {
            patterns.append(value + "\n");
        }
    }

    @Override
    public JComponent createControls() {
        return panel1;
    }
}
