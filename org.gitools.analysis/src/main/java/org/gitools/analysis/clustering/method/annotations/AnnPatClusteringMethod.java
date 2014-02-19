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
package org.gitools.analysis.clustering.method.annotations;

import org.gitools.analysis.clustering.ClusteringData;
import org.gitools.analysis.clustering.ClusteringMethod;
import org.gitools.analysis.clustering.Clusters;
import org.gitools.analysis.clustering.GenericClusteringResults;
import org.gitools.api.analysis.IProgressMonitor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AnnPatClusteringMethod implements ClusteringMethod {

    private String labelPrefix;

    public AnnPatClusteringMethod(String labelPrefix) {
        this.labelPrefix = labelPrefix;
    }

    private String pattern;

    public AnnPatClusteringMethod() {
        this.labelPrefix = "";
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    /**
     * Execute the clustering and return the results
     */

    @Override
    public Clusters cluster(ClusteringData data, IProgressMonitor monitor) {
        monitor.begin("Clustering by annotations", data.getSize() + 1);

        Map<String, Set<String>> clusters = new HashMap<>();
        for (String item : data.getLabels()) {

            if (monitor.isCancelled()) {
                return null;
            }

            String clusterName = data.getInstance(item).getTypedValue(0, String.class);

            if (AnnPatClusteringData.NA.equals(clusterName)) {
                monitor.worked(1);
                continue;
            }

            clusterName = labelPrefix + clusterName;

            Set<String> items = clusters.get(clusterName);
            if (items == null) {
                items = new HashSet<>();
                clusters.put(clusterName, items);
            }
            items.add(item);

            monitor.worked(1);
        }

        if (monitor.isCancelled()) {
            return null;
        }

        monitor.end();

        return new GenericClusteringResults(clusters);
    }
}
