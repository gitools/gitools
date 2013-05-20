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
package org.gitools.ui.modules.wizard;

import org.gitools.core.modules.importer.ModulesImporter;
import org.gitools.core.persistence.formats.FileFormat;
import org.gitools.core.persistence.formats.FileFormats;
import org.gitools.ui.IconNames;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.wizard.common.SaveFilePage;
import org.jetbrains.annotations.NotNull;

public class ModulesImportWizard extends AbstractWizard {

    @NotNull
    private final FileFormat[] supportedFormats = new FileFormat[]{FileFormats.MODULES_2C_MAP, FileFormats.GENE_MATRIX, FileFormats.GENE_MATRIX_TRANSPOSED,};

    private final ModulesImporter importer;

    private ModulesSourcePage moduleCategoryPage;
    private ModulesOrganismPage organismPage;
    private ModulesFeaturesPage featuresPage;
    private SaveFilePage saveFilePage;

    protected ModulesImportWizard(ModulesImporter importer) {
        this.importer = importer;

        setTitle("Import modules...");
        setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_MODULES, 96));
    }

    @Override
    public void addPages() {

        moduleCategoryPage = new ModulesSourcePage(importer);
        addPage(moduleCategoryPage);

        organismPage = new ModulesOrganismPage(importer);
        addPage(organismPage);

        featuresPage = new ModulesFeaturesPage(importer);
        addPage(featuresPage);

        // Destination
        saveFilePage = new SaveFilePage() {
            @Override
            public void updateModel() {
                Settings.getDefault().setLastMapPath(getFolder());
            }
        };
        saveFilePage.setTitle("Select destination file");
        saveFilePage.setFolder(Settings.getDefault().getLastMapPath());
        saveFilePage.setFormats(supportedFormats);
        addPage(saveFilePage);
    }

    @Override
    public void pageEntered(IWizardPage page) {
        if (saveFilePage.equals(page)) {
            if (saveFilePage.getFileNameWithoutExtension().isEmpty()) {
                saveFilePage.setFileNameWithoutExtension(automaticFileName(importer));
            }
        }
    }

    @NotNull
    private String automaticFileName(@NotNull ModulesImporter importer) {
        StringBuilder sb = new StringBuilder();
        sb.append(importer.getOrganism().getName().replace(' ', '_'));
        sb.append("__");
        sb.append(importer.getModuleCategory().getRef().replace(':', '_'));
        sb.append("__");
        sb.append(importer.getFeatureCategory().getRef().replace(':', '_'));
        return sb.toString();
    }

    public SaveFilePage getSaveFilePage() {
        return saveFilePage;
    }
}
