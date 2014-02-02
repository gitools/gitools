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

import org.gitools.matrix.modulemap.HashModuleMap;
import org.gitools.api.PersistenceException;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.api.modulemap.IModuleMap;
import org.gitools.utils.csv.CSVReader;

import javax.enterprise.context.ApplicationScoped;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class IxmModuleMapFormat extends AbstractModuleMapFormat {

    public static final String EXTENSION = "ixm";

    public IxmModuleMapFormat() {
        super(EXTENSION);
    }

    @Override
    protected IModuleMap readResource(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException {
        try {

            progressMonitor.begin("Loading modules...", resourceLocator.getContentLength());

            InputStream in = resourceLocator.openInputStream(progressMonitor);
            CSVReader parser = new CSVReader(new InputStreamReader(in));

            progressMonitor.begin("Reading item names ...", 1);
            List<String> items = Arrays.asList(parser.readNext());
            progressMonitor.info(items.size() + " items");
            progressMonitor.end();

            progressMonitor.begin("Reading modules ...", 1);
            String[] fields;
            HashModuleMap moduleMap = new HashModuleMap();
            while ((fields = parser.readNext()) != null) {
                String module = fields[0];
                for (int j = 1; j < fields.length; j++) {
                    int itemIndex = Integer.parseInt(fields[j]);
                    if (itemIndex >= 0 && itemIndex < items.size()) {
                        moduleMap.addMapping(module, items.get(itemIndex));
                    }
                }
            }

            progressMonitor.info(moduleMap.getModules().size() + " modules and " + moduleMap.getItems().size() + " items annotated");
            progressMonitor.end();
            in.close();

            return moduleMap;
        } catch (Exception e) {
            throw new PersistenceException(e);
        } finally {
            progressMonitor.end();
        }
    }

    @Override
    protected void writeResource(IResourceLocator resourceLocator, IModuleMap moduleMap, IProgressMonitor progressMonitor) throws PersistenceException {
        progressMonitor.begin("Saving modules...", moduleMap.getModules().size());

        try {
            OutputStream out = resourceLocator.openOutputStream();
            final PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));

            final Iterator<String> itemsIterator = moduleMap.getItems().iterator();

            Map<String, Integer> itemsIndex = new HashMap<>();
            int index = 0;
            while (itemsIterator.hasNext()) {

                String item = itemsIterator.next();
                itemsIndex.put(item, index);
                index++;

                pw.print('"');
                pw.print(item);
                pw.print('"');

                if (itemsIterator.hasNext()) {
                    pw.print("\t");
                }
            }
            pw.print('\n');


            for (String module : moduleMap.getModules()) {
                pw.print('"');
                pw.print(module);
                pw.print('"');

                for (String item : moduleMap.getMappingItems(module)) {
                    pw.print('\t');
                    pw.print(itemsIndex.get(item));
                }

                pw.print('\n');

                progressMonitor.worked(1);
            }

            pw.close();
            out.close();
        } catch (Exception e) {
            throw new PersistenceException(e);
        } finally {
            progressMonitor.end();
        }
    }

}
