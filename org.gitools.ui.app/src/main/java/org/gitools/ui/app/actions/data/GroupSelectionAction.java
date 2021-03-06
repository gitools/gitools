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
package org.gitools.ui.app.actions.data;

import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.api.matrix.view.IMatrixViewDimension;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.ui.app.actions.HeatmapDimensionAction;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.HeatmapPosition;
import org.gitools.ui.core.actions.dynamicactions.IHeatmapDimensionAction;

import java.awt.event.ActionEvent;
import java.util.List;

import static com.google.common.base.Predicates.in;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayList;

public class GroupSelectionAction extends HeatmapDimensionAction implements IHeatmapDimensionAction {

    private HeatmapPosition position;

    public GroupSelectionAction(MatrixDimensionKey dimensionKey) {
        super(dimensionKey, "<html><i>Group</i> selected <b>" + dimensionKey.getLabel() + "s<i> here</html>");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        IMatrixViewDimension dimension = getDimension();

        groupSelected(dimension, position.get(dimension));

        Application.get().showNotification("Selected " + getDimensionLabel() + "s grouped.");
    }

    private void groupSelected(IMatrixViewDimension dimension, String identifier) {

        List<String> selected = newArrayList(dimension.getSelected());
        List<String> notSelected = newArrayList(filter(dimension, not(in(dimension.getSelected()))));

        int split = dimension.indexOf(identifier) - selected.indexOf(identifier) - 1;
        if (split < 0) {
            split = 0;
        }

        int selectedAfterSplit = 0;
        int selectedBeforeSplit = 0;
        for (String s : selected) {
            if (dimension.indexOf(s) > split) {
                selectedAfterSplit++;
            } else {
                selectedBeforeSplit++;
            }
        }

        if (selectedBeforeSplit > 0) {
            //group after selected index:
            split++;
        }



        List<String> newOrder = newArrayList(
                concat(
                        notSelected.subList(0, split - selectedBeforeSplit),
                        selected,
                        notSelected.subList(split - selectedBeforeSplit, notSelected.size())
                ));

        dimension.show(newOrder);
    }


    @Override
    public void onConfigure(HeatmapDimension dimension, HeatmapPosition position) {

        // Enable only if there is at least one item selected
        setEnabled(dimension.getSelected().size() > 0);
        this.position = position;
    }
}
