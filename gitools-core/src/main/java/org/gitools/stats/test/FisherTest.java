package org.gitools.stats.test;

import java.util.Arrays;

import org.gitools.stats.FisherExactTest;
import org.gitools.stats.test.results.CommonResult;
import org.gitools.stats.test.results.FisherResult;

import cern.colt.matrix.DoubleMatrix1D;

public class FisherTest extends AbstractTest {
	
	public FisherTest() {
	}
	
	@Override
	public String getName() {
		return "fisher";
	}

	/*@Override
	public String[] getResultNames() {
		return new FisherResult().getNames();
	}*/
	
	@Override
	public Class<? extends CommonResult> getResultClass() {
		return FisherResult.class;
	}
	
	@Override
	public void processPopulation(String name, DoubleMatrix1D population) {
	}
	
	@Override
	public CommonResult processTest(
			String condName, DoubleMatrix1D condItems, 
			String groupName, int[] groupItemIndices) {
		
		int[] ctable = new int[4];
		
		calcContingencyTable(condItems, groupItemIndices, ctable);
		
		FisherExactTest fisher = new FisherExactTest(ctable);
		fisher.testContingencyTable();		
		
		int N = ctable[0] + ctable[1];
		
		return new FisherResult(
				N,
				fisher.getLeftPValue(), fisher.getRightPValue(), fisher.getTwoTailPValue(), ctable[0], 
				ctable[1],
				ctable[2], 
				ctable[3]);
	}

	/** Calculates a contingency table from the data
	 * 
	 * Contingency table format:
	 * 
	 *                             | has a 1.0  | hasn't a 1.0 |
	 * ----------------------------+------------+--------------+
	 *  belongs to the group       |      a     |       b      |
	 * ----------------------------+------------+--------------+
	 *  don't belong to the group  |      c     |       d      |
	 * ----------------------------+------------+--------------+
	 * 
	 * @param condItems all the data for the items of the property
	 * @param groupItems item indices to propItems for the items that belongs to the group
	 * @param ctable contingency table: ctable[0] = a, ctable[1] = b, ctable[2] = c, ctable[3] = d
	 */
	private void calcContingencyTable(
			DoubleMatrix1D condItems,
			int[] groupItems, 
			int[] ctable) {
		
		// Initialize the contingency table with zeros
		for (int i = 0; i < 4; i++)
			ctable[i] = 0;
		

		//sort group item indices
		Arrays.sort(groupItems); //FIXME this is redundant for diferent conditions
		
		// count
		int k = 0;
		for (int i = 0; i < condItems.size(); i++) {
			double value = condItems.getQuick(i);
			
			boolean belongsToGroup = k < groupItems.length && groupItems[k] == i;
			if (belongsToGroup)
				k++;
			
			if (!Double.isNaN(value)) {
				int j = (value == 1.0) ? 0 : 1;
				if (!belongsToGroup)
					j += 2;
				
				ctable[j]++;
			}
		}
	}
}
