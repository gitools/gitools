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
package org.gitools.ui.heatmap.panel.search;

import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.matrix.model.IMatrixView;
import org.gitools.core.matrix.model.IAnnotations;
import org.gitools.ui.utils.DocumentChangeListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    @NotNull
    private static Highlighter.HighlightPainter redHighlightPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);

    private final Heatmap hm;

    @Nullable
    private Pattern searchPat;

    private final Color defaultSearchTextBgColor;
    private final Color defaultSearchTextFgColor;

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

        closeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (searchRows()) {
                    HeatmapSearchPanel.this.hm.getRows().clearHighlightedLabels();
                } else {
                    HeatmapSearchPanel.this.hm.getColumns().clearHighlightedLabels();
                }
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

    private boolean checkMatch(IMatrixView mv, @Nullable IAnnotations am, String label) {

        if (searchPat == null) {
            return false;
        }

        if (searchPat.matcher(label).find()) {
            return true;
        }

        if (am == null) {
            return false;
        }

        if (!am.getIdentifiers().contains(label)) {
            return false;
        }

        boolean found = false;
        for (String annotation : am.getLabels()) {
            String value = am.getAnnotation(label, annotation);
            if (value == null) {
                continue;
            }

            found = searchPat.matcher(value).find();

            if (found) {
                break;
            }
        }

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
            } else {
                sb.append(Pattern.quote(searchText.getText()));
            }

            if (matchCaseChk.isSelected()) {
                searchPat = Pattern.compile(sb.toString());
            } else {
                searchPat = Pattern.compile(sb.toString(), Pattern.CASE_INSENSITIVE);
            }
        } else {
            searchPat = null;
        }

        setNotFound(false);

        updateHighlight();
    }

    private void updateHighlight() {

        IMatrixView mv = hm;
        Set<String> highlighted = new HashSet<String>();

        boolean searchRows = searchRows();
        boolean found = false;

        if (searchRows) {

            hm.getColumns().clearHighlightedLabels();

            // Search rows
            if (highlightAllChk.isSelected()) {
                IAnnotations am = hm.getRows().getAnnotations();

                int rowCount = mv.getRows().size();
                for (int index = 0; index < rowCount; index++) {
                    String label = mv.getRows().getLabel(index);
                    if (checkMatch(mv, am, label)) {
                        highlighted.add(label);
                    }
                }
                hm.getRows().setHighlightedLabels(highlighted);
            } else {
                hm.getRows().clearHighlightedLabels();
            }

        } else {

            hm.getRows().clearHighlightedLabels();

            // Column search
            if (highlightAllChk.isSelected()) {
                IAnnotations am = hm.getColumns().getAnnotations();

                int colCount = mv.getColumns().size();
                for (int index = 0; index < colCount; index++) {
                    String label = mv.getColumns().getLabel(index);
                    if (checkMatch(mv, am, label)) {
                        highlighted.add(label);
                    }
                }
                hm.getColumns().setHighlightedLabels(highlighted);
            } else {
                hm.getColumns().clearHighlightedLabels();
            }
        }
    }

    private void searchPrev() {
        IMatrixView mv = hm;
        int leadRow = mv.getRows().indexOf(mv.getRows().getFocus());
        int leadCol = mv.getColumns().indexOf(mv.getColumns().getFocus());

        boolean searchRows = searchRows();
        boolean found = false;

        int rowCount = mv.getRows().size();
        if (searchRows && leadRow == -1) {
            leadRow = rowCount - 1;
        }

        if (searchRows && leadRow != -1) {

            // Row search
            IAnnotations am = hm.getRows().getAnnotations();

            int index = leadRow - 1;
            if (index < 0) {
                index = rowCount - 1;
            }
            while (index != leadRow) {
                found = checkMatch(mv, am, mv.getRows().getLabel(index));
                if (found) {
                    break;
                }
                index--;
                if (index < 0) {
                    index = rowCount - 1;
                }
            }
            if (index == leadRow) {
                found = checkMatch(mv, am, mv.getRows().getLabel(index));
            }
            mv.getRows().setFocus(hm.getRows().getLabel(index));

        } else if (!searchRows && leadCol != -1) {

            // Column search
            IAnnotations am = hm.getColumns().getAnnotations();

            int colCount = mv.getColumns().size();
            int index = leadCol - 1;
            if (index < 0) {
                index = colCount - 1;
            }
            while (index != leadCol) {
                found = checkMatch(mv, am, mv.getColumns().getLabel(index));
                if (found) {
                    break;
                }
                index--;
                if (index < 0) {
                    index = colCount - 1;
                }
            }
            if (index == leadCol) {
                found = checkMatch(mv, am, mv.getColumns().getLabel(index));
            }
            mv.getColumns().setFocus(mv.getColumns().getLabel(index));
        }

        setNotFound(!found);
    }

    private void searchNext() {
        IMatrixView mv = hm;
        int leadRow = mv.getRows().indexOf(mv.getRows().getFocus());
        int leadCol = mv.getColumns().indexOf(mv.getColumns().getFocus());

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
            IAnnotations am = hm.getRows().getAnnotations();

            int rowCount = mv.getRows().size();
            int index = leadRow + 1;
            if (index >= rowCount) {
                index = 0;
            }
            while (index != leadRow) {
                found = checkMatch(mv, am, mv.getRows().getLabel(index));
                if (found) {
                    break;
                }
                index++;
                if (index >= rowCount) {
                    index = 0;
                }
            }
            if (index == leadRow) {
                found = checkMatch(mv, am, mv.getRows().getLabel(index));
            }
            mv.getRows().setFocus(mv.getRows().getLabel(index));
        } else if (!searchRows && leadCol != -1) {

            // Column search
            IAnnotations am = hm.getColumns().getAnnotations();

            int colCount = mv.getColumns().size();
            int index = leadCol + 1;
            if (index >= colCount) {
                index = 0;
            }
            while (index != leadCol) {
                found = checkMatch(mv, am, mv.getColumns().getLabel(index));
                if (found) {
                    break;
                }
                index++;
                if (index >= colCount) {
                    index = 0;
                }
            }
            if (index == leadCol) {
                found = checkMatch(mv, am, mv.getColumns().getLabel(index));
            }
            mv.getColumns().setFocus(mv.getColumns().getLabel(index));
        }

        setNotFound(!found);
    }

    private boolean searchRows() {
        return rowsOrColumns.isSelected(rowsButton.getModel());
    }

    public void searchOnColumns(boolean searchColumns) {
        rowsOrColumns.clearSelection();
        rowsOrColumns.setSelected(columnsButton.getModel(), searchColumns);
        rowsOrColumns.setSelected(rowsButton.getModel(), !searchColumns);

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
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(closeBtn).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(searchText, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(prevBtn).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(nextBtn).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(textNotFoundLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(rowsButton).addGap(8, 8, 8).addComponent(columnsButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(highlightAllChk).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(matchCaseChk).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(anyWordChk).addContainerGap(16, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(closeBtn).addComponent(searchText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(prevBtn).addComponent(nextBtn).addComponent(highlightAllChk).addComponent(textNotFoundLabel).addComponent(matchCaseChk).addComponent(anyWordChk).addComponent(rowsButton).addComponent(columnsButton)));
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
