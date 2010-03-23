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
package org.gitools.biomart;

import org.gitools.biomart.soap.BiomartSoapService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import org.gitools.biomart.soap.model.Attribute;
import org.gitools.biomart.soap.model.AttributePage;
import org.gitools.biomart.soap.model.Dataset;
import org.gitools.biomart.soap.model.DatasetInfo;
import org.gitools.biomart.soap.model.Filter;
import org.gitools.biomart.soap.model.FilterPage;
import org.gitools.biomart.soap.model.Mart;
import org.gitools.biomart.soap.model.Query;
import org.gitools.biomart.settings.BiomartSource;
import org.gitools.biomart.settings.BiomartSourceManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 *Factory of BiomartService
 * @author xrp02032010
 */
public class BiomartServiceFactory {

	private BiomartServiceFactory instance;

	public BiomartServiceFactory getInstance() {
		if (instance == null) {
			instance = new BiomartServiceFactory();
		}

		return instance;
	}

	private BiomartServiceFactory() {
	}

	/**
	 * Creates a Biomart service from a Biomart source
	 * @param source
	 * @return biomart service
	 */
	public static BiomartSoapService createSoapService(BiomartSource source) throws BiomartServiceException {
		BiomartSoapService bs = new BiomartGenericSoapService(source);
		return bs;
	}


	/**
	 * Creates the default Biomart Service (currently through Biomart Central Portal)
	 * @return biomart service
	 */
	public static BiomartSoapService createDefaultSoapService() throws BiomartServiceException {

		BiomartSource bs = BiomartSourceManager.getDefault().getSources().get(0);


		return createSoapService(bs);
	}

	static public void QueryAsStream() {

		Logger log = Logger.getLogger(BiomartServiceFactory.class.getName());
		try {
			InputStream in = null;
			BufferedReader br = null;

			BiomartSoapService bs;

			bs = BiomartCentralPortalSoapService.getDefault();
			List<Mart> lMart = bs.getRegistry();

			Mart mart = lMart.get(0);
			List<DatasetInfo> lDs = bs.getDatasets(mart);

			DatasetInfo d = lDs.get(0);

			log.info("MART: "
					+ mart.getDisplayName() + "DS: " + d.getDisplayName());

			List<FilterPage> lf = bs.getFilters(mart, d);
			List<AttributePage> dsattrs = bs.getAttributes(mart, d);

			Query query = createSimpleQuery(lf, dsattrs, mart, d);

			JAXBContext context = JAXBContext.newInstance(Query.class.getPackage().getName());
			Marshaller m = context.createMarshaller();
			m.marshal(query, System.out);
			System.out.println();

			in = bs.queryAsStream(query, "TSV");

			br = new BufferedReader(new InputStreamReader(in));


			String res = null;
			Integer rows = 0;
			while ((res = br.readLine()) != null) {
				rows++;
			}
			log.info(
					"NumRows: " + rows);

		} catch (JAXBException ex) {
		Logger.getLogger(BiomartServiceFactory.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			log.severe(ex.getMessage());
		} catch (BiomartServiceException ex) {
			log.severe(ex.getMessage());
		}

	}

	private static Query createSimpleQuery(List<FilterPage> lf, List<AttributePage> dsattrs, Mart mart, DatasetInfo d) throws BiomartServiceException {

		Attribute a = new Attribute();
		a.setName(dsattrs.get(0).getAttributeGroup().get(0).getAttributeCollection().get(0).getAttributeInfo().get(0).getName());

		Attribute a1 = new Attribute();
		a1.setName(dsattrs.get(0).getAttributeGroup().get(0).getAttributeCollection().get(0).getAttributeInfo().get(1).getName());

		Filter f = new Filter();
		f.setName(lf.get(0).getFilterGroup().get(0).getFilterCollection().get(0).getFilterInfo().get(0).getName());
		f.setValue("1");


		Dataset ds = new Dataset();
		ds.setName(d.getName());
		ds.getFilter().add(f);
		ds.getAttribute().add(a);
		ds.getAttribute().add(a1);

		Query query = new Query();
		query.setVirtualSchemaName(mart.getServerVirtualSchema());
		query.setHeader(1);
		query.setCount(0);
		query.setUniqueRows(1);
		query.getDataset().add(ds);

		return query;
	}

/*
	public static void main(String[] args) throws IOException {

		BiomartSource b = BiomartSourceManager.getDefault().getSources().get(0);
		try {
			//Create Service connexion
			IBiomartService r = BiomartServiceFactory.createDefaultservice();

			// Retrieve lists of marts, dataset infos and filters
			List<Mart> l = r.getRegistry();
			System.out.println(l.get(0).getDatabase());

			List<DatasetInfo> ld = r.getDatasets(l.get(0));
			System.out.println(ld.get(0).getDisplayName());

			//Retrieve config

			InputStream in = null;
			BufferedReader br = null;
			String res = null;


			List<FilterPage> lf = r.getFilters(l.get(0), ld.get(0));

			System.out.println(lf.get(0).getFilterGroup().get(0).getFilterCollection().get(0).getFilterInfo().get(0).getName());
			System.out.println(lf.get(0).getFilterGroup().get(0).getFilterCollection().get(0).getFilterInfo().get(0).getOptions());
			System.out.println(lf.get(0).getFilterGroup().get(0).getFilterCollection().get(0).getFilterInfo().get(0).getQualifier());

			//Preparing a QUERY with a Filter

			Mart mart = l.get(0);
			List<AttributePage> dsattrs = r.getAttributes(mart, ld.get(0));
			Attribute a = new Attribute();
			a.setName(dsattrs.get(0).getAttributeGroup().get(0).getAttributeCollection().get(0).getAttributeInfo().get(0).getName());

			Filter f = new Filter();
			f.setName(lf.get(0).getFilterGroup().get(0).getFilterCollection().get(0).getFilterInfo().get(0).getName());
			f.setValue("STOP_GAINED");

			Dataset ds = new Dataset();
			ds.setName(ld.get(0).getName());
			ds.getFilter().add(f);
			ds.getAttribute().add(a);

			Query query = new Query();
			query.setVirtualSchemaName(mart.getServerVirtualSchema());
			query.setHeader(1);
			query.setCount(0);
			query.setUniqueRows(1);
			query.getDataset().add(ds);

			//Execute QUERY
			in = r.queryAsStream(query, "TSV");
			br = new BufferedReader(new InputStreamReader(in));
			res = null;
			while ((res = br.readLine()) != null) {
				System.out.println(res);
			}
			BiomartServiceFactory.QueryAsStream();

		} catch (BiomartServiceException ex) {
			System.out.println(ex);
		}

	}
*/
}
