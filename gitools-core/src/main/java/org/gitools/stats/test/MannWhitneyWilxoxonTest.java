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

package org.gitools.stats.test;


import org.gitools.stats.test.results.CommonResult;

import cern.colt.matrix.DoubleMatrix1D;
import jsc.independentsamples.MannWhitneyTest;
import jsc.tests.H1;
import org.gitools.analysis.groupcomparison.GroupComparisonResult;

public class MannWhitneyWilxoxonTest extends AbstractTest {
	
	public MannWhitneyWilxoxonTest() {
	}
	
	@Override
	public String getName() {
		return "Mann-Whitney-Wilcoxon";
	}


	@Override
	public Class<? extends GroupComparisonResult> getResultClass() {
		return GroupComparisonResult.class;
	}
	
	@Override
	public void processPopulation(String name, DoubleMatrix1D population) {
	}

	
	public GroupComparisonResult processTest(
			double[] group1, double[] group2) {
		
		int g1Nans = 0;
		int g2Nans = 0;

		for (int i = 0; i < group1.length; i++){
			if (Double.isNaN(group1[i]))
				g1Nans++;
		}
		for (int i = 0; i < group2.length; i++){
			if (Double.isNaN(group2[i]))
				g2Nans++;
		}

		double[] group1NoNan = new double[group1.length-g1Nans];
		double[] group2NoNan = new double[group2.length-g2Nans];

		int offset = 0;
		for (int i = 0; i < group1.length; i++) {
			if (!Double.isNaN(group1[i]))
				group1NoNan[ i-offset ] = group1[i];
			else
				offset++;
		}
		offset = 0;
		for (int i = 0; i < group2.length; i++) {
			if (!Double.isNaN(group2[i]))
				group2NoNan[ i-offset ] = group2[i];
			else
				offset++;
		}

		if (group1NoNan.length > 1 && group2NoNan.length > 1) {
			MannWhitneyTest mwwLeft = new MannWhitneyTest(group1NoNan, group2NoNan, H1.LESS_THAN);
			MannWhitneyTest mwwRight = new MannWhitneyTest(group1NoNan, group2NoNan, H1.GREATER_THAN);
			MannWhitneyTest mwwTwoTail = new MannWhitneyTest(group1NoNan, group2NoNan, H1.NOT_EQUAL);


			return new GroupComparisonResult(
					mwwLeft.getN(),
					group1NoNan.length,
					group2NoNan.length,
					mwwLeft.getSP(),
					mwwRight.getSP(),
					mwwTwoTail.getSP());
		} else {
			return new GroupComparisonResult(
					group1NoNan.length+group2NoNan.length,
					group1NoNan.length,
					group2NoNan.length,
					Double.NaN,
					Double.NaN,
					Double.NaN
					);
		}

	}

	@Override
	public CommonResult processTest(String condName, DoubleMatrix1D condItems, String groupName, int[] groupItemIndices) {
		throw new UnsupportedOperationException("Not supported at all.");
	}

}
