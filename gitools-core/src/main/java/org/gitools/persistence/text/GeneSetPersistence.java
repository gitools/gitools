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
import org.gitools.persistence.AbstractResourcePersistence;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneSetPersistence
        extends AbstractResourcePersistence<List<String>> {

    @Override
    public List<String> read(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException {
        progressMonitor.begin("Reading ...", 1);
        progressMonitor.info("From: " + resourceLocator.getURL());

        final Map<String, Integer> labelMap = new HashMap<String, Integer>();

        Reader reader = null;
        try {
            reader = resourceLocator.getReader();
        } catch (Exception e) {
            throw new PersistenceException("Error opening: " + resourceLocator.getURL(), e);
        }

        CSVReader parser = new CSVReader(reader);

        try {
            String[] fields;

            // read file

            while ((fields = parser.readNext()) != null) {

                if (fields.length > 1)
                    throw new PersistenceException("Only one column is allowed at line " + parser.getLineNumber());

                Integer index = labelMap.get(fields[0]);
                if (index == null)
                    labelMap.put(fields[0], labelMap.size());
            }

            reader.close();

            progressMonitor.info(labelMap.size() + " rows");

            progressMonitor.end();
        } catch (IOException e) {
            throw new PersistenceException(e);
        }

        List<String> labels = new ArrayList<String>(labelMap.keySet());
        for (Map.Entry<String, Integer> entry : labelMap.entrySet())
            labels.set(entry.getValue(), entry.getKey());

        return labels;
    }

    @Override
    public void write(IResourceLocator resourceLocator, List<String> entity, IProgressMonitor progressMonitor) throws PersistenceException {
        progressMonitor.begin("Saving matrix...", entity.size());
        progressMonitor.info("To: " + resourceLocator.getURL());

        Writer writer;
        try {
            writer = resourceLocator.getWriter();
        } catch (Exception e) {
            throw new PersistenceException("Error opening: " + resourceLocator.getURL(), e);
        }

        PrintWriter pw = new PrintWriter(writer);

        for (String label : entity)
            pw.println(label);

        try {
            writer.close();
        } catch (Exception e) {
            throw new PersistenceException("Error closing: " + resourceLocator.getURL(), e);
        }

        progressMonitor.end();
    }

}
