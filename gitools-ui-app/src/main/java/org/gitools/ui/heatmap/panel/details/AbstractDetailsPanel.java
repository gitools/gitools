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
import org.gitools.heatmap.HeatmapDim;
import org.gitools.idtype.IdType;
import org.gitools.idtype.IdTypeManager;
import org.gitools.idtype.UrlLink;
import org.gitools.matrix.model.AnnotationResolver;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.ui.heatmap.panel.details.boxes.Cell;
import org.gitools.ui.heatmap.panel.details.boxes.PropertyItem;
import org.gitools.utils.formatter.GenericFormatter;
import org.gitools.utils.textpatt.TextPattern;
import org.jetbrains.annotations.NotNull;

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
    private final Heatmap heatmap;

    /**
     * Instantiates a new Abstract details panel.
     *
     * @param heatmap the heatmap
     */
    public AbstractDetailsPanel(@NotNull final Heatmap heatmap)
    {
        super();
        this.heatmap = heatmap;

        setBackground(Color.WHITE);
        setLayout(new SingleFiledLayout(SingleFiledLayout.COLUMN, SingleFiledLayout.CENTER, 5));

        heatmap.addPropertyChangeListener(new PropertyChangeListener()
        {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                if ("selectionLead" == evt.getPropertyName())
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
    protected final boolean isCellSelected()
    {
        return (
                getMatrixView().getLeadSelectionColumn() != -1 &&
                        getMatrixView().getLeadSelectionRow() != -1
        );
    }

    /**
     * Gets total rows count of the matrix.
     *
     * @return the rows count
     */
    protected final int getRowsCount()
    {
        return getMatrixView().getRowCount();
    }

    /**
     * Gets total columns count of the matrix.
     *
     * @return the columns count
     */
    protected final int getColumnsCount()
    {
        return getMatrixView().getColumnCount();
    }

    /**
     * Gets the current selected cell.
     *
     * @return the selected cell
     */
    protected final Cell getSelectedCell()
    {

        if (!isCellSelected())
        {
            return null;
        }

        int row = getMatrixView().getLeadSelectionRow();
        String rowIdentifier = heatmap.getRowLabel(row);

        int column = getMatrixView().getLeadSelectionColumn();
        String columnIdentifier = heatmap.getColumnLabel(column);

        return new Cell(
                new PropertyItem("Row [" + (row + 1) + "]", null, rowIdentifier, getLink(rowIdentifier, heatmap.getRowDim())),
                new PropertyItem("Column [" + (column + 1) + "]", null, columnIdentifier, getLink(columnIdentifier, heatmap.getColumnDim())),
                getProperties(row, column)
        );
    }

    private IMatrixView getMatrixView()
    {
        return heatmap.getMatrixView();
    }

    @Deprecated
    private Collection<PropertyItem> getProperties(int row, int column)
    {
        int selectedIndex = getMatrixView().getSelectedPropertyIndex();
        ElementDecorator selectedDecorator = heatmap.getActiveCellDecorator();
        Object cellElement = getMatrixView().getCell(row, column);
        IElementAdapter cellAdapter = getMatrixView().getCellAdapter();
        List<PropertyItem> values = new ArrayList<PropertyItem>();
        final List<IElementAttribute> properties = cellAdapter.getProperties();
        for (int index = 0; index < properties.size(); index++)
        {
            final IElementAttribute prop = properties.get(index);
            Object value = cellAdapter.getValue(cellElement, index);
            PropertyItem item = new PropertyItem(
                    prop.getName(),
                    prop.getDescription(),
                    FORMATTER.format(value)
            );

            if (index == selectedIndex && value instanceof Double)
            {
                item.setColor(
                        selectedDecorator.getScale().valueColor((Double) value)
                );
            }

            values.add(item);
        }

        return values;
    }

    private String getLink(String value, HeatmapDim dim)
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


