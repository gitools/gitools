/*
 *  Copyright 2010 Universitat Pompeu Fabra.
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
 * IntogenImportSaveDialog.java
 *
 * Created on 05-mar-2010, 12:28:41
 */

package org.gitools.ui.settings.decorators;

import java.awt.Component;
import java.util.*;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.ui.platform.dialog.DialogHeaderPanel;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.utils.DocumentChangeListener;

public class LoadDecoratorDialog<T> extends javax.swing.JDialog {

    private static class DecoratorListCellRenderer extends DefaultListCellRenderer {

        public DecoratorListCellRenderer(){
        }

        @Override
        public Component getListCellRendererComponent(JList jlist, Object o, int i, boolean bln, boolean bln1) { 
            
            ElementDecorator d = (ElementDecorator) o;
        
            Component listCellRendererComponent = super.getListCellRendererComponent(jlist, d.getName(), i, bln, bln1);
            
            return listCellRendererComponent;
        }
            
        
    }

    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
    
    private static final String MESSAGE = "Select a scale";
    
    private static IElementAdapter adapter;
    
    private List<T> listObjects = new ArrayList<T>();
    
    private DefaultListModel model;
    
    public LoadDecoratorDialog(java.awt.Window parent,  
                                IElementAdapter adapter,
                                T[] objects) {
        this(parent, adapter, objects, null);
    }
    
    public LoadDecoratorDialog(java.awt.Window parent,
                                IElementAdapter adapter,
                                T[] objects,
                                Class<? extends ElementDecorator> decoratorClass) {
        

           
        super(parent);

        this.adapter = adapter;
        
        setModal(true);
		
        initComponents();
        
        
        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override public void valueChanged(ListSelectionEvent e) {
				okButton.setEnabled(!list.getSelectionModel().isSelectionEmpty());
			}
		});

        
        
		this.model = new DefaultListModel();
        list.setCellRenderer(new DecoratorListCellRenderer());
		list.setModel(model);

        if (decoratorClass != null) {
            for (T o : objects) {
                if (o.getClass().equals(decoratorClass)) {
                    listObjects.add(o);
                }
            }
        } else {
            listObjects.addAll(Arrays.asList(objects));
        }
        
        Collections.sort(listObjects, new Comparator<T>(){
            @Override
            public int compare(T o1, T o2){
                ElementDecorator d1 = (ElementDecorator) o1;
                ElementDecorator d2 = (ElementDecorator) o2;
                return d1.getName().compareToIgnoreCase(d2.getName());
            }
        });
               
        resetList("");

		okButton.setEnabled(false);

		//headerPanel = new DialogHeaderPanel();
		headerPanel.setTitle("Load Scale");
		headerPanel.setMessage(MESSAGE);
		//headerPanel.setLeftLogo(IconUtils.getIconResource(IconNames.LOGO_SAVE));
        	
        okButton.setEnabled(false);

		DocumentChangeListener documentListener = new DocumentChangeListener() {
			@Override protected void update(DocumentEvent e) {
                int elementCount = resetList(filter.getText());
				if (elementCount == 0) {
					headerPanel.setMessageStatus(MessageStatus.WARN);
					headerPanel.setMessage("No scale matches your filter");
				} else {
                    headerPanel.setMessageStatus(MessageStatus.INFO);
					headerPanel.setMessage(MESSAGE);
                }
                /*if (filter.getText().isEmpty())
                    okButton.setEnabled(false);
                else
                    okButton.setEnabled(true);
                    */
                
			}
		};

		filter.getDocument().addDocumentListener(documentListener);
        
    }

	public DialogHeaderPanel getHeaderPanel() {
		return headerPanel;
	}

    public ElementDecorator getSelectedDecorator() {
        ElementDecorator d = (ElementDecorator) list.getSelectedValue();
        d.setAdapter(adapter);
        return d;
    }
    
    private int resetList(String filter) {
        model.clear();
        for (T o : listObjects) {
            if (filter.isEmpty()) {
                model.addElement(o);
            }
            else {
                ElementDecorator d = (ElementDecorator) o;
                String regexFilter = "(?i).*"+filter+".*";
                boolean b = d.getName().matches(regexFilter);
                if (b)
                    model.addElement(o);
            } 
        }
        return model.getSize();
    }
    
	
    /** @return the return status of this dialog - one of RET_OK or RET_CANCEL */
    public int getReturnStatus() {
        return returnStatus;
    }

	public boolean isCancelled() {
		return returnStatus == RET_CANCEL;
	}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        filter = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        headerPanel = new org.gitools.ui.platform.dialog.DialogHeaderPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        list = new javax.swing.JList();

        setTitle("Load Scale");
        setLocationByPlatform(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Filter");

        headerPanel.setMessage("");

        list.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(list);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 742, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(filter)
                            .addComponent(jScrollPane1))
                        .addGap(82, 82, 82))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cancelButton)))
                        .addContainerGap())))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, okButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(filter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(okButton))
                .addGap(5, 5, 5))
        );

        getAccessibleContext().setAccessibleName("Load Decorator");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        doClose(RET_OK);
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    /** Closes the dialog */
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
    private javax.swing.JTextField filter;
    private org.gitools.ui.platform.dialog.DialogHeaderPanel headerPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JList list;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables

    private int returnStatus = RET_CANCEL;

	private String getFilter() {
		return filter.getText();
	}
}
