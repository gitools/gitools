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
import org.gitools.utils.csv.CSVReader;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

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
    protected ModuleMap readResource(@NotNull IResourceLocator resourceLocator, @NotNull IProgressMonitor progressMonitor) throws PersistenceException {

        // map between the item names and its index

        Map<String, Integer> itemNameToRowMapping = new TreeMap<String, Integer>();

        // map between modules and item indices

        final Map<String, Set<Integer>> moduleItemsMap = new HashMap<String, Set<Integer>>();

        // read mappings

        try {
            progressMonitor.begin("Reading modules ...", resourceLocator.getContentLength());

            InputStream in = resourceLocator.openInputStream(progressMonitor);
            CSVReader parser = new CSVReader(new InputStreamReader(in));

            readModuleMappings(parser, itemNameToRowMapping, moduleItemsMap);

            in.close();
            progressMonitor.end();
        } catch (Exception ex) {
            throw new PersistenceException(ex);
        }

        progressMonitor.begin("Filtering modules ...", 1);

        // create array of item names
        String[] itemNames = new String[itemNameToRowMapping.size()];
        for (Map.Entry<String, Integer> entry : itemNameToRowMapping.entrySet()) {
            //monitor.debug(entry.getKey() + " --> " + entry.getValue());
            itemNames[entry.getValue()] = entry.getKey();
        }

        // mask of used items
        BitSet used = new BitSet(itemNames.length);

        // remapped indices
        int lastIndex = 0;
        int[] indexMap = new int[itemNames.length];

        // filter modules by size and identify which items are indexed
        List<String> moduleNames = new ArrayList<String>();
        List<int[]> modulesItemIndices = new ArrayList<int[]>();

        Iterator<Entry<String, Set<Integer>>> it = moduleItemsMap.entrySet().iterator();

        while (it.hasNext()) {
            Entry<String, Set<Integer>> entry = it.next();
            Set<Integer> indices = entry.getValue();
            if (indices.size() >= minSize && indices.size() <= maxSize) {
                moduleNames.add(entry.getKey());
                int[] remappedIndices = new int[indices.size()];
                Iterator<Integer> iit = indices.iterator();
                for (int i = 0; i < indices.size(); i++) {
                    int index = iit.next();
                    if (!used.get(index)) {
                        used.set(index);
                        indexMap[index] = lastIndex++;
                    }

                    remappedIndices[i] = indexMap[index];
                }
                modulesItemIndices.add(remappedIndices);
            } else {
                it.remove();
            }
        }

        // reorder item names according with remapped indices
        String[] finalItemNames = new String[lastIndex];
        for (int i = 0; i < itemNames.length; i++)
            if (used.get(i)) {
                finalItemNames[indexMap[i]] = itemNames[i];
            }

        progressMonitor.end();

        ModuleMap mmap = new ModuleMap();
        mmap.setItemNames(finalItemNames);
        mmap.setModuleNames(moduleNames.toArray(new String[moduleNames.size()]));
        mmap.setAllItemIndices(modulesItemIndices.toArray(new int[modulesItemIndices.size()][]));
        return mmap;
    }

    void readModuleMappings(CSVReader parser, Map<String, Integer> itemNameToRowMapping, @NotNull Map<String, Set<Integer>> moduleItemsMap) throws PersistenceException {

        try {
            String[] fields;

            while ((fields = parser.readNext()) != null) {
                if (fields.length < 2) {
                    throw new PersistenceException("At least 2 columns expected at " + parser.getLineNumber() + "(item name and group name).");
                }

                String itemName = fields[0];
                String groupName = fields[1];

                Integer itemIndex = itemNameToRowMapping.get(itemName);
                if (itemIndex == null && !filterRows) {
                    itemIndex = itemNameToRowMapping.size();
                    itemNameToRowMapping.put(itemName, itemIndex);
                }

                if (itemIndex != null) {
                    Set<Integer> itemIndices = moduleItemsMap.get(groupName);
                    if (itemIndices == null) {
                        itemIndices = new TreeSet<Integer>();
                        moduleItemsMap.put(groupName, itemIndices);
                    }
                    itemIndices.add(itemIndex);
                }
            }
        } catch (IOException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    protected void writeResource(@NotNull IResourceLocator resourceLocator, @NotNull ModuleMap moduleMap, @NotNull IProgressMonitor progressMonitor) throws PersistenceException {

        final String[] moduleNames = moduleMap.getModuleNames();

        int numModules = moduleNames.length;

        progressMonitor.begin("Saving modules...", numModules);

        try {
            OutputStream out = resourceLocator.openOutputStream();
            final PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));

            final String[] itemNames = moduleMap.getItemNames();

            final int[][] indices = moduleMap.getAllItemIndices();

            for (int i = 0; i < numModules; i++) {
                for (int index : indices[i]) {
                    pw.print(itemNames[index]);
                    pw.print('\t');
                    pw.print(moduleNames[i]);
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
