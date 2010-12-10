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

package org.gitools.stats.test.factory;

import org.gitools.model.ToolConfig;
import org.gitools.stats.test.BinomialTest;
import org.gitools.stats.test.Test;
import org.gitools.stats.test.BinomialTest.AproximationMode;

public class BinomialTestFactory extends TestFactory {

	public static final String APROXIMATION_PROPERTY = "aproximation";
	
	public static final String EXACT_APROX = "exact";
	public static final String NORMAL_APROX = "normal";
	public static final String POISSON_APROX = "poisson";
	public static final String AUTOMATIC_APROX = "automatic";
	
	private AproximationMode aproxMode;
	
	public BinomialTestFactory(ToolConfig config) {
		super(config);
		
		final String aproxModeName = config.getConfiguration().get(APROXIMATION_PROPERTY);
		
		if ("exact".equalsIgnoreCase(aproxModeName))
			this.aproxMode = AproximationMode.onlyExact;
		else if ("normal".equalsIgnoreCase(aproxModeName))
			this.aproxMode = AproximationMode.onlyNormal;
		else if ("poisson".equalsIgnoreCase(aproxModeName))
			this.aproxMode = AproximationMode.onlyPoisson;
		else if ("automatic".equalsIgnoreCase(aproxModeName))
			this.aproxMode = AproximationMode.automatic;
		else
			this.aproxMode = AproximationMode.onlyExact;
	}
	
	/*public BinomialTestFactory(AproximationMode aproxMode) {
		this.aproxMode = aproxMode;
	}*/
	
	@Override
	public Test create() {
		return new BinomialTest(aproxMode);
	}

}
