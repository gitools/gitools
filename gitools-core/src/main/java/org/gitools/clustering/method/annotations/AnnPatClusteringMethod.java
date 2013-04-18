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
package org.gitools.clustering.method.annotations;

import org.gitools.clustering.ClusteringData;
import org.gitools.clustering.ClusteringMethod;
import org.gitools.clustering.ClusteringResults;
import org.gitools.clustering.GenericClusteringResults;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @noinspection ALL
 */
public class AnnPatClusteringMethod implements ClusteringMethod {

    private String pattern;

    public AnnPatClusteringMethod() {
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    /**
     * Execute the clustering and return the results
     */
    @Nullable
    @Override
    public ClusteringResults cluster(@NotNull ClusteringData data, @NotNull IProgressMonitor monitor) {
        monitor.begin("Clustering by annotations", data.getSize() + 1);

        String[] dataLabels = new String[data.getSize()];
        Map<String, List<Integer>> clusters = new HashMap<String, List<Integer>>();
        for (int i = 0; i < data.getSize() && !monitor.isCancelled(); i++) {
            String label = data.getLabel(i);
            dataLabels[i] = label;
            String clusterName = data.getInstance(i).getTypedValue(0, String.class);
            List<Integer> indices = clusters.get(clusterName);
            if (indices == null) {
                indices = new ArrayList<Integer>();
                clusters.put(clusterName, indices);
            }
            indices.add(i);

            monitor.worked(1);
        }

        if (monitor.isCancelled()) {
            return null;
        }

        monitor.end();

        return new GenericClusteringResults(dataLabels, clusters);
    }
}
