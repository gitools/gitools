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
package org.gitools.model.xml;

import org.gitools.heatmap.header.HeatmapTextLabelsHeader;
import org.gitools.model.AbstractModel;
import org.gitools.model.decorator.ElementDecorator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

@Deprecated
@XmlAccessorType(XmlAccessType.FIELD)
public class AbstractModelDecoratorElement
{
    @XmlJavaTypeAdapter(IndexArrayXmlAdapter.class)
    private int order[];

    @XmlElementWrapper(name = "headerDecoratorType")
    @XmlElement(name = "headerDecorator")
    private List<HeatmapTextLabelsHeader> headerDecorators = new ArrayList<HeatmapTextLabelsHeader>();

    @XmlElementWrapper(name = "elementDecoratorType")
    @XmlElement(name = "elementDecorator")
    private List<ElementDecorator> elementDecorators = new ArrayList<ElementDecorator>();

    public AbstractModelDecoratorElement()
    {

    }

    public AbstractModelDecoratorElement(List<AbstractModel> elems)
    {
        int size = elems.size();
        order = new int[size];
        int nHeaders = 0;
        int nElems = 0;
        int i = 0;

        for (AbstractModel elem : elems)
        {
            if (elem instanceof HeatmapTextLabelsHeader)
            {
                headerDecorators.add((HeatmapTextLabelsHeader) elem);
                order[i] = nHeaders;
                nHeaders++;
            }
            else if (elem instanceof ElementDecorator)
            {
                elementDecorators.add((ElementDecorator) elem);
                order[i] = size + nElems;
                nElems++;
            }
            i++;
        }
    }

    public List<AbstractModel> getList()
    {
        List<AbstractModel> decorators = new ArrayList<AbstractModel>();
        int size = order.length;
        for (int elem : order)
        {
            if (elem >= size)
            {
                decorators.add(elementDecorators.get(elem - size));
            }
            else
            {
                decorators.add(headerDecorators.get(elem));
            }
        }
        return decorators;
    }

}
