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

import org.gitools.heatmap.header.HierarchicalCluster;

public class ClusterPair implements Comparable<ClusterPair> {

    private HierarchicalCluster lCluster;
    private HierarchicalCluster rCluster;
    private Double linkageDistance;

    public ClusterPair() {
        super();
    }

    public ClusterPair(Double linkageDistance, HierarchicalCluster lCluster, HierarchicalCluster rCluster) {
        super();

        this.linkageDistance = linkageDistance;
        this.lCluster = lCluster;
        this.rCluster = rCluster;
    }

    public HierarchicalCluster getlCluster() {
        return lCluster;
    }

    public void setlCluster(HierarchicalCluster lCluster) {
        this.lCluster = lCluster;
    }

    public HierarchicalCluster getrCluster() {
        return rCluster;
    }

    public void setrCluster(HierarchicalCluster rCluster) {
        this.rCluster = rCluster;
    }

    public Double getLinkageDistance() {
        return linkageDistance;
    }

    public void setLinkageDistance(Double distance) {
        this.linkageDistance = distance;
    }

    @Override
    public int compareTo(ClusterPair o) {
        int result;
        if (o == null || o.getLinkageDistance() == null) {
            result = -1;
        } else if (getLinkageDistance() == null) {
            result = 1;
        } else {
            result = getLinkageDistance().compareTo(o.getLinkageDistance());
        }

        if (result == 0) {
            result = Integer.compare(hashCode(), o.hashCode());
        }

        return result;
    }

    public HierarchicalCluster agglomerate() {
        HierarchicalCluster cluster = new HierarchicalCluster(lCluster.getIdentifiers(), rCluster.getIdentifiers());
        cluster.setDistance(getLinkageDistance());
        cluster.setWeight(lCluster.getWeight() + rCluster.getWeight());
        cluster.addChild(lCluster);
        cluster.addChild(rCluster);
        lCluster.setParent(cluster);
        rCluster.setParent(cluster);
        return cluster;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (lCluster != null) {
            sb.append(lCluster);
        }
        if (rCluster != null) {
            if (sb.length() > 0) {
                sb.append(" + ");
            }
            sb.append(rCluster);
        }
        sb.append(" : ").append(linkageDistance);
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClusterPair that = (ClusterPair) o;

        if (!lCluster.equals(that.lCluster)) return false;
        if (!rCluster.equals(that.rCluster)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = lCluster.hashCode();
        result = 31 * result + rCluster.hashCode();
        return result;
    }
}
