package org.gitools.heatmap;

import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.IMatrixLayers;
import org.gitools.matrix.model.IMatrixViewLayers;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.matrix.model.element.ILayerDescriptor;
import org.gitools.model.AbstractModel;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.ElementDecoratorDescriptor;
import org.gitools.model.decorator.ElementDecoratorFactory;
import org.gitools.model.decorator.ElementDecoratorNames;
import org.gitools.stats.test.results.CommonResult;
import org.gitools.stats.test.results.ZScoreResult;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.*;
import java.util.Iterator;

@XmlAccessorType(XmlAccessType.FIELD)
public class HeatmapLayers extends AbstractModel implements IMatrixViewLayers
{
    @XmlElement(name = "top-layer")
    private int topLayer;

    @XmlElementWrapper(name = "decorators")
    @XmlElement(name = "decorator")
    private ElementDecorator[] decorators;

    @XmlTransient
    private IMatrixLayers matrixLayers;

    public HeatmapLayers()
    {
        this.topLayer = 0;
    }

    public HeatmapLayers(IMatrix matrix)
    {
        this();
        this.decorators = cellDecoratorFromMatrix(matrix);
        init(matrix);
    }

    public void init(IMatrix matrix)
    {
        this.matrixLayers = matrix.getLayers();

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
     */
    public ElementDecorator[] getCellDecorators()
    {
        return this.decorators;
    }

    public void setCellDecorators(ElementDecorator[] decorators)
    {
        this.decorators = decorators;
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
    public ILayerDescriptor get(int layerIndex)
    {
        return matrixLayers.get(layerIndex);
    }

    @Override
    public int findId(String id)
    {
        return matrixLayers.findId(id);
    }

    @Override
    public int size()
    {
        return matrixLayers.size();
    }

    @Override
    public Iterator<ILayerDescriptor> iterator()
    {
        return matrixLayers.iterator();
    }


    @Deprecated
    private static ElementDecorator[] cellDecoratorFromMatrix(@NotNull IMatrix matrix)
    {

        ElementDecorator decorator = null;

        IElementAdapter adapter = matrix.getCellAdapter();
        IMatrixLayers attributes = matrix.getLayers();

        int attrIndex = 0;
        if (attrIndex >= 0 && attrIndex < attributes.size())
        {
            Class<?> elementClass = attributes.get(attrIndex).getValueClass();

            Class<?> c = adapter.getElementClass();

            if (CommonResult.class.isAssignableFrom(c) || ZScoreResult.class == c)
            {
                decorator = ElementDecoratorFactory.create(ElementDecoratorNames.ZSCORE, adapter);
            }
            else if (CommonResult.class.isAssignableFrom(c) || CommonResult.class == c)
            {
                decorator = ElementDecoratorFactory.create(ElementDecoratorNames.PVALUE, adapter);
            }
            else if (elementClass == double.class || double.class.isInstance(elementClass))
            {
                decorator = ElementDecoratorFactory.create(ElementDecoratorNames.LINEAR_TWO_SIDED, adapter);
            }
        }

        if (decorator == null)
        {
            decorator = ElementDecoratorFactory.create(ElementDecoratorNames.LINEAR_TWO_SIDED, adapter);
        }

        return getCellDecoratorsFromDecorator(decorator, matrix.getLayers().size());
    }

    @Deprecated
    private static ElementDecorator[] getCellDecoratorsFromDecorator(@NotNull ElementDecorator cellDecorator, int attributesNb)
    {
        ElementDecorator[] cellDecorators = new ElementDecorator[attributesNb];
        for (int i = 0; i < attributesNb; i++)
        {
            ElementDecoratorDescriptor descriptor = ElementDecoratorFactory.getDescriptor(cellDecorator.getClass());
            cellDecorators[i] = ElementDecoratorFactory.create(descriptor, cellDecorator.getAdapter());
        }
        return cellDecorators;
    }
}
