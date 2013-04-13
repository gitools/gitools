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
package org.gitools.ui.heatmap.panel.details;

import com.alee.laf.panel.WebPanel;
import info.clearthought.layout.SingleFiledLayout;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.idtype.IdType;
import org.gitools.idtype.IdTypeManager;
import org.gitools.idtype.UrlLink;
import org.gitools.matrix.model.AnnotationResolver;
import org.gitools.matrix.model.IMatrixLayers;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.element.ILayerDescriptor;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.impl.CategoricalElementDecorator;
import org.gitools.ui.heatmap.panel.details.boxes.Cell;
import org.gitools.ui.heatmap.panel.details.boxes.PropertyItem;
import org.gitools.utils.colorscale.impl.CategoricalColorScale;
import org.gitools.utils.formatter.GenericFormatter;
import org.gitools.utils.textpatt.TextPattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * An abstract details panel of a {@link Heatmap}.
 */
public abstract class AbstractDetailsPanel extends WebPanel
{

    private static final GenericFormatter FORMATTER = new GenericFormatter();
    @NotNull
    private final Heatmap heatmap;

    /**
     * Instantiates a new Abstract details panel.
     *
     * @param heatmap the heatmap
     */
    AbstractDetailsPanel(@NotNull final Heatmap heatmap)
    {
        super();
        this.heatmap = heatmap;

        setBackground(Color.WHITE);
        setLayout(new SingleFiledLayout(SingleFiledLayout.COLUMN, SingleFiledLayout.CENTER, 5));

        heatmap.addPropertyChangeListener(new PropertyChangeListener()
        {
            @Override
            public void propertyChange(@NotNull PropertyChangeEvent evt)
            {
                if ("selectionLead".equals(evt.getPropertyName()))
                {
                    updateDetailsPanel();
                }
            }
        });

        addComponentListener(new ComponentAdapter()
        {
            @Override
            public void componentResized(ComponentEvent e)
            {
                updateDetailsPanel();
            }
        });


        updateBoxes();
    }

    private void updateDetailsPanel()
    {
        removeAll();
        updateBoxes();
    }

    /**
     * This method is called when the heatmap properties had changed. The
     * implementing classes had to add all the boxes again.
     */
    protected abstract void updateBoxes();

    /**
     * Returns true if there is one cell selected
     *
     * @return
     */
    final boolean isCellSelected()
    {
        return (getMatrixView().getColumns().getSelectionLead(  ) != -1 && getMatrixView().getRows().getSelectionLead(  ) != -1);
    }

    /**
     * Gets total rows count of the matrix.
     *
     * @return the rows count
     */
    final int getRowsCount()
    {
        return getMatrixView().getRows().size();
    }

    /**
     * Gets total columns count of the matrix.
     *
     * @return the columns count
     */
    final int getColumnsCount()
    {
        return getMatrixView().getColumns().size();
    }

    /**
     * Gets the current selected cell.
     *
     * @return the selected cell
     */
    @Nullable
    final Cell getSelectedCell()
    {

        if (!isCellSelected())
        {
            return null;
        }

        int row = getMatrixView().getRows().getSelectionLead(  );
        String rowIdentifier = heatmap.getRows().getLabel(row);

        int column = getMatrixView().getColumns().getSelectionLead(  );
        String columnIdentifier = heatmap.getColumns().getLabel(column);

        return new Cell(new PropertyItem("Row [" + (row + 1) + "]", null, rowIdentifier, getLink(rowIdentifier, heatmap.getRows())), new PropertyItem("Column [" + (column + 1) + "]", null, columnIdentifier, getLink(columnIdentifier, heatmap.getColumns())), getProperties(row, column));
    }

    @Nullable
    private IMatrixView getMatrixView()
    {
        return heatmap  ;
    }

    @NotNull
    @Deprecated
    private Collection<PropertyItem> getProperties(int row, int column)
    {
        int selectedIndex = getMatrixView().getLayers().getTopLayer();
        ElementDecorator selectedDecorator = heatmap.getActiveCellDecorator();
        List<PropertyItem> values = new ArrayList<PropertyItem>();
        final IMatrixLayers properties = getMatrixView().getLayers();
        for (int index = 0; index < properties.size(); index++)
        {
            final ILayerDescriptor prop = properties.get(index);
            Object value = getMatrixView().getCellValue(row, column, index);
            PropertyItem item = new PropertyItem(prop.getName(), prop.getDescription(), FORMATTER.format(value));

            if (index == selectedIndex && value instanceof Double)
            {
                if (selectedDecorator instanceof CategoricalElementDecorator) {
                    CategoricalColorScale scale = (CategoricalColorScale) selectedDecorator.getScale();
                    Double v = (Double) value;
                    String name =  (scale.getColorScalePoint(v).getName().equals(""))  ? FORMATTER.format(value) : scale.getColorScalePoint(v).getName();
                    item = new PropertyItem(prop.getName(), prop.getDescription(), name);
                }

                item.setColor(selectedDecorator.getScale().valueColor((Double) value));

            }


            values.add(item);
        }

        return values;
    }

    @Nullable
    private String getLink(String value, @NotNull HeatmapDimension dim)
    {
        IdType idType = dim.getIdType();
        if (idType != null)
        {
            String idTypeKey = idType.getKey();
            List<UrlLink> tlinks = IdTypeManager.getDefault().getLinks(idTypeKey);
            AnnotationResolver resolver = new AnnotationResolver(dim.getAnnotations(), value);
            for (UrlLink link : tlinks)
            {
                TextPattern pat = link.getPattern();
                return pat.generate(resolver);
            }
        }

        return null;
    }

}


