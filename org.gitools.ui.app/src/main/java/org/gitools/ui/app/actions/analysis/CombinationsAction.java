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
import org.gitools.analysis.combination.CombinationAnalysis;
import org.gitools.analysis.combination.CombinationProcessor;
import org.gitools.heatmap.Heatmap;
import org.gitools.ui.app.analysis.combination.editor.CombinationAnalysisEditor;
import org.gitools.ui.app.analysis.combination.wizard.CombinationAnalysisWizard;
import org.gitools.ui.app.analysis.htest.wizard.AnalysisWizard;
import org.gitools.ui.core.components.editor.AbstractEditor;

import java.awt.event.KeyEvent;

public class CombinationsAction extends AbstractAnalysisAction<CombinationAnalysis> {

    public CombinationsAction() {
        super("Combinations...", KeyEvent.VK_M);
    }

    @Override
    protected AbstractEditor newEditor(CombinationAnalysis analysis) {
        return new CombinationAnalysisEditor(analysis);
    }

    @Override
    protected AnalysisWizard<? extends CombinationAnalysis> newWizard(Heatmap heatmap) {
        return new CombinationAnalysisWizard(heatmap);
    }

    @Override
    protected AnalysisProcessor newProcessor(CombinationAnalysis analysis) {
        return new CombinationProcessor(analysis);
    }
}
