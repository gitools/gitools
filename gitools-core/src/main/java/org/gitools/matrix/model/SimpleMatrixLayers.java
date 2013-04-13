package org.gitools.matrix.model;

import java.util.*;

public class SimpleMatrixLayers implements IMatrixLayers<SimpleLayerDescriptor>
{
    private List<SimpleLayerDescriptor> descriptors;
    private Map<String, Integer> idToIndex;

    public SimpleMatrixLayers(List<SimpleLayerDescriptor> descriptors)
    {
        this.descriptors = descriptors;

        init();
    }

    public SimpleMatrixLayers(Class<?> valueClass, String... headers)
    {
        this.descriptors = new ArrayList<SimpleLayerDescriptor>(headers.length);

        for (String header : headers)
        {
            this.descriptors.add(new SimpleLayerDescriptor(header, valueClass));
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
    public SimpleLayerDescriptor get(int layerIndex)
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
    public Iterator<SimpleLayerDescriptor> iterator()
    {
        return descriptors.iterator();
    }
}
