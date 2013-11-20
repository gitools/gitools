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
package org.gitools.analysis.clustering.method.value;

import org.gitools.analysis.clustering.ClusteringData;
import org.gitools.analysis.clustering.ClusteringException;
import org.gitools.analysis.clustering.ClusteringResults;
import org.gitools.analysis.clustering.HierarchicalClusteringResults;
import org.gitools.analysis.clustering.newick.NewickParser;
import org.gitools.analysis.clustering.newick.NewickTree;
import org.gitools.api.analysis.IProgressMonitor;
import weka.core.Instances;
import weka.core.NormalizableDistance;
import weka.core.SelectedTag;

import java.util.List;


public class WekaHCLMethod extends AbstractClusteringValueMethod {

    private SelectedTag linkType;

    private boolean distanceIsBranchLength;

    private int numClusters;

    private boolean printNewick;

    private NormalizableDistance distanceFunction;

    public WekaHCLMethod() {
        classIndex = -1;
    }


    @Override
    public ClusteringResults cluster(ClusteringData clusterData, IProgressMonitor monitor) throws ClusteringException {

        try {
            Instances structure = ClusterUtils.buildInstanceStructure(clusterData, transpose);

            MatrixViewWeka clusterWekaData = new MatrixViewWeka(structure, clusterData, classIndex);

            List<String> labels = ClusterUtils.getLabels(clusterData, transpose);

            if (preprocess) {
                ClusterUtils.dataReductionProcess(clusterWekaData, monitor);
            }

            WekaHierarchicalClusterer clusterer = new WekaHierarchicalClusterer();

            configure(clusterer);

            clusterer.buildClusterer(clusterWekaData);

            ClusteringResults results = null;

            if (!monitor.isCancelled()) {

                monitor.end();

                // Identify cluster by instance
                monitor.begin("Clustering instances ...", clusterWekaData.getMatrixView().getSize());

                NewickParser newickParser = new NewickParser(clusterer.graph() + ";");
                NewickTree tree = newickParser.parse();

                results = new HierarchicalClusteringResults(labels.toArray(new String[labels.size()]), tree, 0);

            }

            return results;

        } catch (Throwable ex) {
            if (ex instanceof OutOfMemoryError) {
                throw new ClusteringException("Insufficient memory for HCL clustering. Increase memory size or try another clustering method", ex);
            } else {
                throw new ClusteringException(ex);
            }
        }
    }

    public NormalizableDistance getDistanceFunction() {
        return distanceFunction;
    }

    public void setDistanceFunction(NormalizableDistance distanceFunction) {
        this.distanceFunction = distanceFunction;
    }

    public boolean isDistanceIsBranchLength() {
        return distanceIsBranchLength;
    }

    public void setDistanceIsBranchLength(boolean distanceIsBranchLength) {
        this.distanceIsBranchLength = distanceIsBranchLength;
    }

    public SelectedTag getLinkType() {
        return linkType;
    }

    public void setLinkType(SelectedTag linkType) {
        this.linkType = linkType;
    }

    public int getNumClusters() {
        return numClusters;
    }

    public void setNumClusters(int numClusters) {
        this.numClusters = numClusters;
    }

    public boolean isPrintNewick() {
        return printNewick;
    }

    public void setPrintNewick(boolean printNewick) {
        this.printNewick = printNewick;
    }

    private void configure(WekaHierarchicalClusterer clusterer) {

        clusterer.setDistanceFunction(distanceFunction);

        clusterer.setPrintNewick(printNewick);

        clusterer.setNumClusters(numClusters);

        clusterer.setLinkType(linkType);

        clusterer.setDistanceIsBranchLength(distanceIsBranchLength);
    }

}
