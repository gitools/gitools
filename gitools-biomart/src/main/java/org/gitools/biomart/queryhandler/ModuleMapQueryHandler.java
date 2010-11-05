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

package org.gitools.biomart.queryhandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.gitools.model.ModuleMap;

public class ModuleMapQueryHandler implements BiomartQueryHandler {

	private ModuleMap mmap;

	private Map<String, Set<String>> map;

	@Override
	public void begin() throws Exception {
		map = new HashMap<String, Set<String>>();
	}

	@Override
	public void line(String[] fields) throws Exception {
		String srcf = fields[0];
		String dstf = fields[1];
		Set<String> items = map.get(srcf);
		if (items == null) {
			items = new HashSet<String>();
			map.put(srcf, items);
		}
		items.add(dstf);
	}

	@Override
	public void end() {
		mmap = new ModuleMap(map);
		map.clear();
		map = null;
	}

	public ModuleMap getModuleMap() {
		return mmap;
	}
}
