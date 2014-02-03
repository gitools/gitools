package org.gitools.analysis.groupcomparison;

import static junit.framework.Assert.assertEquals;
import org.gitools.analysis.WeldRunner;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.resource.ResourceReference;
import org.gitools.persistence.locators.UrlResourceLocator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(WeldRunner.class)
public class GroupComparisonProcessorTest {

    private GroupComparisonAnalysis analysis;

    @Before
    public void setUp() throws Exception {

        analysis = new ResourceReference<>(
                new UrlResourceLocator(getClass().getResource("/groupcomparison/tp53-signalling CIS-effect.comparison")),
                GroupComparisonAnalysis.class
        ).get();

    }

    @Test
    public void testAnalysisProcessor() {

        // Keep the correct results
        ResourceReference<IMatrix> resultsOk = analysis.getResults();
        analysis.setResults(null);

        // Execute the analysis
        /*try {
            AnalysisProcessor processor = new GroupComparisonProcessor(analysis);
            processor.run(ApplicationContext.getProgressMonitor());
        } catch (AnalysisException e) {
            e.printStackTrace();
        }*/

        // Compare results

    }

    @Test
    public void testResourceFormat() {
        assertEquals(analysis.getTitle(), "tp53-signalling CIS-effect");
        assertEquals(analysis.getDescription(), "GBM: CNA effect in expression in TP53 signalling pathway. This test assesses the effecto of copy umber alteration on the expression status of the gene. Read under the following link on how to perform the test: http://help.gitools.org/xwiki/bin/view/Tutorials/Tutorial63");
        assertEquals(analysis.getAttributeIndex(), 0);
        assertEquals(analysis.getMtc(), "bh");
        assertEquals(analysis.getColumnGrouping(), "Group by value");
    }
}
