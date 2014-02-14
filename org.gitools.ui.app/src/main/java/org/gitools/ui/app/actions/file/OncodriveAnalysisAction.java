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
import org.gitools.analysis.htest.oncodrive.OncodriveAnalysis;
import org.gitools.analysis.htest.oncodrive.OncodriveProcessor;
import org.gitools.heatmap.Heatmap;
import org.gitools.ui.app.analysis.htest.editor.OncodriveAnalysisEditor;
import org.gitools.ui.app.analysis.htest.wizard.AnalysisWizard;
import org.gitools.ui.app.analysis.htest.wizard.OncodriveAnalysisWizard;
import org.gitools.ui.platform.editor.AbstractEditor;

import java.awt.event.KeyEvent;

public class OncodriveAnalysisAction extends AbstractAnalysisAction<OncodriveAnalysis> {

    public OncodriveAnalysisAction() {
        super("OncoDrive...", KeyEvent.VK_O);
    }

    @Override
    protected AbstractEditor newEditor(OncodriveAnalysis analysis) {
        return new OncodriveAnalysisEditor(analysis);
    }

    @Override
    protected AnalysisWizard<? extends OncodriveAnalysis> newWizard(Heatmap heatmap) {
        return new OncodriveAnalysisWizard(heatmap);
    }

    @Override
    protected AnalysisProcessor newProcessor(OncodriveAnalysis analysis) {
        return new OncodriveProcessor(analysis);
    }
}
