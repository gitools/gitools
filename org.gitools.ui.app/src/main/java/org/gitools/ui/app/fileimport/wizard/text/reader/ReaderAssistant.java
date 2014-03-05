/*
 * #%L
 * org.gitools.ui.app
 * %%
 * Copyright (C) 2013 - 2014 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.ui.app.fileimport.wizard.text.reader;


import org.gitools.api.matrix.IMatrix;
import org.gitools.matrix.model.MatrixLayer;

public abstract class ReaderAssistant {


    protected final FlatTextImporter reader;
    protected MatrixLayer[] heatmapLayers;

    public ReaderAssistant(FlatTextImporter reader) {
        this.reader = reader;
        update();
    }

    abstract public void fillMatrix(IMatrix matrix);

    public abstract String[] getHeatmapHeaders();

    public abstract void update();

    public MatrixLayer[] getHeatmapLayers() {
        return heatmapLayers;
    }


}
