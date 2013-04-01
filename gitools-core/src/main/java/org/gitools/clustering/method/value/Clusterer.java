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
package org.gitools.clustering.method.value;

import org.gitools.clustering.*;
import org.gitools.matrix.TransposedMatrixView;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import weka.core.EuclideanDistance;
import weka.core.ManhattanDistance;
import weka.core.SelectedTag;

import java.util.List;
import java.util.Properties;

public class Clusterer
{

    public static ClusteringResults matrixClustering(IMatrixView matrixView, Properties clusterParameters, IProgressMonitor monitor, boolean sortMatrix) throws Exception
    {

        ClusteringMethodDescriptor descriptor = null;
        List<ClusteringMethodDescriptor> descriptors = ClusteringMethodFactory.getDefault().getDescriptors();

        for (ClusteringMethodDescriptor desc : descriptors)
        {
            if (desc.getDescription().contains(clusterParameters.getProperty("method")))
            {
                descriptor = desc;
            }
        }
        ClusteringData data = null;
        ClusteringMethod method = null;

        if (descriptor.getMethodClass().equals(WekaCobWebMethod.class))
        {

            method = (WekaCobWebMethod) ClusteringMethodFactory.getDefault().create(descriptor);

            int dimMatrix = new Integer(clusterParameters.getProperty("index"));

            if (Boolean.valueOf(clusterParameters.getProperty("transpose")))
            {
                data = new MatrixRowClusteringData(matrixView, dimMatrix);
            }
            else
            {
                data = new MatrixColumnClusteringData(matrixView, dimMatrix);
            }

            ((WekaCobWebMethod) method).setTranspose(Boolean.valueOf(clusterParameters.getProperty("transpose")));
            ((WekaCobWebMethod) method).setAcuity(Float.valueOf(clusterParameters.getProperty("acuity")));
            ((WekaCobWebMethod) method).setCutoff(Float.valueOf(clusterParameters.getProperty("cutoff")));
            ((WekaCobWebMethod) method).setSeed(Integer.valueOf(clusterParameters.getProperty("seedCobweb")));
            ((WekaCobWebMethod) method).setPreprocess(Boolean.valueOf(clusterParameters.getProperty("preprocessing")));
        }

        if (descriptor.getMethodClass().equals(WekaKmeansMethod.class))
        {

            method = (WekaKmeansMethod) ClusteringMethodFactory.getDefault().create(descriptor);

            int dimMatrix = new Integer(clusterParameters.getProperty("index"));

            if (Boolean.valueOf(clusterParameters.getProperty("transpose")))
            {
                data = new MatrixRowClusteringData(matrixView, dimMatrix);
            }
            else
            {
                data = new MatrixColumnClusteringData(matrixView, dimMatrix);
            }

            ((WekaKmeansMethod) method).setTranspose(Boolean.valueOf(clusterParameters.getProperty("transpose")));
            ((WekaKmeansMethod) method).setPreprocess(Boolean.valueOf(clusterParameters.getProperty("preprocessing")));
            ((WekaKmeansMethod) method).setIterations(Integer.valueOf(clusterParameters.getProperty("iterations", "500")));
            ((WekaKmeansMethod) method).setNumClusters(Integer.valueOf(clusterParameters.getProperty("k", "2")));
            ((WekaKmeansMethod) method).setSeed(Integer.valueOf(clusterParameters.getProperty("seedKmeans", "10")));
            if (clusterParameters.getProperty("distance", "euclidean").toLowerCase().equals("euclidean"))
            {
                ((WekaKmeansMethod) method).setDistanceFunction(new EuclideanDistance());
            }
            else
            {
                ((WekaKmeansMethod) method).setDistanceFunction(new ManhattanDistance());
            }
        }

        if (descriptor.getMethodClass().equals(WekaHCLMethod.class))
        {

            method = (WekaHCLMethod) ClusteringMethodFactory.getDefault().create(descriptor);

            int dimMatrix = new Integer(clusterParameters.getProperty("index"));

            if (Boolean.valueOf(clusterParameters.getProperty("transpose")))
            {
                data = new MatrixRowClusteringData(matrixView, dimMatrix);
            }
            else
            {
                data = new MatrixColumnClusteringData(matrixView, dimMatrix);
            }

            ((WekaHCLMethod) method).setTranspose(Boolean.valueOf(clusterParameters.getProperty("transpose")));
            ((WekaHCLMethod) method).setPreprocess(Boolean.valueOf(clusterParameters.getProperty("preprocessing")));
            ((WekaHCLMethod) method).setLinkType(
                    new SelectedTag(clusterParameters.getProperty("link").toUpperCase(), WekaHierarchicalClusterer.TAGS_LINK_TYPE));

            ((WekaHCLMethod) method).setDistanceIsBranchLength(false);
            ((WekaHCLMethod) method).setNumClusters(1);
            ((WekaHCLMethod) method).setPrintNewick(true);

            if (clusterParameters.getProperty("distance").equalsIgnoreCase("euclidean"))
            {
                ((WekaHCLMethod) method).setDistanceFunction(new EuclideanDistance());
            }
            else
            {
                ((WekaHCLMethod) method).setDistanceFunction(new ManhattanDistance());
            }

        }

        ClusteringResults results = method.cluster(data, monitor);

        monitor.end();

        if (!monitor.isCancelled() && sortMatrix)
        {
            if (Boolean.valueOf(clusterParameters.getProperty("transpose")))
            {
                TransposedMatrixView transposedMatrix = new TransposedMatrixView(matrixView);
                ClusterUtils.updateVisibility(transposedMatrix, results.getDataIndicesByClusterTitle());
            }
            else
            {
                ClusterUtils.updateVisibility(matrixView, results.getDataIndicesByClusterTitle());
            }
        }

        return results;

    }
}