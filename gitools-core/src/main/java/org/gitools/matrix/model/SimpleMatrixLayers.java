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
