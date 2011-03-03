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

package org.gitools.matrix.clustering.methods;

import java.util.Properties; 
import org.gitools.matrix.clustering.ClusterException;


public class ClusterMethodFactory {

	public static ProposedClusterMethod createMethod(Properties properties) throws ClusterException {
		String methodId = properties.getProperty("method");

		if (methodId.toLowerCase().contains(WekaCobWebMethod.ID))
			return new WekaCobWebMethod(properties);
		else if (methodId.toLowerCase().contains(WekaKmeansMethod.ID))
			return new WekaKmeansMethod(properties);
		else if (methodId.toLowerCase().contains(WekaHCLMethod.ID))
			return new WekaHCLMethod(properties);
		
		throw new ClusterException("Unexpected clustering error");
	}
}
