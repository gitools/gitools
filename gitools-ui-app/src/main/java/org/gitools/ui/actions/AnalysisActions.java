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
package org.gitools.ui.actions;

import org.gitools.stats.mtc.BenjaminiHochbergFdr;
import org.gitools.stats.mtc.Bonferroni;
import org.gitools.ui.actions.analysis.*;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.actions.UnimplementedAction;

import java.awt.*;

class AnalysisActions
{

    public static final BaseAction combinations = new CombinationsAction();

    public static final BaseAction correlations = new CorrelationsAction();

    public static final BaseAction overlapping = new OverlappingsAction();

    public static final BaseAction groupComparison = new GroupComparisonAction();

    public static final BaseAction mtcBonferroniAction = new MtcAction(new Bonferroni());

    public static final BaseAction mtcBenjaminiHochbergFdrAction = new MtcAction(new BenjaminiHochbergFdr());

    public static final BaseAction mtcBenjaminiYekutieliFdrAction = new UnimplementedAction("Benjamini & Yekutieli FDR", false)
    {
        @Override
        protected Window getParent()
        {
            return AppFrame.get();
        }
    };


}
