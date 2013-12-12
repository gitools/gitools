/*
 * #%L
 * gitools-ui-platform
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
package org.gitools.ui.platform.dialog;

import javax.swing.*;
import java.awt.*;

public class FontChooserDialog extends javax.swing.JDialog {

    private static final int[] STYLES = new int[]{Font.PLAIN, Font.BOLD, Font.ITALIC, Font.BOLD | Font.ITALIC};

    private static final String[] STYLE_NAMES = new String[]{"Normal", "Bold", "Italic", "Bold Italic"};

    private static final String[] LOGICAL_FONT_FAMILIES = new String[]{"Serif", "SansSerif", "Monospaced", "Dialog", "DialogInput"};

    private boolean cancelled = true;

    public FontChooserDialog(java.awt.Frame parent, Font font, boolean useSystemFonts) {
        super(parent, true);

        if (font == null) {
            font = UIManager.getDefaults().getFont("TabbedPane.font");
        }

        initComponents();

        getRootPane().setDefaultButton(acceptBtn);

        if (useSystemFonts) {
            GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
            fontFamily.setListData(gEnv.getAvailableFontFamilyNames());
        } else {
            fontFamily.setListData(LOGICAL_FONT_FAMILIES);
        }

        fontFamily.setSelectedValue(font.getFamily(), true);

        fontStyle.setModel(new DefaultComboBoxModel(STYLE_NAMES));
        for (int i = 0; i < STYLES.length; i++)
            if (STYLES[i] == font.getStyle()) {
                fontStyle.setSelectedIndex(i);
            }

        fontSize.setValue(font.getSize());
    }

    private void updatePreview() {
        Font font = getFont();
        if (font != null) {
            preview.setFont(font);
        }
    }


    public Font getFont() {
        try {
            String name = (String) fontFamily.getSelectedValue();
            int style = STYLES[fontStyle.getSelectedIndex()];
            int size = (Integer) fontSize.getValue();

            return new Font(name, style, size);
        } catch (Exception ex) {
            return null;
        }
    }

    public boolean isCancelled() {
        return cancelled;
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

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        fontFamily = new javax.swing.JList();
        jLabel2 = new javax.swing.JLabel();
        fontStyle = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        fontSize = new javax.swing.JSpinner();
        preview = new javax.swing.JLabel();
        cancelBtn = new javax.swing.JButton();
        acceptBtn = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Choose font...");
        setLocationByPlatform(true);

        jLabel1.setText("Name");

        fontFamily.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        fontFamily.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                fontFamilyValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(fontFamily);

        jLabel2.setText("Style");

        fontStyle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fontStyleActionPerformed(evt);
            }
        });

        jLabel3.setText("Size");

        fontSize.setModel(new javax.swing.SpinnerNumberModel());
        fontSize.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                fontSizeStateChanged(evt);
            }
        });

        preview.setBackground(java.awt.Color.white);
        preview.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        preview.setText("ABCDEFGH ijklmnopq");
        preview.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        preview.setFocusable(false);
        preview.setOpaque(true);

        cancelBtn.setText("Cancel");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        acceptBtn.setText("Accept");
        acceptBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptBtnActionPerformed(evt);
            }
        });

        jLabel5.setText("Preview");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE).addComponent(jLabel1).addGroup(layout.createSequentialGroup().addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(fontStyle, 0, 210, Short.MAX_VALUE).addGap(18, 18, 18).addComponent(jLabel3).addGap(18, 18, 18).addComponent(fontSize, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(acceptBtn).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cancelBtn)).addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE).addComponent(preview, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE).addComponent(jLabel5).addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(fontStyle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel3).addComponent(fontSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel5).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(preview, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(cancelBtn).addComponent(acceptBtn)).addContainerGap()));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fontFamilyValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_fontFamilyValueChanged
        updatePreview();
    }//GEN-LAST:event_fontFamilyValueChanged

    private void fontStyleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fontStyleActionPerformed
        updatePreview();
    }//GEN-LAST:event_fontStyleActionPerformed

    private void fontSizeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_fontSizeStateChanged
        updatePreview();
    }//GEN-LAST:event_fontSizeStateChanged

    private void acceptBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptBtnActionPerformed
        cancelled = false;
        setVisible(false);
    }//GEN-LAST:event_acceptBtnActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        cancelled = true;
        setVisible(false);
    }//GEN-LAST:event_cancelBtnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton acceptBtn;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JList fontFamily;
    private javax.swing.JSpinner fontSize;
    private javax.swing.JComboBox fontStyle;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel preview;
    // End of variables declaration//GEN-END:variables

}
