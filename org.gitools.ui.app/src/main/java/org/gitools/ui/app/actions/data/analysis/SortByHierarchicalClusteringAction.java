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

import com.google.common.base.Strings;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.heatmap.header.HierarchicalCluster;
import org.gitools.heatmap.header.HierarchicalClusterHeatmapHeader;
import org.gitools.ui.core.HeatmapPosition;
import org.gitools.ui.core.actions.HeatmapAction;
import org.gitools.ui.core.actions.dynamicactions.IHeatmapHeaderAction;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;

import java.awt.event.ActionEvent;


public class SortByHierarchicalClusteringAction extends HeatmapAction implements IHeatmapHeaderAction {

    private HierarchicalClusterHeatmapHeader header;
    private HierarchicalCluster hierarchicalCluster;


    public SortByHierarchicalClusteringAction() {
        super("<html><i>sort by clustering</i></html>");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (header == null) {
            return;
        }

        JobThread.execute(this.getParentWindow(), new JobRunnable() {

            @Override
            public void run(IProgressMonitor monitor) throws Exception {
                SortByHierarchicalClusteringCommand command = new SortByHierarchicalClusteringCommand(header.getHeatmapDimension(),
                        header.getHierarchicalCluster());
                command.execute(monitor);

            }
        });


    }


    @Override
    public void onConfigure(HeatmapHeader header, HeatmapPosition position) {

        setEnabled(header instanceof HierarchicalClusterHeatmapHeader
                && ((HierarchicalClusterHeatmapHeader) header).getHierarchicalCluster() != null);

        if (header instanceof HierarchicalClusterHeatmapHeader) {

            this.header = (HierarchicalClusterHeatmapHeader) header;
            hierarchicalCluster = ((HierarchicalClusterHeatmapHeader) header).getHierarchicalCluster();

            if (!Strings.isNullOrEmpty(position.getHeaderAnnotation())) {

                String clusterName = position.getHeaderAnnotation();
                this.setName("<html><i>Sort by</i> hierarchical clustering</html>");

            } else {
                this.setName("<html><i>Sort by</i> hierarchical clustering</html>");
            }


        }

    }


}
