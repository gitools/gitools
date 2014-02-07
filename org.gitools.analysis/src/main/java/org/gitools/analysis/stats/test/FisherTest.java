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
package org.gitools.analysis.stats.test;

import org.gitools.analysis.stats.FisherExactTest;
import org.gitools.analysis.stats.test.results.CommonResult;
import org.gitools.analysis.stats.test.results.FisherResult;

import java.util.Arrays;

public class FisherTest extends AbstractTest {

    public FisherTest() {
        super("fisher", FisherResult.class);
    }

    @Override
    public CommonResult processTest(Iterable<Double> values) {

        int[] ctable = new int[4];

        //TODO calcContingencyTable(values, groupItemIndices, ctable);

        FisherExactTest fisher = new FisherExactTest(ctable);
        fisher.testContingencyTable();

        int N = ctable[0] + ctable[1];

        return new FisherResult(N, fisher.getLeftPValue(), fisher.getRightPValue(), fisher.getTwoTailPValue(), ctable[0], ctable[1], ctable[2], ctable[3]);
    }

    /**
     * Calculates a contingency table from the data
     * <p/>
     * Contingency table format:
     * <p/>
     *                             | has a 1.0  | hasn't a 1.0 |
     * ----------------------------+------------+--------------+
     * belongs to the group        |      a     |       b      |
     * ----------------------------+------------+--------------+
     * don't belong to the group   |      c     |       d      |
     * ----------------------------+------------+--------------+
     *
     * @param values
     * @param groupItems item indices to propItems for the items that belongs to the group
     * @param ctable     contingency table: ctable[0] = a, ctable[1] = b, ctable[2] = c, ctable[3] = d
     */
    private static void calcContingencyTable(Iterable<Double> values, int[] groupItems, int[] ctable) {

        // Initialize the contingency table with zeros
        for (int i = 0; i < 4; i++)
            ctable[i] = 0;

        //sort group item indices
        Arrays.sort(groupItems); //FIXME this is redundant for diferent conditions

        // count
        int k = 0;
        for (Double value : values) {

            //TODO
            int i = 0;

            boolean belongsToGroup = k < groupItems.length && groupItems[k] == i;
            if (belongsToGroup) {
                k++;
            }

            if (!Double.isNaN(value)) {
                int j = (value == 1.0) ? 0 : 1;
                if (!belongsToGroup) {
                    j += 2;
                }

                ctable[j]++;
            }
        }
    }
}
