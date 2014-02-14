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
package org.gitools.analysis.overlap;


import org.gitools.analysis.AbstractProcessorTest;
import org.gitools.analysis.AnalysisException;
import org.gitools.analysis.AnalysisProcessor;
import org.gitools.analysis.AssertMatrix;
import org.gitools.analysis.overlapping.OverlappingAnalysis;
import org.gitools.analysis.overlapping.OverlappingProcessor;
import org.gitools.api.matrix.IMatrix;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static org.gitools.api.ApplicationContext.getProgressMonitor;

public class OverlapProcessorTest extends AbstractProcessorTest<OverlappingAnalysis> {

    public OverlapProcessorTest() {
        super(OverlappingAnalysis.class, "/overlap/analysis.overlapping");
    }

    @Test
    public void testResourceFormat() {
        assertEquals("analysis", getAnalysis().getTitle());
    }

    @Test
    public void testAnalysisProcessor() throws IOException {

        OverlappingAnalysis analysis = getAnalysis();

        // Keep the correct results
        IMatrix resultsOk = analysis.getCellResults().get();
        analysis.setCellResults(null);

        // Execute the analysis
        try {
            AnalysisProcessor processor = new OverlappingProcessor(analysis);
            processor.run(getProgressMonitor());
        } catch (AnalysisException e) {
            e.printStackTrace();
        }

        // Test the store and load
        IMatrix results = storeAndLoadMatrix(analysis.getCellResults().get());

        // Compare the matrix
        AssertMatrix.assertEquals(resultsOk, results);

    }

}
