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
import org.gitools.ui.app.analysis.clustering.visualization.DendrogramEditor;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.HeatmapPosition;
import org.gitools.ui.core.actions.HeatmapAction;
import org.gitools.ui.core.actions.dynamicactions.IHeatmapHeaderAction;
import org.gitools.ui.platform.icons.IconNames;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;

import java.awt.event.ActionEvent;


public class ViewDendogramAction extends HeatmapAction implements IHeatmapHeaderAction {

    private HierarchicalClusterHeatmapHeader header;
    private String clusterName = "";


    public ViewDendogramAction() {
        super("<html><i>ViewDendogramAction</i></html>");
        setSmallIconFromResource(IconNames.view16);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (header == null) {
            return;
        }

        final HierarchicalCluster cluster = header.getHierarchicalCluster(clusterName);


        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) throws Exception {
                Application.get().getEditorsPanel().addEditor(new DendrogramEditor(cluster));
            }
        });

    }

    @Override
    public void onConfigure(HeatmapHeader header, HeatmapPosition position) {

        setEnabled(header instanceof HierarchicalClusterHeatmapHeader
                    && ((HierarchicalClusterHeatmapHeader) header).getHierarchicalCluster() != null);

        if (header instanceof HierarchicalClusterHeatmapHeader) {

            this.header = (HierarchicalClusterHeatmapHeader) header;

            if (!Strings.isNullOrEmpty(position.getHeaderAnnotation())) {

                this.clusterName = position.getHeaderAnnotation();
                this.setName("<html><i>View</i> <b>" + clusterName + "</b> dendogram</html>");

            } else {

                this.clusterName = "";
                this.setName("<html><i>View</i> <b>entire</b> hierarchical dendogram</html>");
            }


        }

    }
}
