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

package org.gitools.analysis.clustering.methods;

import java.util.Properties;
import org.gitools.analysis.clustering.ClusteringMethod;
import org.gitools.analysis.AnalysisException;


public class ClusteringMethodFactory {

	public static ClusteringMethod createMethod(Properties properties) throws AnalysisException {
		String methodId =properties.getProperty("method");
		if (WekaCobWebMethod.ID.equalsIgnoreCase(methodId))
			return new WekaCobWebMethod(properties);
		else if (WekaKmeansMethod.ID.equalsIgnoreCase(methodId))
			return new WekaKmeansMethod(properties);
		else
			throw new AnalysisException("Unknown clustering method: " + methodId);
	}
}
