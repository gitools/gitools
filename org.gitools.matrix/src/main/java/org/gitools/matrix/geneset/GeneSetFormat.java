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
import org.gitools.utils.readers.text.CSVReader;

import javax.enterprise.context.ApplicationScoped;
import java.io.*;

@ApplicationScoped
public class GeneSetFormat extends AbstractResourceFormat<GeneSet> {

    public static final String EXTENSION = "grp";

    public GeneSetFormat() {
        super(EXTENSION, GeneSet.class);
    }

    @Override
    public boolean isDefaultExtension() {
        return true;
    }

    @Override
    protected GeneSet readResource(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException {
        progressMonitor.begin("Reading ...", 1);

        GeneSet labels = new GeneSet();


        try {

            InputStream in = resourceLocator.openInputStream(progressMonitor);
            CSVReader parser = new CSVReader(new InputStreamReader(in));

            String[] fields;

            // read file
            while ((fields = parser.readNext()) != null) {

                if (fields.length > 1) {
                    throw new PersistenceException("Only one column is allowed at line " + parser.getLineNumber());
                }

                labels.add(fields[0]);

            }

            in.close();

            progressMonitor.info(labels.size() + " rows");

        } catch (IOException e) {
            throw new PersistenceException(e);
        }

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

    }

}
