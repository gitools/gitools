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

package org.gitools.ui.biomart.wizard;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeSelectionModel;

import org.gitools.biomart.restful.model.AttributeDescription;
import org.gitools.biomart.restful.model.AttributePage;
import org.gitools.biomart.restful.model.DatasetInfo;
import org.gitools.biomart.restful.model.MartLocation;
import org.gitools.biomart.BiomartService;
import org.gitools.biomart.restful.model.DatasetConfig;

import org.gitools.ui.biomart.panel.AttributesTreeModel;
import org.gitools.ui.biomart.panel.AttributesTreeModel.AttributeWrapper;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.dialog.ExceptionDialog;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.wizard.common.FilteredTreePage;
import org.gitools.ui.wizard.common.FilteredTreePanel;

public class BiomartAttributePage extends FilteredTreePage {
	
	private MartLocation mart;

	private DatasetInfo dataset;
	
	private List<AttributePage> attrPages;

	private AttributeDescription attribute;
	
	private BiomartService biomartService;

	private DatasetConfig biomartConfig;

	private Boolean reloadData; //determine whether update panel data
	
	public BiomartAttributePage() {
		super();
		
		this.mart = null;
		this.dataset = null;
		this.attrPages = null;

	}
	
	@Override
	public JComponent createControls() {
		JComponent component = super.createControls();
		panel.tree.setRootVisible(false);
		panel.tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		panel.tree.addTreeSelectionListener(new TreeSelectionListener() {	
			@Override public void valueChanged(TreeSelectionEvent e) {
				selectionChanged();
			}
		});
		
		return component;
	}
	
	@Override
	public void updateControls() {
		
		setComplete(false);

		panel.setModel(new AttributesTreeModel(new ArrayList<AttributePage>(0)));
		
		new Thread(new Runnable() {
			@Override public void run() {
				try {
					if (attrPages == null || reloadData) {
						setStatus(MessageStatus.PROGRESS);
						setMessage("Retrieving available attributes ...");
						
						biomartConfig = biomartService.getConfiguration(dataset);
						attrPages = biomartConfig.getAttributePages();

					}
					
					final AttributesTreeModel model = new AttributesTreeModel(attrPages);
					
					SwingUtilities.invokeAndWait(new Runnable() {
						@Override public void run() {
							FilteredTreePanel p = getPanel();
							p.setModel(model);
							p.expandAll();
							setMessage(MessageStatus.INFO, "");
						}
					});
					
				}
				catch (final Throwable cause) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override public void run() {
							setStatus(MessageStatus.ERROR);
							setMessage(cause.getClass().getSimpleName() + ": " + cause.getMessage());
							ExceptionDialog dlg = new ExceptionDialog(AppFrame.instance(), cause);
							dlg.setVisible(true);
						}
					});
				}
			}
		}).start();
	}

	private void selectionChanged() {
		final DefaultMutableTreeNode node = 
			(DefaultMutableTreeNode) panel.tree.getLastSelectedPathComponent();
		
		boolean complete = false;
		
		if (node != null && (node.getUserObject() instanceof AttributeWrapper)) {				
			AttributeWrapper aw = (AttributeWrapper) node.getUserObject();
			complete = aw.getType() == AttributeWrapper.NodeType.ATTRIBUTE;
			if (complete)
				attribute = (AttributeDescription) aw.getObject();
		}
		
		setComplete(complete);
	}
	
	public void setSource(BiomartService biomartService, MartLocation mart, DatasetInfo dataset) {

		if (this.dataset != null && this.dataset.getName().equals(dataset.getName()))
			reloadData = false;
		else
			reloadData = true;

		this.biomartService = biomartService;
		this.mart = mart;
		this.dataset = dataset;
	}

	public synchronized void setAttributePages(List<AttributePage> attrPages) {
		this.attrPages = attrPages;
		reloadData = false;
	}
	
	public synchronized List<AttributePage> getAttributePages() {
		return attrPages;
	}

	public AttributeDescription getAttribute() {
		return attribute;
	}

	@Override
	protected TreeModel createModel(String filterText) {
		return new AttributesTreeModel(attrPages, filterText);
	}

	public DatasetConfig getBiomartConfig() {
		return biomartConfig;
	}
}
