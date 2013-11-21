/*
 * #%L
 * gitools-core
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
/*******************************************************************************
 * Copyright 2013 Lars Behnke
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package org.gitools.analysis.clustering.hierarchical;

import org.gitools.analysis.clustering.ClusteringResults;

import java.util.*;

public class Cluster implements ClusteringResults {

	private String name;
	
	private Cluster parent;

	private List<Cluster> children;

	private Double distance;

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public List<Cluster> getChildren() {
		if (children == null) {
			children = new ArrayList<Cluster>();
		}

		return children;
	}

	public void setChildren(List<Cluster> children) {
		this.children = children;
	}

	public Cluster getParent() {
		return parent;
	}

	public void setParent(Cluster parent) {
		this.parent = parent;
	}

	
	public Cluster(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addChild(Cluster cluster) {
		getChildren().add(cluster);

	}

	public boolean contains(Cluster cluster) {
		return getChildren().contains(cluster);
	}

	@Override
	public String toString() {
		return "Cluster " + name;
	}

	@Override
	public boolean equals(Object obj) {
		String otherName = obj != null ? obj.toString() : "";
		return toString().equals(otherName);
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	public boolean isLeaf() {
		return getChildren().size() == 0;
	}
	
	public int countLeafs() {
	    return countLeafs(this, 0);
	}

    public int countLeafs(Cluster node, int count) {
        if (node.isLeaf()) count++;
        for (Cluster child : node.getChildren()) {
            count += child.countLeafs();
        }
        return count;
    }
    
    public void toConsole(int indent) {
        for (int i = 0; i < indent; i++) {
            System.out.print("  ");
            
        }
        String name = getName() + (isLeaf() ? " (leaf)" : "") + (distance != null ? "  distance: " + distance : "");
        System.out.println(name);
        for (Cluster child : getChildren()) {
            child.toConsole(indent + 1);
        }
    }

    public double getTotalDistance() {
        double dist = getDistance() == null ? 0 : getDistance();
        if (getChildren().size() > 0) {
            dist += children.get(0).getTotalDistance();
        }
        return dist;

    }

    @Override
    public int size() {
        return getChildren().size();
    }

    @Override
    public Collection<String> getClusters() {
        return getClustersMap().keySet();
    }

    @Override
    public Set<String> getItems(String cluster) {
        return getClustersMap().get(cluster);
    }

    @Override
    public Map<String, Set<String>> getClustersMap() {

        Map<String, Set<String>> map = new HashMap<>(size());

        for (Cluster cluster : getChildren()) {
            map.put(cluster.getName(), new HashSet<String>());
        }

        return map;
    }
}
