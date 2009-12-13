/*
 *  Copyright 2009 chris.
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

package org.gitools.ui.view.properties.panels;

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
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import org.gitools.model.figure.heatmap.Heatmap;
import org.gitools.model.figure.heatmap.HeatmapHeader;
import org.gitools.model.matrix.AnnotationMatrix;
import org.gitools.model.matrix.IMatrixView;
import org.gitools.persistence.FilePathResolver;
import org.gitools.persistence.MimeTypes;
import org.gitools.persistence.PersistenceManager;
import org.gitools.ui.component.ColorChooserLabel.ColorChangeListener;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.utils.DocumentChangeListener;
import org.gitools.ui.utils.Options;

public class HeatmapPropertiesHeaderPanel extends HeatmapPropertiesAbstractPanel {

	private boolean rowMode;

    /** Creates new form HeatmapPropertiesHeaderPanel */
    public HeatmapPropertiesHeaderPanel(boolean rowMode) {
		this.rowMode = rowMode;
		
        initComponents();
    }

	@Override
	protected void initControls() {
		if (rowMode)
			headerSizeLabel.setText("Width");
		else
			headerSizeLabel.setText("Height");

		size.addChangeListener(new ChangeListener() {
			@Override public void stateChanged(ChangeEvent e) {
				sizeChangePerformed(); }
		});

		fgColor.addColorChangeListener(new ColorChangeListener() {
			@Override public void colorChanged(Color color) {
				getHeader().setForegroundColor(color);
			}
		});

		bgColor.addColorChangeListener(new ColorChangeListener() {
			@Override public void colorChanged(Color color) {
				getHeader().setBackgroundColor(color);
			}
		});

		labelPattern.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				getHeader().setLabelPattern(
						(String) labelPattern.getSelectedItem());
			}
		});

		linkName.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				getHeader().setLinkName(linkName.getText());
			}
		});

		linkPattern.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override protected void update(DocumentEvent e) {
				getHeader().setLinkPattern(linkPattern.getText());
			}
		});
	}

	private void sizeChangePerformed() {
		SpinnerNumberModel m = (SpinnerNumberModel) size.getModel();
		if (rowMode)
			hm.setRowHeaderSize(m.getNumber().intValue());
		else
			hm.setColumnHeaderSize(m.getNumber().intValue());
	}
	
	@Override
	protected void updateControls() {
		HeatmapHeader hdr = getHeader();
		
		size.setValue(rowMode ?
				hm.getRowHeaderSize() : hm.getColumnHeaderSize());

		fgColor.setColor(hdr.getForegroundColor());
		bgColor.setColor(hdr.getBackgroundColor());

		updateFontTitle();

		updateAnnotations();

		linkName.setText(hdr.getLinkName());
		linkPattern.setText(hdr.getLinkPattern());
	}

	private void updateFontTitle() {
		HeatmapHeader hdr = getHeader();
		final Font font = hdr.getFont();
		fontTitle.setFont(font);
		StringBuilder sb = new StringBuilder();
		sb.append(font.getFontName());
		sb.append(", ").append(font.getSize());
		fontTitle.setText(sb.toString());
	}

	private void updateAnnotations() {
		HeatmapHeader hdr = getHeader();
		AnnotationMatrix annMatrix = hdr.getAnnotations();
		DefaultComboBoxModel pattModel = new DefaultComboBoxModel();

		if (annMatrix != null) {
			File file = PersistenceManager.getDefault().getEntityFile(annMatrix);
			if (file != null)
				annFile.setText(file.getName());

			StringBuilder sb = new StringBuilder();
			ObjectMatrix1D columns = annMatrix.getColumns();	
			pattModel.addElement("${id}");
			for (int i = 0; i < columns.size(); i++) {
				final String name = columns.getQuick(i).toString();
				sb.append("${").append(name).append("}");
				pattModel.addElement(sb.toString());
				sb.setLength(0);
			}

			setAnnotationControlsEnabled(true);
		}
		else {
			annFile.setText("");
			pattModel = new DefaultComboBoxModel(new String[] {"${id}"});
			setAnnotationControlsEnabled(false);
		}

		String patt = (String) labelPattern.getSelectedItem();
		labelPattern.setModel(pattModel);
		if (patt == null || patt.isEmpty())
			labelPattern.setSelectedIndex(0);
		else
			labelPattern.setSelectedItem(patt);
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
		final HeatmapHeader hdr = getHeader();

		final Object source = evt.getSource();
		final String pname = evt.getPropertyName();

		if (source.equals(hm)) {
			if (Heatmap.COLUMN_HEADER_SIZE_CHANGED.equals(pname)
					|| Heatmap.ROW_HEADER_SIZE_CHANGED.equals(evt.getPropertyName()))
				size.setValue(rowMode ?
					hm.getRowHeaderSize() : hm.getColumnHeaderSize());
		}
		else if (source.equals(hdr)) {
			if (HeatmapHeader.FG_COLOR_CHANGED.equals(pname))
				fgColor.setColor(hdr.getForegroundColor());
			else if (HeatmapHeader.BG_COLOR_CHANGED.equals(pname))
				bgColor.setColor(hdr.getBackgroundColor());
			else if (HeatmapHeader.FONT_CHANGED.equals(pname))
				updateFontTitle();
			else if (HeatmapHeader.ANNOTATIONS_CHANGED.equals(pname))
				updateAnnotations();
			else if (HeatmapHeader.LABEL_PATTERN_CHANGED.equals(pname))
				labelPattern.setSelectedItem(hdr.getLabelPattern());
			/*else if (HeatmapHeader.LINK_NAME_CHANGED.equals(pname))
				linkName.setText(hdr.getLinkName());
			else if (HeatmapHeader.LINK_PATTERN_CHANGED.equals(pname))
				linkPattern.setText(hdr.getLinkPattern());*/
		}
	}

	private HeatmapHeader getHeader() {
		return rowMode ?
			hm.getRowHeader() : hm.getColumnHeader();
	}

	private Font selectFont(Font font) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	private File getSelectedPath() {
		JFileChooser fileChooser = new JFileChooser(
				Options.instance().getLastPath());

		fileChooser.setDialogTitle("Select file");
		fileChooser.setMinimumSize(new Dimension(800,600));
		fileChooser.setPreferredSize(new Dimension(800,600));

		int retval = fileChooser.showOpenDialog(AppFrame.instance());
		if (retval == JFileChooser.APPROVE_OPTION)
			return fileChooser.getSelectedFile();

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
        fgColor = new org.gitools.ui.component.ColorChooserLabel();
        jLabel7 = new javax.swing.JLabel();
        bgColor = new org.gitools.ui.component.ColorChooserLabel();
        jLabel8 = new javax.swing.JLabel();
        fontTitle = new javax.swing.JTextField();
        fontSelect = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        annFile = new javax.swing.JTextField();
        annOpen = new javax.swing.JButton();
        annClear = new javax.swing.JButton();
        annFilter = new javax.swing.JButton();
        labelsPanel = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        labelPattern = new javax.swing.JComboBox();
        linksPanel = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        linkPattern = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        linkName = new javax.swing.JTextField();

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Size, Colors and Font"));

        headerSizeLabel.setText("Width");

        jLabel6.setText("Fg");

        jLabel7.setText("Bg");

        jLabel8.setText("Font");

        fontTitle.setEditable(false);
        fontTitle.setFocusable(false);

        fontSelect.setText("Select...");
        fontSelect.setFocusable(false);
        fontSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fontSelectActionPerformed(evt);
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
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fontTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fontSelect)))
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
                        .addComponent(annFile, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(annOpen)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(annClear)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 91, Short.MAX_VALUE)
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

        labelsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Labels"));

        jLabel11.setText("Pattern");

        labelPattern.setEditable(true);

        javax.swing.GroupLayout labelsPanelLayout = new javax.swing.GroupLayout(labelsPanel);
        labelsPanel.setLayout(labelsPanelLayout);
        labelsPanelLayout.setHorizontalGroup(
            labelsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(labelsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelPattern, 0, 190, Short.MAX_VALUE)
                .addContainerGap())
        );
        labelsPanelLayout.setVerticalGroup(
            labelsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(labelsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(labelsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(labelPattern, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        linksPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Links"));

        jLabel12.setText("Url pattern");

        linkPattern.setColumns(10);
        linkPattern.setRows(3);
        linkPattern.setTabSize(4);
        jScrollPane3.setViewportView(linkPattern);

        jLabel1.setText("Name");

        linkName.setColumns(8);

        javax.swing.GroupLayout linksPanelLayout = new javax.swing.GroupLayout(linksPanel);
        linksPanel.setLayout(linksPanelLayout);
        linksPanelLayout.setHorizontalGroup(
            linksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(linksPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(linksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(linksPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(linkName, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE))
                    .addComponent(jLabel12)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE))
                .addContainerGap())
        );
        linksPanelLayout.setVerticalGroup(
            linksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(linksPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(linksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(linkName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(labelsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(linksPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(linksPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

	private void annOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_annOpenActionPerformed
		try {
			File file = getSelectedPath();

			AnnotationMatrix annMatrix =
					(AnnotationMatrix) PersistenceManager.getDefault().load(
						new FilePathResolver(file.getParentFile()),
						file, MimeTypes.ANNOTATION_MATRIX,
						new NullProgressMonitor());

			HeatmapHeader decorator = getHeader();
			decorator.setAnnotations(annMatrix);
		}
		catch (Exception ex) {
			ex.printStackTrace(); //FIXME
		}
	}//GEN-LAST:event_annOpenActionPerformed

	private void fontSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fontSelectActionPerformed
		HeatmapHeader hdr = getHeader();

		Font f = selectFont(hdr.getFont());
		if (f != null)
			hdr.setFont(f);
	}//GEN-LAST:event_fontSelectActionPerformed

	private void annFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_annFilterActionPerformed
		IMatrixView matrixView = hm.getMatrixView();

		int count = rowMode ?
				matrixView.getRowCount() : matrixView.getColumnCount();

		HeatmapHeader decorator = getHeader();

		AnnotationMatrix annotations = decorator.getAnnotations();

		List<Integer> indices = new ArrayList<Integer>();

		for (int i = 0; i < count; i++) {
			String element = rowMode ?
					matrixView.getRowLabel(i) : matrixView.getColumnLabel(i);

			int j = annotations.getRowIndex(element);
			if (j >= 0)
				indices.add(i);
		}

		int[] view = new int[indices.size()];
		for (int i = 0; i < indices.size(); i++)
			view[i] = indices.get(i);

		if (rowMode)
			matrixView.setVisibleRows(view);
		else
			matrixView.setVisibleColumns(view);
	}//GEN-LAST:event_annFilterActionPerformed

	private void annClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_annClearActionPerformed
		HeatmapHeader hdr = getHeader();
		hdr.setAnnotations(null);
	}//GEN-LAST:event_annClearActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton annClear;
    private javax.swing.JTextField annFile;
    private javax.swing.JButton annFilter;
    private javax.swing.JButton annOpen;
    private org.gitools.ui.component.ColorChooserLabel bgColor;
    private org.gitools.ui.component.ColorChooserLabel fgColor;
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
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JComboBox labelPattern;
    private javax.swing.JPanel labelsPanel;
    private javax.swing.JTextField linkName;
    private javax.swing.JTextArea linkPattern;
    private javax.swing.JPanel linksPanel;
    private javax.swing.JSpinner size;
    // End of variables declaration//GEN-END:variables

}
