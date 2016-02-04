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
package org.gitools.ui.app.actions.edit;

import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapLayer;
import org.gitools.matrix.model.hashmatrix.HashMatrix;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.HeatmapPosition;
import org.gitools.ui.core.actions.HeatmapAction;
import org.gitools.ui.core.actions.dynamicactions.IHeatmapLayerAction;
import org.gitools.ui.core.commands.AbstractCommand;
import org.gitools.ui.platform.icons.IconNames;
import org.gitools.ui.platform.progress.JobThread;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class RemoveLayerAction extends HeatmapAction implements IHeatmapLayerAction {


    private HeatmapLayer layer;

    public RemoveLayerAction() {
        super("Delete data layer...");
        setSmallIconFromResource(IconNames.remove16);
    }

    @Override
    public boolean isEnabledByModel(Object model) {
        return (model instanceof Heatmap) && (((Heatmap) model).getContents() instanceof HashMatrix);
    }

    @Override
    public void actionPerformed(ActionEvent e) {


        Object[] options = {"Cancel",
                "No!",
                "Ok"};
        int n = JOptionPane.showOptionDialog(Application.get(),
                "<html>Would you like to <b>delete</b> the selected data layer?",
                "Delete values from heatmap",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[2]);

        if (n == 1 | n == 0) {
            return;
        }

        AbstractCommand cmd = new AbstractCommand() {

            @Override
            public void execute(IProgressMonitor monitor) throws CommandException {

                getHeatmap().removeLayer(layer);

            }
        };


        JobThread.execute(Application.get(), cmd);
        Application.get().showNotification("Data layer deleted");
    }



    @Override
    public void onConfigure(HeatmapLayer object, HeatmapPosition position) {
        setLayer(object);
    }

    public void setLayer(HeatmapLayer layer) {
        this.layer = layer;
    }

    public HeatmapLayer getLayer() {
        return layer;
    }
}
