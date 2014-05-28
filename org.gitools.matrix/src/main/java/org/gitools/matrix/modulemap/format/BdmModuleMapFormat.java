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
import java.util.Set;

@ApplicationScoped
public class BdmModuleMapFormat extends AbstractModuleMapFormat {

    public static final String EXTENSION = "bdm";

    public BdmModuleMapFormat() {
        super(EXTENSION);
    }

    @Override
    protected IModuleMap readResource(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException {

        progressMonitor.begin("Reading data ...", 1);

        HashModuleMap moduleMap = new HashModuleMap();

        try {
            InputStream in = resourceLocator.openInputStream(progressMonitor);
            CSVReader parser = new CSVReader(new InputStreamReader(in));

            // Read columns
            String modules[] = parser.readNext();

            String[] fields;
            while ((fields = parser.readNext()) != null) {

                String item = fields[0];
                for (int i = 1; i < fields.length; i++) {
                    String module = modules[i];
                    if (fields[i]!=null && "1".equals(fields[i].trim())) {
                        moduleMap.addMapping(module, item);
                    }
                }
            }

            in.close();
        } catch (Exception e) {
            throw new PersistenceException(e);
        }

        return moduleMap;

    }

    @Override
    protected void writeResource(IResourceLocator resourceLocator, IModuleMap moduleMap, IProgressMonitor monitor) throws PersistenceException {

        monitor.begin("Saving module map...", moduleMap.getModules().size());

        try {
            OutputStream out = resourceLocator.openOutputStream(monitor);
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));

            Set<String> modules = moduleMap.getModules();
            Set<String> items = moduleMap.getItems();

            for (String column : items) {
                pw.print('\t');
                pw.print(column);
            }
            pw.print('\n');

            for (String item : items) {
                pw.print(item);

                for (String module : modules) {
                    pw.print('\t');
                    pw.print(moduleMap.getMappingItems(module).contains(item) ? "1" : "0");
                }
                pw.print('\n');

                monitor.worked(1);
            }
            pw.close();
            out.close();
        } catch (Exception e) {
            throw new PersistenceException(e);
        }

    }

}
