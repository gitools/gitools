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
package org.gitools.clustering;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * @noinspection ALL
 */
public interface ClusteringResults {

    /**
     * Returns the number of clusters
     */
    int getNumClusters();

    /**
     * Returns the cluster titles
     */
    String[] getClusterTitles();

    /**
     * Returns the cluster index by its title
     */
    int getClusterTitleIndex(String clusterTitle);

    /**
     * Returns the number of data labels
     */
    int getNumDataLabels();

    /**
     * Returns data labels for which there are cluster results
     */
    String[] getDataLabels();

    /**
     * Returns the data label index
     */
    int getDataLabelIndex(String dataLabel);

    /**
     * Returns the data indicess for a given cluster
     */
    int[] getDataIndices(int clusterIndex);

    int[] getDataIndices(String clusterTitle);

    /**
     * Returns the data labels for a given cluster
     */
    @NotNull
    String[] getDataLabels(int clusterIndex);

    @NotNull
    String[] getDataLabels(String clusterTitle);

    /**
     * Returns the cluster the data belongs to
     *
     * @noinspection UnusedDeclaration
     */
    int getClusterIndex(int dataIndex);

    int getClusterIndex(String dataLabel);

    /**
     * Returns a map from cluster title to an array of data indices included in the cluster
     */
    @NotNull
    Map<String, int[]> getDataIndicesByClusterTitle();

    /**
     * Returns a map from the data label to the cluster index the data belongs to
     */
    @NotNull
    Map<String, Integer> getClusterIndexByDataLabel();
}
