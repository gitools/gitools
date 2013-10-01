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

import org.gitools.core.heatmap.HeatmapDimension;
import org.gitools.core.heatmap.header.HeatmapDecoratorHeader;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.wizard.IWizardPage;

import java.util.Arrays;

public class AnnotationDecoratorHeaderWizard extends DecoratorHeaderWizard {

    private final HeatmapDimension headerDimension;
    private AnnotationSourcePage dataSourceAnnotationPage;

    public AnnotationDecoratorHeaderWizard(HeatmapDecoratorHeader header, HeatmapDimension headerDimension) {
        super(header);
        this.headerDimension = headerDimension;
    }

    @Override
    public void addPages() {

        dataSourceAnnotationPage = new AnnotationSourcePage(headerDimension, "The annotation column must not contain numeric values");
        addPage(dataSourceAnnotationPage);

        super.addPages();

    }

    @Override
    public IWizardPage getNextPage(IWizardPage page) {

        IWizardPage nextPage;
        if (page == this.dataSourceAnnotationPage) {

            getHeader().setAnnotationLabels(Arrays.asList(dataSourceAnnotationPage.getSelectedAnnotation()));
            getHeader().setTitle(dataSourceAnnotationPage.getSelectedAnnotation());
            getHeader().setDescription(dataSourceAnnotationPage.getAnnotationMetadata("description"));
            getHeader().setValueUrl(dataSourceAnnotationPage.getAnnotationMetadata("value-url"));
            getHeader().setDescriptionUrl(dataSourceAnnotationPage.getAnnotationMetadata("description-url"));
            getHeader().setSize(headerDimension.getCellSize());

            dataSourceAnnotationPage.setMessage(MessageStatus.INFO, dataSourceAnnotationPage.infoMessage);
            nextPage = super.getNextPage(page);

        } else {
            nextPage = super.getNextPage(page);
        }

        return nextPage;
    }



}
