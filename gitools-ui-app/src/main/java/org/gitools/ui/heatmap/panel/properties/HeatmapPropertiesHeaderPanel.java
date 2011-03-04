/*
 *  Copyright 2009 Universitat Pompeu Fabra.
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
 * HeatmapPropertiesHeaderPanel.java
 *
 * Created on 12-dic-2009, 17:40:35
 */

package org.gitools.ui.heatmap.panel.properties;

import org.gitools.ui.heatmap.header.LabelsHeaderPage;
import org.gitools.ui.heatmap.header.AddHeaderPage;
import edu.upf.bg.progressmonitor.NullProgressMonitor;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.heatmap.model.HeatmapColoredClustersHeader;
import org.gitools.heatmap.model.HeatmapDim;
import org.gitools.heatmap.model.HeatmapHeader;
import org.gitools.heatmap.model.HeatmapLabelsHeader;
import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.persistence.MimeTypes;
import org.gitools.persistence.PersistenceManager;
import org.gitools.ui.clustering.annotations.ColoredClustersHeaderWizard;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.component.ColorChooserLabel.ColorChangeListener;
import org.gitools.ui.platform.wizard.PageDialog;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.utils.LogUtils;
import org.slf4j.LoggerFactory;

public class HeatmapPropertiesHeaderPanel extends HeatmapPropertiesAbstractPanel {

	private boolean rowMode;

	private HeatmapDim hdim;

	//private boolean updatingControls = false;

	private boolean updatingModel = false;

