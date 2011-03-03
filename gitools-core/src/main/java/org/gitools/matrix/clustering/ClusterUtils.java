/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.matrix.clustering;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import org.gitools.matrix.model.IMatrixView;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.GreedyStepwise;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;


public class ClusterUtils {

	private static ClusterUtils instance = null;
	private static final Integer MAX_ATTR = 200;
	private static final Integer MIN_ATTR = 10;
	
	public ClusterUtils() {

	}

	public static ClusterUtils getInstance() {

		if (instance == null)
			instance = new ClusterUtils();

		return instance;
	}

	/**
	 *
	 * Method for performing a dimensionality reduction of the data
	 * Dataset with too much or too few columns are not preprocessed
	 * due to long time required.
	 *
	 * CfsSubsetEval class:
	 * Evaluates the worth of a subset of attributes by considering
	 * the individual predictive ability of each feature along with
	 * the degree of redundancy between them.
	 * Subsets of features that are highly correlated with the class
	 * while having low intercorrelation are preferred.
	 * For more information see:
	 * M. A. Hall (1998). Correlation-based Feature Subset Selection
	 * for Machine Learning. Hamilton, New Zealand.
	 *
	 * GreedyStepwise :
	 * Performs a greedy forward or backward search through the space
	 * of attribute subsets. May start with no/all attributes or from
	 * an arbitrary point in the space. Stops when the addition/deletion
	 * of any remaining attributes results in a decrease in evaluation.
	 * Can also produce a ranked list of attributes by traversing the space
	 * from one side to the other and recording the order that attributes
	 * are selected.
	 *
	 */
	public void dataReductionProcess(MatrixViewWeka data, IProgressMonitor monitor) throws Exception, IOException {

		if (data.numAttributes() > MAX_ATTR || data.numAttributes() < MIN_ATTR) return;

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
	 * @param name
	 * @return
	 */
	public FastVector addAttributes(Integer numAttributes) {

		FastVector attr = new FastVector();

		for (int rows = 0; rows < numAttributes; rows++) 			
			attr.addElement(new Attribute("a" + rows));

		return attr;
	}


	/**
	 *  Update visibility of the matrixView
	 *
	 * @param matrixView
	 * @param clusterResults
	 */
	public void updateVisibility(IMatrixView matrixView, HashMap<Integer, List<Integer>> clusterResults) {

		int[] visibleData = matrixView.getVisibleColumns();

		final int[] sortedVisibleData = new int[matrixView.getVisibleColumns().length];

		int index = 0;


		Integer[] clustersSorted = (Integer[]) clusterResults.keySet().toArray(
				new Integer[clusterResults.keySet().size()]);

		Arrays.sort(clustersSorted);

		for (Integer i : clustersSorted)
			for( Integer val : clusterResults.get(i))
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
	public Instances matrix2Instances(IMatrixView matrixView,Properties clusterParameters){
		FastVector attr = ClusterUtils.getInstance().addAttributes(matrixView.getRowCount());

		return new Instances("matrixToCluster", attr, 0);
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
