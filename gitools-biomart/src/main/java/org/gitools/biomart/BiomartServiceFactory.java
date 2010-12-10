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

package org.gitools.biomart;

import org.gitools.biomart.restful.BiomartRestfulService;
import org.gitools.biomart.settings.BiomartSource;

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
	public static BiomartService createService(BiomartSource source) throws BiomartServiceException {
		BiomartService bs = new BiomartRestfulService(source);
		return bs;
	}
}
