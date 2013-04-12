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
package org.gitools.ui.actions.data;

import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.ui.IconNames;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;

/**
 * @noinspection ALL
 */
public class ShowAllAction extends BaseAction
{

    private static final long serialVersionUID = 7110623490709997414L;

    public enum ElementType
    {
        ROWS, COLUMNS
    }

    private final ElementType type;

    public ShowAllAction(@NotNull ElementType type)
    {
        super(null);

        this.type = type;
        switch (type)
        {
            case ROWS:
                setName("Show all rows");
                setDesc("Show all rows");
                setSmallIconFromResource(IconNames.rowShowAll16);
                setLargeIconFromResource(IconNames.rowShowAll24);
                break;
            case COLUMNS:
                setName("Show all columns");
                setDesc("Show all columns");
                setSmallIconFromResource(IconNames.columnShowAll16);
                setLargeIconFromResource(IconNames.columnShowAll24);
                break;
        }
    }

    @Override
    public boolean isEnabledByModel(Object model)
    {
        return model instanceof Heatmap || model instanceof IMatrixView;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {

        final IMatrixView matrixView = ActionUtils.getMatrixView();
        if (matrixView == null)
        {
            return;
        }

        final IMatrix contents = matrixView.getContents();

        if (type == ElementType.ROWS)
        {
            int rowCount = contents.getRows().size();

            int[] visibleRows = new int[rowCount];

            for (int i = 0; i < rowCount; i++)
                visibleRows[i] = i;

            matrixView.setVisibleRows(visibleRows);

            AppFrame.get().setStatusText(visibleRows.length + " rows");
        }
        else if (type == ElementType.COLUMNS)
        {
            int columnCount = contents.getColumns().size();

            int[] visibleColumns = new int[columnCount];

            for (int i = 0; i < columnCount; i++)
                visibleColumns[i] = i;

            matrixView.setVisibleColumns(visibleColumns);

            AppFrame.get().setStatusText(visibleColumns.length + " columns");
        }
    }
}
