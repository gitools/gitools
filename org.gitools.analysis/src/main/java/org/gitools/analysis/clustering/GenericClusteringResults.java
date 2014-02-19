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
package org.gitools.analysis.clustering;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class GenericClusteringResults implements Clusters {

    private Map<String, Set<String>> clusters;

    protected GenericClusteringResults() {
    }

    public GenericClusteringResults(Map<String, Set<String>> clusters) {
        init(clusters);
    }

    protected void init(Map<String, Set<String>> clusters) {
        this.clusters = clusters;
    }

    @Override
    public int size() {
        return clusters.size();
    }

    @Override
    public Collection<String> getClusters() {
        return clusters.keySet();
    }

    @Override
    public Set<String> getItems(String cluster) {
        return clusters.get(cluster);
    }

    @Override
    public Map<String, Set<String>> getClustersMap() {
        return clusters;
    }
}
