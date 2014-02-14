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
package org.gitools.ui.app.actions.analysis;

import org.gitools.analysis.AnalysisProcessor;
import org.gitools.analysis.overlapping.OverlappingAnalysis;
import org.gitools.analysis.overlapping.OverlappingProcessor;
import org.gitools.heatmap.Heatmap;
import org.gitools.ui.app.actions.file.AbstractAnalysisAction;
import org.gitools.ui.app.analysis.htest.wizard.AnalysisWizard;
import org.gitools.ui.app.analysis.overlapping.OverlappingAnalysisEditor;
import org.gitools.ui.app.analysis.overlapping.wizard.OverlappingAnalysisWizard;
import org.gitools.ui.platform.editor.AbstractEditor;

import java.awt.event.KeyEvent;

public class OverlappingsAction extends AbstractAnalysisAction<OverlappingAnalysis> {

    public OverlappingsAction() {
        super("Overlapping...", KeyEvent.VK_V);
    }

    @Override
    protected AbstractEditor newEditor(OverlappingAnalysis analysis) {
        return new OverlappingAnalysisEditor(analysis);
    }

    @Override
    protected AnalysisWizard<? extends OverlappingAnalysis> newWizard(Heatmap heatmap) {
        return new OverlappingAnalysisWizard(heatmap);
    }

    @Override
    protected AnalysisProcessor newProcessor(OverlappingAnalysis analysis) {
        return new OverlappingProcessor(analysis);
    }
}
