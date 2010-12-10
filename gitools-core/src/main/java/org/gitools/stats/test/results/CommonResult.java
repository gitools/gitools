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

import java.io.Serializable;

import org.gitools.matrix.model.element.AttributeDef;

public class CommonResult implements Serializable{

	public int N;
	public double leftPvalue;
	public double rightPvalue;
	public double twoTailPvalue;
	public double corrLeftPvalue;
	public double corrRightPvalue;
	public double corrTwoTailPvalue;
	
	public CommonResult(
			int N,
			double leftPvalue, double rightPvalue,
			double twoTailPvalue) {
	
		this.leftPvalue = leftPvalue;
		this.N = N;
		this.rightPvalue = rightPvalue;
		this.twoTailPvalue = twoTailPvalue;
	}
	
	@AttributeDef(id = "N", name = "N", description = "Number of elements")
	public int getN() {
		return N;
	}
	
	public void setN(int n) {
		N = n;
	}
	
	@AttributeDef(id = "left-p-value", name = "Left P-Value", description = "P-Value for alternative hipothesis lower than")
	public double getLeftPvalue() {
		return leftPvalue;
	}
	
	public void setLeftPvalue(double leftPvalue) {
		this.leftPvalue = leftPvalue;
	}
	
	@AttributeDef(id = "right-p-value", name = "Right P-Value", description = "P-Value for alternative hipothesis greater than")
	public double getRightPvalue() {
		return rightPvalue;
	}
	
	public void setRightPvalue(double rightPvalue) {
		this.rightPvalue = rightPvalue;
	}
	
	@AttributeDef(id = "two-tail-p-value", name = "Two tail P-Value", description = "P-Value for alternative hipothesis different than")
	public double getTwoTailPvalue() {
		return twoTailPvalue;
	}
	
	public void setTwoTailPvalue(double twoTailPvalue) {
		this.twoTailPvalue = twoTailPvalue;
	}
	
	@AttributeDef(id = "corrected-left-p-value", name = "Corrected left P-Value", description = "Corrected P-Value for alternative hipothesis lower than")
	public double getCorrLeftPvalue() {
		return corrLeftPvalue;
	}
	
	public void setCorrLeftPvalue(double corrLeftPvalue) {
		this.corrLeftPvalue = corrLeftPvalue;
	}
	
	@AttributeDef(id = "corrected-right-p-value", name = "Corrected right P-Value", description = "Corrected P-Value for alternative hipothesis greater than")
	public double getCorrRightPvalue() {
		return corrRightPvalue;
	}

	public void setCorrRightPvalue(double corrRightPvalue) {
		this.corrRightPvalue = corrRightPvalue;
	}
	
	@AttributeDef(id = "corrected-two-tail-p-value", name = "Corrected two tail P-Value", description = "Corrected P-Value for alternative hipothesis different than")
	public double getCorrTwoTailPvalue() {
		return corrTwoTailPvalue;
	}
	
	public void setCorrTwoTailPvalue(double corrTwoTailPvalue) {
		this.corrTwoTailPvalue = corrTwoTailPvalue;
	}
}
