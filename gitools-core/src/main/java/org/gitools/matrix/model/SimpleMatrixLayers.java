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
package org.gitools.matrix.model;

import java.util.*;

public class SimpleMatrixLayers implements IMatrixLayers<SimpleMatrixLayer>
{
    private List<SimpleMatrixLayer> descriptors;
    private Map<String, Integer> idToIndex;

    public SimpleMatrixLayers(List<SimpleMatrixLayer> descriptors)
    {
        this.descriptors = descriptors;

        init();
    }

    public SimpleMatrixLayers(Class<?> valueClass, String... headers)
    {
        this.descriptors = new ArrayList<SimpleMatrixLayer>(headers.length);

        for (String header : headers)
        {
            this.descriptors.add(new SimpleMatrixLayer(header, valueClass));
        }

        init();
    }

    private void init()
    {
        this.idToIndex = new HashMap<String, Integer>(descriptors.size());
        for (int i=0; i < descriptors.size(); i++)
        {
            this.idToIndex.put(descriptors.get(i).getId(), i);
        }
    }

    @Override
    public SimpleMatrixLayer get(int layerIndex)
    {
        return descriptors.get(layerIndex);
    }

    @Override
    public int findId(String id)
    {
        if (idToIndex.containsKey(id))
        {
            return idToIndex.get(id);
        }

        return -1;
    }

    @Override
    public int size()
    {
        return descriptors.size();
    }

    @Override
    public Iterator<SimpleMatrixLayer> iterator()
    {
        return descriptors.iterator();
    }
}
