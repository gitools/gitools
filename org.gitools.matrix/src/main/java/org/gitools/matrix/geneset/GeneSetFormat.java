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
package org.gitools.matrix.geneset;

import org.gitools.api.PersistenceException;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.resource.AbstractResourceFormat;
import org.gitools.utils.text.CSVReader;

import javax.enterprise.context.ApplicationScoped;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class GeneSetFormat extends AbstractResourceFormat<GeneSet> {

    public static final String EXTENSION = "grp";

    public GeneSetFormat() {
        super(EXTENSION, GeneSet.class);
    }


    @Override
    protected GeneSet readResource(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException {
        progressMonitor.begin("Reading ...", 1);

        final Map<String, Integer> labelMap = new HashMap<>();


        try {

            InputStream in = resourceLocator.openInputStream(progressMonitor);
            CSVReader parser = new CSVReader(new InputStreamReader(in));

            String[] fields;

            // read file
            while ((fields = parser.readNext()) != null) {

                if (fields.length > 1) {
                    throw new PersistenceException("Only one column is allowed at line " + parser.getLineNumber());
                }

                Integer index = labelMap.get(fields[0]);
                if (index == null) {
                    labelMap.put(fields[0], labelMap.size());
                }
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
