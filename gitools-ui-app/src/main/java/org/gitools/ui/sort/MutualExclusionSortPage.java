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
package org.gitools.ui.sort;

import org.apache.commons.lang.ArrayUtils;
import org.gitools.heatmap.Heatmap;
import org.gitools.label.AnnotationsPatternProvider;
import org.gitools.label.LabelProvider;
import org.gitools.label.MatrixColumnsLabelProvider;
import org.gitools.label.MatrixRowsLabelProvider;
import org.gitools.matrix.filter.MatrixViewLabelFilter.FilterDimension;
import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.dialog.ExceptionDialog;
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.gitools.ui.platform.wizard.PageDialog;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.utils.DocumentChangeListener;
import org.gitools.ui.utils.FileChooserUtils;
import org.gitools.ui.wizard.common.PatternSourcePage;
import org.jetbrains.annotations.NotNull;

import javax.swing.event.DocumentEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MutualExclusionSortPage extends AbstractWizardPage
{

    private final Heatmap hm;

    private String rowsPatt;
    private String colsPatt;

    public MutualExclusionSortPage(Heatmap hm)
    {
        this.hm = hm;

        initComponents();

        ActionListener dimChangedListener = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                dimChanged();
            }
        };

        rowsRb.addActionListener(dimChangedListener);
        colsRb.addActionListener(dimChangedListener);

        rowsPattBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                selectRowsPattern();
            }
        });

        colsPattBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                selectColsPattern();
            }
        });

        patterns.getDocument().addDocumentListener(new DocumentChangeListener()
        {
            @Override
            protected void update(DocumentEvent e)
            {
                saveBtn.setEnabled(patterns.getDocument().getLength() > 0);
            }
        });

        rowsPatt = "${id}";
        rowsPattFld.setText("id");

        colsPatt = "${id}";
        colsPattFld.setText("id");

        dimChanged();

        setTitle("Sort by mutual exclusion");
        setMessage("Puts the selected rows/columns at the top of the matrix and " + "sorts them by their mutual exclusion.");
        setComplete(true);
    }

    private void dimChanged()
    {
        boolean rs = rowsRb.isSelected();
        rowsPattFld.setEnabled(rs);
        rowsPattBtn.setEnabled(rs);
        boolean cs = colsRb.isSelected();
        colsPattFld.setEnabled(cs);
        colsPattBtn.setEnabled(cs);
    }

    @NotNull
    String readNamesFromFile(File file) throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null)
        {
            line = line.trim();
            if (!line.isEmpty())
            {
                sb.append(line).append('\n');
            }
        }

        return sb.toString();
    }

    private void selectRowsPattern()
    {
        PatternSourcePage page = new PatternSourcePage(hm.getRows().getAnnotations(), true);
        PageDialog dlg = new PageDialog(AppFrame.get(), page);
        dlg.setVisible(true);
        if (dlg.isCancelled())
        {
            return;
        }

        rowsPatt = page.getPattern();
        rowsPattFld.setText(page.getPatternTitle());
    }

    private void selectColsPattern()
    {
        PatternSourcePage page = new PatternSourcePage(hm.getColumns().getAnnotations(), true);
        PageDialog dlg = new PageDialog(AppFrame.get(), page);
        dlg.setVisible(true);
        if (dlg.isCancelled())
        {
            return;
        }

        colsPatt = page.getPattern();
        colsPattFld.setText(page.getPatternTitle());
    }

    @NotNull
    public FilterDimension getFilterDimension()
    {
        if (rowsRb.isSelected())
        {
            return FilterDimension.ROWS;
        }
        else
        {
            return FilterDimension.COLUMNS;
        }
    }

    public void setFilterDimension(@NotNull FilterDimension fd)
    {
        if (fd.equals(FilterDimension.COLUMNS))
        {
            colsRb.setSelected(true);
            rowsRb.setSelected(false);
        }
        else if (fd.equals(FilterDimension.ROWS))
        {
            colsRb.setSelected(false);
            rowsRb.setSelected(true);
        }
        dimChanged();
    }

    public String getPattern()
    {
        if (rowsRb.isSelected())
        {
            return rowsPatt;
        }
        else
        {
            return colsPatt;
        }
    }

    @NotNull
    private ArrayList<String> getSelected()
    {
        FilterDimension dim = rowsRb.isSelected() ? FilterDimension.ROWS : FilterDimension.COLUMNS;
        ArrayList<String> selected = new ArrayList<String>();
        LabelProvider labelProvider = dim == FilterDimension.ROWS ? new MatrixRowsLabelProvider(hm.getMatrixView()) : new MatrixColumnsLabelProvider(hm.getMatrixView());
        if (!getPattern().equalsIgnoreCase("${id}"))
        {
            AnnotationMatrix am = dim == FilterDimension.ROWS ? hm.getRows().getAnnotations() : hm.getColumns().getAnnotations();
            labelProvider = new AnnotationsPatternProvider(labelProvider, am, getPattern());
        }

        int[] selectedIndices = dim == FilterDimension.ROWS ? hm.getMatrixView().getSelectedRows() : hm.getMatrixView().getSelectedColumns();
        for (int i = 0; i < selectedIndices.length; i++)
            selected.add(labelProvider.getLabel(selectedIndices[i]));

        return selected;
    }

    @NotNull
    private ArrayList<String> getUnselected()
    {
        FilterDimension dim = rowsRb.isSelected() ? FilterDimension.ROWS : FilterDimension.COLUMNS;
        ArrayList<String> unselected = new ArrayList<String>();
        LabelProvider labelProvider = dim == FilterDimension.ROWS ? new MatrixRowsLabelProvider(hm.getMatrixView()) : new MatrixColumnsLabelProvider(hm.getMatrixView());
        if (!getPattern().equalsIgnoreCase("${id}"))
        {
            AnnotationMatrix am = dim == FilterDimension.ROWS ? hm.getRows().getAnnotations() : hm.getColumns().getAnnotations();
            labelProvider = new AnnotationsPatternProvider(labelProvider, am, getPattern());
        }

        int[] selectedIndices = dim == FilterDimension.ROWS ? hm.getMatrixView().getSelectedRows() : hm.getMatrixView().getSelectedColumns();
        int visibleCount = dim == FilterDimension.ROWS ? hm.getMatrixView().getRowCount() : hm.getMatrixView().getColumnCount();


        int[] unselectedIndices = new int[visibleCount - selectedIndices.length];
        int count = 0;
        for (int i = 0; i < visibleCount; i++)
        {
            if (!(ArrayUtils.contains(selectedIndices, i)))
            {
                unselectedIndices[count] = i;
                count++;
            }
        }

        for (int i = 0; i < unselectedIndices.length; i++)
            unselected.add(labelProvider.getLabel(unselectedIndices[i]));
        return unselected;
    }

    void setValues(@NotNull List<String> values)
    {
        Iterator<String> it = values.iterator();
        while (it.hasNext())
        {
            patterns.append(it.next() + "\n");
        }

    }

    @NotNull
    public List<String> getValues()
    {
        List<String> values = new ArrayList<String>();
        StringReader sr = new StringReader(patterns.getText());
        BufferedReader br = new BufferedReader(sr);
        String line;
        try
        {
            while ((line = br.readLine()) != null)
            {
                line = line.trim();
                if (!line.isEmpty())
                {
                    values.add(line);
                }
            }
        } catch (IOException ex)
        {
            ExceptionDialog dlg = new ExceptionDialog(AppFrame.get(), ex);
            dlg.setVisible(true);
        }

        return values;
    }

    public boolean isUseRegexChecked()
    {
        return useRegexCheck.isSelected();
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        applyGroup = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        useRegexCheck = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        patterns = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        loadBtn = new javax.swing.JButton();
        rowsPattFld = new javax.swing.JTextField();
        saveBtn = new javax.swing.JButton();
        rowsPattBtn = new javax.swing.JButton();
        colsPattFld = new javax.swing.JTextField();
        colsPattBtn = new javax.swing.JButton();
        rowsRb = new javax.swing.JRadioButton();
        colsRb = new javax.swing.JRadioButton();
        pasteSelected1 = new javax.swing.JButton();
        pasteUnselected1 = new javax.swing.JButton();

        jLabel1.setText("Labels to include:");

        useRegexCheck.setText("Use regular expressions");

        patterns.setColumns(20);
        patterns.setRows(6);
        jScrollPane1.setViewportView(patterns);

        jLabel3.setFont(jLabel3.getFont().deriveFont(jLabel3.getFont().getSize() - 2f));
        jLabel3.setText("One label or regular expression per line");

        loadBtn.setText("Load...");
        loadBtn.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                loadBtnActionPerformed(evt);
            }
        });

        rowsPattFld.setEditable(false);

        saveBtn.setText("Save...");
        saveBtn.setEnabled(false);
        saveBtn.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                saveBtnActionPerformed(evt);
            }
        });

        rowsPattBtn.setText("Change...");

        colsPattFld.setEditable(false);

        colsPattBtn.setText("Change...");

        applyGroup.add(rowsRb);
        rowsRb.setSelected(true);
        rowsRb.setText("Rows");

        applyGroup.add(colsRb);
        colsRb.setText("Columns");

        pasteSelected1.setText("paste Selected");
        pasteSelected1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                pasteSelected1ActionPerformed(evt);
            }
        });

        pasteUnselected1.setText("paste Unselected");
        pasteUnselected1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                pasteUnselected1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(colsRb).addComponent(rowsRb)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(rowsPattFld, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(rowsPattBtn)).addGroup(layout.createSequentialGroup().addComponent(colsPattFld, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(colsPattBtn)))).addComponent(jLabel3).addComponent(useRegexCheck)).addContainerGap()).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(pasteUnselected1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(pasteSelected1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(loadBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(saveBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(rowsPattBtn).addComponent(rowsRb).addComponent(rowsPattFld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(colsPattBtn).addComponent(colsRb).addComponent(colsPattFld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(loadBtn).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(saveBtn).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(pasteSelected1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(pasteUnselected1)).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel3).addGap(18, 18, 18).addComponent(useRegexCheck).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
    }// </editor-fold>//GEN-END:initComponents

    private void loadBtnActionPerformed(java.awt.event.ActionEvent evt)
    {//GEN-FIRST:event_loadBtnActionPerformed

        try
        {
            File file = FileChooserUtils.selectFile("Select the file containing values", Settings.getDefault().getLastFilterPath(), FileChooserUtils.MODE_OPEN);

            if (file == null)
            {
                return;
            }

            Settings.getDefault().setLastFilterPath(file.getParent());

            patterns.setText(readNamesFromFile(file));
        } catch (IOException ex)
        {
            ExceptionDialog edlg = new ExceptionDialog(AppFrame.get(), ex);
            edlg.setVisible(true);
        }
    }//GEN-LAST:event_loadBtnActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt)
    {//GEN-FIRST:event_saveBtnActionPerformed
        try
        {
            File file = FileChooserUtils.selectFile("Select file name ...", Settings.getDefault().getLastFilterPath(), FileChooserUtils.MODE_SAVE);

            if (file == null)
            {
                return;
            }

            Settings.getDefault().setLastFilterPath(file.getParent());

            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.append(patterns.getText()).append('\n');
            bw.close();
        } catch (Exception ex)
        {
            ExceptionDialog edlg = new ExceptionDialog(AppFrame.get(), ex);
            edlg.setVisible(true);
        }
    }//GEN-LAST:event_saveBtnActionPerformed

    private void pasteSelected1ActionPerformed(java.awt.event.ActionEvent evt)
    {//GEN-FIRST:event_pasteSelected1ActionPerformed
        ArrayList<String> selectedColsLabels = getSelected();
        setValues(selectedColsLabels);
    }//GEN-LAST:event_pasteSelected1ActionPerformed

    private void pasteUnselected1ActionPerformed(java.awt.event.ActionEvent evt)
    {//GEN-FIRST:event_pasteUnselected1ActionPerformed
        ArrayList<String> unselectedColsLabels = getUnselected();
        setValues(unselectedColsLabels);
    }//GEN-LAST:event_pasteUnselected1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup applyGroup;
    private javax.swing.JButton colsPattBtn;
    private javax.swing.JTextField colsPattFld;
    private javax.swing.JRadioButton colsRb;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton loadBtn;
    private javax.swing.JButton pasteSelected1;
    private javax.swing.JButton pasteUnselected1;
    private javax.swing.JTextArea patterns;
    private javax.swing.JButton rowsPattBtn;
    private javax.swing.JTextField rowsPattFld;
    private javax.swing.JRadioButton rowsRb;
    private javax.swing.JButton saveBtn;
    private javax.swing.JCheckBox useRegexCheck;
    // End of variables declaration//GEN-END:variables

}
