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
package org.gitools.ui.app.wizard;

import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.datasources.modules.importer.ModuleCategory;
import org.gitools.datasources.modules.importer.ModulesImporter;
import org.gitools.datasources.modules.importer.Organism;
import org.gitools.datasources.modules.importer.Version;
import org.gitools.ui.app.wizard.common.FilteredListPage;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;

import javax.swing.*;

public class ModulesOrganismPage extends FilteredListPage {

    private final ModulesImporter importer;

    private ModuleCategory modCategory;
    private Version version;
    private boolean loaded;

    public ModulesOrganismPage(ModulesImporter importer) {
        this.importer = importer;
        this.loaded = false;

        setTitle("Select organism");
    }

    @Override
    public void updateControls() {
        super.updateControls();

        if (!loaded || modCategory != importer.getModuleCategory() || version != importer.getVersion()) {

            JobThread.execute(Application.get(), new JobRunnable() {
                @Override
                public void run(IProgressMonitor monitor) {
                    try {
                        monitor.begin("Getting available organisms ...", 1);

                        modCategory = importer.getModuleCategory();
                        version = importer.getVersion();
                        final Organism[] organisms = importer.getOrganisms();
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                setListData(organisms);
                                for (Organism o : organisms)
                                    if (o.getName().equals("homo sapiens")) {
                                        setSelectedValue(o);
                                    }
                                loaded = true;
                            }
                        });
                    } catch (Exception ex) {
                        monitor.exception(ex);
                    }
                }
            });
        }
    }

    @Override
    public void updateModel() {
        importer.setOrganism(getOrganism());
    }


    private Organism getOrganism() {
        return (Organism) getSelectedValue();
    }
}
