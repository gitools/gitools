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
package org.gitools.ui.heatmap.header.wizard.heatmapheader;

import org.gitools.core.heatmap.header.HeatmapDecoratorHeader;
import org.gitools.ui.heatmap.header.wizard.TextLabelsConfigPage;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;

public class DecoratorHeaderWizard extends AbstractWizard {

    private final HeatmapDecoratorHeader header;

    private ColorScalePage colorScalePage;
    private HeatmapHeaderConfigPage configPage;
    private TextLabelsConfigPage textConfigPage;

    public DecoratorHeaderWizard(HeatmapDecoratorHeader header) {
        super();

        this.header = header;
    }

    @Override
    public void addPages() {

        configPage = new HeatmapHeaderConfigPage(header);
        addPage(configPage);

        colorScalePage = new ColorScalePage(header);
        addPage(colorScalePage);

        textConfigPage = new TextLabelsConfigPage(header);
        addPage(textConfigPage);

    }

    public boolean isLastPage(IWizardPage page) {

        if (page == this.colorScalePage) {
            return !header.isLabelVisible();
        } else {
            return super.isLastPage(page);
        }
    }

    @Override
    public boolean canFinish() {
        return (currentPage == colorScalePage) || (currentPage == textConfigPage);
    }

    @Override
    public IWizardPage getNextPage(IWizardPage page) {
        IWizardPage nextPage;

        if (page == this.configPage) {
            textConfigPage.setFgColorEnabled(header.isForceLabelColor() || header.getLabelPosition() != HeatmapDecoratorHeader.LabelPositionEnum.inside);
            nextPage = super.getNextPage(page);
        } else {
            nextPage = super.getNextPage(page);
        }

        return nextPage;
    }

    protected HeatmapDecoratorHeader getHeader() {
        return header;
    }


}
