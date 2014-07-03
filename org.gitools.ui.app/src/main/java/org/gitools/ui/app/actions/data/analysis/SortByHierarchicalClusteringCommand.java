/*
 * #%L
 * org.gitools.ui.app
 * %%
 * Copyright (C) 2013 - 2014 Universitat Pompeu Fabra - Biomedical Genomics group
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

import com.google.common.base.Function;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.SortDirection;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.header.HierarchicalCluster;
import org.gitools.matrix.sort.SortByLabelComparator;
import org.gitools.ui.app.commands.AbstractCommand;
import org.gitools.ui.core.Application;

import java.util.HashMap;
import java.util.Map;

public class SortByHierarchicalClusteringCommand extends AbstractCommand {


    private final HeatmapDimension clusteringDimension;
    private HierarchicalCluster hierarchicalCluster;

    public SortByHierarchicalClusteringCommand(HeatmapDimension clusteringDimension, HierarchicalCluster hierarchicalCluster) {

        this.clusteringDimension = clusteringDimension;
        this.hierarchicalCluster = hierarchicalCluster;
    }

    @Override
    public void execute(IProgressMonitor monitor) {


        try {
            if (getExitStatus() > 0) {
                return;
            }

            monitor.begin("Sorting according to clustering", 1);

            clusteringDimension.sort(
                    new SortByLabelComparator(clusteringDimension,
                            SortDirection.ASCENDING,
                            new SortByHierarchicalClusteringCommand.HierarchicalSortFunction(hierarchicalCluster),
                            -1,
                            false) {
                        @Override
                        public int compare(String idx1, String idx2) {

                            String v1 = transformFunction.apply(idx1);
                            String v2 = transformFunction.apply(idx2);

                            if (v1 == null && v2 == null) {
                                return 0;
                            } else if (v1 != null && v2 == null) {
                                return -1;
                            } else if (v1 == null && v2 != null) {
                                return 1;
                            }

                            return v1.compareTo(v2);
                        }
                    });

            Application.get().refresh();


            setExitStatus(0);

        } catch (Exception e) {
            setExitStatus(-1);
        }

    }


    static class HierarchicalSortFunction implements Function<String, String> {

        Map<String, String> map;

        public HierarchicalSortFunction(HierarchicalCluster rootCluster) {
            map = rootCluster.getMaxLevel(new HashMap<String, String>());
            /*for (String s : map.keySet()) {
                System.out.println(s + ": " + map.get(s));
            }*/
        }

        @Override
        public String apply(String identifier) {

            return map.get(identifier);

        }
    }
}
