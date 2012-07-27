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

package edu.upf.bg.aggregation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AggregatorFactory {

	private static final List<IAggregator> aggregators = 
		new ArrayList<IAggregator>();
	
	private static final Map<String, IAggregator> aggregatorsMap
		= new HashMap<String, IAggregator>();
	
	static {
		put(MultAggregator.INSTANCE);
		put(SumAggregator.INSTANCE);
		put(MeanAggregator.INSTANCE);
		put(LogSumAggregator.INSTANCE);
		put(SumAbsAggregator.INSTANCE);
	}
	
	private static void put(IAggregator aggregator) {
		aggregators.add(aggregator);
		aggregatorsMap.put(aggregator.toString(), aggregator);
	}
	
	public static IAggregator create(String name) {
		return aggregatorsMap.get(name);
	}
	
	public static Collection<IAggregator> getAggregators() {
		return Collections.unmodifiableCollection(aggregators);
	}
	
	public static IAggregator[] getAggregatorsArray() {
		final IAggregator[] aggregatorsArray = 
			new IAggregator[aggregators.size()];
		aggregators.toArray(aggregatorsArray);
		return aggregatorsArray;
	}
}
