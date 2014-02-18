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
import org.gitools.analysis.htest.oncodrive.OncodriveAnalysis;
import org.gitools.analysis.htest.oncodrive.format.OncodriveAnalysisFormat;
import org.gitools.analysis.overlapping.format.OverlappingAnalysisFormat;
import org.gitools.analysis.stats.test.factory.TestFactory;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.persistence.FileFormat;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.decorator.impl.PValueDecorator;
import org.gitools.heatmap.decorator.impl.ZScoreDecorator;

public class OncodriveAnalysisEditor extends AbstractHtestAnalysisEditor<OncodriveAnalysis> {

    public OncodriveAnalysisEditor(OncodriveAnalysis analysis) {
        super(analysis, "/vm/analysis/oncodrive/analysis_details.vm", OncodriveAnalysisFormat.EXTENSION);
    }

    @Override
    protected String getFileName() {
        return getModel().getTitle();
    }

    @Override
    protected FileFormat[] getFileFormats() {
        return new FileFormat[] { OncodriveAnalysisFormat.FILE_FORMAT };
    }

    @Override
    protected Heatmap createResultsHeatmap(OncodriveAnalysis analysis) {

        IMatrix results = analysis.getResults().get();

        if (results instanceof Heatmap) {
            return (Heatmap) results;
        }

        //Deprecated
        Heatmap heatmap = new Heatmap(results);
        heatmap.setTitle(analysis.getTitle() + " (results)");
        return heatmap;
    }

}
