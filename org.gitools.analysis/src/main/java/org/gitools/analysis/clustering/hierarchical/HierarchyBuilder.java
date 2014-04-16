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

import java.util.*;
import java.util.concurrent.CancellationException;

public class HierarchyBuilder {

    private SortedSet<ClusterPair> distances;
    private Multimap<Integer, ClusterPair> distancesMap;

    private Set<HierarchicalCluster> clusters;

    public SortedSet<ClusterPair> getDistances() {
        return distances;
    }

    public Set<HierarchicalCluster> getClusters() {
        return clusters;
    }

    public HierarchyBuilder(Set<HierarchicalCluster> clusters, SortedSet<ClusterPair> distances) {
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

        while (true) {

            if (isTreeComplete()) {
                break;
            }

            try {
                ClusterPair minDistLink = distances.first();
                distances.remove(minDistLink);
                clusters.remove(minDistLink.getrCluster());
                clusters.remove(minDistLink.getlCluster());

                HierarchicalCluster oldClusterL = minDistLink.getlCluster();
                HierarchicalCluster oldClusterR = minDistLink.getrCluster();
                HierarchicalCluster newCluster = minDistLink.agglomerate();

                monitor.begin("Agglomerating level "+ level + " of " + maxLevel + "", clusters.size());
                level++;

                for (HierarchicalCluster iClust : clusters) {

                    monitor.worked(1);
                    if (monitor.isCancelled()) {
                        throw new CancellationException();
                    }

                    ClusterPair link1 = findByClusters(iClust, oldClusterL);
                    ClusterPair link2 = findByClusters(iClust, oldClusterR);
                    ClusterPair newLinkage = new ClusterPair();
                    newLinkage.setlCluster(iClust);
                    newLinkage.setrCluster(newCluster);
                    List<Double> distanceValues = new ArrayList<>();
                    if (link1 != null) {
                        distanceValues.add(link1.getLinkageDistance());
                        distances.remove(link1);
                    }
                    if (link2 != null) {
                        distanceValues.add(link2.getLinkageDistance());
                        distances.remove(link2);
                    }

                    if (link1 == null && link2 == null) {

                        for (ClusterPair pair : distances) {
                            if (pair.getlCluster().equals(iClust) || pair.getrCluster().equals(iClust)) {
                                System.out.println(pair);
                            }
                        }
                    }


                    Double newDistance = linkageStrategy
                            .calculateDistance(distanceValues);
                    newLinkage.setLinkageDistance(newDistance);
                    distances.add(newLinkage);
                    distancesMap.put(newLinkage.hashCode(), newLinkage);

                }
                clusters.add(newCluster);

            } catch (NoSuchElementException e) {
                break;
            }

        }

    }

    private ClusterPair findByClusters(HierarchicalCluster c1, HierarchicalCluster c2) {

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

    public HierarchicalCluster getRootCluster() {
        if (!isTreeComplete()) {
            throw new RuntimeException("No root available");
        }
        return clusters.iterator().next();
    }

}
