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

import java.util.List;
import javax.swing.tree.TreeModel;
import org.biomart._80.martservicesoap.AttributeInfo;
import org.biomart._80.martservicesoap.AttributePage;
import org.biomart._80.martservicesoap.DatasetInfo;
import org.biomart._80.martservicesoap.Mart;
import org.biomart._80.martservicesoap.MartServiceSoap;
import org.gitools.ui.biomart.wizard.AttributesTreeModel;
import org.gitools.ui.wizard.common.FilteredTreePanel;

public class BiomartAttributePanel extends FilteredTreePanel {

	private MartServiceSoap port;
	private Mart mart;
	private DatasetInfo dataset;

	private List<AttributePage> attrPages;

	private AttributeInfo attribute;

	private boolean updated;

	public BiomartAttributePanel(MartServiceSoap port) {
		super();

		this.port = port;
		this.mart = null;
		this.dataset = null;
		this.attrPages = null;

		updated = false;

		tree.setRootVisible(false);

		updateAttributes();
	}

	private void updateAttributes() {
		try {
			if (attrPages == null) {
				attrPages = port.getAttributes(
						dataset.getName(), mart.getServerVirtualSchema());

				setAttributePages(attrPages);
			}

			final AttributesTreeModel model = new AttributesTreeModel(attrPages);

			setModel(model);
			expandAll();

			updated = true;
		}
		catch (Exception e) {
			e.printStackTrace(); //FIXME
		}
	}

	@Override
	protected TreeModel createModel(String filterText) {
		return new AttributesTreeModel(attrPages, filterText);
	}

	public synchronized void setAttributePages(List<AttributePage> attrPages) {
		this.attrPages = attrPages;
	}

	public synchronized List<AttributePage> getAttributePages() {
		return attrPages;
	}
}
