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
package org.gitools.ui.wizard.common;

import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.persistence._DEPRECATED.FileFormat;
import org.gitools.core.persistence._DEPRECATED.FileFormats;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;
import org.gitools.ui.settings.Settings;
import org.jetbrains.annotations.NotNull;

public class ExportHeatmapLabelsWizard extends AbstractWizard {

    @NotNull
    private final FileFormat[] supportedFormats = new FileFormat[]{FileFormats.TEXT};

    private final Heatmap hm;

    private ExportHeatmapLabelsPage sourcePage;
    private PatternSourcePage patPage;
    private SaveFilePage savePage;

    public ExportHeatmapLabelsWizard(Heatmap hm) {
        this.hm = hm;

        setTitle("Export labels ...");
    }

    @Override
    public void addPages() {
        sourcePage = new ExportHeatmapLabelsPage();
        addPage(sourcePage);

        patPage = new PatternSourcePage(true);
        addPage(patPage);

        savePage = new SaveFilePage();
        savePage.setTitle("Select destination file");
        savePage.setFolder(Settings.getDefault().getLastExportPath());
        savePage.setFormats(supportedFormats);
        addPage(savePage);
    }

    @Override
    public void performFinish() {
        Settings.getDefault().setLastExportPath(savePage.getFolder());
        Settings.getDefault().save();
    }

    @Override
    public void pageLeft(IWizardPage page) {
        if (page == sourcePage) {
            switch (sourcePage.getWhichLabels()) {
                case VISIBLE_ROWS:
                case HIDDEN_ROWS:
                    patPage.setHeatmapDimension(hm.getRows());
                    break;

                case VISIBLE_COLUMNS:
                case HIDDEN_COLUMNS:
                    patPage.setHeatmapDimension(hm.getColumns());
                    break;
            }
        }
    }

    @NotNull
    public ExportHeatmapLabelsPage.WhichLabels getWhichLabels() {
        return sourcePage.getWhichLabels();
    }

    public String getPattern() {
        return patPage.getPattern();
    }

    public SaveFilePage getSavePage() {
        return savePage;
    }

}
