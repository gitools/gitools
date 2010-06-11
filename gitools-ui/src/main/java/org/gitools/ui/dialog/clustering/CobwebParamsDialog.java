/*
 *  Copyright 2010 xrafael.
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
 * CobwebParamsDialog2.java
 *
 * Created on May 4, 2010, 3:01:12 PM
 */

package org.gitools.ui.dialog.clustering;


import java.util.Properties;
import org.gitools.ui.platform.dialog.MessageStatus;

public class CobwebParamsDialog extends javax.swing.JDialog {

	/** A return status code - returned if Cancel button has been pressed */
	public static final int RET_CANCEL = 0;
	/** A return status code - returned if OK button has been pressed */
	public static final int RET_OK = 1;
	private int returnStatus = RET_CANCEL;


	private ClusteringDialog parent;


    /** Creates new form cobwebParamsPanel */
    public CobwebParamsDialog(ClusteringDialog parent, Properties values) {

		super(parent);
		setModal(true);

        initComponents();

		headerCompo.setTitle("Herarchical Clustering parameters");
		headerCompo.setMessage("");
		this.parent = parent;

		validate();


		if (values != null) setValuesParameters(values);

    }

	private void setValuesParameters(Properties values){

		cutOffField.setText(values.getProperty("cutoff"));
		seedField.setText(values.getProperty("seedCobweb"));
		acuityField.setText(values.getProperty("acuity"));
		
	}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        seedField = new javax.swing.JTextField();
        acuityField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        cutOffField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        headerCompo = new org.gitools.ui.platform.dialog.DialogHeaderPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Parameters");
        setLocationByPlatform(true);

        cancelButton.setText("Cancel");
        cancelButton.setDefaultCapable(false);
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        seedField.setText("42");

        acuityField.setText("1.0");

        jLabel2.setText("Cutoff: ");
        jLabel2.setToolTipText("Set the category utility threshold by which to prune nodes.\nRecomendation: Decrement this value in case to increase \nthe number of clusters");

        cutOffField.setText("0.0028");

        jLabel3.setText("Seed: ");
        jLabel3.setToolTipText("Set a random value used to start clustering method");

        jLabel1.setText("Acuity: ");
        jLabel1.setToolTipText("Set the minimum standard deviation for numeric attributes.\nHence, this parameter represents the error measure of a node\nof a tree with an only instance. This means that value sets the \nminimum variance of an attribute.\n\nRecomendation: Increase the value if we want a wide tree or\nnodes with great number of instances");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerCompo, javax.swing.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jSeparator1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(seedField)
                            .addComponent(cutOffField)
                            .addComponent(acuityField, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGap(12, 12, 12))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(296, Short.MAX_VALUE)
                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelButton)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerCompo, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(acuityField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cutOffField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(seedField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton)
                    .addComponent(cancelButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
		doClose(RET_CANCEL);
}//GEN-LAST:event_cancelButtonActionPerformed

	private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
		Properties params =  new Properties();

		if (isValidNumber(cutOffField.getText()) && cutOffField.getText() != null && !cutOffField.getText().equals("")){			
			if (isValidInteger(seedField.getText()) && seedField.getText() != null && !seedField.getText().equals("")){				
				if (isValidNumber(acuityField.getText()) && acuityField.getText() != null && !acuityField.getText().equals(""))	{

					params.put("cutoff",cutOffField.getText());
					params.put("seedCobweb",seedField.getText());
					params.put("acuity",acuityField.getText());

					parent.setParams(params);

					doClose(RET_OK);					
				}else{

				headerCompo.setMessageStatus(MessageStatus.ERROR);
				headerCompo.setMessage("Invalid acuity value, it should be a real number");}
			}else{

				headerCompo.setMessageStatus(MessageStatus.ERROR);
				headerCompo.setMessage("Invalid seed value, it should be an integer number");}
		}else{
			
			headerCompo.setMessageStatus(MessageStatus.ERROR);
			headerCompo.setMessage("Invalid cutoff value, it should be a real number");
		}

}//GEN-LAST:event_okButtonActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField acuityField;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField cutOffField;
    private org.gitools.ui.platform.dialog.DialogHeaderPanel headerCompo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton okButton;
    private javax.swing.JTextField seedField;
    // End of variables declaration//GEN-END:variables


	private void doClose(int retStatus) {
		returnStatus = retStatus;
		setVisible(false);
		dispose();
	}

	private boolean isValidNumber(String text) {
		try {
			Double.parseDouble(text);
		}
		catch (NumberFormatException ex) {
			return false;
		}
		return true;
	}

	private boolean isValidInteger(String text) {
		try {
			Integer.parseInt(text);
		} catch (NumberFormatException ex) {
			return false;
		}
		return true;
	}

	public int getReturnStatus() {
		return returnStatus;
	}

}