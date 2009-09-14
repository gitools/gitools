package org.gitools.ui.wizard.biomart;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.biomart._80.martservicesoap.AttributeCollection;
import org.biomart._80.martservicesoap.AttributeGroup;
import org.biomart._80.martservicesoap.AttributePage;
import org.biomart._80.martservicesoap.MartServiceSoap;
import org.gitools.ui.wizard.common.FilteredTreePage;

public class BiomartAttributePage extends FilteredTreePage {

	private static class AttributesTreeModel implements TreeModel {
		private List<TreeModelListener> listeners;
		private List<AttributePage> pages;
		
		private enum NodeType {
			ROOT, PAGE, GROUP, COLLECTION, ATTRIBUTE
		}
		
		public AttributesTreeModel(List<AttributePage> pages) {
			this.pages = pages;
			this.listeners = new ArrayList<TreeModelListener>();
		}
		
		@Override
		public void addTreeModelListener(TreeModelListener l) {
			listeners.add(l);
		}
		
		@Override
		public void removeTreeModelListener(TreeModelListener l) {
			listeners.remove(l);
		}
		
		@Override
		public int getChildCount(Object parent) {
			switch (nodeType(parent)) {
			case ROOT:
				return pages.size();
			case PAGE:
				return ((AttributePage) parent).getAttributeGroup().size();
			case GROUP:
				return ((AttributeGroup) parent).getAttributeCollection().size();
			case COLLECTION:
				return ((AttributeCollection) parent).getAttributeInfo().size();
			default:
				return 0;
			}
		}
		
		@Override
		public Object getChild(Object parent, int index) {
			switch (nodeType(parent)) {
			case ROOT:
				return pages.get(index);
			case PAGE:
				return ((AttributePage) parent).getAttributeGroup().get(index);
			case GROUP:
				return ((AttributeGroup) parent).getAttributeCollection().get(index);
			case COLLECTION:
				return ((AttributeCollection) parent).getAttributeInfo().get(index);
			default:
				return null;
			}
		}
		
		@Override
		public int getIndexOfChild(Object parent, Object child) {
			List<?> list;
			switch (nodeType(parent)) {
			case ROOT:
				return pages.indexOf(child);
			case PAGE:
				list = ((AttributePage) parent).getAttributeGroup();
				return list.indexOf(child);
			case GROUP:
				list = ((AttributeGroup) parent).getAttributeCollection();
				return list.indexOf(child);
			case COLLECTION:
				list = ((AttributeCollection) parent).getAttributeInfo();
				return list.indexOf(child);
			default:
				return 0;
			}
		}
		
		@Override
		public Object getRoot() {
			return this;
		}
		
		@Override
		public boolean isLeaf(Object node) {
			return nodeType(node) == NodeType.ATTRIBUTE;
		}
		
		@Override
		public void valueForPathChanged(TreePath path, Object newValue) {
			// nothing to do
		}
		
		public NodeType nodeType(Object o) {
			if (o == this)
				return NodeType.ROOT;
			else if (o instanceof AttributePage)
				return NodeType.PAGE;
			else if (o instanceof AttributeGroup)
				return NodeType.GROUP;
			else if (o instanceof AttributeCollection)
				return NodeType.COLLECTION;
			return NodeType.ATTRIBUTE;
		}
	}
	
	private MartServiceSoap port;
	private String datasetName;
	private String virtualSchema;
	
	private boolean updated;
	
	public BiomartAttributePage(MartServiceSoap port) {
		super();
	
		this.port = port;
		
		updated = false;
	}
	
	@Override
	public JComponent createControls() {
		JComponent component = super.createControls();
		panel.tree.setRootVisible(false);
		return component;
	}
	
	@Override
	public void updateControls() {
		if (updated)
			return;
		
		setMessage("Retrieving available attributes ...");
		
		try {
			List<AttributePage> attrPages = port.getAttributes(datasetName, virtualSchema);
			AttributesTreeModel model = new AttributesTreeModel(attrPages);
			panel.tree.setModel(model);
		}
		catch (Exception e) {
			setMessage(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void setSource(String datasetName, String virtualSchema) {
		this.datasetName = datasetName;
		this.virtualSchema = virtualSchema;
	}
}
