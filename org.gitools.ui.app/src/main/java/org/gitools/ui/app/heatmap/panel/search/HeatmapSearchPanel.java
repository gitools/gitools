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
package org.gitools.ui.app.heatmap.panel.search;

import com.alee.laf.label.WebLabel;
import com.alee.managers.notification.NotificationIcon;
import com.alee.managers.notification.WebNotificationPopup;
import org.gitools.api.matrix.IAnnotations;
import org.gitools.api.matrix.view.IMatrixView;
import org.gitools.heatmap.Heatmap;
import org.gitools.ui.app.heatmap.panel.HeatmapPanel;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.utils.DocumentChangeListener;
import org.gitools.ui.platform.icons.IconNames;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;


public class HeatmapSearchPanel extends javax.swing.JPanel {


    private static Highlighter.HighlightPainter redHighlightPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);

    private final Heatmap hm;
    private final HeatmapPanel heatmapPanel;


    private Pattern searchPat;

    private final Color defaultSearchTextBgColor;
    private final Color defaultSearchTextFgColor;
    private String foundAnnotation;
    private String foundValue;
    private WebNotificationPopup notificationPopup;

    public HeatmapSearchPanel(Heatmap hm, HeatmapPanel heatmapPanel) {
        this.hm = hm;
        this.heatmapPanel = heatmapPanel;
        this.setBackground(new Color(255, 253, 247));

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
        searchText.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    close();
                }
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

    public void close() {
        if (searchRows()) {
            HeatmapSearchPanel.this.hm.getRows().clearHighlightedLabels();
        } else {
            HeatmapSearchPanel.this.hm.getColumns().clearHighlightedLabels();
        }
        setVisible(false);
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

    private boolean checkMatch(IMatrixView mv, IAnnotations am, String label) {

        if (searchPat == null) {
            return false;
        }

        if (searchPat.matcher(label).find()) {
            foundAnnotation = "ids";
            foundValue = label;
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
                foundAnnotation = annotation;
                foundValue = value;
                break;
            }
        }

        return found;
    }

    private void updateSearch() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
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
            sb.append(")");


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
        Set<String> highlighted = new HashSet<>();

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
            if (found) {
                mv.getRows().setFocus(hm.getRows().getLabel(index));
                showSearchNotification();
                heatmapPanel.makeRowFocusVisible();
            }


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
            if (found) {
                mv.getColumns().setFocus(mv.getColumns().getLabel(index));
                showSearchNotification();
                heatmapPanel.makeColumnFocusVisible();
            }
        }

        setNotFound(!found);

    }

    private void showSearchNotification() {
        String text = "<html>Found <b>"
                + searchPat.matcher(foundValue).replaceAll("<u>$1</u>")
                //+ foundValue.replaceAll(searchPat.toString(), "<u>" + searchPat.toString() + "</u>")
                + "</b> in <i>" + foundAnnotation + "</i></html>";

        if (notificationPopup == null) {
            notificationPopup = new WebNotificationPopup();
            notificationPopup.setIcon(NotificationIcon.text);
        }

        notificationPopup.setContent(new WebLabel(text));
        Application.get().showNotificationPopup(notificationPopup);
        searchText.requestFocus();
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

            if (found) {
                mv.getRows().setFocus(mv.getRows().getLabel(index));
                showSearchNotification();
                heatmapPanel.makeRowFocusVisible();
            }

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

            if (found) {
                mv.getColumns().setFocus(mv.getColumns().getLabel(index));
                showSearchNotification();
                heatmapPanel.makeColumnFocusVisible();
            }


        }

        setNotFound(!found);
    }

    public boolean searchRows() {
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
        searchText = new javax.swing.JTextField(20);
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

        closeBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource(IconNames.close16))); // NOI18N
        closeBtn.setFocusable(false);
        closeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeBtnActionPerformed(evt);
            }
        });

        prevBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource(IconNames.nextEventLeft16))); // NOI18N
        prevBtn.setText("Previous");
        prevBtn.setFocusable(false);

        nextBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource(IconNames.nextEventRight16))); // NOI18N
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

        FlowLayout layout = new FlowLayout(FlowLayout.RIGHT, 10, 10);
        this.setLayout(layout);

        this.add(anyWordChk);
        this.add(highlightAllChk);
        this.add(matchCaseChk);

        this.add(new JSeparator(JSeparator.VERTICAL));

        this.add(rowsButton);
        this.add(columnsButton);

        this.add(prevBtn);
        this.add(nextBtn);

        this.add(searchText);

        this.add(closeBtn);


        /*javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(closeBtn).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(searchText, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(prevBtn).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(nextBtn).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(textNotFoundLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(rowsButton).addGap(8, 8, 8).addComponent(columnsButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(highlightAllChk).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(matchCaseChk).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(anyWordChk).addContainerGap(16, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(closeBtn).addComponent(searchText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(prevBtn).addComponent(nextBtn).addComponent(highlightAllChk).addComponent(textNotFoundLabel).addComponent(matchCaseChk).addComponent(anyWordChk).addComponent(rowsButton).addComponent(columnsButton)));*/
    }// </editor-fold>//GEN-END:initComponents

    private void closeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeBtnActionPerformed
        close();
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
