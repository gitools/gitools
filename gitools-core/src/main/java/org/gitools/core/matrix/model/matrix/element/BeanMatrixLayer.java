/*
 * #%L
 * gitools-core
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
package org.gitools.core.matrix.model.matrix.element;

import org.gitools.core.matrix.model.MatrixLayer;

import java.lang.reflect.Method;

public class BeanMatrixLayer extends MatrixLayer {

    private static final long serialVersionUID = 1735870808859461498L;

    private Method getterMethod;
    private Method setterMethod;

    public BeanMatrixLayer(String id, String name, String description, Class<?> valueClass, Method getterMethod, Method setterMethod) {

        super(id, valueClass, name, description);

        this.getterMethod = getterMethod;
        this.setterMethod = setterMethod;
    }

    public Method getGetterMethod() {
        return getterMethod;
    }

    public Method getSetterMethod() {
        return setterMethod;
    }
}
