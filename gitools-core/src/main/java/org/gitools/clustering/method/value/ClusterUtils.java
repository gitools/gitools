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

import org.gitools.clustering.ClusteringData;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.newick.NewickNode;
import org.gitools.newick.NewickParserException;
import org.gitools.newick.NewickTree;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.GreedyStepwise;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class ClusterUtils
{

    private static final Integer MAX_ATTR = 200;
    private static final Integer MIN_ATTR = 10;


    /**
     * Method for performing a dimensionality reduction of the data
     * Dataset with too much or too few columns are not preprocessed
     * due to long time required.
     * <p/>
     * CfsSubsetEval class:
     * Evaluates the worth of a subset of attributes by considering
     * the individual predictive ability of each feature along with
     * the degree of redundancy between them.
     * Subsets of features that are highly correlated with the class
     * while having low intercorrelation are preferred.
     * For more information see:
     * M. A. Hall (1998). Correlation-based Feature Subset Selection
     * for Machine Learning. Hamilton, New Zealand.
     * <p/>
     * GreedyStepwise :
     * Performs a greedy forward or backward search through the space
     * of attribute subsets. May start with no/all attributes or from
     * an arbitrary point in the space. Stops when the addition/deletion
     * of any remaining attributes results in a decrease in evaluation.
     * Can also produce a ranked list of attributes by traversing the space
     * from one side to the other and recording the order that attributes
     * are selected.
     */
    public static void dataReductionProcess(MatrixViewWeka data, IProgressMonitor monitor) throws Exception, IOException
    {

        if (data.numAttributes() > MAX_ATTR || data.numAttributes() < MIN_ATTR)
        {
            return;
        }

        monitor.begin("Preprocessing data for clustering  ...", 1);

        CfsSubsetEval eval = new CfsSubsetEval();
        eval.setLocallyPredictive(true);

        GreedyStepwise search = new GreedyStepwise();
        search.setSearchBackwards(false);
        search.setGenerateRanking(false);

        AttributeSelection attSelection = new AttributeSelection();
        attSelection.setEvaluator(eval);
        attSelection.setSearch(search);
        attSelection.SelectAttributes(data);

        data.setFilteredAttributes(attSelection.selectedAttributes());

        monitor.end();

    }

    /**
     * Adding attributes (rows name)
     *
     * @param name
     * @return
     */
    public static FastVector addAttributes(Integer numAttributes)
    {

        FastVector attr = new FastVector();

        for (int rows = 0; rows < numAttributes; rows++)
            attr.addElement(new Attribute("a" + rows));

        return attr;
    }

    /**
     * Update visibility of the matrixView
     *
     * @param matrixView
     * @param clusterResults
     */
    public static void updateVisibility(IMatrixView matrixView, Map<String, int[]> clusterResults)
    {

        int[] visibleData = matrixView.getVisibleColumns();

        final int[] sortedVisibleData = new int[matrixView.getVisibleColumns().length];

        int index = 0;


        String[] clustersSorted = (String[]) clusterResults.keySet().toArray(
                new String[clusterResults.keySet().size()]);

        Arrays.sort(clustersSorted);

        for (String i : clustersSorted)
            for (Integer val : clusterResults.get(i))
                sortedVisibleData[index++] = visibleData[val];

        matrixView.setVisibleColumns(sortedVisibleData);

    }

    /**
     * Creation of an Instance structure from a IMatrixView
     *
     * @param matrixView
     * @param clusterParameters
     * @return
     */
    @Deprecated // It is not necessary to check for transposed !!!
    public static Instances buildInstanceStructure(ClusteringData clusterData, boolean transposed)
    {

        FastVector attr = null;

        if (transposed)
        {
            attr = addAttributes(((MatrixRowClusteringData) clusterData).getNumAttributes());
        }
        else
        {
            attr = addAttributes(((MatrixColumnClusteringData) clusterData).getNumAttributes());
        }

        return new Instances("matrixToCluster", attr, 0);
    }

    @Deprecated // It is not necessary to check for transposed !!!
    public static List<String> getLabels(ClusteringData clusterData, boolean transpose)
    {

        List<String> labels = new ArrayList<String>();

        if (transpose)
        {
            for (int i = 0; i < ((MatrixRowClusteringData) clusterData).getSize(); i++)
                labels.add(((MatrixRowClusteringData) clusterData).getLabel(i));
        }
        else
        {
            for (int i = 0; i < ((MatrixColumnClusteringData) clusterData).getSize(); i++)
                labels.add(((MatrixColumnClusteringData) clusterData).getLabel(i));
        }
        return labels;
    }

    /* Transform value to a formatted string which can be sorted correctly
     (i.e: 3, 2, 10  -> 02, 03, 10)
     */
    @Deprecated // Use a Java Formatter and or StringBuilder !!!
    public static String valueToString(Integer value, Integer maxLength)
    {
        String num = value.toString();
        int numLenght = num.length();
        for (int i = numLenght; i < maxLength; i++)
            num = "0" + num;

        return num;
    }

	/*
    public static ClusteringResults getHCLResultsByLevel(HierarchicalClusteringResults res, int level) {

		ClusteringResults results = null;

		HashMap<String, List<Integer>> clusterResults = new HashMap<String, List<Integer>>();

		List<NewickNode> leaves = res.getTree().getRoot().getLeaves(level);

		Integer maxLengthClusters = Integer.toString(leaves.size()).length();
		
		int i = 0;

		for (NewickNode node : leaves) {

			List<Integer> instancesCluster = new ArrayList<Integer>();
			
			 for (NewickNode leave : (List<NewickNode>) node.getLeaves()) {
					instancesCluster.add(Integer.valueOf(leave.getName()));
			}

			clusterResults.put(valueToString(i, maxLengthClusters), instancesCluster);

			i++;
		}

		results = new HierarchicalClusteringResults(res.getDataLabels(), clusterResults, res.getTree(), res.getStrNewickTree());

		return results;
	}
	 */

    public static List<Integer> getTreeLeaves(NewickTree tree) throws NumberFormatException, IOException, NewickParserException
    {

        List<Integer> instancesCluster = new ArrayList<Integer>();

        List<NewickNode> leaves = tree.getRoot().getLeaves();

        for (NewickNode node : leaves)
            instancesCluster.add(new Integer(node.getName()));

        return instancesCluster;
    }

/*
	public void dataReductionProcess2(MatrixViewWeka data) throws Exception, IOException {

		if (data.numAttributes() > MAX_ATTR) return;

		Instances dataSet = data.getDataSet();

		AttributeSelection filter = new AttributeSelection();
		CfsSubsetEval eval = new CfsSubsetEval();
		GreedyStepwise search = new GreedyStepwise();
		search.setSearchBackwards(false);
		search.setGenerateRanking(false);
		filter.setEvaluator(eval);
		filter.setSearch(search);
		filter.setInputFormat(data);

		Instances newData = Filter.useFilter(data, filter);
		System.out.println(newData);

		System.out.println(newData);

	}
*/

}
