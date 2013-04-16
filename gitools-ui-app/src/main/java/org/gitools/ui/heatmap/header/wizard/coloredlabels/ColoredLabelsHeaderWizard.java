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
package org.gitools.ui.heatmap.header.wizard.coloredlabels;

import org.gitools.clustering.ClusteringData;
import org.gitools.clustering.ClusteringResults;
import org.gitools.clustering.method.annotations.AnnPatClusteringMethod;
import org.gitools.clustering.method.annotations.AnnPatColumnClusteringData;
import org.gitools.clustering.method.annotations.AnnPatRowClusteringData;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.header.ColoredLabel;
import org.gitools.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.matrix.model.matrix.AnnotationMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;
import org.gitools.ui.wizard.common.PatternSourcePage;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

/**
 * @noinspection ALL
 */
public class ColoredLabelsHeaderWizard extends AbstractWizard
{

    private final Heatmap heatmap;
    private final HeatmapDimension hdim;
    private final boolean applyToRows;

    private boolean editionMode;

    private final String lastPattern;
    private final HeatmapColoredLabelsHeader header;

    private final AnnPatClusteringMethod clusteringMethod;

    private PatternSourcePage sourcePage;
    private ColoredLabelsConfigPage headerPage;
    private ColoredLabelsGroupsPage clustersPage;

    public ColoredLabelsHeaderWizard(Heatmap heatmap, HeatmapDimension hdim, HeatmapColoredLabelsHeader header, boolean applyToRows)
    {
        super();

        this.heatmap = heatmap;
        this.hdim = hdim;
        this.applyToRows = applyToRows;

        this.lastPattern = "";
        this.header = header;

        clusteringMethod = new AnnPatClusteringMethod();
    }

    @Override
    public void addPages()
    {
        if (!editionMode)
        {
            sourcePage = new PatternSourcePage(hdim.getAnnotations(), true);
            addPage(sourcePage);
        }

        headerPage = new ColoredLabelsConfigPage(header);
        addPage(headerPage);

        clustersPage = new ColoredLabelsGroupsPage(header.getClusters());
        clustersPage.setValueEditable(false);
        addPage(clustersPage);
    }

    @Override
    public boolean canFinish()
    {
        return currentPage != sourcePage;
    }

    @Override
    public void pageLeft(IWizardPage currentPage)
    {
        super.pageLeft(currentPage);

        if (currentPage != sourcePage || editionMode)
        {
            return;
        }

        String pattern = sourcePage.getPattern();
        if (lastPattern.equals(pattern))
        {
            return;
        }

        IMatrixView mv = heatmap  ;
        AnnotationMatrix am = hdim.getAnnotations();
        header.setAnnotationPattern(pattern);

        final ClusteringData data = applyToRows ? new AnnPatRowClusteringData(mv, am, pattern) : new AnnPatColumnClusteringData(mv, am, pattern);

        header.setTitle("Colors: " + sourcePage.getPatternTitle());

        JobThread.execute(AppFrame.get(), new JobRunnable()
        {
            @Override
            public void run(@NotNull IProgressMonitor monitor)
            {
                try
                {
                    final ClusteringResults results = clusteringMethod.cluster(data, monitor);

                    header.updateFromClusterResults(results);
                } catch (Throwable ex)
                {
                    monitor.exception(ex);
                }
            }
        });
        clustersPage.setColoredLabels(header.getClusters());
    }

    @Override
    public void performFinish()
    {
        ColoredLabel[] cls = clustersPage.getColoredLabels();
        header.setClusters(cls);
    }

    public HeatmapColoredLabelsHeader getHeader()
    {
        return header;
    }

    public void setEditionMode(boolean editionMode)
    {
        this.editionMode = editionMode;
    }
}
