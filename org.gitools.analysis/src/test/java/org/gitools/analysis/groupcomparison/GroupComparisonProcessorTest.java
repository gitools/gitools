/*
 * #%L
 * org.gitools.analysis
 * %%
 * Copyright (C) 2013 - 2014 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.analysis.groupcomparison;

import org.gitools.analysis.AbstractProcessorTest;
import org.gitools.analysis.AssertMatrix;
import org.gitools.api.matrix.IMatrix;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;

public class GroupComparisonProcessorTest extends AbstractProcessorTest<GroupComparisonAnalysis> {

    public GroupComparisonProcessorTest() {
        super(GroupComparisonAnalysis.class, "/groupcomparison/tp53-signalling CIS-effect.comparison");
    }

    @Test
    public void testAnalysisProcessor() throws IOException {

        GroupComparisonAnalysis analysis = getAnalysis();

        // Keep the correct results
        IMatrix resultsOk = analysis.getResults().get();
        analysis.setResults(null);

        //TODO Execute the analysis
        /*try {
            AnalysisProcessor processor = new GroupComparisonProcessor(analysis);
            processor.run(ApplicationContext.getProgressMonitor());
        } catch (AnalysisException e) {
            e.printStackTrace();
        }*/

        // Test store and load
        IMatrix results = storeAndLoadMatrix(resultsOk);

        // Compare the matrix
        AssertMatrix.assertEquals(resultsOk, results);
    }

    @Test
    public void testResourceFormat() {
        assertEquals("tp53-signalling CIS-effect", getAnalysis().getTitle());
        assertEquals("GBM: CNA effect in expression in TP53 signalling pathway. This test assesses the effecto of copy umber alteration on the expression status of the gene. Read under the following link on how to perform the test: http://help.gitools.org/xwiki/bin/view/Tutorials/Tutorial63", getAnalysis().getDescription());
    }
}
