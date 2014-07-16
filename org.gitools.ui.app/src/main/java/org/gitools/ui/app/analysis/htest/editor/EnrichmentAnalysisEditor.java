/*
 * #%L
 * gitools-ui-app
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
package org.gitools.ui.app.analysis.htest.editor;

import org.gitools.analysis.htest.enrichment.EnrichmentAnalysis;
import org.gitools.analysis.htest.enrichment.EnrichmentProcessor;
import org.gitools.analysis.htest.enrichment.format.EnrichmentAnalysisFormat;
import org.gitools.api.matrix.IMatrix;
import org.gitools.heatmap.Heatmap;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EnrichmentAnalysisEditor extends AbstractHtestAnalysisEditor<EnrichmentAnalysis> {

    public EnrichmentAnalysisEditor(EnrichmentAnalysis analysis) {
        super(analysis, "/vm/analysis/enrichment/analysis_details.vm", EnrichmentAnalysisFormat.EXTENSION);
    }

    protected Heatmap createResultsHeatmap(EnrichmentAnalysis analysis) {

        IMatrix results = analysis.getResults().get();

        results.setMetadata(EnrichmentAnalysis.CACHE_KEY_ENHRICHMENT, analysis);

        if (results instanceof Heatmap) {

            return (Heatmap) results;
        }

        return EnrichmentProcessor.createResultHeatmapFromMatrix(analysis.getTitle(), analysis.getTestConfig(), results);

    }

}
