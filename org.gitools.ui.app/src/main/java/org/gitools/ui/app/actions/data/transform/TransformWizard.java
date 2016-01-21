/*
 * #%L
 * org.gitools.ui.app
 * %%
 * Copyright (C) 2013 - 2016 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.ui.app.actions.data.transform;

import org.gitools.api.matrix.ConfigurableTransformFunction;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.MatrixLayer;
import org.gitools.ui.platform.wizard.AbstractWizard;

import java.util.List;

public class TransformWizard extends AbstractWizard {
    private Heatmap heatmap;
    private SelectTransformationsPage selectTransformationsPage;


    public TransformWizard(Heatmap heatmap) {

        this.heatmap = heatmap;
    }

    @Override
    public void addPages() {
        selectTransformationsPage = new SelectTransformationsPage(heatmap);
        addPage("transformations", selectTransformationsPage);
    }


    @Override
    public void performFinish() {
        super.performFinish();
    }

    public List<ConfigurableTransformFunction> getFunctions() {
        return selectTransformationsPage.getTransformationFunctions();
    }


    public IMatrixLayer getLayer() {
        return selectTransformationsPage.getLayer();
    }

    public MatrixLayer<Double> getNewLayer() {

        return selectTransformationsPage.getNewLayer();
    }
}
