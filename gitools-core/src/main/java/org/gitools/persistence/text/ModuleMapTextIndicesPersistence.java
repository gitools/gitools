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

package org.gitools.persistence.text;

import edu.upf.bg.csv.CSVReader;
import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.model.ModuleMap;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.*;
import java.util.zip.DataFormatException;

public class ModuleMapTextIndicesPersistence extends ModuleMapPersistence<ModuleMap> {

    public ModuleMapTextIndicesPersistence() {
    }

    @Override
    public ModuleMap read(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException {

        Reader reader;

        progressMonitor.begin("Loading modules...", 1);
        progressMonitor.info("From: " + resourceLocator.getURL());

        try {
            reader = resourceLocator.getReader();
        } catch (Exception e) {
            throw new PersistenceException("Error opening: " + resourceLocator.getURL(), e);
        }

        CSVReader parser = new CSVReader(reader);

        ModuleMap moduleMap = new ModuleMap();

        try {
            loadItemNames(moduleMap, progressMonitor, parser);
            loadModules(moduleMap, progressMonitor, parser);
            reader.close();
        } catch (Exception e) {
            throw new PersistenceException(e);
        }

        progressMonitor.end();

        return moduleMap;
    }

    @Override
    public void write(IResourceLocator resourceLocator, ModuleMap moduleMap, IProgressMonitor progressMonitor) throws PersistenceException {
        progressMonitor.begin("Saving modules...", moduleMap.getModuleNames().length);
        progressMonitor.info("To: " + resourceLocator.getURL());

        try {
            final PrintWriter pw = new PrintWriter(resourceLocator.getWriter());

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
        } catch (Exception e) {
            throw new PersistenceException(e);
        } finally {
            progressMonitor.end();
        }
    }

    private void loadItemNames(ModuleMap moduleMap, IProgressMonitor progressMonitor, CSVReader csvReader) throws IOException, DataFormatException {

        progressMonitor.begin("Reading item names ...", 1);

        final String[] itemNames = csvReader.readNext();

        moduleMap.setItemNames(itemNames);

        progressMonitor.info(itemNames.length + " items");

        progressMonitor.end();

    }

    private void loadModules(ModuleMap moduleMap, IProgressMonitor progressMonitor, CSVReader parser) throws NumberFormatException, IOException {

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
                if (index != null)
                    valid.set(index);
            }
        } else
            valid.set(0, itemNames.length);

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

            if (items.size() >= minSize && items.size() <= maxSize)
                mapItemIndices.put(moduleName, items);
            else
                items.clear();
        }

        // Remap indices as there are items that may not be used

        int lastIndex = 0;
        int[] indexMap = new int[itemNames.length];
        for (int i = 0; i < itemNames.length; i++)
            if (used.get(i))
                indexMap[i] = lastIndex++;

        int i = 0;
        String[] finalItemNames = new String[lastIndex];
        for (int j = 0; j < itemNames.length; j++)
            if (used.get(j))
                finalItemNames[i++] = itemNames[j];

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
