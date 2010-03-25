/*
 *  Copyright 2010 cperez.
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
package org.gitools.biomart.test;

import edu.upf.bg.progressmonitor.DefaultProgressMonitor;
import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import javax.xml.bind.JAXBException;
import org.gitools.biomart.BiomartCentralPortalSoapService;
import org.gitools.biomart.BiomartServiceException;
import org.gitools.biomart.BiomartServiceFactory;
import org.gitools.biomart.soap.BiomartSoapService;
import org.gitools.biomart.soap.model.Attribute;
import org.gitools.biomart.soap.model.AttributePage;
import org.gitools.biomart.soap.model.Dataset;
import org.gitools.biomart.soap.model.DatasetInfo;
import org.gitools.biomart.soap.model.Filter;
import org.gitools.biomart.soap.model.FilterPage;
import org.gitools.biomart.soap.model.Mart;
import org.gitools.biomart.soap.model.Query;
import org.gitools.biomart.restful.model.DatasetConfig;
import org.gitools.biomart.settings.BiomartSource;
import org.gitools.biomart.settings.BiomartSourceManager;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;

public class BiomartSoapServiceTest {

	private static final Logger log = LoggerFactory.getLogger(BiomartSoapServiceTest.class);

	private String source;
	private BiomartSoapService bs;

	@Before
	public void before() {
		source = "biomart";

		//bs = BiomartCentralPortalService.getDefault();

		bs = defaultConnexionTest();

	}

	private BiomartSoapService defaultConnexionTest() {
		BiomartSoapService srv = null;
		try {

			List<BiomartSource> lBs = BiomartSourceManager.getDefault().getSources();
			assertNotNull(lBs);
			assertTrue(lBs.size() > 0);

			BiomartSource bs = lBs.get(0);
			assertNotNull(bs);

			srv = BiomartServiceFactory.createDefaultSoapService();

			assertNotNull(srv);


		} catch (BiomartServiceException ex) {
			log.error(ex.getMessage());
		}

		return srv;
	}

	@Test
	public void getRegistry() {
		try {


			List<Mart> lMart = bs.getRegistry();

			assertNotNull(lMart);
			assertTrue(lMart.size() > 0);

		} catch (BiomartServiceException ex) {
			log.error(ex.getMessage());
		}
	}

	@Test
	public void getDatasets() {
		try {


			List<Mart> lMart = bs.getRegistry();
			assertNotNull(lMart);
			assertTrue(lMart.size() > 0);


			Mart m = lMart.get(0);
			List<DatasetInfo> ds = bs.getDatasets(m);
			assertNotNull(ds);
			assertTrue(ds.size() > 0);

			log.info("num DS: " + ds.size());

		} catch (BiomartServiceException ex) {
			log.error(ex.getMessage());
		}
	}

	@Test
	public void getAttributes() {
		try {



			List<Mart> lMart = bs.getRegistry();
			assertNotNull(lMart);
			assertTrue(lMart.size() > 0);

			Mart m = lMart.get(0);

			List<DatasetInfo> ds = bs.getDatasets(m);
			assertNotNull(ds);
			assertTrue(ds.size() > 0);

			DatasetInfo d = ds.get(0);

			List<AttributePage> latt = bs.getAttributes(m, d);

			log.info("MART: " + m.getDisplayName() + "DS: " + d.getDisplayName());
			assertNotNull(latt);
			assertTrue(latt.size() > 0);
			assertTrue(latt.get(0).getAttributeGroup().size() > 0);
			assertTrue(latt.get(0).getAttributeGroup().get(0).getAttributeCollection().size() > 0);
			assertNotNull(latt.get(0).getAttributeGroup().get(0).getAttributeCollection().get(0).getName());
			assertNotSame(latt.get(0).getAttributeGroup().get(0).getAttributeCollection().get(0).getName(), "");

			log.info("Num Att Pages: " + latt.size());

		} catch (BiomartServiceException ex) {
			log.error(null, ex);
		}
	}

	@Test
	public void getFilters() {
		try {



			List<Mart> lMart = bs.getRegistry();
			assertNotNull(lMart);
			assertTrue(lMart.size() > 0);

			Mart m = lMart.get(0);

			List<DatasetInfo> ds = bs.getDatasets(m);
			assertNotNull(ds);
			assertTrue(ds.size() > 0);

			DatasetInfo d = ds.get(0);

			List<FilterPage> lf = bs.getFilters(m, d);

			log.info("MART: " + m.getDisplayName() + "DS: " + d.getDisplayName());
			assertNotNull(lf);
			assertTrue(lf.size() > 0);
			assertTrue(lf.get(0).getFilterGroup().size() > 0);
			assertTrue(lf.get(0).getFilterGroup().get(0).getFilterCollection().size() > 0);
			assertNotNull(lf.get(0).getFilterGroup().get(0).getFilterCollection().get(0).getName());
			assertNotSame(lf.get(0).getFilterGroup().get(0).getFilterCollection().get(0).getName(), "");

			log.info("Num Filter Pages: " + lf.size());
			log.info("Num Filter Groups: " + lf.get(0).getFilterGroup().size());


		} catch (BiomartServiceException ex) {
			log.error(ex.getMessage());
		}
	}

	@Test
	public void QueryAsStream() {
		try {
			InputStream in = null;
			BufferedReader br = null;


			List<Mart> lMart = bs.getRegistry();
			assertNotNull(lMart);
			assertTrue(lMart.size() > 0);

			Mart mart = lMart.get(0);

			List<DatasetInfo> lDs = bs.getDatasets(mart);
			assertNotNull(lDs);
			assertTrue(lDs.size() > 0);

			DatasetInfo d = lDs.get(0);

			log.info("MART: " + mart.getDisplayName() + "DS: " + d.getDisplayName());

			List<FilterPage> lf = bs.getFilters(mart, d);
			List<AttributePage> dsattrs = bs.getAttributes(mart, d);

			Query query = createSimpleQuery(lf, dsattrs, mart, d);
/*
			JAXBContext context = JAXBContext.newInstance(Query.class);
			Marshaller m = context.createMarshaller();
			m.marshal(query, System.out);
			System.out.println();
*/
			in = bs.queryAsStream(query, "TSV");
			assertNotNull(in);
			br = new BufferedReader(new InputStreamReader(in));
			assertNotNull(br);

			String res = null;
			Integer rows = 0;
			while ((res = br.readLine()) != null) {
				rows++;
			}
			log.info("NumRows: " + rows);
			assertTrue(rows > 0);


		}  catch (IOException ex) {
			log.error(ex.getMessage());
		} catch (BiomartServiceException ex) {
			log.error(ex.getMessage());
		}

	}

	private Query createSimpleQuery(List<FilterPage> lf, List<AttributePage> dsattrs, Mart mart, DatasetInfo d) throws BiomartServiceException {

		Attribute a = new Attribute();
		a.setName(dsattrs.get(0).getAttributeGroup().get(0).getAttributeCollection().get(0).getAttributeInfo().get(0).getName());

		Attribute a1 = new Attribute();
		a1.setName(dsattrs.get(0).getAttributeGroup().get(0).getAttributeCollection().get(0).getAttributeInfo().get(1).getName());

		Filter f = new Filter();
		f.setName(lf.get(0).getFilterGroup().get(0).getFilterCollection().get(0).getFilterInfo().get(0).getName());
		f.setValue("1");

		log.info("ATT: " + a.getName() + "," + a1.getName() + " FILTER: " + f.getName() + " VAL:" + f.getValue());
		log.info("VirtualSchema: "+mart.getServerVirtualSchema());
		
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

	@Test
	public void queryModule() {

		try {

			final File file = new File(System.getProperty("user.home", ".") + File.separator + "testQuery.test");
			String format = "TSV";
			IProgressMonitor monitor = new DefaultProgressMonitor();

			List<Mart> lMart = bs.getRegistry();
			assertNotNull(lMart);
			assertTrue(lMart.size() > 0);

			Mart mart = lMart.get(0);
			List<DatasetInfo> lDs = bs.getDatasets(mart);
			assertNotNull(lDs);
			assertTrue(lDs.size() > 0);

			DatasetInfo d = lDs.get(0);

			List<FilterPage> lf = bs.getFilters(mart, d);
			List<AttributePage> dsattrs = bs.getAttributes(mart, d);

			Query query = createSimpleQuery(lf, dsattrs, mart, d);

			bs.queryModule(query, file, format, monitor);
			log.info("File lenght: " + file.length());
			assertTrue(file.length() > 0);
			file.delete();

		} catch (BiomartServiceException ex) {
			log.error(ex.getMessage());
		}

	}

	@Test
	public void queryTable() {
		try {
			final File file = new File(System.getProperty("user.home", ".") + File.separator + "testQuery.test");
			String format = "TSV";
			IProgressMonitor monitor = new DefaultProgressMonitor();

			List<Mart> lMart = bs.getRegistry();
			assertNotNull(lMart);
			assertTrue(lMart.size() > 0);
			Mart mart = lMart.get(0);
			List<DatasetInfo> lDs = bs.getDatasets(mart);
			assertNotNull(lDs);
			assertTrue(lDs.size() > 0);
			DatasetInfo d = lDs.get(0);
			List<FilterPage> lf = bs.getFilters(mart, d);
			List<AttributePage> dsattrs = bs.getAttributes(mart, d);
			Query query = createSimpleQuery(lf, dsattrs, mart, d);

			bs.queryTable(query, file, format, true, "", monitor);

			log.info("File lenght: " + file.length());
			assertTrue(file.length() > 0);
			file.delete();


		} catch (BiomartServiceException ex) {
			log.error(ex.getMessage());
		}
	}

	@Test
	public void getDatasetConfig() throws IOException, JAXBException {

		try {

			List<Mart> lMart = bs.getRegistry();
			assertNotNull(lMart);
			assertTrue(lMart.size() > 0);
			Mart mart = lMart.get(0);
			List<DatasetInfo> lDs = bs.getDatasets(mart);
			assertNotNull(lDs);
			assertTrue(lDs.size() > 0);
			DatasetInfo d = lDs.get(0);
			DatasetConfig conf = bs.getDatasetConfig(d);
			assertNotNull(conf);
			assertTrue(conf.getAttributePages().size() > 0);
			assertTrue(conf.getAttributePages().get(0).getAttributeGroups().size() > 0);
			assertTrue(conf.getAttributePages().get(0).getAttributeGroups().get(0).getAttributeCollections().size() > 0);

		} catch (BiomartServiceException ex) {
			log.error(ex.getMessage());
		}

	}
}
