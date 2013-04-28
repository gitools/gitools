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

import org.gitools.core.model.ModuleMap;
import org.gitools.core.persistence.IResourceLocator;
import org.gitools.core.persistence.PersistenceException;
import org.gitools.core.persistence._DEPRECATED.FileSuffixes;
import org.gitools.utils.csv.CSVReader;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;
import java.util.zip.DataFormatException;

public class IndexedModuleMapFormat extends AbstractModuleMapFormat<ModuleMap> {

    public IndexedModuleMapFormat() {
        super(FileSuffixes.MODULES_INDEXED_MAP, ModuleMap.class);
    }

    @NotNull
    @Override
    protected ModuleMap readResource(@NotNull IResourceLocator resourceLocator, @NotNull IProgressMonitor progressMonitor) throws PersistenceException {

        progressMonitor.begin("Loading modules...", resourceLocator.getContentLength());

        ModuleMap moduleMap = new ModuleMap();

        try {
            InputStream in = resourceLocator.openInputStream(progressMonitor);
            CSVReader parser = new CSVReader(new InputStreamReader(in));

            loadItemNames(moduleMap, progressMonitor, parser);
            loadModules(moduleMap, progressMonitor, parser);
            in.close();
        } catch (Exception e) {
            throw new PersistenceException(e);
        }

        progressMonitor.end();

        return moduleMap;
    }

    @Override
    protected void writeResource(@NotNull IResourceLocator resourceLocator, @NotNull ModuleMap moduleMap, @NotNull IProgressMonitor progressMonitor) throws PersistenceException {
        progressMonitor.begin("Saving modules...", moduleMap.getModuleNames().length);

        try {
            OutputStream out = resourceLocator.openOutputStream();
            final PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));

            final String[] itemNames = moduleMap.getItemNames();

            if (itemNames.length > 0) {
                pw.print('"');
                pw.print(itemNames[0]);
                pw.print('"');

                for (int i = 1; i < itemNames.length; i++) {
                    pw.print("\t\"");
                    pw.print(itemNames[i]);
                    pw.print('"');
                }
            }
            pw.print('\n');

            final String[] moduleNames = moduleMap.getModuleNames();

            final int[][] indices = moduleMap.getAllItemIndices();

            int numModules = moduleNames.length;

            for (int i = 0; i < numModules; i++) {
                pw.print('"');
                pw.print(moduleNames[i]);
                pw.print('"');

                for (int index : indices[i]) {
                    pw.print('\t');
                    pw.print(index);
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

    private void loadItemNames(@NotNull ModuleMap moduleMap, @NotNull IProgressMonitor progressMonitor, @NotNull CSVReader csvReader) throws IOException, DataFormatException {

        progressMonitor.begin("Reading item names ...", 1);

        final String[] itemNames = csvReader.readNext();

        moduleMap.setItemNames(itemNames);

        progressMonitor.info(itemNames.length + " items");

        progressMonitor.end();

    }

    private void loadModules(@NotNull ModuleMap moduleMap, @NotNull IProgressMonitor progressMonitor, @NotNull CSVReader parser) throws NumberFormatException, IOException {

        progressMonitor.begin("Reading modules ...", 1);

        String[] itemNames = moduleMap.getItemNames();

        // Prepare valid item names depending on whether item name filtering is enabled or not

        BitSet valid = new BitSet(itemNames.length);

        if (isItemNamesFilterEnabled()) {
            Map<String, Integer> itemIndices = new HashMap<String, Integer>();
            for (int i = 0; i < itemNames.length; i++)
                itemIndices.put(itemNames[i], i);

            for (String name : getItemNames()) {
                Integer index = itemIndices.get(name);
                if (index != null) {
                    valid.set(index);
                }
            }
        } else {
            valid.set(0, itemNames.length);
        }

        // Load mapping and mark items used

        BitSet used = new BitSet(itemNames.length);

        String[] fields;
        final Map<String, Set<Integer>> mapItemIndices = new HashMap<String, Set<Integer>>();

        int minSize = getMinSize();
        int maxSize = getMaxSize();

        while ((fields = parser.readNext()) != null) {

            String moduleName = fields[0];

            Set<Integer> items = new HashSet<Integer>();

            for (int j = 1; j < fields.length; j++) {
                int index = Integer.parseInt(fields[j]);
                boolean inRange = index >= 0 && index < itemNames.length;
                if (inRange && valid.get(index)) {
                    items.add(index);
                    used.set(index);
                }
            }

            if (items.size() >= minSize && items.size() <= maxSize) {
                mapItemIndices.put(moduleName, items);
            } else {
                items.clear();
            }
        }

        // Remap indices as there are items that may not be used

        int lastIndex = 0;
        int[] indexMap = new int[itemNames.length];
        for (int i = 0; i < itemNames.length; i++)
            if (used.get(i)) {
                indexMap[i] = lastIndex++;
            }

        int i = 0;
        String[] finalItemNames = new String[lastIndex];
        for (int j = 0; j < itemNames.length; j++)
            if (used.get(j)) {
                finalItemNames[i++] = itemNames[j];
            }

        i = 0;
        String[] moduleNames = new String[mapItemIndices.size()];

        int[][] moduleItemIndices = new int[moduleNames.length][];

        for (Map.Entry<String, Set<Integer>> entry : mapItemIndices.entrySet()) {
            moduleNames[i] = entry.getKey();

            int[] indices = new int[entry.getValue().size()];
            Iterator<Integer> it = entry.getValue().iterator();
            for (int j = 0; j < indices.length; j++)
                indices[j] = indexMap[it.next()];
            moduleItemIndices[i] = indices;
            i++;
        }

        moduleMap.setItemNames(finalItemNames);
        moduleMap.setModuleNames(moduleNames);
        moduleMap.setAllItemIndices(moduleItemIndices);

        progressMonitor.info(moduleNames.length + " modules and " + finalItemNames.length + " items annotated");

        progressMonitor.end();
    }
}
