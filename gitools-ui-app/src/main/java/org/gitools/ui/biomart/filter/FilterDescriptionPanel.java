/*
 *  Copyright 2010 xavier.
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
 * FilterDescriptionPanel1.java
 *
 * Created on Apr 2, 2010, 9:42:28 AM
 */
package org.gitools.ui.biomart.filter;

import java.util.List;
import javax.swing.BoxLayout;
import org.gitools.biomart.restful.model.DatasetConfig;
import org.gitools.biomart.restful.model.DatasetInfo;
import org.gitools.biomart.restful.model.Filter;
import org.gitools.biomart.restful.model.FilterCollection;
import org.gitools.biomart.restful.model.FilterDescription;
import org.gitools.biomart.restful.model.FilterGroup;
import org.gitools.biomart.restful.model.FilterPage;
import org.gitools.biomart.restful.model.Option;

public class FilterDescriptionPanel extends javax.swing.JPanel {

	private FilterCollectionPanel parentCollection;
	private FilterDescription filterDescription;
	private FilterComponent filterComponent;
	private Integer currentHeight;
	private Boolean renderPanel;
	private Boolean renderLabel;

	public FilterDescriptionPanel(FilterDescription description, FilterCollectionPanel collectionParent, boolean labelRendered) {

		this.filterDescription = description;
		this.parentCollection = collectionParent;
		this.currentHeight = 0;
		this.renderLabel = labelRendered;

		if (filterDescription.getDisplayType() != null) {

			this.renderPanel = true;

			buildComponent();

		}
		else if (filterDescription.getPointerDataset() != null) {

			try {
				DatasetInfo d = new DatasetInfo();

				d.setName(filterDescription.getPointerDataset());

				d.setInterface(filterDescription.getPointerInterface());

				DatasetConfig configuration = getParentCollection().getFilterConfigurationPage().getBiomartService().getConfiguration(d);

				buildPointerFilterComponents(configuration);				

				renderPanel = true;
			}
			catch (final Exception ex) {

				System.out.println("Pointer dataset :" +filterDescription.getPointerDataset()+ " has not been found");

				renderPanel = false;

				return;
			}
		}
		else  this.renderPanel = false;
	}

	/**
	 * BuildPointer elements with default options if it is the case
	 * @param configuration
	 * @param DefaultOptions
	 */
	private void buildPointerFilterComponents(DatasetConfig configuration) {

		for (FilterPage page : configuration.getFilterPages())

			if (!page.isHidden() && !page.isHideDisplay())

				for (FilterGroup group : page.getFilterGroups())

					if (!group.isHidden() && !group.isHideDisplay())

						for (FilterCollection collection : group.getFilterCollections())

							for (FilterDescription desc : collection.getFilterDescriptions())

								if ((desc.getInternalName().equals(filterDescription.getPointerFilter()))
									&& (!desc.isHideDisplay())) {

									this.renderPanel = true;

									// WARN: This code is for solving bugs in xml config avoiding build combos
									// where it is not the case
									// SOLUTION is check if component receives pushactions then it is a
									// combo component!

									if (parentCollection.getFilterConfigurationPage().
											getDefaultSelecComposData().get(filterDescription.getPointerFilter()) != null){

										desc.setStyle("menu");

										desc.setDisplayType("list");
									}

									filterDescription = desc;

									buildComponent();
								}
	}

	/**
	 * Builds components for the filterDescription
	 */
	private void buildComponent() {

		String displayType = filterDescription.getDisplayType();

		boolean multipleValues = filterDescription.getMultipleValues() == 1;

		String style = filterDescription.getStyle();

		String graph = filterDescription.getGraph();

		FilterComponent child = null;

		if (displayType.equals("container")) {

			String displayStyleOption = filterDescription.getOptions().get(0).getDisplayType();

			int multipleValuesOption = filterDescription.getOptions().get(0).getMultipleValues();

			FilterComponent componentParent = new FilterSelectComponent(filterDescription, this);

			Option filterOptions = filterDescription.getOptions().get(0);


			if (displayStyleOption.equals("list")) {

				if (multipleValuesOption == 1) {

					child = new FilterCheckBoxComponent(filterOptions);

				} else {

					child = new FilterRadioComponent(filterOptions);

				}
			} else if (displayStyleOption.equals("text")) {

				child = new FilterTextComponent(filterOptions);
			}

			componentParent.addChildComponent(child);

			filterComponent = componentParent;

		} else {
			if (displayType.equals("list")) {

				if (style.equals("menu") && (graph == null || !graph.equals("1"))) {

					FilterSelectComponent selecComponent = new FilterSelectComponent(filterDescription, this);

					//Retrieve PushAction Data from default option
					parentCollection.getFilterConfigurationPage().storeSelecComponentsDefaultData(
							selecComponent.getPushActionData_defaultOption());

					filterComponent =selecComponent;
				}
				else if (style.equals("menu") && graph != null && graph.equals("1"))
					filterComponent = new FilterTextComponent(filterDescription, this);
				else if (multipleValues)
					filterComponent = new FilterCheckBoxComponent(filterDescription, this);
				else
					filterComponent = new FilterRadioComponent(filterDescription, this);
			}
			else if (displayType.equals("text"))
				filterComponent = new FilterTextComponent(filterDescription, this);
			else {
				System.out.println("Component "+ filterDescription.getInternalName() +"has not been builded");
				return;
			}
		}



		currentHeight = filterComponent.getCurrentHeight();

		this.removeAll();

		//this.setLayout(new GridLayout(child != null? 2 : 1, 1));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		this.add(filterComponent);

		if (child != null) {

			this.add(child);
			currentHeight += child.getCurrentHeight();
		}

		validate();
	}

	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 396, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 298, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

	public FilterDescription getFilterDescription() {
		return filterDescription;
	}

	public final FilterCollectionPanel getParentCollection() {
		return parentCollection;
	}

	public List<Filter> getFilters() {
		return filterComponent.getFilters();
	}

	public void setFilterComponents(FilterComponent components) {
		this.filterComponent = components;
	}

	public Boolean isChild() {
		return (filterDescription == null);
	}

	Integer getCurrentHeight() {
		return currentHeight;
	}

	public Boolean getRenderPanel() {
		return renderPanel;
	}

	public void setRenderPanel(Boolean rendered) {
		this.renderPanel = rendered;
	}

	public Boolean getRenderLabel() {
		return renderLabel;
	}

	public void setRenderLabel(Boolean renderLabel) {
		this.renderLabel = renderLabel;
	}

	


}
