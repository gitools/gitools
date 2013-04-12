package org.gitools.heatmap;

import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.model.AbstractModel;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.ElementDecoratorDescriptor;
import org.gitools.model.decorator.ElementDecoratorFactory;
import org.gitools.model.decorator.ElementDecoratorNames;
import org.gitools.persistence.ResourceReference;
import org.gitools.persistence.formats.analysis.adapter.ResourceReferenceXmlAdapter;
import org.gitools.stats.test.results.CommonResult;
import org.gitools.stats.test.results.ZScoreResult;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class HeatmapBody extends AbstractModel
{
    int selectedView;
    @XmlJavaTypeAdapter(ResourceReferenceXmlAdapter.class)
    private ResourceReference<IMatrix> data;
    private ElementDecorator[] views;

    public HeatmapBody()
    {
    }

    public HeatmapBody(IMatrix data)
    {
        this.data = new ResourceReference<IMatrix>("data", data);
        this.selectedView = 0;
        this.views = cellDecoratorFromMatrix(data);

    }

    @Deprecated
    private static ElementDecorator[] cellDecoratorFromMatrix(@NotNull IMatrix matrix)
    {

        ElementDecorator decorator = null;

        IElementAdapter adapter = matrix.getCellAdapter();
        List<IElementAttribute> attributes = matrix.getCellAttributes();

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

        return getCellDecoratorsFromDecorator(decorator, matrix.getCellAttributes().size());
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

    public ResourceReference<IMatrix> getData()
    {
        return data;
    }

    public void setData(ResourceReference<IMatrix> data)
    {
        this.data = data;
    }

    public int getSelectedView()
    {
        return selectedView;
    }

    public void setSelectedView(int viewIndex)
    {
        this.selectedView = viewIndex;
    }

    public final ElementDecorator getActiveView()
    {
        return views[getSelectedView()];
    }

    public final ElementDecorator[] getViews()
    {
        return this.views;
    }

    public void setViews(ElementDecorator[] views)
    {
        this.views = views;
    }

    public void init()
    {
        //To change body of created methods use File | Settings | File Templates.
    }

    public void detach()
    {
        if (data != null && data.isLoaded())
        {
            data.get().detach();
        }
    }


}
