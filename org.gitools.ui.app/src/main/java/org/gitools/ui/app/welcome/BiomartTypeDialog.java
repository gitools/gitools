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
package org.gitools.ui.app.welcome;

class BiomartTypeDialog extends javax.swing.JDialog {
    /**
     * A return status code - returned if Cancel button has been pressed
     */
    private static final int RET_CANCEL = 0;
    /**
     * A return status code - returned if OK button has been pressed
     */
    private static final int RET_OK = 1;

    public static final int TABLE = 1;
    public static final int MODULES = 2;

    /**
     * Creates new form BiomartTypeDialog
     */
    public BiomartTypeDialog(java.awt.Frame parent) {
        super(parent);
        setModal(true);

        initComponents();

        okButton.requestFocusInWindow();
    }

    /**
     * @return the return status of this dialog - one of RET_OK or RET_CANCEL
     */
    public boolean isCancelled() {
        return returnStatus == RET_CANCEL;
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

        typeGroup = new javax.swing.ButtonGroup();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tableCb = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        modulesCb = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();

        setTitle("Import Biomart data");
        setLocationByPlatform(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        okButton.setText("Next >");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.setDefaultCapable(false);
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/logos/BiomartImport.png"))); // NOI18N

        jLabel2.setText("What kind of data do you want to import ?");

        typeGroup.add(tableCb);
        tableCb.setSelected(true);
        tableCb.setText("Table");

        jLabel3.setText("Select this option to download Biomart datasets.");

        typeGroup.add(modulesCb);
        modulesCb.setText("Modules");

        jLabel4.setText("Select this option to download modules (gene sets).");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 253, Short.MAX_VALUE).addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cancelButton).addContainerGap()).addGroup(layout.createSequentialGroup().addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(tableCb).addComponent(jLabel2).addGroup(layout.createSequentialGroup().addGap(12, 12, 12).addComponent(jLabel3)).addComponent(modulesCb).addGroup(layout.createSequentialGroup().addGap(12, 12, 12).addComponent(jLabel4))).addGap(43, 43, 43)))));

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[]{cancelButton, okButton});

        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jLabel2).addGap(18, 18, 18).addComponent(tableCb).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel3).addGap(18, 18, 18).addComponent(modulesCb).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel4)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(cancelButton).addComponent(okButton)).addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        doClose(RET_OK);
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JRadioButton modulesCb;
    private javax.swing.JButton okButton;
    private javax.swing.JRadioButton tableCb;
    private javax.swing.ButtonGroup typeGroup;
    // End of variables declaration//GEN-END:variables

    private int returnStatus = RET_CANCEL;

    public int getSelection() {
        if (tableCb.isSelected()) {
            return TABLE;
        } else if (modulesCb.isSelected()) {
            return MODULES;
        }
        return 0;
    }
}