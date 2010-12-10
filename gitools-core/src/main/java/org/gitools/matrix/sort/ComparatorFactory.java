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

package org.gitools.matrix.sort;

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
