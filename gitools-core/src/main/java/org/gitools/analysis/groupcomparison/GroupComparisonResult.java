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

package org.gitools.analysis.groupcomparison;

import org.gitools.matrix.model.element.AttributeDef;
import org.gitools.stats.test.results.CommonResult;


public class GroupComparisonResult extends CommonResult {

	public int N_group1;
	public int N_group2;

    public GroupComparisonResult() {
        super(0,0.0,0.0,0.0);
    }

	public GroupComparisonResult(
			int N,
			double leftPvalue, double rightPvalue,
			double twoTailPvalue) {

		super(N, leftPvalue, rightPvalue, twoTailPvalue);
	}

	public GroupComparisonResult(
			int N, int N_group1, int N_group2,
			double leftPvalue, double rightPvalue,
			double twoTailPvalue) {

		super(N, leftPvalue, rightPvalue, twoTailPvalue);
		this.N_group1 = N_group1;
		this.N_group2 = N_group2;
	}

	@AttributeDef(id = "N-group1", name = "N Group 1", description = "Number of elements in Group 1")
	public int getN_group1() {
		return N_group1;
	}

	public void setN_group1(int N_group1) {
		this.N_group1 = N_group1;
	}

	@AttributeDef(id = "N-group2", name = "N Group 2", description = "Number of elements in Group 2")
	public int getN_group2() {
		return N_group2;
	}

	public void setN_group2(int N_group2) {
		this.N_group2 = N_group2;
	}




}
