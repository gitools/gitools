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

import org.gitools.analysis.clustering.hierarchical.strategy.LinkageStrategy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class HierarchyBuilder {

    private List<ClusterPair> distances;
    private List<Cluster> clusters;

    public List<ClusterPair> getDistances() {
        return distances;
    }

    public List<Cluster> getClusters() {
        return clusters;
    }

    public HierarchyBuilder(List<Cluster> clusters, List<ClusterPair> distances) {
        this.clusters = clusters;
        this.distances = distances;
    }

    public void agglomerate(LinkageStrategy linkageStrategy) {
        Collections.sort(distances);
        if (distances.size() > 0) {
            ClusterPair minDistLink = distances.remove(0);
            clusters.remove(minDistLink.getrCluster());
            clusters.remove(minDistLink.getlCluster());

            Cluster oldClusterL = minDistLink.getlCluster();
            Cluster oldClusterR = minDistLink.getrCluster();
            Cluster newCluster = minDistLink.agglomerate(null);

            for (Cluster iClust : clusters) {
                ClusterPair link1 = findByClusters(iClust, oldClusterL);
                ClusterPair link2 = findByClusters(iClust, oldClusterR);
                ClusterPair newLinkage = new ClusterPair();
                newLinkage.setlCluster(iClust);
                newLinkage.setrCluster(newCluster);
                Collection<Double> distanceValues = new ArrayList<Double>();
                if (link1 != null) {
                    distanceValues.add(link1.getLinkageDistance());
                    distances.remove(link1);
                }
                if (link2 != null) {
                    distanceValues.add(link2.getLinkageDistance());
                    distances.remove(link2);
                }
                Double newDistance = linkageStrategy
                        .calculateDistance(distanceValues);
                newLinkage.setLinkageDistance(newDistance);
                distances.add(newLinkage);

            }
            clusters.add(newCluster);
        }
    }

    private ClusterPair findByClusters(Cluster c1, Cluster c2) {
        ClusterPair result = null;
        for (ClusterPair link : distances) {
            boolean cond1 = link.getlCluster().equals(c1)
                    && link.getrCluster().equals(c2);
            boolean cond2 = link.getlCluster().equals(c2)
                    && link.getrCluster().equals(c1);
            if (cond1 || cond2) {
                result = link;
                break;
            }
        }
        return result;
    }

    public boolean isTreeComplete() {
        return clusters.size() == 1;
    }

    public Cluster getRootCluster() {
        if (!isTreeComplete()) {
            throw new RuntimeException("No root available");
        }
        return clusters.get(0);
    }

}
