package org.gitools.ui.biomart.panel;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.biomart._80.martservicesoap.AttributeCollection;
import org.biomart._80.martservicesoap.AttributeGroup;
import org.biomart._80.martservicesoap.AttributeInfo;
import org.biomart._80.martservicesoap.AttributePage;

/*import org.gitools.biomart.cxf.AttributeCollection;
import org.gitools.biomart.cxf.AttributeGroup;
import org.gitools.biomart.cxf.AttributeInfo;
import org.gitools.biomart.cxf.AttributePage;*/

public class AttributesTreeModel extends DefaultTreeModel {

	private static final long serialVersionUID = 8325493802045161663L;

	public static class AttributeWrapper {

		public enum NodeType {
			ROOT, PAGE, GROUP, COLLECTION, ATTRIBUTE
		}
		
		private Object object;
		private NodeType type;
		private String name;
		
		public AttributeWrapper(Object object) {
			this.object = object;
			type = nodeType(object);
			name = nodeName(object, type);
			if (name.endsWith(":"))
				name = name.substring(0, name.length() - 1);
		}
		
		private NodeType nodeType(Object o) {
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
		
		private String nodeName(Object o, NodeType type) {
			switch (type) {
			case ROOT: return "root";
			case PAGE: return ((AttributePage) object).getDisplayName();
			case GROUP: return ((AttributeGroup) object).getDisplayName();
			case COLLECTION: return ((AttributeCollection) object).getDisplayName();
			case ATTRIBUTE: return ((AttributeInfo) object).getDisplayName();
			default: return "unknown type";
			}
		}
	
		public String getName() {
			return name;
		}
		
		public NodeType getType() {
			return type;
		}
		
		public Object getObject() {
			return object;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	public AttributesTreeModel(List<AttributePage> pages) {
		this(pages, "");
	}
	
	public AttributesTreeModel(List<AttributePage> pages, String filterText) {
		super(new DefaultMutableTreeNode("root"));
		
		filterText = filterText.toLowerCase();
		
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) getRoot();
		for (AttributePage page : pages) {
			if (page.getFormatters().toLowerCase().contains("tsv")) {
				DefaultMutableTreeNode pageNode =
					new DefaultMutableTreeNode(new AttributeWrapper(page));
				
				if (populatePage(pageNode, page.getAttributeGroup(), filterText) > 0)
					node.add(pageNode);
			}
		}
	}

	private int populatePage(
			DefaultMutableTreeNode node,
			List<AttributeGroup> groups, String filterText) {
		
		for (AttributeGroup group : groups) {
			DefaultMutableTreeNode groupNode =
				new DefaultMutableTreeNode(new AttributeWrapper(group));
			
			if (populateGroup(groupNode, group.getAttributeCollection(), filterText) > 0)
				node.add(groupNode);
		}
		
		return node.getChildCount();
	}

	private int populateGroup(
			DefaultMutableTreeNode node,
			List<AttributeCollection> collections, String filterText) {
		
		for (AttributeCollection coll : collections) {
			DefaultMutableTreeNode collNode =
				new DefaultMutableTreeNode(new AttributeWrapper(coll));
			
			if (populateCollection(collNode, coll.getAttributeInfo(), filterText) > 0)
				node.add(collNode);
		}
		
		return node.getChildCount();
	}

	private int populateCollection(
			DefaultMutableTreeNode node,
			List<AttributeInfo> attrs, String filterText) {
				
		for (AttributeInfo attr : attrs) {
			AttributeWrapper aw = new AttributeWrapper(attr);
			if (filterText.isEmpty()
					|| aw.getName().toLowerCase().contains(filterText))
				node.add(new DefaultMutableTreeNode(aw));
		}
		
		return node.getChildCount();
	}
}
