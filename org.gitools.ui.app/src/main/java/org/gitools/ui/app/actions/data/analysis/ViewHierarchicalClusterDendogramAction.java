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
package org.gitools.ui.app.actions.data.analysis;

import org.gitools.heatmap.header.HierarchicalClusterHeatmapHeader;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrix;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.ui.app.analysis.clustering.visualization.DendrogramEditor;
import org.gitools.ui.app.heatmap.editor.HeatmapEditor;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.HeatmapPosition;
import org.gitools.ui.core.actions.HeatmapAction;
import org.gitools.ui.core.actions.dynamicactions.IHeatmapHeaderAction;
import org.gitools.ui.core.components.editor.AbstractEditor;
import org.gitools.ui.core.components.editor.EditorsPanel;
import org.gitools.ui.platform.icons.IconNames;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;

import java.awt.event.ActionEvent;


public class ViewHierarchicalClusterDendogramAction extends HeatmapAction implements IHeatmapHeaderAction {

    private HierarchicalClusterHeatmapHeader header;


    public ViewHierarchicalClusterDendogramAction() {
        super("<html><i>ViewHierarchicalClusterDendogramAction</i></html>");
        setSmallIconFromResource(IconNames.view16);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (header == null) {
            return;
        }

        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) throws Exception {
                Application.get().getEditorsPanel().addEditor(new DendrogramEditor(header.getHierarchicalCluster()));
            }
        });

    }

    public HeatmapEditor getOpenInEditor(IMatrix data) {
        EditorsPanel epanel = Application.get().getEditorsPanel();

        for (AbstractEditor e : epanel.getEditors()) {
            if (e instanceof HeatmapEditor) {
                if (((Heatmap) e.getModel()).getContents() == data) {
                    return (HeatmapEditor) e;
                }
            }
        }
        return null;
    }


    @Override
    public void onConfigure(HeatmapHeader header, HeatmapPosition position) {

        setEnabled(header instanceof HierarchicalClusterHeatmapHeader
                    && ((HierarchicalClusterHeatmapHeader) header).getHierarchicalCluster() != null);

        if (header instanceof HierarchicalClusterHeatmapHeader) {

            this.header = (HierarchicalClusterHeatmapHeader) header;

            this.setName("<html><i>View</i> hierarchical clustering dendogram</html>");

        }

    }
}
