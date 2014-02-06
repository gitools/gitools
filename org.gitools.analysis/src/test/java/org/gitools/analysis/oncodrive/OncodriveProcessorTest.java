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
package org.gitools.analysis.oncodrive;


import static junit.framework.Assert.assertEquals;
import org.gitools.analysis.AbstractProcessorTest;
import org.gitools.analysis.AnalysisException;
import org.gitools.analysis.AnalysisProcessor;
import org.gitools.analysis.AssertMatrix;
import org.gitools.analysis.correlation.CorrelationAnalysis;
import org.gitools.analysis.correlation.CorrelationProcessor;
import org.gitools.analysis.htest.oncodrive.OncodriveAnalysis;
import org.gitools.analysis.htest.oncodrive.OncodriveProcessor;
import static org.gitools.api.ApplicationContext.getProgressMonitor;
import org.gitools.api.matrix.IMatrix;
import org.junit.Test;

import java.io.IOException;

public class OncodriveProcessorTest extends AbstractProcessorTest<OncodriveAnalysis> {

    public OncodriveProcessorTest() {
        super(OncodriveAnalysis.class, "/oncodrive/test01.oncodrive");
    }

    @Test
    public void testResourceFormat() {
        assertEquals("test", getAnalysis().getTitle());
    }

    @Test
    public void testAnalysisProcessor() throws IOException {

        OncodriveAnalysis analysis = getAnalysis();

        // Keep the correct results
        IMatrix resultsOk = analysis.getResults().get();
        analysis.setResults(null);

        try {
            AnalysisProcessor processor = new OncodriveProcessor(analysis);
            processor.run(getProgressMonitor());
        } catch (AnalysisException e) {
            e.printStackTrace();
        }

        // Test the store and load
        IMatrix results = storeAndLoadMatrix(analysis.getResults().get());

        // Compare the matrix
        AssertMatrix.assertEquals(resultsOk, results);

    }

}
