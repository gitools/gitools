/*
 *  Copyright 2009 cperez.
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
 * HeatmapPropertiesPanel.java
 *
 * Created on Dec 11, 2009, 4:05:43 PM
 */

package org.gitools.ui.heatmap.panel.properties;

import javax.swing.JPanel;
import org.gitools.heatmap.model.Heatmap;

public class HeatmapPropertiesPanel extends JPanel {

	private Heatmap heatmap;

    /** Creates new form HeatmapPropertiesPanel */
    public HeatmapPropertiesPanel() {
        initComponents();

		tabs.addTab("Document", new HeatmapPropertiesDocumentPanel());
		tabs.addTab("Rows", new HeatmapPropertiesHeaderPanel(true));
		tabs.addTab("Columns", new HeatmapPropertiesHeaderPanel(false));
		tabs.addTab("Cells", new HeatmapPropertiesCellsPanel());
    }

	public Heatmap getHeatmap() {
		return heatmap;
	}

	public void setHeatmap(Heatmap heatmap) {
		this.heatmap = heatmap;
		
		for (int i = 0; i < tabs.getTabCount(); i++) {
			HeatmapPropertiesAbstractPanel panel =
					(HeatmapPropertiesAbstractPanel) tabs.getComponentAt(i);
			panel.setHeatmap(heatmap);
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

        tabs = new javax.swing.JTabbedPane();

        setPreferredSize(new java.awt.Dimension(300, 646));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabs, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabs, javax.swing.GroupLayout.DEFAULT_SIZE, 646, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane tabs;
    // End of variables declaration//GEN-END:variables

}
