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
package org.gitools.biomart.filtermanagement;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.dom4j.io.XMLWriter;
import org.dom4j.io.OutputFormat;

import org.gitools.biomart.BiomartServiceException;
import org.gitools.biomart.BiomartServiceFactory;
import org.gitools.biomart.soap.BiomartSoapService;
import org.gitools.biomart.soap.model.DatasetInfo;
import org.gitools.biomart.soap.model.Filter;

import org.gitools.biomart.soap.model.Mart;
import org.gitools.biomart.restful.model.DatasetConfig;
import org.gitools.biomart.restful.model.FilterCollection;
import org.gitools.biomart.restful.model.FilterGroup;
import org.gitools.biomart.restful.model.FilterPage;

public class BiomartFilterManager {

	private final String FILTER_PAGE_ID = "filters";
	private BiomartFilterManager instance;

	public BiomartFilterManager getInstance() {
		if (instance == null) {
			instance = new BiomartFilterManager();
		}

		return instance;
	}

	private BiomartFilterManager() {
	}

	/**
	 * Retrieves a Filter object from a panel.
	 * The Filter Object permits to build the Query
	 * @param panel
	 * @return
	 */
	public List<Filter> getBiomartFilters(IBiomartFilterPanel panel) {
		return panel.getFilters();

	}

	/**
	 * Given a FilterCollection and a group produces the associated panel
	 * @param fpg
	 * @param fd
	 * @param groupName
	 * @param collectionName
	 * @return
	 */
	public IBiomartFilterPanel createBiomartFilterPanel(DatasetConfig ds,
			String groupName, String collectionName) throws BiomartServiceException {

		IBiomartFilterPanel panel = null;

		//transform to FilterCollection invocations
		FilterCollection fc = getFilterCollection(ds, groupName, collectionName);

		String displayType = fc.getFilterDescriptions().get(0).getDisplayType();
		String multipleValues = fc.getFilterDescriptions().get(0).getMultipleValues();
		String style = fc.getFilterDescriptions().get(0).getStyle();
		String graph = fc.getFilterDescriptions().get(0).getGraph();

		if (displayType.equals("container")) {
			String displayStyleOption = fc.getFilterDescriptions().get(0).getOptions().get(0).getDisplayType();
			String multipleValuesOption = fc.getFilterDescriptions().get(0).getOptions().get(0).getMultipleValues();

			if (displayStyleOption.equals("list")) {
				if (multipleValuesOption.equals("1")) {
					panel = new CheckBoxListBiomartFilterPanel(fc);
				} else {
					panel = new RadioListBiomartFilterPanel(fc);
				}
			} else if (displayStyleOption.equals("text")) {
				panel = new TextBiomartFilterPanel(fc);
			}

		} else if (displayType.equals("list")) {
			if (style.equals("menu") && !graph.equals("1")) {
				panel = new SelectBiomartFilterPanel(fc);
			} else if (style.equals("menu") && graph.equals("1")) {
				panel = new TextBiomartFilterPanel(fc);
			} else if (multipleValues.equals("1")) {
				panel = new CheckBoxListBiomartFilterPanel(fc);
			} else {
				panel = new RadioListBiomartFilterPanel(fc);
			}
		} else if (displayType.equals("text")) {

			panel = new TextBiomartFilterPanel(fc);
		}

		return panel;
	}

	private FilterCollection getFilterCollection(DatasetConfig ds, String groupName, String collectionName) throws BiomartServiceException {
		for (FilterPage f : ds.getFilterPages()) {
			if (f.getInternalName() != null && f.getInternalName().equals(FILTER_PAGE_ID)) {
				for (FilterGroup g : f.getFilterGroups()) {
					if (g.getInternalName().equals(groupName)) {
						for (FilterCollection c : g.getFilterCollections()) {
							if (c.getInternalName().equals(collectionName)) {
								return c;
							}
						}
					}
				}
			}

		}
		throw new BiomartServiceException("No Filter collection found for input options");
	}

	public static void main(String[] s) throws BiomartServiceException {

		try
		{
			JAXBContext jc = JAXBContext.newInstance(DatasetConfig.class.getPackage().getName());
			Marshaller m = jc.createMarshaller();
			// Build the xml envelop

			ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
			OutputFormat form = new OutputFormat();
			XMLWriter writer = new XMLWriter(baos1, form);

			DatasetConfig dc = new DatasetConfig();
			m.marshal(dc, baos1);
			writer.flush();
			System.out.println(baos1);

			//Create Service connexion
			BiomartSoapService r = BiomartServiceFactory.createDefaultSoapService();

			// Retrieve lists of marts, dataset infos and filters
			List<Mart> l = r.getRegistry();
			List<DatasetInfo> ld = r.getDatasets(l.get(0));

			DatasetConfig ds = r.getDatasetConfig(ld.get(0));
			ds.getDataset();

			/**
			 * UNMARSHALL
			jc = JAXBContext.newInstance(DatasetConfig.class.getPackage().getName());
			Unmarshaller u = jc.createUnmarshaller();
			InputStreamReader in = new InputStreamReader(new FileInputStream("C:\\Work\\configDS.xml"));

			DatasetConfig ds = (DatasetConfig) u.unmarshal(in);
			ds.getDataset();
			 */

		}
		catch (IOException ex) {
			Logger.getLogger(BiomartFilterManager.class.getName()).log(Level.SEVERE, null, ex);
		} catch (JAXBException ex) {
			Logger.getLogger(BiomartFilterManager.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
