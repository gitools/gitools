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
package org.gitools.ui.analysis.groupcomparison.wizard;

import org.apache.commons.lang.ArrayUtils;
import org.gitools.heatmap.Heatmap;
import org.gitools.label.AnnotationsPatternProvider;
import org.gitools.label.LabelProvider;
import org.gitools.label.MatrixColumnsLabelProvider;
import org.gitools.matrix.filter.MatrixViewLabelFilter;
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

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GroupComparisonGroupingByLabelPage extends AbstractWizardPage
{

    private final Heatmap hm;

    private String colsPatt;

    public GroupComparisonGroupingByLabelPage(Heatmap hm)
    {
        this.hm = hm;

        initComponents();
        setComplete(false);


        colsPattBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                selectColsPattern();
            }
        });

        patterns1.getDocument().addDocumentListener(new DocumentChangeListener()
        {
            @Override
            protected void update(DocumentEvent e)
            {
                saveBtn1.setEnabled(patterns1.getDocument().getLength() > 0);
                setComplete(patterns1.getDocument().getLength() > 0 && patterns2.getDocument().getLength() > 0);
            }
        });

        patterns2.getDocument().addDocumentListener(new DocumentChangeListener()
        {
            @Override
            protected void update(DocumentEvent e)
            {
                saveBtn2.setEnabled(patterns2.getDocument().getLength() > 0);
                setComplete(patterns1.getDocument().getLength() > 0 && patterns2.getDocument().getLength() > 0);
            }
        });


        colsPatt = "${id}";
        colsPattFld.setText("id");


        setTitle("Group Comparison Analysis");
        setMessage("Choose the two column groups to compare");
        setFilterDimension(FilterDimension.COLUMNS);
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


    /**
     * @noinspection UnusedDeclaration
     */
    @NotNull
    public FilterDimension getFilterDimension()
    {
        return FilterDimension.COLUMNS;
    }

    private void setFilterDimension(FilterDimension fd)
    {
        colsRb.setSelected(true);
    }

    String getPattern()
    {
        return colsPatt;
    }

    @NotNull
    public int[] getGroup1()
    {
        return getGroupIndices(patterns1);
    }

    @NotNull
    public int[] getGroup2()
    {
        return getGroupIndices(patterns2);
    }

    @NotNull
    private int[] getGroupIndices(@NotNull JTextArea patterns)
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

        //conversion to
        LabelProvider labelProvider = new MatrixColumnsLabelProvider(hm.getMatrixView());
        AnnotationMatrix am = hm.getColumns().getAnnotations();
        labelProvider = new AnnotationsPatternProvider(labelProvider, am, getPattern());
        int[] visibleCols = hm.getMatrixView().getVisibleColumns();
        int[] groupCols = MatrixViewLabelFilter.filterLabels(labelProvider, values, isUseRegexChecked(), visibleCols);
        int[] groupColIndices = new int[groupCols.length];
        for (int i = 0; i < groupCols.length; i++)
        {
            groupColIndices[i] = ArrayUtils.indexOf(visibleCols, groupCols[i]);
        }

        return groupColIndices;
    }

    @NotNull
    private ArrayList<String> getSelectedColumns()
    {
        ArrayList<String> selected = new ArrayList<String>();
        LabelProvider labelProvider = new MatrixColumnsLabelProvider(hm.getMatrixView());
        if (!getPattern().equalsIgnoreCase("${id}"))
        {
            labelProvider = new AnnotationsPatternProvider(labelProvider, hm.getColumns().getAnnotations(), getPattern());
        }

        int[] selectedIndices = hm.getMatrixView().getSelectedColumns();
        for (int i = 0; i < selectedIndices.length; i++)
            selected.add(labelProvider.getLabel(selectedIndices[i]));

        return selected;
    }


    void setValues(@NotNull List<String> values, @NotNull JTextArea patterns)
    {
        Iterator<String> it = values.iterator();
        while (it.hasNext())
        {
            patterns.append(it.next() + "\n");
        }

    }

    @NotNull
    private ArrayList<String> getUnselectedColumns()
    {
        ArrayList<String> unselected = new ArrayList<String>();
        LabelProvider labelProvider = new MatrixColumnsLabelProvider(hm.getMatrixView());
        if (!getPattern().equalsIgnoreCase("${id}"))
        {
            labelProvider = new AnnotationsPatternProvider(labelProvider, hm.getColumns().getAnnotations(), getPattern());
        }

        int[] selectedIndices = hm.getMatrixView().getSelectedColumns();
        int visibleColumnsCount = hm.getMatrixView().getColumnCount();
            /*for (int i=0; i < selectedIndices.length; i++) {
                visibleIndices = ArrayUtils.removeElement(visibleIndices, selectedIndices[i]);
			}*/
        int[] unselectedIndices = new int[visibleColumnsCount - selectedIndices.length];
        int count = 0;
        for (int i = 0; i < visibleColumnsCount; i++)
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

    boolean isUseRegexChecked()
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
        patterns1 = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        loadBtn1 = new javax.swing.JButton();
        saveBtn1 = new javax.swing.JButton();
        colsPattFld = new javax.swing.JTextField();
        colsPattBtn = new javax.swing.JButton();
        colsRb = new javax.swing.JRadioButton();
        pasteSelected1 = new javax.swing.JButton();
        pasteUnselected1 = new javax.swing.JButton();
        loadBtn2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        patterns2 = new javax.swing.JTextArea();
        pasteUnselected2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        pasteSelected2 = new javax.swing.JButton();
        saveBtn2 = new javax.swing.JButton();

        jLabel1.setText("Column labels for group 1:");

        useRegexCheck.setText("Use regular expressions");

        patterns1.setColumns(20);
        patterns1.setRows(6);
        jScrollPane1.setViewportView(patterns1);

        jLabel3.setFont(jLabel3.getFont().deriveFont(jLabel3.getFont().getSize() - 2f));
        jLabel3.setText("One label or regular expression per line");

        loadBtn1.setText("Load...");
        loadBtn1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                loadBtn1ActionPerformed(evt);
            }
        });

        saveBtn1.setText("Save...");
        saveBtn1.setEnabled(false);
        saveBtn1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                saveBtn1ActionPerformed(evt);
            }
        });

        colsPattFld.setEditable(false);

        colsPattBtn.setText("Change...");

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

        loadBtn2.setText("Load...");
        loadBtn2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                loadBtn2ActionPerformed(evt);
            }
        });

        patterns2.setColumns(20);
        patterns2.setRows(6);
        jScrollPane2.setViewportView(patterns2);

        pasteUnselected2.setText("paste Unselected");
        pasteUnselected2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                pasteUnselected2ActionPerformed(evt);
            }
        });

        jLabel2.setText("Column labels for group 2:");

        pasteSelected2.setText("paste Selected");
        pasteSelected2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                pasteSelected2ActionPerformed(evt);
            }
        });

        saveBtn2.setText("Save...");
        saveBtn2.setEnabled(false);
        saveBtn2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                saveBtn2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(colsRb).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(colsPattFld, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(colsPattBtn)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(pasteUnselected1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(saveBtn1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE).addComponent(loadBtn1, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE).addComponent(pasteSelected1, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE))).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel2).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(pasteUnselected2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(saveBtn2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE).addComponent(loadBtn2, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE).addComponent(pasteSelected2, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE))).addComponent(jLabel3).addComponent(useRegexCheck)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(colsPattBtn).addComponent(colsRb).addComponent(colsPattFld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(loadBtn1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(saveBtn1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(pasteSelected1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(pasteUnselected1)).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(loadBtn2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(saveBtn2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(pasteSelected2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(pasteUnselected2)).addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addComponent(jLabel3).addGap(18, 18, 18).addComponent(useRegexCheck).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
    }// </editor-fold>//GEN-END:initComponents

    private void loadBtn1ActionPerformed(java.awt.event.ActionEvent evt)
    {//GEN-FIRST:event_loadBtn1ActionPerformed

        try
        {
            File file = FileChooserUtils.selectFile("Select the file containing values", Settings.getDefault().getLastFilterPath(), FileChooserUtils.MODE_OPEN);

            if (file == null)
            {
                return;
            }

            Settings.getDefault().setLastFilterPath(file.getParent());

            patterns1.setText(readNamesFromFile(file));
        } catch (IOException ex)
        {
            ExceptionDialog edlg = new ExceptionDialog(AppFrame.get(), ex);
            edlg.setVisible(true);
        }
    }//GEN-LAST:event_loadBtn1ActionPerformed


    private void saveBtn1ActionPerformed(java.awt.event.ActionEvent evt)
    {//GEN-FIRST:event_saveBtn1ActionPerformed
        try
        {
            File file = FileChooserUtils.selectFile("Select file name ...", Settings.getDefault().getLastFilterPath(), FileChooserUtils.MODE_SAVE);

            if (file == null)
            {
                return;
            }

            Settings.getDefault().setLastFilterPath(file.getParent());

            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.append(patterns1.getText()).append('\n');
            bw.close();
        } catch (Exception ex)
        {
            ExceptionDialog edlg = new ExceptionDialog(AppFrame.get(), ex);
            edlg.setVisible(true);
        }
    }//GEN-LAST:event_saveBtn1ActionPerformed

    private void pasteSelected1ActionPerformed(java.awt.event.ActionEvent evt)
    {//GEN-FIRST:event_pasteSelected1ActionPerformed
        ArrayList<String> selectedColsLabels = getSelectedColumns();
        setValues(selectedColsLabels, patterns1);
    }//GEN-LAST:event_pasteSelected1ActionPerformed

    private void pasteUnselected1ActionPerformed(java.awt.event.ActionEvent evt)
    {//GEN-FIRST:event_pasteUnselected1ActionPerformed
        ArrayList<String> unselectedColsLabels = getUnselectedColumns();
        setValues(unselectedColsLabels, patterns1);
    }//GEN-LAST:event_pasteUnselected1ActionPerformed

    private void loadBtn2ActionPerformed(java.awt.event.ActionEvent evt)
    {//GEN-FIRST:event_loadBtn2ActionPerformed

        try
        {
            File file = FileChooserUtils.selectFile("Select the file containing values", Settings.getDefault().getLastFilterPath(), FileChooserUtils.MODE_OPEN);

            if (file == null)
            {
                return;
            }

            Settings.getDefault().setLastFilterPath(file.getParent());

            patterns2.setText(readNamesFromFile(file));
        } catch (IOException ex)
        {
            ExceptionDialog edlg = new ExceptionDialog(AppFrame.get(), ex);
            edlg.setVisible(true);
        }
    }//GEN-LAST:event_loadBtn2ActionPerformed

    private void pasteUnselected2ActionPerformed(java.awt.event.ActionEvent evt)
    {//GEN-FIRST:event_pasteUnselected2ActionPerformed
        ArrayList<String> unselectedColsLabels = getUnselectedColumns();
        setValues(unselectedColsLabels, patterns2);
    }//GEN-LAST:event_pasteUnselected2ActionPerformed

    private void pasteSelected2ActionPerformed(java.awt.event.ActionEvent evt)
    {//GEN-FIRST:event_pasteSelected2ActionPerformed
        ArrayList<String> selectedColsLabels = getSelectedColumns();
        setValues(selectedColsLabels, patterns2);
    }//GEN-LAST:event_pasteSelected2ActionPerformed

    private void saveBtn2ActionPerformed(java.awt.event.ActionEvent evt)
    {//GEN-FIRST:event_saveBtn2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_saveBtn2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup applyGroup;
    private javax.swing.JButton colsPattBtn;
    private javax.swing.JTextField colsPattFld;
    private javax.swing.JRadioButton colsRb;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton loadBtn1;
    private javax.swing.JButton loadBtn2;
    private javax.swing.JButton pasteSelected1;
    private javax.swing.JButton pasteSelected2;
    private javax.swing.JButton pasteUnselected1;
    private javax.swing.JButton pasteUnselected2;
    private javax.swing.JTextArea patterns1;
    private javax.swing.JTextArea patterns2;
    private javax.swing.JButton saveBtn1;
    private javax.swing.JButton saveBtn2;
    private javax.swing.JCheckBox useRegexCheck;
    // End of variables declaration//GEN-END:variables


}
