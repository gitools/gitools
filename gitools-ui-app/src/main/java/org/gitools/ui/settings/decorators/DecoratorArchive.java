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
package org.gitools.ui.settings.decorators;

import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.ElementDecoratorDescriptor;
import org.gitools.model.decorator.ElementDecoratorFactory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;


/**
 * @author michi
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DecoratorArchive
{

    public transient final static String VERSION = "2.0";

    private List<ElementDecorator> scaleDecorators = new ArrayList<ElementDecorator>();

    private transient final String DEFAULT = "Default";

    public DecoratorArchive()
    {
    }

    public void add(ElementDecorator[] decorators)
    {
        for (ElementDecorator d : decorators)
        {
            add(d);
        }
    }

    public void add(ElementDecorator decorator)
    {
        String name = decorator.getName();
        Set<String> map = getDecorators().keySet();
        if (map.contains(name))
        {
            remove(scaleDecorators, name);
            scaleDecorators.add(decorator);
        }
        else
        {
            scaleDecorators.add(decorator);
        }
    }

    private void remove(List<ElementDecorator> scaleDecorators, String name)
    {
        for (ElementDecorator d : scaleDecorators)
        {
            if (d.getName().equals(name))
            {
                scaleDecorators.remove(d);
                return;
            }
        }
    }

    public Map<String, ElementDecorator> getDecorators()
    {
        Map<String, ElementDecorator> decoratorMap = new HashMap<String, ElementDecorator>();

        for (ElementDecorator d : scaleDecorators)
        {
            decoratorMap.put(d.getName(), d);
        }
        return decoratorMap;
    }

    public ElementDecorator[] getDefaultElementDecoratros()
    {
        List<ElementDecoratorDescriptor> descriptors = ElementDecoratorFactory.getDescriptors();
        ElementDecorator[] decorators = new ElementDecorator[descriptors.size()];
        int i = 0;
        for (ElementDecoratorDescriptor descriptor : descriptors)
        {
            ElementDecorator decorator = null;
            try
            {
                decorator = descriptor.getDecoratorClass().
                        getConstructor().newInstance();
            } catch (Exception e)
            {
                return null;
            }
            //decorator = (descriptor.getDecoratorClass()) decorator;
            decorator.setName(DEFAULT + " " + descriptor.getName());
            decorators[i] = decorator;
            i++;
        }
        return decorators;
    }
}
