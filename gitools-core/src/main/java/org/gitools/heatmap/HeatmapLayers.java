package org.gitools.heatmap;

import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.IMatrixLayers;
import org.gitools.matrix.model.IMatrixViewLayers;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.matrix.model.element.ILayerDescriptor;
import org.gitools.model.AbstractModel;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.ElementDecoratorFactory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class HeatmapLayers extends AbstractModel implements IMatrixViewLayers<HeatmapLayer>
{
    @XmlElement(name = "top-layer")
    private int topLayer;

    @XmlElement(name = "layer")
    private List<HeatmapLayer> layers;
    private transient Map<String, Integer> layersIdToIndex;

    public HeatmapLayers()
    {
        this.topLayer = 0;
    }

    public HeatmapLayers(IMatrix matrix)
    {
        this();
        createLayers(matrix);
        init(matrix);
    }

    private void createLayers(IMatrix matrix)
    {
        IMatrixLayers<? extends ILayerDescriptor> matrixLayers = matrix.getLayers();
        this.layers = new ArrayList<HeatmapLayer>(matrixLayers.size());

        for (int i=0; i < matrixLayers.size(); i++)
        {
            ILayerDescriptor layer = matrixLayers.get(i);
            ElementDecorator defaultDecorator = ElementDecoratorFactory.defaultDecorator(matrix, i);
            this.layers.add(new HeatmapLayer(layer.getId(), layer.getValueClass(), defaultDecorator));
        }
    }

    public void init(IMatrix matrix)
    {
        IMatrixLayers matrixLayers = matrix.getLayers();
        this.layersIdToIndex = new HashMap<String, Integer>(matrixLayers.size());
        for(int i=0; i < layers.size(); i++)
        {
            this.layersIdToIndex.put(layers.get(i).getId(), i);
        }

        // Initialize decorators adapters
        IElementAdapter adapter = matrix.getCellAdapter();
        for (ElementDecorator decorator : getCellDecorators())
        {
            decorator.setAdapter(adapter);
        }
    }


    /**
     * Get cell decorators.
     *
     * @return the element decorator [ ]
     * @deprecated
     */
    public ElementDecorator[] getCellDecorators()
    {
        ElementDecorator[] decorators = new ElementDecorator[layers.size()];

        for (int i=0; i < layers.size(); i++)
        {
            decorators[i] = layers.get(i).getDecorator();
        }

        return decorators;
    }

    @Deprecated
    public void setCellDecorators(ElementDecorator[] decorators)
    {

        for (int i=0; i < decorators.length; i++)
        {
            layers.get(i).setDecorator(decorators[i]);
        }
    }

    @Override
    public int getTopLayer()
    {
        return topLayer;
    }

    @Override
    public void setTopLayer(int layerIndex)
    {
        this.topLayer = layerIndex;
    }

    @Override
    public HeatmapLayer get(int layerIndex)
    {
        return layers.get(layerIndex);
    }

    @Override
    public int findId(String id)
    {
        return layersIdToIndex.get(id);
    }

    @Override
    public int size()
    {
        return layers.size();
    }

    @Override
    public Iterator<HeatmapLayer> iterator()
    {
        return layers.iterator();
    }
}
