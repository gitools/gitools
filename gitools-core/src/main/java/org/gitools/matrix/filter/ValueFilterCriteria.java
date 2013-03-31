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

package org.gitools.matrix.filter;

import org.gitools.utils.cutoffcmp.CutoffCmp;

public class ValueFilterCriteria {

	protected String attributeName;
	protected int attributeIndex;
	protected CutoffCmp comparator;
	protected double value;

	public ValueFilterCriteria(String attributeName, int attributeIndex, CutoffCmp comparator, double value) {
		this.attributeName = attributeName;
		this.attributeIndex = attributeIndex;
		this.comparator = comparator;
		this.value = value;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public int getAttributeIndex() {
		return attributeIndex;
	}

	public void setAttributeIndex(int attributeIndex) {
		this.attributeIndex = attributeIndex;
	}

	public CutoffCmp getComparator() {
		return comparator;
	}

	public void setComparator(CutoffCmp comparator) {
		this.comparator = comparator;
	}

	public double getValue() {
		return this.value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return attributeName.toString() + " " + comparator.toString() + " " + value;
	}
}
