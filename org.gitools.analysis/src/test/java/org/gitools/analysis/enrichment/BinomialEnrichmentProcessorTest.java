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
package org.gitools.analysis.enrichment;


import org.gitools.analysis.AbstractProcessorTest;
import org.gitools.analysis.AnalysisException;
import org.gitools.analysis.AnalysisProcessor;
import org.gitools.analysis.AssertMatrix;
import org.gitools.analysis.htest.enrichment.EnrichmentAnalysis;
import org.gitools.analysis.htest.enrichment.EnrichmentProcessor;
import org.gitools.api.matrix.IMatrix;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static org.gitools.api.ApplicationContext.getProgressMonitor;

public class BinomialEnrichmentProcessorTest extends AbstractProcessorTest<EnrichmentAnalysis> {

    public BinomialEnrichmentProcessorTest() {
        super(EnrichmentAnalysis.class, "/enrichment/test01.enrichment");
    }

    @Test
    public void testResourceFormat() {
        assertEquals("test", getAnalysis().getTitle());
    }

    @Test
    public void testAnalysisProcessor() throws IOException {

        EnrichmentAnalysis analysis = getAnalysis();

        // Keep the correct results
        IMatrix resultsOk = analysis.getResults().get();
        analysis.setResults(null);

        // Execute the analysis
        try {
            AnalysisProcessor processor = new EnrichmentProcessor(analysis);
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
