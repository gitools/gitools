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

package org.gitools.heatmap.header;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import org.gitools.api.analysis.Clusters;

import java.util.*;

public class HierarchicalCluster implements Clusters, Comparable<HierarchicalCluster> {

    private String name;

    private Set<String> identifiers;

    private String parentName;

    private List<HierarchicalCluster> children;

    private Double distance;

    private Double weight = 0.0;

    private int color;

    public HierarchicalCluster() {
        //JAXB requirement
    }

    public HierarchicalCluster(String... identifiers) {
        this.identifiers = Sets.newHashSet(identifiers);
    }

    public HierarchicalCluster(Set<String> leftIds, Set<String> rightIds) {
        this.identifiers = new HashSet<>(leftIds.size() + rightIds.size());
        this.identifiers.addAll(leftIds);
        this.identifiers.addAll(rightIds);
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public List<HierarchicalCluster> getChildren() {
        if (children == null) {
            children = new ArrayList<>();
        }

        return children;
    }

    public void setChildren(List<HierarchicalCluster> children) {
        this.children = children;
    }

    public void setParent(HierarchicalCluster parent) {
        this.parentName = parent.getName();
    }

    public Set<String> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(Set<String> identifiers) {
        this.identifiers = identifiers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        for (HierarchicalCluster child : children) {
            child.setParentName(name);
        }
    }

    public void addChild(HierarchicalCluster cluster) {
        getChildren().add(cluster);
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = (weight == null ? 0.0 : weight);
    }

    public boolean contains(HierarchicalCluster cluster) {
        return getChildren().contains(cluster);
    }

    @Override
    public String toString() {
        return Joiner.on('&').join(identifiers);
    }

    public boolean isLeaf() {
        return getChildren().size() == 0;
    }

    public int countLeafs() {
        return countLeafs(this, 0);
    }

    public int countLeafs(HierarchicalCluster node, int count) {
        if (node.isLeaf()) count++;
        for (HierarchicalCluster child : node.getChildren()) {
            count += child.countLeafs();
        }
        return count;
    }

    public void toConsole(int indent) {
        for (int i = 0; i < indent; i++) {
            System.out.print("  ");

        }
        String name = Joiner.on('&').join(getIdentifiers()) + (isLeaf() ? " (leaf)" : "") + (distance != null ? "  distance: " + distance : "");
        System.out.println(name);
        for (HierarchicalCluster child : getChildren()) {
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

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public Collection<String> getClusters() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> getItems(String cluster) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Set<String>> getClustersMap() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int compareTo(HierarchicalCluster o) {

        int result;
        if (o == null || o.getWeight() == null) {
            result = -1;
        } else if (getWeight() == null) {
            result = 1;
        } else {
            Double w1 = getWeight() / identifiers.size();
            Double w2 = o.getWeight() / o.getIdentifiers().size();
            result = w1.compareTo(w2);
        }

        return result;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}
