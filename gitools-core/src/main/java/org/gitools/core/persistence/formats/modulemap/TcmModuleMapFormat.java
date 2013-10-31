/*
 * #%L
 * gitools-core
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
package org.gitools.core.persistence.formats.modulemap;

import org.gitools.core.model.HashModuleMap;
import org.gitools.core.model.IModuleMap;
import org.gitools.core.persistence.IResourceLocator;
import org.gitools.core.persistence.PersistenceException;
import org.gitools.utils.csv.CSVReader;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import java.io.*;

/**
 * Read/Write modules from a two columns tabulated file,
 * first column for item and second for module.
 */

public class TcmModuleMapFormat extends AbstractModuleMapFormat {

    public static final String EXTENSION = "tcm";

    public TcmModuleMapFormat() {
        super(EXTENSION);
    }

    @NotNull
    @Override
    protected IModuleMap readResource(@NotNull IResourceLocator resourceLocator, @NotNull IProgressMonitor progressMonitor) throws PersistenceException {

        HashModuleMap moduleMap = new HashModuleMap();
        try {
            progressMonitor.begin("Reading modules ...", resourceLocator.getContentLength());

            InputStream in = resourceLocator.openInputStream(progressMonitor);
            CSVReader parser = new CSVReader(new InputStreamReader(in));

            String[] fields;
            while ((fields = parser.readNext()) != null) {
                if (fields.length < 2) {
                    throw new PersistenceException("At least 2 columns expected at " + parser.getLineNumber() + "(item name and module name).");
                }
                moduleMap.addMapping(fields[1], fields[0]);
            }
            in.close();
            progressMonitor.end();
        } catch (Exception ex) {
            throw new PersistenceException(ex);
        }

        progressMonitor.end();

        return moduleMap;
    }

    @Override
    protected void writeResource(@NotNull IResourceLocator resourceLocator, @NotNull IModuleMap moduleMap, @NotNull IProgressMonitor progressMonitor) throws PersistenceException {

        progressMonitor.begin("Saving modules...", moduleMap.getModules().size());

        try {
            OutputStream out = resourceLocator.openOutputStream();
            final PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));

            for (String module : moduleMap.getModules()) {
                for (String item : moduleMap.getMappingItems(module)) {
                    pw.print(item);
                    pw.print('\t');
                    pw.print(module);
                    pw.print('\n');
                }

                progressMonitor.worked(1);
            }

            pw.close();
            out.close();

            progressMonitor.end();
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }
}
