package org.gitools.model.table.sort;

import java.text.Collator;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ComparatorFactory {

	private static final Map<Class<? extends Object>, Comparator> list = 
		new HashMap<Class<? extends Object>, Comparator>();
	
	static {
		list.put(Float.class, new Comparator<Float>() {
			@Override
			public int compare(Float o1, Float o2) {
				return (int) (o1 - o2);
			}
		});
		
		list.put(Double.class, new Comparator<Double>() {
			@Override
			public int compare(Double o1, Double o2) {
				return (int) (o1 - o2);
			}
		});
		
		list.put(Integer.class, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1 - o2;
			}
		});
		
		list.put(Long.class, new Comparator<Long>() {
			@Override
			public int compare(Long o1, Long o2) {
				return (int) (o1 - o2);
			}
		});
		
		list.put(String.class, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				Collator collator = Collator.getInstance();
				return collator.compare(o1, o2);
			}
		});
	}
	
	public Comparator create(Class<? extends Object> cls) {
		return list.get(cls);
	}
}
