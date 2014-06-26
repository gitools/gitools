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

import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.header.HeatmapDecoratorHeader;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.HeatmapPosition;
import org.gitools.ui.core.actions.HeatmapAction;
import org.gitools.ui.core.actions.dynamicactions.IHeatmapHeaderAction;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class InvertOrderAction extends HeatmapAction implements IHeatmapHeaderAction {

    private HeatmapHeader header;

    public InvertOrderAction() {
        super("<html><i>Invert</i> order</html>");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (header == null) {
            return;
        }

        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                monitor.begin("Sorting ...", 1);

                HeatmapDimension dimension = header.getHeatmapDimension();

                List<String> visible = new ArrayList<>(dimension.toList());
                Collections.reverse(visible);
                dimension.show(visible);


            }
        });

        Application.get().showNotification("Inverted order of " + header.getHeatmapDimension().getId().getLabel() + "s");
    }

    @Override
    public void onConfigure(HeatmapHeader header, HeatmapPosition position) {

        this.header = header;

        if (header instanceof HeatmapDecoratorHeader) {
            ((HeatmapDecoratorHeader) header).setSortLabel(position.getHeaderAnnotation());
        }

        String dimension = header.getHeatmapDimension().getId().getLabel();

        setName("<html><i>Invert</i> order of all " + dimension + "s </html>");
    }
}
