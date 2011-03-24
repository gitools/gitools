/*
 *  Copyright 2011 chris.
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

package org.gitools.ui.heatmap.header.coloredlabels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Iterator;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.gitools.clustering.HierarchicalClusteringResults;
import org.gitools.clustering.method.value.ClusterUtils;
import org.gitools.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.ui.IconNames;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.dialog.ExceptionDialog;
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.gitools.ui.platform.wizard.IWizard;
//import org.mitre.bio.phylo.dom.Forest;
//import org.mitre.bio.phylo.dom.Phylogeny;
//import org.mitre.bio.phylo.dom.gui.Tree2DPanel;
//import org.mitre.bio.phylo.dom.gui.Tree2DScrollPane;
//import org.mitre.bio.phylo.dom.gui.treepainter.RectangleTree2DPainter;
//import org.mitre.bio.phylo.dom.io.NewickReader;

public class ColoredLabelsHCLPage extends AbstractWizardPage {

	private HeatmapColoredLabelsHeader header;
	//private Tree2DScrollPane treeScrollPane;
	private Action zoomInAction, zoomOutAction;


    /** Creates new form ColoredClustersConfigPage */
    public ColoredLabelsHCLPage(final HeatmapColoredLabelsHeader header) {
		super();

		this.header = header;

		initComponents();

		//createActions();

		setTitle("Header configuration");
		setComplete(true);

		zoomInBtn.setVisible(false);
		zoomInBtn.setAction(zoomInAction);
		zoomInBtn.setIcon(IconUtils.getImageIconResourceScaledByHeight(IconNames.zoomIn16, 16));

		zoomOutBtn.setVisible(false);
		zoomOutBtn.setAction(zoomOutAction);
		zoomOutBtn.setIcon(IconUtils.getImageIconResourceScaledByHeight(IconNames.zoomOut16, 16));

		int treeDepth = header.getHCLResults().getNewickTree().getTreeDepth();
		SpinnerModel sm = new SpinnerNumberModel(header.getHCLCurrentDepth(), 0, treeDepth, 1);

		this.levelSpin.setModel(sm);

		int numClusters = header.getHCLResults().getNewickTree().getRoot().getLeaves(
				(Integer) levelSpin.getValue()).size();

		lblClusterLevel.setText(String.valueOf(numClusters));

		lblTreeDepth.setText(String.valueOf(treeDepth));

		lblMaxClusters.setText(String.valueOf(header.getHCLResults().getNewickTree().getRoot().getLeaves().size()));

		levelSpin.addChangeListener(new ChangeListener() {
			@Override public void stateChanged(ChangeEvent e) {
					
					int numClusters = header.getHCLResults().getNewickTree().getRoot().getLeaves(
							(Integer) levelSpin.getValue()).size();

					lblClusterLevel.setText(String.valueOf(numClusters));
			}
		});

		treePanel.setLayout(new BorderLayout());
		
		// Read in the file(s) with the selected PhyloReader
		String newickString = header.getHCLResults().getStrNewickTree();
		BufferedReader br = new BufferedReader(new StringReader(newickString));

		// Load the file
		/*try {
			Forest f = new NewickReader().read(br);
			if (f != null) {
				for (Iterator i = f.getPhylogenies(); i.hasNext();) {
					Phylogeny p = (Phylogeny) i.next();
					this.treeScrollPane = this.newTree2DScrollPane(p);
					this.addTree2DScrollPane(treeScrollPane);
				}
			}
		} catch (Exception ex) {
			ExceptionDialog dlg = new ExceptionDialog(AppFrame.instance(), ex);
			dlg.setVisible(true);
		}

		treePanel.repaint();
		treeScrollPane.validate();*/
		
		validate();
	}

	/*private Tree2DScrollPane newTree2DScrollPane(Phylogeny phylogeny) {
		Tree2DScrollPane pane = new Tree2DScrollPane(phylogeny);

		pane.removeMouseListener(pane);
		Tree2DPanel tree2DPane = pane.getTree2DPanel();
		tree2DPane.removeMouseListener(tree2DPane);

		RectangleTree2DPainter painter = new RectangleTree2DPainter(phylogeny, tree2DPane);
		pane.setTree2DPainter(painter);

		return pane;
	}

   public void addTree2DScrollPane(Tree2DScrollPane scrollPane) {
		treePanel.add(scrollPane, BorderLayout.CENTER);
		treePanel.setFocusable(false);
	}

	public void createActions() {
		zoomInAction = new ZoomInActionClass(null, null);
		zoomOutAction = new ZoomOutActionClass(null, null);
	}*/


	/** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        colorGroup = new javax.swing.ButtonGroup();
        jLabel3 = new javax.swing.JLabel();
        levelSpin = new javax.swing.JSpinner();
        jComboBox1 = new javax.swing.JComboBox();
        treePanel = new javax.swing.JPanel();
        zoomInBtn = new javax.swing.JButton();
        zoomOutBtn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        lblTreeDepth = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblClusterLevel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblMaxClusters = new javax.swing.JLabel();

        setBorder(null);

        jLabel3.setText("Draw clusters for tree level:");

        levelSpin.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(0), null, Integer.valueOf(1)));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.setMinimumSize(new java.awt.Dimension(0, 0));

        javax.swing.GroupLayout treePanelLayout = new javax.swing.GroupLayout(treePanel);
        treePanel.setLayout(treePanelLayout);
        treePanelLayout.setHorizontalGroup(
            treePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 744, Short.MAX_VALUE)
        );
        treePanelLayout.setVerticalGroup(
            treePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 153, Short.MAX_VALUE)
        );

        jLabel1.setText("Maximum tree levels:");

        lblTreeDepth.setText("0");

        jLabel2.setText("Resulting number of clusters :");

        lblClusterLevel.setText("0");

        jLabel4.setText("Maximum number of clusters :");

        lblMaxClusters.setText("0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(treePanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(levelSpin, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel2)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblMaxClusters, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(lblClusterLevel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lblTreeDepth, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jLabel1))
                        .addGap(18, 345, Short.MAX_VALUE)
                        .addComponent(zoomInBtn)
                        .addGap(28, 28, 28)
                        .addComponent(zoomOutBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(levelSpin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblClusterLevel)
                                .addGap(18, 18, 18)
                                .addComponent(lblTreeDepth))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(53, 53, 53)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(lblMaxClusters)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(93, 93, 93)
                        .addComponent(jLabel1)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(zoomOutBtn)
                    .addComponent(zoomInBtn))
                .addGap(18, 18, 18)
                .addComponent(treePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup colorGroup;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel lblClusterLevel;
    private javax.swing.JLabel lblMaxClusters;
    private javax.swing.JLabel lblTreeDepth;
    private javax.swing.JSpinner levelSpin;
    private javax.swing.JPanel treePanel;
    private javax.swing.JButton zoomInBtn;
    private javax.swing.JButton zoomOutBtn;
    // End of variables declaration//GEN-END:variables


	public HeatmapColoredLabelsHeader getHeader() {
		return header;
	}

	public void setHeader(HeatmapColoredLabelsHeader header) {
		this.header = header;
	}

	public Integer getLevel() {
		return (Integer) this.levelSpin.getValue();
	}

	/*public class ZoomInActionClass extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public ZoomInActionClass(String text, KeyStroke shortCut) {
				super(text);
				putValue(ACCELERATOR_KEY, shortCut);
		}

		public void actionPerformed(ActionEvent e) {
				Tree2DScrollPane tvp = (Tree2DScrollPane) treeScrollPane;
				tvp.zoomIn(null);
		}
	}

	public class ZoomOutActionClass extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public ZoomOutActionClass(String text, KeyStroke shortCut) {
				super(text);
				putValue(ACCELERATOR_KEY, shortCut);
		}

		public void actionPerformed(ActionEvent e) {
				Tree2DScrollPane tvp = (Tree2DScrollPane) treeScrollPane;
				tvp.zoomOut(null);
		}
	}*/

}
