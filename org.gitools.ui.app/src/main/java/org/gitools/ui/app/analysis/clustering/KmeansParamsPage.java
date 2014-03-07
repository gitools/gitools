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
package org.gitools.ui.app.analysis.clustering;

import org.gitools.analysis.clustering.distance.EuclideanDistance;
import org.gitools.analysis.clustering.distance.ManhattanDistance;
import org.gitools.analysis.clustering.kmeans.KmeansPlusPlusMethod;
import org.gitools.ui.platform.wizard.AbstractWizardPage;

public class KmeansParamsPage extends AbstractWizardPage {

    private KmeansPlusPlusMethod method;

    public KmeansParamsPage(KmeansPlusPlusMethod method) {
        super();
        this.method = method;
        initComponents();

        setTitle("Clustering method selection");
        setComplete(true);
    }

    private boolean validated() {
        return (isValidInteger(iterField.getText()) && isValidInteger(seedField.getText()) &&
                isValidInteger(kField.getText()));
    }

    @Override
    public void updateModel() {

        if (validated()) {

            method.setIterations(Integer.valueOf(iterField.getText()));
            method.setNumClusters(Integer.valueOf(kField.getText()));
            method.setSeed(Integer.valueOf(seedField.getText()));

            if (distAlgCombo.getSelectedItem().toString().equalsIgnoreCase("euclidean")) {
                method.setDistanceFunction(new EuclideanDistance());
            } else {
                method.setDistanceFunction(new ManhattanDistance());
            }
        }

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

        optGroup = new javax.swing.ButtonGroup();
        jLabel3 = new javax.swing.JLabel();
        seedField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        distAlgCombo = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        iterField = new javax.swing.JTextField();
        kField = new javax.swing.JTextField();

        jLabel3.setText("Seed: ");
        jLabel3.setToolTipText("Set a random value used to start clustering method");

        seedField.setText("10");

        jLabel7.setText("Distance algorithm: ");
        jLabel7.setToolTipText("The distance function to use for instances comparison");

        distAlgCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Euclidean", "Manhattan"}));

        jLabel1.setText("Num. Clusters: ");
        jLabel1.setToolTipText("Set number of clusters");

        jLabel2.setText("Max Iterations: ");
        jLabel2.setToolTipText("Set maximum number of iterations\nValue must be > 0");

        iterField.setText("500");

        kField.setText("2");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel7).addComponent(jLabel1).addComponent(jLabel2).addComponent(jLabel3)).addGap(12, 12, 12).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(distAlgCombo, 0, 290, Short.MAX_VALUE).addComponent(seedField, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE).addComponent(iterField, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE).addComponent(kField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel7).addComponent(distAlgCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(kField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(iterField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel2)).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(seedField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel3)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox distAlgCombo;
    private javax.swing.JTextField iterField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JTextField kField;
    private javax.swing.ButtonGroup optGroup;
    private javax.swing.JTextField seedField;
    // End of variables declaration//GEN-END:variables

    private boolean isValidInteger(String text) {
        try {
            Integer.parseInt(text);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

}
