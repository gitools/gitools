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
import org.gitools.model.GeneSet;
import org.gitools.persistence.FileSuffixes;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.MimeTypes;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.formats.AbstractResourceFormat;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class GeneSetFormat extends AbstractResourceFormat<GeneSet> {

    public GeneSetFormat() {
        super(FileSuffixes.GENE_SET, MimeTypes.GENE_SET, GeneSet.class);
    }

    @Override
    protected GeneSet readResource(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException {
        progressMonitor.begin("Reading ...", 1);

        final Map<String, Integer> labelMap = new HashMap<String, Integer>();


        try {

            InputStream in = resourceLocator.openInputStream();
            CSVReader parser = new CSVReader(new InputStreamReader(in));

            String[] fields;

            // read file
            while ((fields = parser.readNext()) != null) {

                if (fields.length > 1)
                    throw new PersistenceException("Only one column is allowed at line " + parser.getLineNumber());

                Integer index = labelMap.get(fields[0]);
                if (index == null)
                    labelMap.put(fields[0], labelMap.size());
            }

            in.close();

            progressMonitor.info(labelMap.size() + " rows");

            progressMonitor.end();
        } catch (IOException e) {
            throw new PersistenceException(e);
        }

        GeneSet labels = new GeneSet();
        labels.addAll(labelMap.keySet());
        for (Map.Entry<String, Integer> entry : labelMap.entrySet())
            labels.set(entry.getValue(), entry.getKey());

        return labels;
    }

    @Override
    protected void writeResource(IResourceLocator resourceLocator, GeneSet resource, IProgressMonitor progressMonitor) throws PersistenceException {
        progressMonitor.begin("Saving matrix...", resource.size());

        try {

            OutputStream out = resourceLocator.openOutputStream();
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));

            for (String label : resource)
                pw.println(label);

            out.close();
        } catch (Exception e) {
            throw new PersistenceException(e);
        }

        progressMonitor.end();
    }

}
