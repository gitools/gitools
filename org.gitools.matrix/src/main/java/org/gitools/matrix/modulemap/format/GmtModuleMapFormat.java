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

@ApplicationScoped
public class GmtModuleMapFormat extends AbstractModuleMapFormat {

    public static final String EXTENSION = "gmt";

    public GmtModuleMapFormat() {
        super(EXTENSION);
    }

    @Override
    protected IModuleMap readResource(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException {

        progressMonitor.begin("Reading names ...", 1);
        HashModuleMap moduleMap = new HashModuleMap();
        try {

            InputStream in = resourceLocator.openInputStream(progressMonitor);
            CSVReader parser = new CSVReader(new InputStreamReader(in));

            String[] fields;

            while ((fields = parser.readNext()) != null) {

                if (fields.length < 2) {
                    throw new PersistenceException("Invalid row, at least 2 columns required (name and description) at line " + parser.getLineNumber());
                }

                String module = fields[0];

                //TODO Use fields[1] as layer description

                for (int i = 2; i < fields.length; i++) {
                    String item = fields[i];
                    moduleMap.addMapping(module, item);
                }
            }
            in.close();
            progressMonitor.info(moduleMap.getModules().size() + " modules");
            progressMonitor.end();
        } catch (IOException e) {
            throw new PersistenceException(e);
        }

        return moduleMap;
    }

    @Override
    protected void writeResource(IResourceLocator resourceLocator, IModuleMap moduleMap, IProgressMonitor progressMonitor) throws PersistenceException {
        progressMonitor.begin("Saving module maps...", moduleMap.getModules().size());

        try {

            OutputStream out = resourceLocator.openOutputStream();
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));

            for (String module : moduleMap.getModules()) {
                pw.append(module);
                pw.append('\t'); // description, but currently not used
                for (String item : moduleMap.getMappingItems(module)) {
                    pw.append('\t').append(item);
                }
                pw.println();
                progressMonitor.worked(1);
            }

            out.close();
        } catch (Exception e) {
            throw new PersistenceException(e);
        }

        progressMonitor.end();
    }
}
