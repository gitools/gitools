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

package org.gitools.idmapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MappingData {

	private MappingNode srcNode;
	private MappingNode dstNode;

	private Set<String> dstIds;
	private Map<String, Set<String>> map;

	public MappingData(String srcId, String dstId) {
		this.srcNode = new StringMappingNode(srcId);
		this.dstNode = new StringMappingNode(dstId);
		
		dstIds = new HashSet<String>();
		map = new HashMap<String, Set<String>>();
	}

	public MappingNode getSrcNode() {
		return srcNode;
	}

	public MappingNode getDstNode() {
		return dstNode;
	}

	public void setDstNode(MappingNode dstNode) {
		this.dstNode = dstNode;
	}

	public Set<String> getSrcIds() {
		return map.keySet();
	}

	public Set<String> getDstIds() {
		return dstIds;
	}

	public Set<String> get(String srcId) {
		Set<String> d = map.get(srcId);
		if (d == null)
			d = new HashSet<String>();
		return d;
	}
	
	public void put(String srcId, String dstId) {
		Set<String> ids = map.get(srcId);
		if (ids == null) {
			ids = new HashSet<String>();
			map.put(srcId, ids);
		}
		dstIds.add(dstId);
		ids.add(dstId);
	}

	public void clearDstIds() {
		dstIds.clear();
	}

	public void set(String srcId, Set<String> dids) {
		map.put(srcId, dids);
		dstIds.addAll(dids);
	}

	public Map<String, Set<String>> getMap() {
		return map;
	}

	public void map(Map<String, Set<String>> dstmap) {
		clearDstIds();
		
		for (Map.Entry<String, Set<String>> e : map.entrySet()) {
			Set<String> dset = new HashSet<String>();
			for (String sname : e.getValue()) {
				Set<String> dnames = dstmap.get(sname);
				if (dnames != null)
					dset.addAll(dnames);
			}
			set(e.getKey(), dset);
		}
	}

	public void clear() {
		dstIds.clear();
		map.clear();
	}

	public boolean isEmpty() {
		return map.keySet().isEmpty();
	}

	public void identity(Set<String> set) {
		clear();

		for (String id : set)
			put(id, id);
	}

	public void removeEmptyKeys() {
		List<String> rm = new ArrayList<String>();
		for (Map.Entry<String, Set<String>> e : map.entrySet())
			if (e.getValue().isEmpty())
				rm.add(e.getKey());

		for (String k : rm)
			map.remove(k);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(map.size()).append(" keys, ");
		sb.append(dstIds.size()).append(" distinct values. ");
		sb.append(map.toString());
		return sb.toString();
	}
}
