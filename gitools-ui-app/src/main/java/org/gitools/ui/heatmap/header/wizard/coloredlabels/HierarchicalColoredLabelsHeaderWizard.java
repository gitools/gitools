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

import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDim;
import org.gitools.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.heatmap.header.HeatmapHierarchicalColoredLabelsHeader;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;
import org.gitools.utils.progressmonitor.IProgressMonitor;

public class HierarchicalColoredLabelsHeaderWizard extends AbstractWizard
{

    private Heatmap heatmap;
    private HeatmapDim hdim;

    private HeatmapHierarchicalColoredLabelsHeader header;
    private int previousLevel;

    private ColoredLabelsConfigPage headerPage;
    private ColoredLabelsGroupsPage clustersPage;
    private HclLevelPage hclPage;

    public HierarchicalColoredLabelsHeaderWizard(
            Heatmap heatmap, HeatmapDim hdim,
            HeatmapHierarchicalColoredLabelsHeader header)
    {

        super();

        this.heatmap = heatmap;
        this.hdim = hdim;

        this.header = header;
        previousLevel = header.getTreeLevel();
    }

    @Override
    public void addPages()
    {
        headerPage = new ColoredLabelsConfigPage(header);
        addPage(headerPage);

        hclPage = new HclLevelPage(header);
        addPage(hclPage);

        clustersPage = new ColoredLabelsGroupsPage(header.getClusters());
        addPage(clustersPage);
    }

    @Override
    public boolean canFinish()
    {
        return true;
    }

    @Override
    public void performCancel()
    {
        JobThread.execute(AppFrame.get(), new JobRunnable()
        {
            @Override
            public void run(IProgressMonitor monitor)
            {
                try
                {
                    header.setTreeLevel(previousLevel);
                } catch (Throwable ex)
                {
                    monitor.exception(ex);
                }
            }
        });
    }

    @Override
    public void pageLeft(IWizardPage currentPage)
    {
        super.pageLeft(currentPage);

        if (currentPage == hclPage)
        {
            JobThread.execute(AppFrame.get(), new JobRunnable()
            {
                @Override
                public void run(IProgressMonitor monitor)
                {
                    try
                    {
                        header.setTreeLevel(hclPage.getLevel());
                    } catch (Throwable ex)
                    {
                        monitor.exception(ex);
                    }
                }
            });
        }
    }

    public HeatmapColoredLabelsHeader getHeader()
    {
        return header;
    }
}
