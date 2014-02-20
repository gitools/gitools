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

import com.google.common.collect.Sets;
import org.gitools.analysis.clustering.ClusteringData;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.view.IMatrixViewDimension;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.GreedyStepwise;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;

import java.util.*;


public class ClusterUtils {

    private static final Integer MAX_ATTR = 200;
    private static final Integer MIN_ATTR = 10;

    /**
     * Adding attributes (rows name)
     *
     * @param numAttributes Total number of attributes to add
     * @return
     */

    private static FastVector addAttributes(Integer numAttributes) {

        FastVector attr = new FastVector();

        for (int rows = 0; rows < numAttributes; rows++)
            attr.addElement(new Attribute("a" + rows));

        return attr;
    }

    /**
     * Update visibility of the matrixView
     *
     * @param dimension
     * @param clusterResults
     */
    public static void updateVisibility(IMatrixViewDimension dimension, Map<String, Set<String>> clusterResults) {

        List<String> clustersSorted = new ArrayList<>(clusterResults.keySet());
        Collections.sort(clustersSorted);

        Set<String> visible = Sets.newHashSet(dimension);
        List<String> identifiers = new ArrayList<>();
        for (String cluster : clustersSorted) {
            for (String identifier : clusterResults.get(cluster)) {
                if (visible.contains(identifier)) {
                    identifiers.add(identifier);
                }
            }
        }

        dimension.show(identifiers);

    }

    @Deprecated
    public static Instances buildInstanceStructure(ClusteringData clusterData) {
        return new Instances("matrixToCluster", addAttributes(((MatrixClusteringData) clusterData).getNumAttributes()), 0);
    }

    @Deprecated
    public static List<String> getLabels(ClusteringData clusterData) {

        List<String> labels = new ArrayList<>();
        for (int i = 0; i < clusterData.getSize(); i++) {
            labels.add(clusterData.getLabel(i));
        }
        return labels;
    }

    /* Transform value to a formatted string which can be sorted correctly
     (i.e: 3, 2, 10  -> 02, 03, 10)
     */
    @Deprecated // Use a Java Formatter and or StringBuilder !!!
    public static String valueToString(Integer value, Integer maxLength) {
        String num = value.toString();
        int numLenght = num.length();
        for (int i = numLenght; i < maxLength; i++)
            num = "0" + num;

        return num;
    }


}
