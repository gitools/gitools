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

import org.apache.commons.lang.exception.ExceptionUtils;
import org.gitools.ui.platform.application.IApplicationTracking;
import org.gitools.ui.platform.progress.GitoolsGlassPane;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionGlassPane extends GitoolsGlassPane {

    private final Throwable cause;

    public static void show(Window parent, Throwable cause) {
        ExceptionGlassPane dlg = new ExceptionGlassPane(parent, cause);
        dlg.setVisible(true);
    }

    public ExceptionGlassPane(Window parent, Throwable cause) {
        super(parent);
        setShield(1.0f);

        if (parent instanceof RootPaneContainer) {
            ((RootPaneContainer) parent).setGlassPane(this);
        }

        initComponents();

        getRootPane().setDefaultButton(closeButton);


        descriptionArea.setFont(descriptionArea.getFont().deriveFont(Font.BOLD, 18));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setForeground(Color.RED);
        descriptionArea.setBorder(null);
        jScrollPane1.setBorder(null);
        jLabel1.setFont(jLabel1.getFont().deriveFont(Font.BOLD, 20));
        jLabel2.setFont(jLabel2.getFont().deriveFont(Font.BOLD));

        this.cause = cause;
        String errorMessage = getFriendlyMessage(cause);
        descriptionArea.setText(errorMessage);


        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        cause.printStackTrace(pw);
        pw.close();
        traceArea.setText(sw.toString());

        if (parent instanceof IApplicationTracking) {
            IApplicationTracking track = (IApplicationTracking) parent;

            String description;
            Throwable rootCause = ExceptionUtils.getRootCause(cause);
            if (rootCause != null) {
                description = ExceptionUtils.getStackTrace(rootCause);
            } else {
                description = ExceptionUtils.getStackTrace(cause);
            }

            if (description.length() > 150) {
                description = description.substring(0, 145);
            }

            track.trackException(anonymizer(description));
        }

    }

    private String anonymizer(String description) {

        //replace custom strings between ' '
        description = description.replaceAll("('[^']*')", "*");

        //replace folders and urls
        description = description.replaceAll("/[^ ]+", "*");

        //replace java package name
        description = description.replaceAll("at [a-z\\.A-Z0-9]*\\(", "at (");
        description = description.replaceAll("^[a-z\\.]*", "");

        return description;
    }

    private String getFriendlyMessage(Throwable cause) {
        String message = cause.getLocalizedMessage() == null ? "<empty error message>" : cause.getLocalizedMessage();
        if (message.contains("heap space")) {
            message = message + ": Not enough memory for this operation.";
        }
        return message;
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        closeButton = new javax.swing.JButton();
        copyButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        descriptionArea = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        traceArea = new javax.swing.JTextArea();
        jSeparator1 = new javax.swing.JSeparator();

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        copyButton.setText("Copy");
        copyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Error");

        jLabel2.setText("Trace (for developers)");

        descriptionArea.setColumns(20);
        descriptionArea.setEditable(false);
        descriptionArea.setRows(2);
        jScrollPane1.setViewportView(descriptionArea);

        traceArea.setColumns(20);
        traceArea.setRows(5);
        jScrollPane2.setViewportView(traceArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 451, Short.MAX_VALUE).addGap(180, 180, 180)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 619, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addComponent(copyButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 521, Short.MAX_VALUE).addComponent(closeButton)).addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 619, Short.MAX_VALUE)).addContainerGap()).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 619, Short.MAX_VALUE).addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 619, Short.MAX_VALUE)).addContainerGap()))));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(copyButton).addComponent(closeButton)).addContainerGap()));

        //pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        doClose();
    }//GEN-LAST:event_closeButtonActionPerformed

    private void copyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyButtonActionPerformed
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println(cause.getMessage());
        cause.printStackTrace(pw);
        pw.close();

        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection ss = new StringSelection(sw.toString());
        cb.setContents(ss, ss);
    }//GEN-LAST:event_copyButtonActionPerformed

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose();
    }

    private void doClose() {
        setVisible(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    private javax.swing.JButton copyButton;
    private javax.swing.JTextArea descriptionArea;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextArea traceArea;

    public Container getContentPane() {
        return this;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
