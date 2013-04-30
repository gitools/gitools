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
package org.gitools.ui.platform.progress;

import org.gitools.ui.platform.help.GitoolsTips;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

class JobProgressDialog extends javax.swing.JDialog {

    public void setTipsLabel(JLabel tipsLabel) {
        this.tipsLabel = tipsLabel;
    }

    public interface CancelListener {
        void cancelled();
    }

    @NotNull
    private final List<CancelListener> listeners = new ArrayList<CancelListener>();

    /**
     * Creates new form ProgressDialog
     */
    public JobProgressDialog(Window parent, boolean modal, boolean showGitoolsTips) {
        super(parent);
        setModal(modal);

        initComponents();

        msgLabel.setText("");
        infoLabel.setText("");

        if (showGitoolsTips) {
            tips = new GitoolsTips();
            setTimerLoop();
        }

        progressBar.setMinimum(0);
        progressBar.setIndeterminate(true);

    }

    private void setTimerLoop() {
        timer = new Timer();
        timer.schedule(new ShowNewTip(), 3000, //initial delay
                1 * 10000); //subsequent rate
    }

    class ShowNewTip extends TimerTask {
        public void run() {
            if (infoLabel.getText().equals("")) {
                tipsLabel.setText(tips.getRandomTip());
            }
        }
    }


    public void addCancelListener(CancelListener listener) {
        listeners.add(listener);
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

        msgLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        cancelBtn = new javax.swing.JButton();
        infoLabel = new javax.swing.JLabel();
        tipsLabel = new JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setLocationByPlatform(true);

        msgLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        msgLabel.setText("Working...");

        cancelBtn.setText("Cancel");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        infoLabel.setText("info");
        tipsLabel.setText("tips");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).
                addGroup(layout.createSequentialGroup().addContainerGap().
                        addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).
                                addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE).
                                addComponent(msgLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 551, javax.swing.GroupLayout.PREFERRED_SIZE).
                                addComponent(infoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 551, javax.swing.GroupLayout.PREFERRED_SIZE).
                                addComponent(tipsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 551, javax.swing.GroupLayout.PREFERRED_SIZE).
                                addComponent(cancelBtn, javax.swing.GroupLayout.Alignment.TRAILING)).addContainerGap()));

        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).
                addGroup(
                        layout.createSequentialGroup().addContainerGap()
                                .addComponent(msgLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(tipsLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cancelBtn).addContainerGap()));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        for (CancelListener listener : listeners)
            listener.cancelled();
    }//GEN-LAST:event_cancelBtnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelBtn;
    private javax.swing.JLabel infoLabel;
    private javax.swing.JLabel msgLabel;
    private javax.swing.JLabel tipsLabel;
    private GitoolsTips tips;
    Timer timer;
    private javax.swing.JProgressBar progressBar;
    // End of variables declaration//GEN-END:variables

    public void setMessage(String msg) {
        msgLabel.setText(msg);
        infoLabel.setText("");
    }

    public void setMessage(String msg, String info) {
        msgLabel.setText(msg);
        infoLabel.setText(info);
    }

    public void setInfo(String info) {
        tipsLabel.setText("");
        infoLabel.setText(info);
    }

    public void setWork(int work) {
        progressBar.setMaximum(work);
    }

    public void setProgress(int progress) {
        if (progress == 0) {
            progressBar.setIndeterminate(true);
        } else {
            progressBar.setIndeterminate(false);
            progressBar.setValue(progress);
        }
    }
}
