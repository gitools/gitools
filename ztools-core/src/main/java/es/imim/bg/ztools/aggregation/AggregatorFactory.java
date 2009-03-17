package es.imim.bg.ztools.aggregation;

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
		put(new MultAggregator());
		put(new SumAggregator());
		put(new MeanAggregator());
		put(new LogSumAggregator());
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
}
