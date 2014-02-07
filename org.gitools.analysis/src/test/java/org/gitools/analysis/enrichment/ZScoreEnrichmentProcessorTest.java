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


import junit.framework.Assert;
import static junit.framework.Assert.assertEquals;
import org.gitools.analysis.AbstractProcessorTest;
import org.gitools.analysis.AnalysisException;
import org.gitools.analysis.AnalysisProcessor;
import org.gitools.analysis.AssertMatrix;
import org.gitools.analysis.htest.enrichment.EnrichmentAnalysis;
import org.gitools.analysis.htest.enrichment.EnrichmentProcessor;
import static org.gitools.api.ApplicationContext.getProgressMonitor;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.junit.Test;

import java.io.IOException;

public class ZScoreEnrichmentProcessorTest extends AbstractProcessorTest<EnrichmentAnalysis> {

    public ZScoreEnrichmentProcessorTest() {
        super(EnrichmentAnalysis.class, "/enrichment/test02.enrichment");
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

        // Compare the two matrices
        for (MatrixDimensionKey key : resultsOk.getDimensionKeys()) {
            assertEquals(resultsOk.getDimension(key), results.getDimension(key));
        }
        Assert.assertEquals(resultsOk.getLayers().size(), results.getLayers().size());

        AssertMatrix.assertEquals("N", resultsOk, results);
        AssertMatrix.assertEquals("observed", resultsOk, results);

        AssertMatrix.assertEquals("expected-mean", resultsOk, results, 0.004);
        AssertMatrix.assertEquals("expected-stdev", resultsOk, results, 0.03);
        AssertMatrix.assertEquals("z-score", resultsOk, results, 0.7);
        AssertMatrix.assertEquals("left-p-value", resultsOk, results, 0.07);
        AssertMatrix.assertEquals("right-p-value", resultsOk, results, 0.07);
        AssertMatrix.assertEquals("two-tail-p-value", resultsOk, results, 0.13);
        AssertMatrix.assertEquals("corrected-left-p-value", resultsOk, results, 0.48);
        AssertMatrix.assertEquals("corrected-right-p-value", resultsOk, results, 0.48);
        AssertMatrix.assertEquals("corrected-two-tail-p-value", resultsOk, results, 0.68);

    }

}
