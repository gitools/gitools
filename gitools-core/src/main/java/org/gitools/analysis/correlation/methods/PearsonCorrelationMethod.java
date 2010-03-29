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

package org.gitools.analysis.correlation.methods;

import java.util.Properties;
import org.gitools.analysis.AbstractMethod;
import org.gitools.analysis.MethodException;
import org.gitools.analysis.correlation.CorrelationMethod;
import org.gitools.analysis.correlation.CorrelationResult;

public class PearsonCorrelationMethod extends AbstractMethod implements CorrelationMethod {

	public static final String ID = "pearson";

	public PearsonCorrelationMethod() {
		this(new Properties());
	}

	public PearsonCorrelationMethod(Properties properties) {
		super(ID,
				"Pearson's correlation",
				"Pearson's product-moment correlation",
				CorrelationResult.class, properties);
	}

	@Override
	public CorrelationResult correlation(double[] x, double[] y, int[] indices, int indicesLength) throws MethodException {
		CorrelationResult result = new CorrelationResult();

		double sumxy = 0;
		double sumx = 0;
		double sumx2 = 0;
		double sumy = 0;
		double sumy2 = 0;
		double n = indicesLength;

		for (int k = 0; k < indicesLength; k++) {
			int i = indices[k];
			double xi = x[i];
			double yi = y[i];
			sumxy += xi * yi;
			sumx += xi;
			sumx2 += xi * xi;
			sumy += yi;
			sumy2 += yi * yi;
		}

		double r = (sumxy - (sumx * sumy / n))
				/ Math.sqrt(
					(sumx2 - (sumx * sumx / n))
					* (sumy2 - (sumy * sumy / n)));

		double se = Math.sqrt((1 - (r * r)) / (n - 2));

		result.setN(indicesLength);
		result.setScore(r);
		result.setStandardError(se);

		return result;
	}
}
