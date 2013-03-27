/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.persistence.formats.text;

import edu.upf.bg.csv.CSVReader;
import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.model.ModuleMap;
import org.gitools.persistence.FileSuffixes;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.MimeTypes;
import org.gitools.persistence.PersistenceException;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * Read/Write modules from a two columns tabulated file,
 * first column for item and second for module.
 */

public class TwoColumnModuleMapFormat extends AbstractModuleMapFormat<ModuleMap> {


    public TwoColumnModuleMapFormat() {
        super(FileSuffixes.MODULES_2C_MAP, MimeTypes.MODULES_2C_MAP, ModuleMap.class);
    }

    @Override
    protected ModuleMap readResource(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException {

        // map between the item names and its index

        Map<String, Integer> itemNameToRowMapping = new TreeMap<String, Integer>();

        if (isItemNamesFilterEnabled()) {
            String[] itemNames = getItemNames();
            for (int i = 0; i < itemNames.length; i++) {
                if (itemNameToRowMapping.containsKey(itemNames[i]))
                    throw new PersistenceException("Modules not mappable to heatmap due to duplicated row: " + itemNames[i]);
                else
                    itemNameToRowMapping.put(itemNames[i], i);
            }
        }

        // map between modules and item indices

        final Map<String, Set<Integer>> moduleItemsMap =
                new HashMap<String, Set<Integer>>();

        // read mappings

        try {
            progressMonitor.begin("Reading modules ...", 1);

            InputStream in = resourceLocator.openInputStream();
            CSVReader parser = new CSVReader(new InputStreamReader(in));

            readModuleMappings(parser, isItemNamesFilterEnabled(),
                    itemNameToRowMapping, moduleItemsMap);

            in.close();
            progressMonitor.end();
        } catch (Exception ex) {
            throw new PersistenceException(ex);
        }

        progressMonitor.begin("Filtering modules ...", 1);

        int minSize = getMinSize();
        int maxSize = getMaxSize();

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

        Iterator<Entry<String, Set<Integer>>> it =
                moduleItemsMap.entrySet().iterator();

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
            } else
                it.remove();
        }

        // reorder item names according with remapped indices
        String[] finalItemNames = new String[lastIndex];
        for (int i = 0; i < itemNames.length; i++)
            if (used.get(i))
                finalItemNames[indexMap[i]] = itemNames[i];

        progressMonitor.end();

        ModuleMap mmap = new ModuleMap();
        mmap.setItemNames(finalItemNames);
        mmap.setModuleNames(moduleNames.toArray(new String[moduleNames.size()]));
        mmap.setAllItemIndices(modulesItemIndices.toArray(new int[modulesItemIndices.size()][]));
        return mmap;
    }

    protected void readModuleMappings(CSVReader parser, boolean filterRows, Map<String, Integer> itemNameToRowMapping, Map<String, Set<Integer>> moduleItemsMap) throws PersistenceException {

        try {
            String[] fields;

            while ((fields = parser.readNext()) != null) {
                if (fields.length < 2)
                    throw new PersistenceException(
                            "At least 2 columns expected at "
                                    + parser.getLineNumber()
                                    + "(item name and group name).");

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
    protected void writeResource(IResourceLocator resourceLocator, ModuleMap moduleMap, IProgressMonitor progressMonitor) throws PersistenceException {

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
