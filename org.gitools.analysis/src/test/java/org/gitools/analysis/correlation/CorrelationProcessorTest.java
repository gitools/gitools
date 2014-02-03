package org.gitools.analysis.correlation;


import static junit.framework.Assert.assertEquals;
import org.gitools.analysis.AnalysisException;
import org.gitools.analysis.AnalysisProcessor;
import org.gitools.analysis.AssertMatrix;
import org.gitools.analysis.WeldRunner;
import static org.gitools.api.ApplicationContext.getPersistenceManager;
import static org.gitools.api.ApplicationContext.getProgressMonitor;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.resource.IResourceFormat;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.api.resource.ResourceReference;
import org.gitools.persistence.locators.UrlResourceLocator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

@RunWith(WeldRunner.class)
public class CorrelationProcessorTest {

    private CorrelationAnalysis analysis;

    @Before
    public void setUp() throws Exception {

        analysis = new ResourceReference<>(
                new UrlResourceLocator(getClass().getResource("/correlation/analysis.correlations")),
                CorrelationAnalysis.class
        ).get();

    }

    @Test
    public void testResourceFormat() {
        assertEquals("analysis", analysis.getTitle());
        assertEquals("pearson", analysis.getMethod());
    }

    @Test
    public void testAnalysisProcessor() throws IOException {

        // Keep the correct results
        IMatrix resultsOk = analysis.getResults().get();
        analysis.setResults(null);

        // Execute the analysis
        try {
            AnalysisProcessor processor = new CorrelationProcessor(analysis);
            processor.run(getProgressMonitor());
        } catch (AnalysisException e) {
            e.printStackTrace();
        }

        // Compare results
        IMatrix results = analysis.getResults().get();

        // Write the results to a temporal file
        File tmpFile = File.createTempFile("correlation-test", ".tdm");
        IResourceLocator tmpLocator = new UrlResourceLocator(tmpFile);
        IResourceFormat<IMatrix> format = getPersistenceManager().getFormat("tdm", IMatrix.class);
        getPersistenceManager().store(tmpLocator, results, format, getProgressMonitor());

        // Load the results from the temporal file
        results = new ResourceReference<>(tmpLocator, format).get();

        // Compare the matrix
        AssertMatrix.assertEquals(resultsOk, results);

        // Remove temporal file
        //tmpFile.delete();
    }

}
