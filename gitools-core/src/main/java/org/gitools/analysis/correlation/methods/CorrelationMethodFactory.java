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

package org.gitools.analysis.correlation.methods;

import java.util.Properties;
import org.gitools.analysis.AnalysisException;
import org.gitools.analysis.correlation.CorrelationMethod;


public class CorrelationMethodFactory {

	public static CorrelationMethod createMethod(String methodId, Properties methodProperties) throws AnalysisException {
		if (PearsonCorrelationMethod.ID.equalsIgnoreCase(methodId))
			return new PearsonCorrelationMethod(methodProperties);
		else if (SpearmanCorrelationMethod.ID.equalsIgnoreCase(methodId))
			return new PearsonCorrelationMethod(methodProperties);
		else
			throw new AnalysisException("Unknown correlation method: " + methodId);
	}
}
