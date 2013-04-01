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
package org.gitools.table.model;

import org.gitools.heatmap.header.HeatmapTextLabelsHeader;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.model.AbstractModel;
import org.gitools.model.Figure;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.impl.FormattedTextElementDecorator;
import org.gitools.model.xml.AbstractModelXmlAdapter;
import org.gitools.persistence.IResource;
import org.gitools.persistence.IResourceLocator;
import org.gitools.table.model.impl.TableView;
import org.gitools.utils.xml.adapter.ColorXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "tableFigure")
public class TableFigure extends Figure implements Serializable, IResource
{

    private static final long serialVersionUID = 9006041133309250290L;

    private IResourceLocator locator;

    private TableView tableView;

    @XmlJavaTypeAdapter(AbstractModelXmlAdapter.class)
    private List<AbstractModel> cellDecorators;

    @XmlJavaTypeAdapter(AbstractModelXmlAdapter.class)
    private List<AbstractModel> headerDecorators;

    private boolean showGrid;

    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    private Color gridColor;

    private int rowSize;

    public TableFigure()
    {

        showGrid = true;
        gridColor = Color.WHITE;
        rowSize = 18;

        cellDecorators = new ArrayList<AbstractModel>();
        headerDecorators = new ArrayList<AbstractModel>();
    }

    public TableFigure(TableView tableView)
    {

        showGrid = true;
        gridColor = Color.WHITE;
        rowSize = 18;

        this.tableView = tableView;
        cellDecorators = new ArrayList<AbstractModel>();
        headerDecorators = new ArrayList<AbstractModel>();

        Object cellElement;

        ElementDecorator cellDecorator;
        IElementAdapter elementAdapter;
        HeatmapTextLabelsHeader headerDecorator;

        for (int i = 0; i < tableView.getColumnCount(); i++)
        {

            elementAdapter = tableView.getCellColumnAdapter(i);
            cellDecorator = new FormattedTextElementDecorator(elementAdapter);

            headerDecorator = new HeatmapTextLabelsHeader();
            cellDecorators.add(cellDecorator);
            headerDecorators.add(headerDecorator);

        }
    }

    public TableFigure(ITable table, List<AbstractModel> cellDecorators,
                       List<AbstractModel> headerDecorators) throws TableFormatException
    {

        int columns = table.getColumnCount();

        if ((columns == cellDecorators.size())
                && (columns == headerDecorators.size()))
        {

            this.cellDecorators = cellDecorators;
            this.headerDecorators = headerDecorators;
            showGrid = true;
            gridColor = Color.WHITE;
            rowSize = 18;
        }
        else
        {
            throw new TableFormatException(
                    "Wrong number of columns in table decorators");
        }
    }

    public IResourceLocator getLocator()
    {
        return locator;
    }

    public void setLocator(IResourceLocator locator)
    {
        this.locator = locator;
    }

    public void setDecorator(AbstractModel decorator, int column)
    {
        cellDecorators.set(column, decorator);
    }

    public void setHeaderDecorator(AbstractModel decorator, int column)
    {
        cellDecorators.set(column, decorator);
    }

    public TableView getTableView()
    {
        return tableView;
    }

    public void setTableView(TableView tableView)
    {
        this.tableView = tableView;
    }

    public AbstractModel getHeaderDecorator(int column)
    {
        return headerDecorators.get(column);
    }

    public AbstractModel getDecorator(int column)
    {
        return cellDecorators.get(column);
    }
}

