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

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import org.gitools.analysis.clustering.hierarchical.strategy.LinkageStrategy;
import org.gitools.api.analysis.IProgressMonitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.CancellationException;

public class HierarchyBuilderWithMargin {

    private SortedSet<ClusterPair> distances;
    private Multimap<Integer, ClusterPair> distancesMap;

    private Set<Cluster> clusters;

    public SortedSet<ClusterPair> getDistances() {
        return distances;
    }

    public Set<Cluster> getClusters() {
        return clusters;
    }

    public HierarchyBuilderWithMargin(Set<Cluster> clusters, SortedSet<ClusterPair> distances) {
        this.clusters = clusters;
        this.distances = distances;

        this.distancesMap = LinkedListMultimap.create();
        for (ClusterPair distance : distances) {
            this.distancesMap.put(distance.hashCode(), distance);
        }
    }

    public void agglomerate(LinkageStrategy linkageStrategy, IProgressMonitor monitor, int dimensionSize) {

        int level = 1;
        int maxLevel = dimensionSize - 1;
        double margin = 0.1;

        ClusterPair minDistLink = distances.first();
        distances.remove(minDistLink);

        List<ClusterPair> pairs = new ArrayList<>();
        pairs.add(minDistLink);

        Double currentDistance = minDistLink.getLinkageDistance();
        List<Double> distanceValues = new ArrayList<>();
        distanceValues.add(currentDistance);

        while (true) {

            if (isTreeComplete()) {
                break;
            }

            try {
                minDistLink = distances.first();

                if (currentDistance + margin > minDistLink.getLinkageDistance()) {
                    pairs.add(minDistLink);
                    distanceValues.add(minDistLink.getLinkageDistance());
                    currentDistance = linkageStrategy.calculateDistance(distanceValues);
                    distances.remove(minDistLink);
                    continue;
                }

                // Agglomerate
                // Create a new cluster with all the nearby ClusterPairs
                Cluster newCluster = new Cluster();

                Set<Cluster> childrenSet = new HashSet<>();
                for (ClusterPair pair : pairs) {
                    Cluster left = pair.getlCluster();
                    Cluster right = pair.getrCluster();

                    // Set parent
                    left.setParent(newCluster);
                    right.setParent(newCluster);

                    // Add identifiers
                    newCluster.getIdentifiers().addAll(left.getIdentifiers());
                    newCluster.getIdentifiers().addAll(right.getIdentifiers());

                    // Cluster children
                    childrenSet.add(left);
                    childrenSet.add(right);
                }
                newCluster.getChildren().addAll(childrenSet);
                double weight = 0.0;
                for (Cluster child : childrenSet) {
                    weight += child.getWeight();
                    clusters.remove(child);
                }
                newCluster.setWeight(weight);
                newCluster.setDistance( currentDistance );

                // Create new cluster pairs between this new cluster and all other clusters
                monitor.begin("Agglomerating level "+ level + " of " + maxLevel + "", clusters.size());
                level++;

                for (Cluster iClust : clusters) {

                    monitor.worked(1);
                    if (monitor.isCancelled()) {
                        throw new CancellationException();
                    }

                    ClusterPair newLinkage = new ClusterPair();
                    newLinkage.setlCluster(iClust);
                    newLinkage.setrCluster(newCluster);

                    distanceValues.clear();
                    for (Cluster children : childrenSet) {
                        ClusterPair link = findByClusters(iClust, children);
                        if (link != null) {
                            distanceValues.add(link.getLinkageDistance());
                            distances.remove(link);
                        }
                    }

                    Double newDistance = linkageStrategy.calculateDistance(distanceValues);
                    newLinkage.setLinkageDistance(newDistance);
                    distances.add(newLinkage);
                    distancesMap.put(newLinkage.hashCode(), newLinkage);

                }
                clusters.add(newCluster);
                pairs.clear();

                if (!distances.contains(minDistLink)) {
                    minDistLink = distances.first();
                }

                pairs.add(minDistLink);
                currentDistance = minDistLink.getLinkageDistance();
                distanceValues.clear();
                distanceValues.add(currentDistance);

            } catch (NoSuchElementException e) {
                break;
            }

        }

    }

    private ClusterPair findByClusters(Cluster c1, Cluster c2) {

        int hash1 = c1.hashCode();
        int hash2 = c2.hashCode();

        int pair1 = 31 * hash1 + hash2;
        int pair2 = 31 * hash2 + hash1;

        for (ClusterPair link : distancesMap.get(pair1)) {
            if (link.getlCluster().equals(c1) && link.getrCluster().equals(c2)) {
                distancesMap.remove(pair1, link);
                return link;
            }
        }

        for (ClusterPair link : distancesMap.get(pair2)) {
            if (link.getlCluster().equals(c2) && link.getrCluster().equals(c1)) {
                distancesMap.remove(pair2, link);
                return link;
            }
        }

        return null;
    }

    public boolean isTreeComplete() {
        return clusters.size() == 1;
    }

    public Cluster getRootCluster() {
        if (!isTreeComplete()) {
            throw new RuntimeException("No root available");
        }
        return clusters.iterator().next();
    }

}
