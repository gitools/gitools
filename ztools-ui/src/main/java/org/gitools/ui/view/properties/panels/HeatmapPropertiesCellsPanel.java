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
 * HeaderPropertiesCellsPanel.java
 *
 * Created on 12-dic-2009, 17:33:49
 */

package org.gitools.ui.view.properties.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.ElementDecoratorDescriptor;
import org.gitools.model.decorator.ElementDecoratorFactory;
import org.gitools.model.figure.heatmap.Heatmap;
import org.gitools.ui.component.ColorChooserLabel.ColorChangeListener;
import org.gitools.ui.panels.decorator.ElementDecoratorPanelFactory;

public class HeatmapPropertiesCellsPanel extends HeatmapPropertiesAbstractPanel {

	private Map<ElementDecoratorDescriptor, ElementDecorator> decoratorCache
			= new HashMap<ElementDecoratorDescriptor, ElementDecorator>();
	
    /** Creates new form HeaderPropertiesCellsPanel */
    public HeatmapPropertiesCellsPanel() {
        initComponents();
    }

	@Override
	protected void initControls() {
		gridColor.addColorChangeListener(new ColorChangeListener() {
			@Override public void colorChanged(Color color) {
				hm.setGridColor(color); }
		});

		cellDecorator.addItemListener(new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				cellDecoratorChanged(e); }
		});
	}

	@Override
	protected void updateControls() {
		cellWidth.setValue(hm.getCellWidth());
		cellHeight.setValue(hm.getCellHeight());
		showGrid.setSelected(hm.isShowGrid());
		gridColor.setColor(hm.getGridColor());

		final List<ElementDecoratorDescriptor> descList =
			ElementDecoratorFactory.getDescriptors();
		final ElementDecoratorDescriptor[] descriptors =
			new ElementDecoratorDescriptor[descList.size()];
		descList.toArray(descriptors);

		cellDecorator.setModel(new DefaultComboBoxModel(descriptors));

		ElementDecoratorDescriptor descriptor =
				ElementDecoratorFactory.getDescriptor(hm.getCellDecorator().getClass());
		
		cellDecorator.setSelectedItem(descriptor);

		changeDecoratorPanel(descriptor);
	}

	@Override
	protected void heatmapPropertyChange(PropertyChangeEvent evt) {
		String pname = evt.getPropertyName();

		if (evt.getSource().equals(hm)) {
			if (Heatmap.CELL_SIZE_CHANGED.equals(pname)) {
				cellWidth.setValue(hm.getCellWidth());
				cellHeight.setValue(hm.getCellHeight());
			}
		}
	}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        cellWidth = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        cellHeight = new javax.swing.JSpinner();
        cellSyncSize = new javax.swing.JToggleButton();
        showGrid = new javax.swing.JCheckBox();
        gridColor = new org.gitools.ui.component.ColorChooserLabel();
        jPanel1 = new javax.swing.JPanel();
        cellDecorator = new javax.swing.JComboBox();
        decoPanel = new javax.swing.JPanel();

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Size and Grid"));

        jLabel3.setText("Width");

        cellWidth.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        cellWidth.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                cellWidthStateChanged(evt);
            }
        });

        jLabel4.setText("Height");

        cellHeight.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        cellHeight.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                cellHeightStateChanged(evt);
            }
        });

        cellSyncSize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Chain.png"))); // NOI18N
        cellSyncSize.setToolTipText("Synchronize width and height");
        cellSyncSize.setFocusable(false);
        cellSyncSize.setMaximumSize(new java.awt.Dimension(30, 30));
        cellSyncSize.setMinimumSize(new java.awt.Dimension(0, 0));
        cellSyncSize.setPreferredSize(new java.awt.Dimension(28, 28));

        showGrid.setText("Show grid");
        showGrid.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                showGridStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cellWidth, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cellHeight, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cellSyncSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(showGrid)
                        .addGap(18, 18, 18)
                        .addComponent(gridColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cellSyncSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(cellWidth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)
                        .addComponent(cellHeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(showGrid)
                    .addComponent(gridColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Scale"));

        decoPanel.setAutoscrolls(true);
        decoPanel.setFocusable(false);
        decoPanel.setLayout(new javax.swing.BoxLayout(decoPanel, javax.swing.BoxLayout.LINE_AXIS));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(decoPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cellDecorator, javax.swing.GroupLayout.Alignment.LEADING, 0, 266, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cellDecorator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(decoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

	private void cellWidthStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_cellWidthStateChanged
		int width = (Integer) cellWidth.getValue();
		int dif = width - hm.getCellWidth();
		hm.setCellWidth(width);
		if (cellSyncSize.isSelected()) {
			int height = hm.getCellHeight() + dif;
			if (height > 0)
				hm.setCellHeight(height);
		}
	}//GEN-LAST:event_cellWidthStateChanged

	private void cellHeightStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_cellHeightStateChanged
		int height = (Integer) cellHeight.getValue();
		int dif = height - hm.getCellHeight();
		hm.setCellHeight(height);
		if (cellSyncSize.isSelected()) {
			int width = hm.getCellWidth() + dif;
			if (width > 0)
				hm.setCellWidth(width);
		}
	}//GEN-LAST:event_cellHeightStateChanged

	private void showGridStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_showGridStateChanged
		hm.setShowGrid(showGrid.isSelected());
	}//GEN-LAST:event_showGridStateChanged

	private void cellDecoratorChanged(ItemEvent evt) {
		final ElementDecoratorDescriptor descriptor =
			(ElementDecoratorDescriptor) evt.getItem();

		if (evt.getStateChange() == ItemEvent.DESELECTED)
			decoratorCache.put(descriptor, hm.getCellDecorator());
		else if (evt.getStateChange() == ItemEvent.SELECTED) {
			ElementDecorator decorator = decoratorCache.get(descriptor);
			if (decorator == null)
				decorator = ElementDecoratorFactory.create(
						descriptor, hm.getMatrixView().getCellAdapter());

			hm.setCellDecorator(decorator);

			changeDecoratorPanel(descriptor);
		}
	}

	private void changeDecoratorPanel(ElementDecoratorDescriptor descriptor) {
		final JPanel confPanel = new JPanel();
		confPanel.setLayout(new BorderLayout());
		
		Class<? extends ElementDecorator> decoratorClass = descriptor.getDecoratorClass();
		
		JComponent c = ElementDecoratorPanelFactory.create(decoratorClass, hm);
		
		confPanel.add(c, BorderLayout.CENTER);
		
		decoPanel.removeAll();
		decoPanel.setLayout(new BorderLayout());
		//decoPanel.setMaximumSize(new Dimension(20, 20));
		//decoPanel.setPreferredSize(new Dimension(20, 20));
		decoPanel.add(c, BorderLayout.CENTER);
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cellDecorator;
    private javax.swing.JSpinner cellHeight;
    private javax.swing.JToggleButton cellSyncSize;
    private javax.swing.JSpinner cellWidth;
    private javax.swing.JPanel decoPanel;
    private org.gitools.ui.component.ColorChooserLabel gridColor;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JCheckBox showGrid;
    // End of variables declaration//GEN-END:variables

}
