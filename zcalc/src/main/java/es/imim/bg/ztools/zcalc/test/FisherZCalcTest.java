package es.imim.bg.ztools.zcalc.test;

import cern.colt.matrix.DoubleMatrix1D;
import es.imim.bg.ztools.stats.FisherExactTest;
import es.imim.bg.ztools.zcalc.results.FisherResult;
import es.imim.bg.ztools.zcalc.results.ZCalcResult;

public class FisherZCalcTest extends AbstractZCalcTest {
	
	public FisherZCalcTest() {
	}
	
	@Override
	public String getName() {
		return "fisher";
	}

	@Override
	public String[] getResultNames() {
		return new FisherResult().getNames();
	}
	
	@Override
	public void startCondition(String propName, DoubleMatrix1D propItems) {
	}
	
	@Override
	public ZCalcResult processGroup(
			String propName, DoubleMatrix1D propItems, 
			String groupName, int[] groupItemIndices) {
		
		int[] ctable = new int[4];
		
		calcContingencyTable(propItems, groupItemIndices, ctable);
		
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
	 *                               | has a 1.0  | hasn't a 1.0 |
	 * ------------------------------+------------+--------------+
	 *  belongs to the group         |      a     |       b      |
	 * ------------------------------+------------+--------------+
	 *  doesn't belongs to the group |      c     |       d      |
	 * ------------------------------+------------+--------------+
	 * 
	 * @param propItems all the data for the items of the property
	 * @param groupItems item indices to propItems for the items that belongs to the group
	 * @param ctable contingency table: ctable[0] = a, ctable[1] = b, ctable[2] = c, ctable[3] = d
	 */
	private void calcContingencyTable(
			DoubleMatrix1D propItems,
			int[] groupItems, 
			int[] ctable) {
		
		// Initialize the contingency table with zeros
		for (int i = 0; i < 4; i++)
			ctable[i] = 0;
		
		// count
		int k = 0;
		for (int i = 0; i < propItems.size(); i++) {
			double value = propItems.getQuick(i);
			
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
