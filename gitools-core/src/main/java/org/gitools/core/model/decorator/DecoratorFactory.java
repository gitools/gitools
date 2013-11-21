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
package org.gitools.core.model.decorator;

import org.gitools.api.matrix.IMatrix;
import org.gitools.core.model.decorator.impl.*;

import java.util.ArrayList;
import java.util.List;


public class DecoratorFactory {

    private static final List<DecoratorDescriptor> descriptors = new ArrayList<>();

    static {
        descriptors.add(new DecoratorDescriptor(DecoratorNames.BINARY, BinaryDecorator.class));

        descriptors.add(new DecoratorDescriptor(DecoratorNames.LINEAR, LinearDecorator.class));

        descriptors.add(new DecoratorDescriptor(DecoratorNames.PVALUE, PValueDecorator.class));

        descriptors.add(new DecoratorDescriptor(DecoratorNames.ZSCORE, ZScoreDecorator.class));

        descriptors.add(new DecoratorDescriptor(DecoratorNames.CORRELATION, CorrelationDecorator.class));

        descriptors.add(new DecoratorDescriptor(DecoratorNames.CATEGORICAL, CategoricalDecorator.class));
    }


    public static <D extends Decorator> D create(Class<D> decoratorClass) {
        D decorator;
        try {
            decorator = decoratorClass.newInstance();
        } catch (Exception e) {
            return null;
        }

        return decorator;
    }


    public static DecoratorDescriptor getDescriptor(Class<? extends Decorator> decoratorClass) {

        for (DecoratorDescriptor desc : descriptors)
            if (desc.getDecoratorClass().equals(decoratorClass)) {
                return desc;
            }

        return null;
    }


    public static List<DecoratorDescriptor> getDescriptors() {
        return descriptors;
    }

    @Deprecated
    public static Decorator defaultDecorator(IMatrix matrix, String layer) {

        /*
        Decorator decorator;

        if (matrix instanceof ObjectMatrix) {
            ObjectMatrix om = (ObjectMatrix) matrix;

            Class<?> c = om.getObjectCellAdapter().getElementClass();

            if (CommonResult.class.isAssignableFrom(c) || ZScoreResult.class == c) {
                decorator = new ZScoreDecorator();
            } else if (CommonResult.class.isAssignableFrom(c) || CommonResult.class == c) {
                decorator = new PValueDecorator();
            } else {
                decorator = new LinearDecorator();
            }

        } else {
            decorator = new LinearDecorator();
        }

        return decorator;
        */

        return new LinearDecorator();
    }

}
