/*
 * #%L
 * org.gitools.heatmap
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
package org.gitools.heatmap.header;

import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.matrix.model.hashmatrix.HashMatrix;
import org.gitools.matrix.model.matrix.AnnotationMatrix;
import org.gitools.utils.translators.DoubleTranslator;


@Deprecated
public class ConvertStringToDoubleAdapter extends HashMatrix {

    private final AnnotationMatrix annotations;

    public ConvertStringToDoubleAdapter(AnnotationMatrix annotations) {
        super(annotations.getLayers(), annotations.getRows());
        this.annotations = annotations;

    }

    @Override
    public <T> T get(IMatrixLayer<T> layer, String... identifiers) {
        return (T) DoubleTranslator.get().stringToValue((String) annotations.get(layer, identifiers));
    }
}
