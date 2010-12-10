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

package org.gitools.stats.test.results;

import org.gitools.matrix.model.element.AttributeDef;

public class FisherResult extends CommonResult {

	public int a;
	public int b;
	public int c;
	public int d;
	
	public FisherResult() {
		super(0, 0.0, 0.0, 0.0);
		a = b = c = d = 0;
	}
	
	public FisherResult(
			int n, 
			double leftPvalue, double rightPvalue, double twoTailPvalue, 
			int a, int b, int c, int d) {
		
		super(n, leftPvalue, rightPvalue, twoTailPvalue);
		
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}
	
	@AttributeDef(id = "a", name = "a", description = "Number of positive events that belongs to the module")
	public int getA() {
		return a;
	}
	
	public void setA(int a) {
		this.a = a;
	}
	
	@AttributeDef(id = "b", name = "b", description = "Number of no positive events that belongs to the module")
	public int getB() {
		return b;
	}
	
	public void setB(int b) {
		this.b = b;
	}
	
	@AttributeDef(id = "c", name = "c", description = "Number of positive events that don't belong to the module")
	public int getC() {
		return c;
	}
	
	public void setC(int c) {
		this.c = c;
	}
	
	@AttributeDef(id = "d", name = "d", description = "Number of no positive events that don't belong to the module")
	public int getD() {
		return d;
	}
	
	public void setD(int d) {
		this.d = d;
	}
}
