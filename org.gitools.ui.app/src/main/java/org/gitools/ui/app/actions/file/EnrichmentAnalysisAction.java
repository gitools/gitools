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
package org.gitools.ui.app.actions.file;

import org.gitools.analysis.AnalysisProcessor;
import org.gitools.analysis.htest.enrichment.EnrichmentAnalysis;
import org.gitools.analysis.htest.enrichment.EnrichmentProcessor;
import org.gitools.heatmap.Heatmap;
import org.gitools.ui.app.actions.analysis.AbstractAnalysisAction;
import org.gitools.ui.app.analysis.htest.editor.EnrichmentAnalysisEditor;
import org.gitools.ui.app.analysis.htest.wizard.EnrichmentAnalysisWizard;
import org.gitools.ui.core.components.editor.AbstractEditor;
import org.gitools.ui.core.components.wizard.AnalysisWizard;
import org.gitools.ui.platform.icons.IconNames;

import java.awt.event.KeyEvent;

public class EnrichmentAnalysisAction extends AbstractAnalysisAction<EnrichmentAnalysis> {

    public EnrichmentAnalysisAction() {
        super("Enrichment...", KeyEvent.VK_E);
        setDesc("Run an enrichment analysis");
        setSmallIconFromResource(IconNames.empty16);
    }

    @Override
    protected AbstractEditor newEditor(EnrichmentAnalysis analysis) {
        return new EnrichmentAnalysisEditor(analysis);
    }

    @Override
    protected AnalysisWizard<? extends EnrichmentAnalysis> newWizard(Heatmap heatmap) {
        return new EnrichmentAnalysisWizard(heatmap);
    }

    @Override
    protected AnalysisProcessor newProcessor(EnrichmentAnalysis analysis) {
        return new EnrichmentProcessor(analysis);
    }


}
