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

package org.gitools.ui.biomart.panel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.gitools.biomart.restful.model.AttributeDescription;
import org.gitools.biomart.restful.model.AttributePage;
import org.gitools.biomart.restful.model.DatasetInfo;

import org.gitools.biomart.BiomartServiceFactory;
import org.gitools.biomart.restful.BiomartRestfulService;
import org.gitools.biomart.restful.model.MartLocation;
import org.gitools.biomart.settings.BiomartSource;
import org.gitools.biomart.settings.BiomartSourceManager;
import org.gitools.ui.biomart.panel.AttributesTreeModel.AttributeWrapper;
import org.gitools.ui.wizard.common.FilteredTreePanel;

public class BiomartAttributePanel extends FilteredTreePanel {

	private BiomartRestfulService port;
	private MartLocation mart;
	private DatasetInfo dataset;

	private List<AttributePage> attrPages;

	private List<AttributeDescription> selectedAttr;
	private List<String> selectedAttrNames;
	private Set<String> selectedAttrNamesSet;

	public static interface AttributeSelectionListener {
		void selectionChanged();
	}

	private List<AttributeSelectionListener> attributeSelectionListeners =
			new ArrayList<AttributeSelectionListener>();

	public BiomartAttributePanel() {
		super();

		this.port = null;
		this.mart = null;
		this.dataset = null;
		this.attrPages = null;

		this.selectedAttr = new ArrayList<AttributeDescription>();
		this.selectedAttrNames = new ArrayList<String>();
		this.selectedAttrNamesSet = new HashSet<String>();

		tree.setRootVisible(false);
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override public void valueChanged(TreeSelectionEvent e) {
				selectionChanged(e);
			}
		});
	}
/*
	public void setBiomartParameters(
			MartServiceSoap port,
			MartLocation mart,
			DatasetInfo dataset) {

		this.port = port;
		this.mart = mart;
		this.dataset = dataset;

		loadAttributePages();
	}
	*/
	public void setBiomartParameters(
			BiomartRestfulService port,
			MartLocation mart,
			DatasetInfo dataset) {

		this.port = port;
		this.mart = mart;
		this.dataset = dataset;

		loadAttributePages();
	}


	private void loadAttributePages() {
		setControlsEnabled(false);

		TreeNode node = new DefaultMutableTreeNode("Loading...");
		TreeModel model = new DefaultTreeModel(node);
		setModel(model);

		//TODO fire loading starts

		new Thread(new Runnable() {
			@Override public void run() {
				loadingThread(); }
		}).start();
	}

	public List<AttributeDescription> getSelectedAttributes() {
		return selectedAttr;
	}

	public List<String> getSelectedAttributeNames() {
		return selectedAttrNames;
	}

	private void loadingThread() {
		try {

			List<BiomartSource> lBs = BiomartSourceManager.getDefault().getSources();
			BiomartSource bsrc = lBs.get(0);
			BiomartRestfulService service = BiomartServiceFactory.createRestfulService(bsrc);

			final List<AttributePage> pages = service.getAttributes(mart, dataset);
					//BiomartCentralPortalSoapService.getDefault().getAttributes(mart, dataset);

			SwingUtilities.invokeAndWait(new Runnable() {
				@Override public void run() {
					setControlsEnabled(true);

					setAttributePages(pages);

					//TODO fire loading ends
				}
			});
		}
		catch (Exception e) {
			//TODO fire loading exception
			e.printStackTrace();
		}
	}

	private void selectionChanged(TreeSelectionEvent e) {
		TreePath[] paths = e.getPaths();
		if (paths == null)
			return;

		StringBuilder sb = new StringBuilder();

		for (TreePath sel : paths) {
			DefaultMutableTreeNode node =
					(DefaultMutableTreeNode) sel.getLastPathComponent();
			AttributeWrapper attrw =
					(AttributeWrapper) node.getUserObject();
			AttributeDescription attribute =
					attrw.getType() == AttributeWrapper.NodeType.ATTRIBUTE
					? (AttributeDescription) attrw.getObject() : null;

			if (e.isAddedPath(sel)) {
				if (attribute != null) {
					if (!selectedAttrNamesSet.contains(attribute.getInternalName())) { // xrp: check if internal name instead
						sb.setLength(0);

						Object[] opath = sel.getPath();
						if (opath.length > 1) {
							sb.append(opath[1].toString());
							for (int i = 2; i < opath.length; i++)
								sb.append(" > ").append(opath[i].toString());
						}

						selectedAttr.add(attribute);
						selectedAttrNames.add(sb.toString());
						selectedAttrNamesSet.add(attribute.getInternalName()); // xrp: check if internal name instead

						for (AttributeSelectionListener l : attributeSelectionListeners)
							l.selectionChanged();
					}
				}
				else
					tree.getSelectionModel().removeSelectionPath(sel);
			}
			else if (attribute != null) {
				int i = selectedAttr.indexOf(attribute);
				selectedAttr.remove(i);
				selectedAttrNames.remove(i);
				selectedAttrNamesSet.remove(attribute.getInternalName());// xrp: check if internal name instead

				for (AttributeSelectionListener l : attributeSelectionListeners)
					l.selectionChanged();
			}
		}
	}

	public void addAttributeSelectionListener(AttributeSelectionListener listener) {
		attributeSelectionListeners.add(listener);
	}

	public void removeAttributeSelectionListener(AttributeSelectionListener listener) {
		attributeSelectionListeners.remove(listener);
	}

	private void setControlsEnabled(boolean enabled) {
		filterField.setEnabled(enabled);
		//expandBtn.setEnabled(enabled);
		//collapseBtn.setEnabled(enabled);
	}

	@Override
	protected TreeModel updateModel(String filterText) {
		if (attrPages == null)
				return null;

		return new AttributesTreeModel(attrPages, filterText);
	}

	public synchronized void setAttributePages(List<AttributePage> attrPages) {
		this.attrPages = attrPages;

		final AttributesTreeModel model = attrPages == null ? null :
				new AttributesTreeModel(attrPages);
		
		setModel(model);
		expandAll();
	}

	public synchronized List<AttributePage> getAttributePages() {
		return attrPages;
	}
}
