/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.table.model;

import edu.upf.bg.xml.adapter.ColorXmlAdapter;
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
public class TableFigure extends Figure implements Serializable, IResource {

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

	public TableFigure() {

		showGrid = true;
		gridColor = Color.WHITE;
		rowSize = 18;

		cellDecorators = new ArrayList<AbstractModel>();
		headerDecorators = new ArrayList<AbstractModel>();
	}

	public TableFigure(TableView tableView) {

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

		for (int i = 0; i < tableView.getColumnCount(); i++) {
			
			elementAdapter = tableView.getCellColumnAdapter(i);
			cellDecorator = new FormattedTextElementDecorator(elementAdapter);

			headerDecorator = new HeatmapTextLabelsHeader();
			cellDecorators.add(cellDecorator);
			headerDecorators.add(headerDecorator);

		}
	}

	public TableFigure(ITable table, List<AbstractModel> cellDecorators,
			List<AbstractModel> headerDecorators) throws TableFormatException {

		int columns = table.getColumnCount();

		if ((columns == cellDecorators.size())
				&& (columns == headerDecorators.size())) {

			this.cellDecorators = cellDecorators;
			this.headerDecorators = headerDecorators;
			showGrid = true;
			gridColor = Color.WHITE;
			rowSize = 18;
		} else
			throw new TableFormatException(
					"Wrong number of columns in table decorators");
	}

    public IResourceLocator getLocator() {
        return locator;
    }

    public void setLocator(IResourceLocator locator) {
        this.locator = locator;
    }

    public void setDecorator(AbstractModel decorator, int column) {
		cellDecorators.set(column, decorator);
	}

	public void setHeaderDecorator(AbstractModel decorator, int column) {
		cellDecorators.set(column, decorator);
	}

	public TableView getTableView() {
		return tableView;
	}

	public void setTableView(TableView tableView) {
		this.tableView = tableView;
	}

	public AbstractModel getHeaderDecorator(int column) {
		return headerDecorators.get(column);
	}

	public AbstractModel getDecorator(int column) {
		return cellDecorators.get(column);
	}
}