    /** Creates new form HeatmapPropertiesHeaderPanel */
    public HeatmapPropertiesHeaderPanel(boolean rowMode) {
		this.rowMode = rowMode;

        initComponents();

		headerList.setModel(new DefaultListModel());
		headerList.addListSelectionListener(new ListSelectionListener() {
			@Override public void valueChanged(ListSelectionEvent e) {
				updateHeaderSelection(); }
		});

		headerSize.addChangeListener(new ChangeListener() {
			@Override public void stateChanged(ChangeEvent e) {
				headerSizeChanged(); }
		});

		headerVisible.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				headerVisibleChanged(); }
		});

		headerBgColor.addColorChangeListener(new ColorChangeListener() {
			@Override public void colorChanged(Color color) {
				headerBgColorChanged();
			}
		});
    }

	@Override
	public void setHeatmap(Heatmap heatmap) {
		this.hdim = rowMode ?
			heatmap.getRowDim() : heatmap.getColumnDim();
		
		super.setHeatmap(heatmap);
	}

	@Override
	protected void initControls() {
	}

	@Override
	protected void updateControls() {
		updatingControls = true;

		updateAnnotations();

		updateHeaders();

		updateHeaderSelection();

		updatingControls = false;
	}

	private void updateAnnotations() {
		AnnotationMatrix annMatrix = hdim.getAnnotations();
		
		if (annMatrix != null) {
			//FIXME what if cache is disabled ?
			/*FileRef fileRef = PersistenceManager.getDefault().getEntityFileRef(annMatrix);
			File file = fileRef != null ? fileRef.getFile() : null;
			if (file != null)
				annFile.setText(file.getName());*/

			setAnnotationControlsEnabled(true);
		}
		else {
			annFile.setText("");
			setAnnotationControlsEnabled(false);
		}
	}

	private void updateHeaders() {
		int index = headerList.getSelectedIndex();
		DefaultListModel m = (DefaultListModel) headerList.getModel();
		m.clear();
		for (HeatmapHeader h : hdim.getHeaders())
			m.addElement(h.getTitle());
		headerList.setModel(m);
		if (index >= 0)
			headerList.setSelectedIndex(index);
	}

	private void updateHeaderSelection() {
		int index = headerList.getSelectedIndex();
		boolean sel = index >= 0;
		headerSize.setEnabled(sel);
		headerVisible.setEnabled(sel);
		headerBgColor.setEnabled(sel);
		headerEditBtn.setEnabled(sel);
		headerRemoveBtn.setEnabled(sel);
		headerUpBtn.setEnabled(sel && index > 0);
		headerDownBtn.setEnabled(sel && index < headerList.getModel().getSize() - 1);
		if (sel) {
			HeatmapHeader h = hdim.getHeaders().get(index);
			headerSize.setValue(h.getSize());
			headerVisible.setSelected(h.isVisible());
			headerBgColor.setColor(h.getBackgroundColor());
		}
	}

	private void headerSizeChanged() {
		int index = headerList.getSelectedIndex();
		if (index == -1)
			return;

		SpinnerNumberModel m = (SpinnerNumberModel) headerSize.getModel();
		hdim.getHeaders().get(index).setSize(m.getNumber().intValue());
	}

	private void headerVisibleChanged() {
		int index = headerList.getSelectedIndex();
		if (index == -1)
			return;

		hdim.getHeaders().get(index).setVisible(headerVisible.isSelected());
	}

	private void headerBgColorChanged() {
		int index = headerList.getSelectedIndex();
		if (index == -1)
			return;

		Color color = headerBgColor.getColor();
		hdim.getHeaders().get(index).setBackgroundColor(color);
	}

	private void setAnnotationControlsEnabled(boolean enabled) {
		JComponent[] components = new JComponent[] {
			annClear, annFilter,
			/*labelPattern, linkName, linkPattern*/
		};

		for (JComponent c : components)
			c.setEnabled(enabled);
	}

	@Override
	protected void heatmapPropertyChange(PropertyChangeEvent evt) {
		if (updatingModel)
			return;

		final HeatmapLabelsHeader hdr = hdim.getLabelsHeader();

		final Object src = evt.getSource();
		final String pname = evt.getPropertyName();

		//System.out.println(getClass().getSimpleName() + " " + src + " " + pname);

		updatingControls = true;

		if (src.equals(hm)) {
		}
		else if (src.equals(hdim)) {
			if (HeatmapDim.ANNOTATIONS_CHANGED.equals(pname))
				updateAnnotations();
			else if (HeatmapDim.HEADERS_CHANGED.equals(pname))
				updateHeaders();
		}

		updatingControls = false;
	}

	private File getSelectedPath() {
		JFileChooser fileChooser = new JFileChooser(
				Settings.getDefault().getLastAnnotationPath());

		fileChooser.setDialogTitle("Select file");
		fileChooser.setMinimumSize(new Dimension(800,600));
		fileChooser.setPreferredSize(new Dimension(800,600));

		int retval = fileChooser.showOpenDialog(AppFrame.instance());
		if (retval == JFileChooser.APPROVE_OPTION) {
			Settings.getDefault().setLastAnnotationPath(
					fileChooser.getSelectedFile().getParent());
			return fileChooser.getSelectedFile();
		}

		return null;
	}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel9 = new javax.swing.JLabel();
        annFile = new javax.swing.JTextField();
        annOpen = new javax.swing.JButton();
        annImport = new javax.swing.JButton();
        annClear = new javax.swing.JButton();
        annFilter = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        headerList = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        headerAddBtn = new javax.swing.JButton();
        headerRemoveBtn = new javax.swing.JButton();
        headerEditBtn = new javax.swing.JButton();
        headerUpBtn = new javax.swing.JButton();
        headerDownBtn = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        headerSize = new javax.swing.JSpinner();
        headerVisible = new javax.swing.JCheckBox();
        headerBgColor = new org.gitools.ui.platform.component.ColorChooserLabel();

        jLabel9.setText("Annotations");

        annFile.setEditable(false);
        annFile.setFocusable(false);

        annOpen.setText("Load...");
        annOpen.setFocusable(false);
        annOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                annOpenActionPerformed(evt);
            }
        });

        annImport.setText("Import...");
        annImport.setEnabled(false);
        annImport.setFocusable(false);
        annImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                annImportActionPerformed(evt);
            }
        });

        annClear.setText("Clear");
        annClear.setFocusable(false);
        annClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                annClearActionPerformed(evt);
            }
        });

        annFilter.setText("Filter");
        annFilter.setToolTipText("Filter out elements without annotations");
        annFilter.setFocusable(false);
        annFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                annFilterActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(headerList);

        jLabel1.setText("Headers");

        headerAddBtn.setText("Add");
        headerAddBtn.setFocusable(false);
        headerAddBtn.setRequestFocusEnabled(false);
        headerAddBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                headerAddBtnActionPerformed(evt);
            }
        });

        headerRemoveBtn.setText("Remove");
        headerRemoveBtn.setEnabled(false);
        headerRemoveBtn.setFocusable(false);
        headerRemoveBtn.setRequestFocusEnabled(false);
        headerRemoveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                headerRemoveBtnActionPerformed(evt);
            }
        });

        headerEditBtn.setText("Edit");
        headerEditBtn.setEnabled(false);
        headerEditBtn.setFocusable(false);
        headerEditBtn.setRequestFocusEnabled(false);
        headerEditBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                headerEditBtnActionPerformed(evt);
            }
        });

        headerUpBtn.setText("Up");
        headerUpBtn.setEnabled(false);
        headerUpBtn.setFocusable(false);
        headerUpBtn.setRequestFocusEnabled(false);
        headerUpBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                headerUpBtnActionPerformed(evt);
            }
        });

        headerDownBtn.setText("Down");
        headerDownBtn.setEnabled(false);
        headerDownBtn.setFocusable(false);
        headerDownBtn.setRequestFocusEnabled(false);
        headerDownBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                headerDownBtnActionPerformed(evt);
            }
        });

        jLabel2.setText("Size");

        headerSize.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(5)));

        headerVisible.setText("Visible");

        headerBgColor.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel9))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(annFile, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(annOpen)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(annImport)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(annClear)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                        .addComponent(annFilter)))
                .addContainerGap())
            .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(214, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(headerAddBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(headerRemoveBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(headerEditBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addComponent(headerUpBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(headerDownBtn)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(headerSize, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(headerVisible)
                .addGap(18, 18, 18)
                .addComponent(headerBgColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(44, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(annFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(annOpen)
                    .addComponent(annImport)
                    .addComponent(annClear)
                    .addComponent(annFilter))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(headerAddBtn)
                    .addComponent(headerRemoveBtn)
                    .addComponent(headerEditBtn)
                    .addComponent(headerDownBtn)
                    .addComponent(headerUpBtn))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(headerSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(headerVisible))
                    .addComponent(headerBgColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(420, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

	private void annOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_annOpenActionPerformed
		try {
			File file = getSelectedPath();

			if (file != null) {
				AnnotationMatrix annMatrix =
						(AnnotationMatrix) PersistenceManager.getDefault().load(
							file, MimeTypes.ANNOTATION_MATRIX,
							new NullProgressMonitor());

				hdim.setAnnotations(annMatrix);

				annFile.setText(file.getName());
			}
		}
		catch (Exception ex) {
			LogUtils.logException(ex, LoggerFactory.getLogger(getClass()));
		}
	}//GEN-LAST:event_annOpenActionPerformed

	private void annFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_annFilterActionPerformed
		IMatrixView matrixView = hm.getMatrixView();

		int count = rowMode ?
				matrixView.getRowCount() : matrixView.getColumnCount();

		AnnotationMatrix annotations = hdim.getAnnotations();

		List<Integer> indices = new ArrayList<Integer>();

		for (int i = 0; i < count; i++) {
			String element = rowMode ?
					matrixView.getRowLabel(i) : matrixView.getColumnLabel(i);

			int j = annotations.getRowIndex(element);
			if (j >= 0)
				indices.add(i);
		}

		int[] view = rowMode ?
			matrixView.getVisibleRows() : matrixView.getVisibleColumns();

		int[] newView = new int[indices.size()];
		for (int i = 0; i < indices.size(); i++)
			newView[i] = view[indices.get(i)];

		if (rowMode)
			matrixView.setVisibleRows(newView);
		else
			matrixView.setVisibleColumns(newView);
	}//GEN-LAST:event_annFilterActionPerformed

	private void annClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_annClearActionPerformed
		hdim.setAnnotations(null);
	}//GEN-LAST:event_annClearActionPerformed

	private static class AnnAttr {
		private String name;

		public AnnAttr() { }

		public AnnAttr(String name) {
			this.name = name; }

		public String getName() {
			return name; }

		public String getPattern() {
			return "${" + name + "}"; }

		@Override public String toString() {
			return getName(); }
	}

	private void headerAddBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_headerAddBtnActionPerformed
		AddHeaderPage headerPage = new AddHeaderPage();
		PageDialog tdlg = new PageDialog(AppFrame.instance(), headerPage);
		tdlg.setTitle("Header type selection");
		tdlg.setVisible(true);
		if (tdlg.isCancelled())
			return;
	
		Class<? extends HeatmapHeader> cls = headerPage.getHeaderClass();
		if (cls.equals(HeatmapLabelsHeader.class)) {
			HeatmapLabelsHeader h = new HeatmapLabelsHeader(hdim);
			LabelsHeaderPage labelPage = new LabelsHeaderPage(hdim, h);
			PageDialog dlg = new PageDialog(AppFrame.instance(), labelPage);
			dlg.setVisible(true);
			if (dlg.isCancelled())
				return;

			hdim.addHeader(h);
		}
		else {
			HeatmapColoredClustersHeader h = new HeatmapColoredClustersHeader(hdim);
			ColoredClustersHeaderWizard wiz =
					new ColoredClustersHeaderWizard(hm, hdim, h, rowMode);
			WizardDialog wdlg = new WizardDialog(AppFrame.instance(), wiz);
			wdlg.setTitle("Add header");
			wdlg.setVisible(true);
			if (wdlg.isCancelled())
				return;

			hdim.addHeader(h);
		}
		updateHeaders();
	}//GEN-LAST:event_headerAddBtnActionPerformed

	private void headerEditBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_headerEditBtnActionPerformed
		int index = headerList.getSelectedIndex();
		HeatmapHeader h = hdim.getHeaders().get(index);
		Class<? extends HeatmapHeader> cls = h.getClass();
		if (cls.equals(HeatmapLabelsHeader.class)) {
			LabelsHeaderPage labelPage = new LabelsHeaderPage(hdim, (HeatmapLabelsHeader) h);
			PageDialog dlg = new PageDialog(AppFrame.instance(), labelPage);
			dlg.setVisible(true);
		}
		else {
			ColoredClustersHeaderWizard wiz =
					new ColoredClustersHeaderWizard(
						hm, hdim, (HeatmapColoredClustersHeader) h, rowMode);
			wiz.setEditionMode(true);
			WizardDialog wdlg = new WizardDialog(AppFrame.instance(), wiz);
			wdlg.setTitle("Edit header");
			wdlg.setVisible(true);
		}
		updateHeaders();
	}//GEN-LAST:event_headerEditBtnActionPerformed

	private void headerUpBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_headerUpBtnActionPerformed
		int index = headerList.getSelectedIndex();
		if (index == -1 || index == 0)
			return;

		hdim.upHeader(index);
		headerList.setSelectedIndex(index - 1);
	}//GEN-LAST:event_headerUpBtnActionPerformed

	private void headerDownBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_headerDownBtnActionPerformed
		int index = headerList.getSelectedIndex();
		if (index == -1 || index >= headerList.getModel().getSize() - 1)
			return;

		hdim.downHeader(index);
		headerList.setSelectedIndex(index + 1);
	}//GEN-LAST:event_headerDownBtnActionPerformed

	private void headerRemoveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_headerRemoveBtnActionPerformed
		int index = headerList.getSelectedIndex();
		if (index == -1)
			return;

		hdim.removeHeader(index);
	}//GEN-LAST:event_headerRemoveBtnActionPerformed

	private void annImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_annImportActionPerformed
		// TODO add your handling code here:
	}//GEN-LAST:event_annImportActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton annClear;
    private javax.swing.JTextField annFile;
    private javax.swing.JButton annFilter;
    private javax.swing.JButton annImport;
    private javax.swing.JButton annOpen;
    private javax.swing.JButton headerAddBtn;
    private org.gitools.ui.platform.component.ColorChooserLabel headerBgColor;
    private javax.swing.JButton headerDownBtn;
    private javax.swing.JButton headerEditBtn;
    private javax.swing.JList headerList;
    private javax.swing.JButton headerRemoveBtn;
    private javax.swing.JSpinner headerSize;
    private javax.swing.JButton headerUpBtn;
    private javax.swing.JCheckBox headerVisible;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables

}
