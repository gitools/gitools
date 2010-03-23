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

import java.util.List;
import org.gitools.biomart.BiomartGenericRestfulService;
import org.gitools.biomart.BiomartServiceException;
import org.gitools.biomart.restful.model.MartLocation;
import org.gitools.biomart.settings.BiomartSource;
import org.gitools.biomart.settings.BiomartSourceManager;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;


public class BiomartRestServiceTest {

	private BiomartGenericRestfulService bs;
	private static final Logger log = LoggerFactory.getLogger(BiomartRestServiceTest.class.getName());

	@Before
	public void before() {

		bs = defaultConnexionTest();

	}

	private BiomartGenericRestfulService defaultConnexionTest() {
		BiomartGenericRestfulService srv = null;
		try {

			List<BiomartSource> lBs = BiomartSourceManager.getDefault().getSources();

			BiomartSource bsrc = lBs.get(0);
			assertNotNull(bsrc);

			srv = new BiomartGenericRestfulService(bsrc);
			assertNotNull(srv);


		} catch (BiomartServiceException ex) {
			log.error(ex.getMessage());
		}

		return srv;
	}

	@Test
	public void getRegistry() {
		try {

			List<MartLocation> lMart = bs.getRegistry();

			assertNotNull(lMart);
			assertTrue(lMart.size() > 0);

		} catch (BiomartServiceException ex) {
			log.error(ex.getMessage());
		}
	}
}
