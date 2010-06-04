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

package org.gitools.biomart.test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.gitools.biomart.BiomartServiceException;
import org.gitools.biomart.BiomartServiceFactory;
import org.gitools.biomart.restful.BiomartRestfulService;
import org.gitools.biomart.restful.model.DatasetConfig;
import org.gitools.biomart.restful.model.DatasetInfo;
import org.gitools.biomart.restful.model.MartLocation;
import org.gitools.biomart.settings.BiomartSource;
import org.gitools.biomart.settings.BiomartSourceManager;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;



public class BiomartFilterTest {

	private BiomartRestfulService bs;
	private static final Logger log = LoggerFactory.getLogger(BiomartRestServiceTest.class.getName());

	@Before
	public void before() {

		bs = defaultConnexionTest();

	}


	private BiomartRestfulService defaultConnexionTest() {
		BiomartRestfulService srv = null;
		try {

			List<BiomartSource> lBs = BiomartSourceManager.getDefault().getSources();

			BiomartSource bsrc = lBs.get(0);
			assertNotNull(bsrc);

			srv = BiomartServiceFactory.createRestfulService(bsrc);
			assertNotNull(srv);


		} catch (BiomartServiceException ex) {
			log.error(ex.getMessage());
		}

		return srv;
	}
	//@Test
	public void filterManagementTest(){


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

			// Retrieve lists of marts, dataset infos and filters
			List<MartLocation> l = bs.getRegistry();
			List<DatasetInfo> ld = bs.getDatasets(l.get(0));

			DatasetConfig ds = bs.getConfiguration(ld.get(0));
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
		catch (BiomartServiceException ex) {
			log.error(ex.getMessage());
		}		catch (IOException ex) {
			log.error(ex.getMessage());
		} catch (JAXBException ex) {
			log.error(ex.getMessage());
		}
	}
}
