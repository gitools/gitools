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
package org.gitools.ui.app.actions.edit;

import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.ui.app.actions.HeatmapDimensionAction;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.pages.common.PatternSourcePage;
import org.gitools.ui.platform.dialog.GlassPanePageDialog;
import org.gitools.ui.platform.icons.IconNames;

import java.awt.event.ActionEvent;

public class AnnotationAction extends HeatmapDimensionAction {

    public AnnotationAction(MatrixDimensionKey dim) {
        super(dim, "<html><i>Annotations</i>...</html>");

        setSmallIconFromResource(IconNames.annotation16);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        HeatmapDimension heatmapDimension = getDimension();

        PatternSourcePage annotationPage = new PatternSourcePage(heatmapDimension, true, true);
        annotationPage.setTitle("View and load available " + heatmapDimension.getId().getLabel() + " annotations");

        GlassPanePageDialog p = new GlassPanePageDialog(Application.get(), annotationPage);
        //PageDialog tdlg = new PageDialog(Application.get(), annotationPage);

        //tdlg.setTitle("Header type selection");
        p.open();
        if (p.isCancelled())
            return;

    }

}
