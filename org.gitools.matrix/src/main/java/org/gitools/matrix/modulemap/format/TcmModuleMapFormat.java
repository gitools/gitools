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
package org.gitools.matrix.modulemap.format;

import org.gitools.api.PersistenceException;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.modulemap.IModuleMap;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.matrix.modulemap.HashModuleMap;
import org.gitools.utils.readers.text.CSVReader;

import javax.enterprise.context.ApplicationScoped;
import java.io.*;

/**
 * Read/Write modules from a two columns tabulated file,
 * first column for item and second for module.
 */

@ApplicationScoped
public class TcmModuleMapFormat extends AbstractModuleMapFormat {

    public static final String EXTENSION = "tcm";

    public TcmModuleMapFormat() {
        super(EXTENSION);
    }

    @Override
    public boolean isDefaultExtension() {
        return true;
    }

    @Override
    protected IModuleMap readResource(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException {

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
        } catch (Exception ex) {
            throw new PersistenceException(ex);
        }

        return moduleMap;
    }

    @Override
    protected void writeResource(IResourceLocator resourceLocator, IModuleMap moduleMap, IProgressMonitor progressMonitor) throws PersistenceException {

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

        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }
}
