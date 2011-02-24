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

import cern.colt.matrix.ObjectMatrix1D;
import edu.upf.bg.progressmonitor.NullProgressMonitor;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.heatmap.model.HeatmapClusterBand;
import org.gitools.heatmap.model.HeatmapDim;
import org.gitools.heatmap.model.HeatmapLabelsHeader;
import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.persistence.MimeTypes;
import org.gitools.persistence.PersistenceManager;
import org.gitools.ui.actions.data.ColorClusterGenerateAction;
import org.gitools.ui.dialog.ListDialog;
import org.gitools.ui.platform.component.ColorChooserLabel.ColorChangeListener;
import org.gitools.ui.platform.dialog.FontChooserDialog;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.utils.DocumentChangeListener;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.utils.LogUtils;
import org.gitools.ui.wizard.clustering.color.ClusterSetEditorDialog;
import org.slf4j.LoggerFactory;

public class HeatmapPropertiesHeaderPanel extends HeatmapPropertiesAbstractPanel {

	private boolean rowMode;

	private HeatmapDim dim;

	//private boolean updatingControls = false;

	private boolean updatingModel = false;

    /** Creates new form HeatmapPropertiesHeaderPanel */
    public HeatmapPropertiesHeaderPanel(boolean rowMode) {
		this.rowMode = rowMode;

        initComponents();

		// TODO activate in the next version:
		//colorAnnButton.setVisible(false);
		//colorAnnEditBtn.setVisible(false);
    }

	@Override
	public void setHeatmap(Heatmap heatmap) {
		this.dim = rowMode ?
			heatmap.getRowDim() : heatmap.getColumnDim();
		
		super.setHeatmap(heatmap);
	}

