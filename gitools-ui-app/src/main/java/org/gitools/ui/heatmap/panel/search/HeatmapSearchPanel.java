/*
 *  Copyright 2011 Universitat Pompeu Fabra.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

/*
 * HetamapSearchPanel.java
 *
 * Created on 23-feb-2011, 9:07:45
 */
package org.gitools.ui.heatmap.panel.search;

import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.ui.utils.DocumentChangeListener;

import javax.swing.event.DocumentEvent;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class HeatmapSearchPanel extends javax.swing.JPanel {

    private static Highlighter.HighlightPainter redHighlightPainter =
            new DefaultHighlighter.DefaultHighlightPainter(Color.RED);

    private Heatmap hm;

    private Pattern searchPat;

    private Color defaultSearchTextBgColor;
    private Color defaultSearchTextFgColor;

    public HeatmapSearchPanel(Heatmap hm) {
        this.hm = hm;

        initComponents();

        defaultSearchTextBgColor = searchText.getBackground();
        defaultSearchTextFgColor = searchText.getForeground();

        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentHidden(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
                searchText.requestFocus();
            }
        });

        searchText.getDocument().addDocumentListener(new DocumentChangeListener() {
            @Override
            protected void update(DocumentEvent e) {
                updateSearch();
            }
        });
        searchText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchNext();
            }
        });

        prevBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchPrev();
            }
        });

        nextBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchNext();
            }
        });

        highlightAllChk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                updateHighlight();
            }
        });

        matchCaseChk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSearch();
            }
        });

        anyWordChk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSearch();
            }
        });

        rowsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSearch();
            }
        });

        columnsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSearch();
            }
        });

        setNotFound(false);

    }

    private void setNotFound(boolean b) {
        if (b) {
            //searchText.setBackground(Color.RED);
            searchText.setForeground(Color.RED);
            textNotFoundLabel.setVisible(true);
        } else {
            //searchText.setBackground(defaultSearchTextColor);
            searchText.setForeground(defaultSearchTextFgColor);
            textNotFoundLabel.setVisible(false);
        }
    }

    private boolean checkMatch(IMatrixView mv, AnnotationMatrix am, String label) {

        if (searchPat == null) {
            return false;
        }

        if (searchPat.matcher(label).find())
            return true;

        if (am == null)
            return false;

        int annRow = am.getRowIndex(label);
        if (annRow == -1)
            return false;

        boolean found = false;
        for (int i = 0; i < am.getColumnCount() && !found; i++)
            found = searchPat.matcher(am.getCell(annRow, i)).find();

        return found;
    }

    private void updateSearch() {
        StringBuilder sb = new StringBuilder();
        if (!searchText.getText().isEmpty()) {
            if (anyWordChk.isSelected()) {
                String[] tokens = searchText.getText().split(" ");
                if (tokens.length > 0) {
                    sb.append(Pattern.quote(tokens[0].trim()));
                    for (int i = 1; i < tokens.length; i++)
                        sb.append("|").append(Pattern.quote(tokens[i].trim()));
                }
            } else
                sb.append(Pattern.quote(searchText.getText()));

            if (matchCaseChk.isSelected())
                searchPat = Pattern.compile(sb.toString());
            else
                searchPat = Pattern.compile(sb.toString(), Pattern.CASE_INSENSITIVE);
        } else {
            searchPat = null;
        }

        setNotFound(false);

        updateHighlight();
    }

    private void updateHighlight() {

        IMatrixView mv = hm.getMatrixView();
        Set<String> highlighted = new HashSet<String>();

        boolean searchRows = searchRows();
        boolean found = false;

        if (searchRows) {

            // Search rows
            if (highlightAllChk.isSelected()) {
                AnnotationMatrix am = hm.getRowDim().getAnnotations();

                int rowCount = mv.getRowCount();
                for (int index = 0; index < rowCount; index++) {
                    String label = mv.getRowLabel(index);
                    if (checkMatch(mv, am, label))
                        highlighted.add(label);
                }
                hm.getRowDim().setHighlightedLabels(highlighted);
            } else {
                hm.getRowDim().clearHighlightedLabels();
            }

        } else {

            // Column search
            if (highlightAllChk.isSelected()) {
                AnnotationMatrix am = hm.getColumnDim().getAnnotations();

                int colCount = mv.getColumnCount();
                for (int index = 0; index < colCount; index++) {
                    String label = mv.getColumnLabel(index);
                    if (checkMatch(mv, am, label))
                        highlighted.add(label);
                }
                hm.getColumnDim().setHighlightedLabels(highlighted);
            } else {
                hm.getColumnDim().clearHighlightedLabels();
            }
        }
    }

    private void searchPrev() {
        IMatrixView mv = hm.getMatrixView();
        int leadRow = mv.getLeadSelectionRow();
        int leadCol = mv.getLeadSelectionColumn();

        boolean searchRows = searchRows();
        boolean found = false;

        int rowCount = mv.getRowCount();
        if (searchRows && leadRow == -1)
            leadRow = rowCount - 1;

        if (searchRows && leadRow != -1) {

            // Row search
            AnnotationMatrix am = hm.getRowDim().getAnnotations();

            int index = leadRow - 1;
            if (index < 0)
                index = rowCount - 1;
            while (index != leadRow) {
                found = checkMatch(mv, am, mv.getRowLabel(index));
                if (found)
                    break;
                index--;
                if (index < 0)
                    index = rowCount - 1;
            }
            if (index == leadRow) {
                found = checkMatch(mv, am, mv.getRowLabel(index));
            }
            mv.setLeadSelection(index, leadCol);

        } else if (!searchRows && leadCol != -1) {

            // Column search
            AnnotationMatrix am = hm.getColumnDim().getAnnotations();

            int colCount = mv.getColumnCount();
            int index = leadCol - 1;
            if (index < 0)
                index = colCount - 1;
            while (index != leadCol) {
                found = checkMatch(mv, am, mv.getColumnLabel(index));
                if (found)
                    break;
                index--;
                if (index < 0)
                    index = colCount - 1;
            }
            if (index == leadCol)
                found = checkMatch(mv, am, mv.getColumnLabel(index));
            mv.setLeadSelection(leadRow, index);
        }

        setNotFound(!found);
    }

    private void searchNext() {
        IMatrixView mv = hm.getMatrixView();
        int leadRow = mv.getLeadSelectionRow();
        int leadCol = mv.getLeadSelectionColumn();

        boolean searchRows = searchRows();
        boolean found = false;

        if (searchRows && leadRow == -1) {
            leadRow = 0;
        }

        if (!searchRows && leadCol == -1) {
            leadCol = 0;
        }

        if (searchRows) {

            // Row search
            AnnotationMatrix am = hm.getRowDim().getAnnotations();

            int rowCount = mv.getRowCount();
            int index = leadRow + 1;
            if (index >= rowCount)
                index = 0;
            while (index != leadRow) {
                found = checkMatch(mv, am, mv.getRowLabel(index));
                if (found)
                    break;
                index++;
                if (index >= rowCount)
                    index = 0;
            }
            if (index == leadRow)
                found = checkMatch(mv, am, mv.getRowLabel(index));
            mv.setLeadSelection(index, leadCol);
        } else if (!searchRows && leadCol != -1) {

            // Column search
            AnnotationMatrix am = hm.getColumnDim().getAnnotations();

            int colCount = mv.getColumnCount();
            int index = leadCol + 1;
            if (index >= colCount)
                index = 0;
            while (index != leadCol) {
                found = checkMatch(mv, am, mv.getColumnLabel(index));
                if (found)
                    break;
                index++;
                if (index >= colCount)
                    index = 0;
            }
            if (index == leadCol)
                found = checkMatch(mv, am, mv.getColumnLabel(index));
            mv.setLeadSelection(leadRow, index);
        }

        setNotFound(!found);
    }

    private boolean searchRows() {
        return rowsOrColumns.isSelected(rowsButton.getModel());
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rowsOrColumns = new javax.swing.ButtonGroup();
        closeBtn = new javax.swing.JButton();
        searchText = new javax.swing.JTextField();
        prevBtn = new javax.swing.JButton();
        nextBtn = new javax.swing.JButton();
        highlightAllChk = new javax.swing.JCheckBox();
        matchCaseChk = new javax.swing.JCheckBox();
        textNotFoundLabel = new javax.swing.JLabel();
        anyWordChk = new javax.swing.JCheckBox();
        rowsButton = new javax.swing.JRadioButton();
        columnsButton = new javax.swing.JRadioButton();

        setFocusable(false);
        setRequestFocusEnabled(false);

        closeBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/SearchClose.png"))); // NOI18N
        closeBtn.setFocusable(false);
        closeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeBtnActionPerformed(evt);
            }
        });

        prevBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/SearchPrev.png"))); // NOI18N
        prevBtn.setText("Previous");
        prevBtn.setFocusable(false);

        nextBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/SearchNext.png"))); // NOI18N
        nextBtn.setText("Next");
        nextBtn.setFocusable(false);
        nextBtn.setRequestFocusEnabled(false);

        highlightAllChk.setSelected(true);
        highlightAllChk.setText("Highlight all");
        highlightAllChk.setFocusable(false);
        highlightAllChk.setRequestFocusEnabled(false);

        matchCaseChk.setText("Match case");
        matchCaseChk.setFocusable(false);
        matchCaseChk.setRequestFocusEnabled(false);

        textNotFoundLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/SearchNotFound.png"))); // NOI18N
        textNotFoundLabel.setText("Text not found");

        anyWordChk.setText("Any word");
        anyWordChk.setFocusable(false);
        anyWordChk.setRequestFocusEnabled(false);

        rowsOrColumns.add(rowsButton);
        rowsButton.setSelected(true);
        rowsButton.setText("Rows");
        rowsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rowsButtonActionPerformed(evt);
            }
        });

        rowsOrColumns.add(columnsButton);
        columnsButton.setText("Columns");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(closeBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchText, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(prevBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nextBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(textNotFoundLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rowsButton)
                .addGap(8, 8, 8)
                .addComponent(columnsButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(highlightAllChk)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(matchCaseChk)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(anyWordChk)
                .addContainerGap(16, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(closeBtn)
                .addComponent(searchText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(prevBtn)
                .addComponent(nextBtn)
                .addComponent(highlightAllChk)
                .addComponent(textNotFoundLabel)
                .addComponent(matchCaseChk)
                .addComponent(anyWordChk)
                .addComponent(rowsButton)
                .addComponent(columnsButton))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void closeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeBtnActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_closeBtnActionPerformed

    private void rowsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rowsButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rowsButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox anyWordChk;
    private javax.swing.JButton closeBtn;
    private javax.swing.JRadioButton columnsButton;
    private javax.swing.JCheckBox highlightAllChk;
    private javax.swing.JCheckBox matchCaseChk;
    private javax.swing.JButton nextBtn;
    private javax.swing.JButton prevBtn;
    private javax.swing.JRadioButton rowsButton;
    private javax.swing.ButtonGroup rowsOrColumns;
    private javax.swing.JTextField searchText;
    private javax.swing.JLabel textNotFoundLabel;
    // End of variables declaration//GEN-END:variables
}
