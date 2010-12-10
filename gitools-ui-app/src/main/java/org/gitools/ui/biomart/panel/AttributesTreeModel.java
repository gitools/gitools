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

package org.gitools.ui.biomart.panel;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.gitools.biomart.restful.model.AttributeCollection;
import org.gitools.biomart.restful.model.AttributeGroup;
import org.gitools.biomart.restful.model.AttributeDescription;
import org.gitools.biomart.restful.model.AttributePage;

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
			if (o == this) {
				return NodeType.ROOT;
			} else if (o instanceof AttributePage) {
				return NodeType.PAGE;
			} else if (o instanceof AttributeGroup) {
				return NodeType.GROUP;
			} else if (o instanceof AttributeCollection) {
				return NodeType.COLLECTION;
			}
			return NodeType.ATTRIBUTE;
		}

		private String nodeName(Object o, NodeType type) {
			String nodeName = "unknown type";
			switch (type) {
				case ROOT:
					return "root";

				case PAGE:
					AttributePage page = ((AttributePage) object);
					nodeName = page.getDisplayName();
					if (nodeName == null)
						nodeName = page.getDescription();
					if (nodeName == null)
						nodeName = "Attribute page without name";
				break;

				case GROUP:
					AttributeGroup group = ((AttributeGroup) object);
					nodeName = group.getDisplayName();
					if (nodeName == null)
						nodeName = group.getDescription();
					if (nodeName == null)
						nodeName = group.getInternalName();
					if (nodeName == null)
						nodeName = "Attribute group without name";
				break;

				case COLLECTION:
					AttributeCollection coll = ((AttributeCollection) object);
					nodeName = coll.getDisplayName();
					if (nodeName == null)
						nodeName = coll.getDescription();
					if (nodeName == null)
						nodeName = coll.getInternalName();
					if (nodeName == null)
						nodeName = "Attribute collection without name";
				break;

				case ATTRIBUTE:
					AttributeDescription desc = ((AttributeDescription) object);
					nodeName = desc.getDisplayName();
					if (nodeName == null)
						nodeName = desc.getDescription();
					if (nodeName == null)
						nodeName = desc.getInternalName();
					if (nodeName == null)
						nodeName = "Attribute group without name";
				break;
			}
			return nodeName;
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
			if (!page.isHidden() && !page.isHideDisplay()) {
				if (page.getOutFormats().toLowerCase().contains("tsv")) {
					DefaultMutableTreeNode pageNode =
							new DefaultMutableTreeNode(new AttributeWrapper(page));

					if (populatePage(pageNode, page.getAttributeGroups(), filterText) > 0)
						node.add(pageNode);
				}
			}
		}
	}

	private int populatePage(
			DefaultMutableTreeNode node,
			List<AttributeGroup> groups, String filterText) {

		for (AttributeGroup group : groups) {
			DefaultMutableTreeNode groupNode =
					new DefaultMutableTreeNode(new AttributeWrapper(group));

			if (populateGroup(groupNode, group.getAttributeCollections(), filterText) > 0) {
				node.add(groupNode);
			}
		}

		return node.getChildCount();
	}

	private int populateGroup(
			DefaultMutableTreeNode node,
			List<AttributeCollection> collections, String filterText) {

		for (AttributeCollection coll : collections) {
			DefaultMutableTreeNode collNode =
					new DefaultMutableTreeNode(new AttributeWrapper(coll));

			if (populateCollection(collNode, coll.getAttributeDescriptions(), filterText) > 0) {
				node.add(collNode);
			}
		}

		return node.getChildCount();
	}

	private int populateCollection(
			DefaultMutableTreeNode node,
			List<AttributeDescription> attrs, String filterText) {

		for (AttributeDescription attr : attrs) {
			if (attr.getDisplayName() != null) //xrp: identified null displayNames
			{
				AttributeWrapper aw = new AttributeWrapper(attr);
				if (filterText.isEmpty()
						|| aw.getName().toLowerCase().contains(filterText)) {
					node.add(new DefaultMutableTreeNode(aw));
				}
			}
		}

		return node.getChildCount();
	}
}
