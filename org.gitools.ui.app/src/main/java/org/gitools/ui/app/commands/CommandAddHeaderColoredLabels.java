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
package org.gitools.ui.app.commands;

import org.gitools.analysis.clustering.ClusteringData;
import org.gitools.analysis.clustering.ClusteringException;
import org.gitools.analysis.clustering.ClusteringMethod;
import org.gitools.analysis.clustering.annotations.AnnPatClusteringData;
import org.gitools.analysis.clustering.annotations.AnnPatClusteringMethod;
import org.gitools.api.analysis.Clusters;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.heatmap.header.ColoredLabel;
import org.gitools.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.ui.core.Application;
import org.gitools.utils.color.ColorGenerator;
import org.gitools.utils.progressmonitor.DefaultProgressMonitor;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommandAddHeaderColoredLabels extends HeaderCommand {

    private final boolean textVisible;
    private List<String> colors;
    private List<String> ids;
    private boolean autoGenerateColors;

    public CommandAddHeaderColoredLabels(String heatmap, String side, String pattern, List<String> colors, List<String> ids,
                                         boolean autoGenerateColors, boolean textVisible, String sort) {
        super(heatmap, side, sort, pattern);
        this.colors = colors;
        this.ids = ids;
        this.autoGenerateColors = autoGenerateColors;
        this.textVisible = textVisible;
    }

    @Override
    public void execute(IProgressMonitor monitor) throws CommandException {


        super.execute(monitor);
        if (getExitStatus() > 0) {
            return;
        }


        List<ColoredLabel> cls = new ArrayList<>();
        HeatmapColoredLabelsHeader header = new HeatmapColoredLabelsHeader(hdim);

        header.setAnnotationPattern(pattern);
        header.setHeatmapDimension(hdim);
        header.setTitle(header.deriveTitleFromPattern());

        if (textVisible) {
            header.setLabelVisible(true);
        }

        if (autoGenerateColors) {
            cls = header.getClusters();
            autoGenerateColoredLabels(header, new AnnPatClusteringMethod());
        }


        if (!(colors == null || ids == null)) {

            if (colors.size() != ids.size()) {
                setExitStatus(1);
                throw new CommandException("The number of specified colors and values must match");
            }

            for (int i = 0; i < colors.size(); i++) {
                ColoredLabel cl = new ColoredLabel(ids.get(i), Color.decode(colors.get(i)));
                cls = addCluster(cls, cl);
            }

            if (cls.size() == 0) {
                throw new CommandException("No color labels have been created.");
            }

            header.setClusters(cls);

        }

        hdim.addHeader(header);

        applySort();

        Application.get().refresh();

        if (sort != null)

            setExitStatus(0);
    }

    @Deprecated
    private List<ColoredLabel> addCluster(List<ColoredLabel> coloredLabels, ColoredLabel cl) {
        String key = cl.getValue();
        for (int i = 0; i < coloredLabels.size(); i++) {
            if (coloredLabels.get(i).getValue().equals(key)) {
                coloredLabels.set(i, cl);
                break;
            }
        }
        coloredLabels.add(cl);

        return coloredLabels;
    }

    public static void updateFromClusterResults(HeatmapColoredLabelsHeader header, Collection<String> clusters) {
        ColorGenerator cg = new ColorGenerator();

        List<ColoredLabel> coloredLabels = new ArrayList<>(clusters.size());
        for (String cluster : clusters) {
            coloredLabels.add(new ColoredLabel(cluster, cg.next(cluster)));
        }

        header.setClusters(coloredLabels);
    }


    public static void autoGenerateColoredLabels(HeatmapColoredLabelsHeader header, ClusteringMethod clusteringMethod) {

        ClusteringData data = new AnnPatClusteringData(header.getHeatmapDimension(), header.getAnnotationPattern());
        Clusters results = null;
        try {
            results = clusteringMethod.cluster(data, new DefaultProgressMonitor());
        } catch (ClusteringException e) {
            e.printStackTrace();
        }
        updateFromClusterResults(header, results.getClusters());

    }
}
