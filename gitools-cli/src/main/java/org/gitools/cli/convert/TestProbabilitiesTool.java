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

package org.gitools.cli.convert;

import cern.jet.stat.Probability;

public class TestProbabilitiesTool {

	public static void main(String[] args) {
		System.out.println("k"+"\t" +"n"+ "\t" +"p"+ "\t" +"lpv"+ "\t" +"rpv"+ "\t" +"rppv"+ "\t" +"tpv"+ "\tRl\tRr\tRt");
		int n = 39;
		double p = 0.026426896012509773;
		for (int k = 0; k < n; k++) {
			double lpv = Probability.binomial(k, n, p);
			double rpv = k > 0 ? Probability.binomialComplemented(k-1, n, p) : 1.0;
			double tpv = lpv + rpv > 1.0 ? 1.0 : lpv + rpv;
			double rppv = k > 0 ? Probability.poissonComplemented(k-1, n*p) : 1.0;
			double rppv2 = 1 - Probability.poisson(k, n*p);
			System.out.println(k + "\t" + n + "\t" + p + "\t" + lpv + "\t" + rpv + "\t" + rppv2 + "\t" + tpv + "\t0\t0\t0");
		}
	}

}