	@Override
	protected void initControls() {
		if (rowMode)
			headerSizeLabel.setText("Width");
		else
			headerSizeLabel.setText("Height");

		size.addChangeListener(new ChangeListener() {
			@Override public void stateChanged(ChangeEvent e) {
				if (!updatingControls)
					sizeChangePerformed(); }
		});

		fgColor.addColorChangeListener(new ColorChangeListener() {
			@Override public void colorChanged(Color color) {
				if (!updatingControls)
					dim.getLabelsHeader().setForegroundColor(color);
			}
		});

		bgColor.addColorChangeListener(new ColorChangeListener() {
			@Override public void colorChanged(Color color) {
				if (!updatingControls)
					dim.getLabelsHeader().setBackgroundColor(color);
			}
		});

		labelPattern.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override protected void update(DocumentEvent e) {
				if (!updatingControls) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override public void run() {
							updatingModel = true;
							dim.getLabelsHeader().setLabelPattern(labelPattern.getText());
							updatingModel = false;
						}
					});
				}
			}
		});


		linkName.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				if (!updatingControls)
					dim.getLabelsHeader().setLinkName(linkName.getText());
			}
		});

		linkPattern.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override protected void update(DocumentEvent e) {
				if (!updatingControls)
					dim.getLabelsHeader().setLinkPattern(linkPattern.getText());
			}
		});
	}

	private void sizeChangePerformed() {
		SpinnerNumberModel m = (SpinnerNumberModel) size.getModel();
		dim.getLabelsHeader().setSize(m.getNumber().intValue());
	}
	
	@Override
	protected void updateControls() {
		updatingControls = true;

		HeatmapLabelsHeader hdr = dim.getLabelsHeader();
		
		size.setValue(dim.getLabelsHeader().getSize());

		fgColor.setColor(hdr.getForegroundColor());
		bgColor.setColor(hdr.getBackgroundColor());

		updateFontTitle();

		updateAnnotations();

		linkName.setText(hdr.getLinkName());
		linkPattern.setText(hdr.getLinkPattern());

		updatingControls = false;
	}

	private void updateFontTitle() {
		HeatmapLabelsHeader hdr = dim.getLabelsHeader();
		final Font font = hdr.getFont();
		fontTitle.setFont(font);
		StringBuilder sb = new StringBuilder();
		sb.append(font.getFontName());
		sb.append(", ").append(font.getSize());
		fontTitle.setText(sb.toString());
	}

	private void updateAnnotations() {
		AnnotationMatrix annMatrix = dim.getAnnotations();
		
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

		String patt = dim.getLabelsHeader().getLabelPattern();
		if (patt == null || patt.isEmpty())
			patt = "${id}";

		labelPattern.setText(patt);
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

		final HeatmapLabelsHeader hdr = dim.getLabelsHeader();

		final Object source = evt.getSource();
		final String pname = evt.getPropertyName();

		//System.out.println(getClass().getSimpleName() + " " + src + " " + pname);

		if (source.equals(hm)) {
			/*if (Heatmap.COLUMN_HEADER_SIZE_CHANGED.equals(pname)
					|| Heatmap.ROW_HEADER_SIZE_CHANGED.equals(evt.getPropertyName()))
				size.setValue(rowMode ?
					hm.getRowHeaderSize() : hm.getColumnHeaderSize());*/
		}
		else if (source.equals(hdr)) {
			updatingControls = true;

			if (HeatmapLabelsHeader.FG_COLOR_CHANGED.equals(pname))
				fgColor.setColor(hdr.getForegroundColor());
			else if (HeatmapLabelsHeader.BG_COLOR_CHANGED.equals(pname))
				bgColor.setColor(hdr.getBackgroundColor());
			else if (HeatmapLabelsHeader.FONT_CHANGED.equals(pname))
				updateFontTitle();
			else if (HeatmapLabelsHeader.LABEL_PATTERN_CHANGED.equals(pname))
				updateAnnotations();

			/*else if (HeatmapLabelsHeader.LINK_NAME_CHANGED.equals(pname))
				linkName.setText(hdr.getLinkName());
			else if (HeatmapLabelsHeader.LINK_PATTERN_CHANGED.equals(pname))
				linkPattern.setText(hdr.getLinkPattern());*/

			updatingControls = false;
		}
		else if (source.equals(dim)) {
			if (HeatmapDim.ANNOTATIONS_CHANGED.equals(pname)) {
				updateAnnotations();
				//updateColorAnnotations();
			}
			else if(HeatmapDim.HEADER_SIZE_CHANGED.equals(pname))
				size.setValue(dim.getHeaderSize());
		}
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

        jPanel3 = new javax.swing.JPanel();
        headerSizeLabel = new javax.swing.JLabel();
        size = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();
        fgColor = new org.gitools.ui.platform.component.ColorChooserLabel();
        jLabel7 = new javax.swing.JLabel();
        bgColor = new org.gitools.ui.platform.component.ColorChooserLabel();
        jLabel8 = new javax.swing.JLabel();
        fontTitle = new javax.swing.JTextField();
        fontSelect = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        labelPattern = new javax.swing.JTextField();
        attributePatternBtn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        linkName = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        linkPattern = new javax.swing.JTextArea();
        selectUrlPattern = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        annFile = new javax.swing.JTextField();
        annOpen = new javax.swing.JButton();
        annClear = new javax.swing.JButton();
        annFilter = new javax.swing.JButton();
        labelsPanel = new javax.swing.JPanel();
        addCluster = new javax.swing.JButton();
        clusterEdit = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        clusterList = new javax.swing.JList();
        upCluster = new javax.swing.JButton();
        downCluster = new javax.swing.JButton();
        sortByCluster = new javax.swing.JButton();

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Labels"));

        headerSizeLabel.setText("Width");

        size.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        jLabel6.setText("Fg");

        jLabel7.setText("Bg");

        jLabel8.setText("Font");

        fontTitle.setEditable(false);
        fontTitle.setFocusable(false);

        fontSelect.setText("...");
        fontSelect.setToolTipText("Select a font");
        fontSelect.setFocusable(false);
        fontSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fontSelectActionPerformed(evt);
            }
        });

        jLabel11.setText("Label pattern");

        attributePatternBtn.setText("...");
        attributePatternBtn.setToolTipText("Select annotation fields to show in labels");
        attributePatternBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                attributePatternBtnActionPerformed(evt);
            }
        });

        jLabel1.setText("Url name");

        linkName.setColumns(8);

        jLabel12.setText("Url pattern");

        linkPattern.setColumns(10);
        linkPattern.setLineWrap(true);
        linkPattern.setRows(3);
        linkPattern.setTabSize(4);
        jScrollPane3.setViewportView(linkPattern);

        selectUrlPattern.setText("...");
        selectUrlPattern.setToolTipText("Select from a predefined set of URL's");
        selectUrlPattern.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectUrlPatternActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(headerSizeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(size, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6)
                        .addGap(12, 12, 12)
                        .addComponent(fgColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addGap(12, 12, 12)
                        .addComponent(bgColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel11)
                    .addComponent(jLabel1)
                    .addComponent(jLabel12)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(linkName, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                            .addComponent(labelPattern, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fontTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(selectUrlPattern, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                            .addComponent(attributePatternBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                            .addComponent(fontSelect, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(headerSizeLabel)
                        .addComponent(size, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(fgColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6)
                        .addComponent(bgColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7)))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(fontTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fontSelect))
                .addGap(18, 18, 18)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelPattern, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(attributePatternBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(linkName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectUrlPattern))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Annotations"));

        jLabel9.setText("File");

        annFile.setEditable(false);
        annFile.setFocusable(false);

        annOpen.setText("Open...");
        annOpen.setFocusable(false);
        annOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                annOpenActionPerformed(evt);
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

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(annFile, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(annOpen)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(annClear)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 137, Short.MAX_VALUE)
                        .addComponent(annFilter)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(annFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(annOpen)
                    .addComponent(annClear)
                    .addComponent(annFilter))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        labelsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Clusters"));

        addCluster.setText("Add");
        addCluster.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addClusterActionPerformed(evt);
            }
        });

        clusterEdit.setText("Edit");
        clusterEdit.setEnabled(false);
        clusterEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clusterEditActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(clusterList);

        upCluster.setText("Up");
        upCluster.setEnabled(false);
        upCluster.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upClusterActionPerformed(evt);
            }
        });

        downCluster.setText("Down");
        downCluster.setEnabled(false);
        downCluster.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downClusterActionPerformed(evt);
            }
        });

        sortByCluster.setText("Sort");
        sortByCluster.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortByClusterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout labelsPanelLayout = new javax.swing.GroupLayout(labelsPanel);
        labelsPanel.setLayout(labelsPanelLayout);
        labelsPanelLayout.setHorizontalGroup(
            labelsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(labelsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(labelsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                    .addGroup(labelsPanelLayout.createSequentialGroup()
                        .addComponent(addCluster)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clusterEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(upCluster)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(downCluster)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 88, Short.MAX_VALUE)
                        .addComponent(sortByCluster)))
                .addContainerGap())
        );
        labelsPanelLayout.setVerticalGroup(
            labelsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(labelsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(labelsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addCluster)
                    .addComponent(clusterEdit)
                    .addComponent(upCluster)
                    .addComponent(downCluster)
                    .addComponent(sortByCluster))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(labelsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(53, Short.MAX_VALUE))
        );

        jPanel3.getAccessibleContext().setAccessibleName("Labels");
    }// </editor-fold>//GEN-END:initComponents

	private void annOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_annOpenActionPerformed
		try {
			File file = getSelectedPath();

			if (file != null) {
				AnnotationMatrix annMatrix =
						(AnnotationMatrix) PersistenceManager.getDefault().load(
							file, MimeTypes.ANNOTATION_MATRIX,
							new NullProgressMonitor());

				dim.setAnnotations(annMatrix);

				annFile.setText(file.getName());
			}
		}
		catch (Exception ex) {
			LogUtils.logException(ex, LoggerFactory.getLogger(getClass()));
		}
	}//GEN-LAST:event_annOpenActionPerformed

	private void fontSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fontSelectActionPerformed
		HeatmapLabelsHeader hdr = dim.getLabelsHeader();

		FontChooserDialog dlg =
				new FontChooserDialog(AppFrame.instance(), hdr.getFont());
		dlg.setVisible(true);

		if (!dlg.isCancelled())
			hdr.setFont(dlg.getFont());
	}//GEN-LAST:event_fontSelectActionPerformed

	private void annFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_annFilterActionPerformed
		IMatrixView matrixView = hm.getMatrixView();

		int count = rowMode ?
				matrixView.getRowCount() : matrixView.getColumnCount();

		AnnotationMatrix annotations = dim.getAnnotations();

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
		dim.setAnnotations(null);
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

	private void attributePatternBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_attributePatternBtnActionPerformed

		List<AnnAttr> attributes = new ArrayList<AnnAttr>();
		attributes.add(new AnnAttr() {
			@Override public String getName() {
				return "ID"; }

			@Override public String getPattern() {
				return "${id}"; }
		});

		/*attributes.add(new AnnAttr() {
			@Override public String getName() {
				return "INDEX"; }

			@Override public String getPattern() {
				return "${index}"; }
		});*/

		Heatmap h = getHeatmap();
		AnnotationMatrix annMatrix = rowMode ?
			h.getRowDim().getAnnotations() :
			h.getColumnDim().getAnnotations();

		if (annMatrix != null) {
			ObjectMatrix1D columns = annMatrix.getColumns();

			for (int i = 0; i < columns.size(); i++)
				attributes.add(new AnnAttr(columns.getQuick(i).toString()));
		}

		AnnAttr[] attributesArray = new AnnAttr[attributes.size()];
		attributes.toArray(attributesArray);

		ListDialog<AnnAttr> dlg = new ListDialog<AnnAttr>(null, true, attributesArray);
		dlg.setTitle("Select annotation ...");
		dlg.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		dlg.setVisible(true);

		if (dlg.getReturnStatus() == ListDialog.RET_OK) {
			AnnAttr attr = dlg.getSelectedObject();
			try {
				//TODO remove selected text before
				labelPattern.getDocument().insertString(labelPattern.getCaretPosition(), attr.getPattern(), null);
			} catch (BadLocationException ex) {
				Logger.getLogger(HeatmapPropertiesHeaderPanel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}//GEN-LAST:event_attributePatternBtnActionPerformed

	private void addClusterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addClusterActionPerformed
		if(!updatingControls) {
			ColorClusterGenerateAction ccga = new ColorClusterGenerateAction(getHeatmap(), rowMode);
			ccga.actionPerformed(evt);

			boolean enableEditBtn;
			if (rowMode) 
				enableEditBtn = !getHeatmap().getRowDim().getClustersHeader().getClusterBands().isEmpty() ? true : false;
			else 
				enableEditBtn = !getHeatmap().getColumnDim().getClustersHeader().getClusterBands().isEmpty() ? true : false;
			clusterEdit.setEnabled(enableEditBtn);
		}
	}//GEN-LAST:event_addClusterActionPerformed

	private void clusterEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clusterEditActionPerformed
		if (!updatingControls) {
			ClusterSetEditorDialog csed = new ClusterSetEditorDialog(AppFrame.instance(), getHeatmap(), rowMode);
			List<HeatmapClusterBand> clusterSets = csed.getClusterSets();
			if (rowMode)
				getHeatmap().getRowDim().getClustersHeader().setClusterBands(clusterSets);
			else
				getHeatmap().getColumnDim().getClustersHeader().setClusterBands(clusterSets);
		}
	}//GEN-LAST:event_clusterEditActionPerformed

	private void selectUrlPatternActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectUrlPatternActionPerformed
		// TODO add your handling code here:
	}//GEN-LAST:event_selectUrlPatternActionPerformed

	private void upClusterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upClusterActionPerformed
		// TODO add your handling code here:
	}//GEN-LAST:event_upClusterActionPerformed

	private void downClusterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downClusterActionPerformed
		// TODO add your handling code here:
	}//GEN-LAST:event_downClusterActionPerformed

	private void sortByClusterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortByClusterActionPerformed
		// TODO add your handling code here:
	}//GEN-LAST:event_sortByClusterActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addCluster;
    private javax.swing.JButton annClear;
    private javax.swing.JTextField annFile;
    private javax.swing.JButton annFilter;
    private javax.swing.JButton annOpen;
    private javax.swing.JButton attributePatternBtn;
    private org.gitools.ui.platform.component.ColorChooserLabel bgColor;
    private javax.swing.JButton clusterEdit;
    private javax.swing.JList clusterList;
    private javax.swing.JButton downCluster;
    private org.gitools.ui.platform.component.ColorChooserLabel fgColor;
    private javax.swing.JButton fontSelect;
    private javax.swing.JTextField fontTitle;
    private javax.swing.JLabel headerSizeLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField labelPattern;
    private javax.swing.JPanel labelsPanel;
    private javax.swing.JTextField linkName;
    private javax.swing.JTextArea linkPattern;
    private javax.swing.JButton selectUrlPattern;
    private javax.swing.JSpinner size;
    private javax.swing.JButton sortByCluster;
    private javax.swing.JButton upCluster;
    // End of variables declaration//GEN-END:variables

}
