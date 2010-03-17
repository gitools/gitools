/*
 *  Copyright 2010 chris.
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

package org.gitools.stats.mtc;

import java.util.HashMap;
import java.util.Map;

public class MTCFactory {

	private static final Map<String, Class<? extends MTC>> nameMap =
			new HashMap<String, Class<? extends MTC>>();

	private static final Map<Class<? extends MTC>, String> classMap =
			new HashMap<Class<? extends MTC>, String>();

	static {
		nameMap.put("bonferroni", Bonferroni.class);
		nameMap.put("bh", BenjaminiHochbergFdr.class);

		for (Map.Entry<String, Class<? extends MTC>> e : nameMap.entrySet())
			classMap.put(e.getValue(), e.getKey());
	}

	public static String[] getAvailableMtcNames() {
		String[] names = new String[nameMap.size()];
		nameMap.keySet().toArray(names);
		return names;
	}

	public static MTC createFromName(String name) {
		MTC mtc = null;
		try {
			Class<? extends MTC> cls = nameMap.get(name);
			mtc = cls.newInstance();
		}
		catch (Exception ex) {
			return null;
		}
		return mtc;
	}
}
