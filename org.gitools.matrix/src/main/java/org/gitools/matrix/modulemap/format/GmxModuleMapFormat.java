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

import com.google.common.base.Joiner;
import org.gitools.api.PersistenceException;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.modulemap.IModuleMap;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.modulemap.HashModuleMap;
import org.gitools.utils.readers.text.CSVReader;

import javax.enterprise.context.ApplicationScoped;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

@ApplicationScoped
public class GmxModuleMapFormat extends AbstractModuleMapFormat {

    public static final String EXTENSION = "gmx";
    private static Joiner TAB_JOINER = Joiner.on('\t');

    public GmxModuleMapFormat() {
        super(EXTENSION);
    }

    @Override
    protected IModuleMap readResource(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException {
        progressMonitor.begin("Reading ...", 1);
        HashModuleMap moduleMap = new HashModuleMap();
        try {

            InputStream in = resourceLocator.openInputStream(progressMonitor);
            CSVReader parser = new CSVReader(new InputStreamReader(in));

            // read column names
            String[] modules = parser.readNext();

            // Discard descriptions
            parser.readNext();

            String[] fields;
            while ((fields = parser.readNext()) != null) {

                if (fields.length > modules.length) {
                    throw new PersistenceException("Row with more columns than expected at line " + parser.getLineNumber());
                }

                for (int i = 0; i < fields.length; i++) {
                    moduleMap.addMapping(modules[i], fields[i]);
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
        progressMonitor.begin("Saving module map...", moduleMap.getModules().size());

        try {
            OutputStream out = resourceLocator.openOutputStream();
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));

            List<String> modules = newArrayList(moduleMap.getModules());
            Map<String, List<String>> itemsMap = new HashMap<>();

            int maxItemsPerModule = 0;
            for (String module : modules) {

                List<String> items = newArrayList(moduleMap.getMappingItems(module));

                if (items.size() > maxItemsPerModule) {
                    maxItemsPerModule = items.size();
                }

                itemsMap.put(module, items);
            }

            // Module labels
            pw.println(TAB_JOINER.join(modules));

            // descriptions
            for (int m = 1; m < modules.size(); m++) {
                pw.append('\t');
            }
            pw.println();

            // data
            List<String> line = new ArrayList<>(modules.size());
            for (int l=0; l < maxItemsPerModule; l++) {
                for (String module : modules) {

                    List<String> items = itemsMap.get(module);

                    if (l >= items.size()) {
                        line.add("");
                    } else {
                        line.add(items.get(l));
                    }
                }
                pw.println(TAB_JOINER.join(line));
                line.clear();
            }

            out.close();
        } catch (Exception e) {
            throw new PersistenceException(e);
        }

        progressMonitor.end();
    }


}
