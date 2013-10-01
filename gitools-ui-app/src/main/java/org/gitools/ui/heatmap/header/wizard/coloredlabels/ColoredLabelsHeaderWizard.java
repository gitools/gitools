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

import org.gitools.core.clustering.method.annotations.AnnPatClusteringMethod;
import org.gitools.core.heatmap.HeatmapDimension;
import org.gitools.core.heatmap.header.ColoredLabel;
import org.gitools.core.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;
import org.gitools.ui.wizard.common.PatternSourcePage;
import org.gitools.utils.color.generator.ColorRegistry;

public class ColoredLabelsHeaderWizard extends AbstractWizard {

    private final HeatmapDimension hdim;

    private boolean editionMode;

    private final String lastPattern;
    private final HeatmapColoredLabelsHeader header;

    private final AnnPatClusteringMethod clusteringMethod;

    private PatternSourcePage sourcePage;
    private ColoredLabelsConfigPage headerPage;
    private ColoredLabelsGroupsPage clustersPage;

    public ColoredLabelsHeaderWizard(HeatmapDimension hdim, HeatmapColoredLabelsHeader header) {
        super();

        this.hdim = hdim;

        this.lastPattern = "";
        this.header = header;

        clusteringMethod = new AnnPatClusteringMethod();
    }

    @Override
    public void addPages() {
        if (!editionMode) {
            sourcePage = new PatternSourcePage(hdim, true);
            addPage(sourcePage);
        }

        headerPage = new ColoredLabelsConfigPage(header);
        addPage(headerPage);

        clustersPage = new ColoredLabelsGroupsPage(header.getClusters());
        clustersPage.setValueEditable(false);
        addPage(clustersPage);
    }

    @Override
    public boolean canFinish() {
        return currentPage != sourcePage;
    }

    @Override
    public void pageLeft(IWizardPage currentPage) {
        super.pageLeft(currentPage);

        if (currentPage != sourcePage || editionMode) {
            return;
        }

        String pattern = sourcePage.getPattern();
        if (lastPattern.equals(pattern)) {
            return;
        }

        header.setAnnotationPattern(pattern);

        header.setTitle(sourcePage.getPatternTitle());
        header.setAnnotationMetadata(sourcePage.getSelectedValues()[0]);

        header.autoGenerateColoredLabels(clusteringMethod);

        clustersPage.setColoredLabels(header.getClusters());
    }

    @Override
    public void performFinish() {
        ColoredLabel[] cls = clustersPage.getColoredLabels();
        ColorRegistry cr = ColorRegistry.get();
        for (ColoredLabel cl : cls) {
            cr.registerId(cl.getValue(), cl.getColor());
        }
        header.setClusters(cls);
    }

    public HeatmapColoredLabelsHeader getHeader() {
        return header;
    }

    public void setEditionMode(boolean editionMode) {
        this.editionMode = editionMode;
    }
}
