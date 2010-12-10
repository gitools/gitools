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

package org.gitools.persistence.text;

import org.gitools.matrix.model.BaseMatrix;
import org.gitools.persistence.AbstractEntityPersistence;

public abstract class BaseMatrixPersistence<T extends BaseMatrix>
		extends AbstractEntityPersistence<T> {

	public static final String POPULATION_LABELS = "population_labels";
	public static final String BACKGROUND_VALUE = "background_value";

	/** Returns the array of labels to consider as background population,
	 * or null if no population is specified.
	 *
	 * @return population labels
	 */
	protected String[] getPopulationLabels() {
		return (String[]) getProperties().get(POPULATION_LABELS);
	}

	/** The value to use for population rows not having data in the file
	 *
	 * @return background value
	 */
	protected Double getBackgroundValue() {
		if (getProperties().containsKey(BACKGROUND_VALUE))
			return (Double) getProperties().get(BACKGROUND_VALUE);
		else
			return 0.0;
	}
}
