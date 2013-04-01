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
package org.gitools.model.decorator;

import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.model.decorator.impl.*;

import java.util.ArrayList;
import java.util.List;


public class ElementDecoratorFactory
{

    private static final List<ElementDecoratorDescriptor> descriptors
            = new ArrayList<ElementDecoratorDescriptor>();

    static
    {
        descriptors.add(new ElementDecoratorDescriptor(
                ElementDecoratorNames.BINARY, BinaryElementDecorator.class));

        descriptors.add(new ElementDecoratorDescriptor(
                ElementDecoratorNames.LINEAR_TWO_SIDED, LinearTwoSidedElementDecorator.class));

        descriptors.add(new ElementDecoratorDescriptor(
                ElementDecoratorNames.PVALUE, PValueElementDecorator.class));

        descriptors.add(new ElementDecoratorDescriptor(
                ElementDecoratorNames.ZSCORE, ZScoreElementDecorator.class));

        descriptors.add(new ElementDecoratorDescriptor(
                ElementDecoratorNames.CORRELATION, CorrelationElementDecorator.class));

        descriptors.add(new ElementDecoratorDescriptor(
                ElementDecoratorNames.CATEGORICAL, CategoricalElementDecorator.class));

		/*descriptors.add(new ElementDecoratorDescriptor(
                ElementDecoratorNames.FORMATTED_TEXT, FormattedTextElementDecorator.class));*/

    }

    public static ElementDecorator create(
            String name,
            IElementAdapter adapter)
    {

        for (ElementDecoratorDescriptor descriptor : descriptors)
            if (descriptor.getName().equals(name))
            {
                return create(descriptor, adapter);
            }

        return null;
    }

    public static ElementDecorator create(
            ElementDecoratorDescriptor descriptor,
            IElementAdapter adapter)
    {

        final Class<? extends ElementDecorator> cls = descriptor.getDecoratorClass();

        ElementDecorator decorator = null;
        try
        {
            decorator = cls.getConstructor(IElementAdapter.class)
                    .newInstance(new Object[]{adapter});
        } catch (Exception e)
        {
            return null;
        }

        return decorator;
    }

    public static ElementDecoratorDescriptor getDescriptor(
            Class<? extends ElementDecorator> decoratorClass)
    {

        for (ElementDecoratorDescriptor desc : descriptors)
            if (desc.getDecoratorClass().equals(decoratorClass))
            {
                return desc;
            }

        return null;
    }

    public static List<ElementDecoratorDescriptor> getDescriptors()
    {
        return descriptors;
    }
}
