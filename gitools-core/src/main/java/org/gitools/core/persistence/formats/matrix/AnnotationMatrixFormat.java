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
package org.gitools.core.persistence.formats.matrix;

import org.gitools.core.matrix.model.matrix.AnnotationMatrix;
import org.gitools.core.persistence.IResourceLocator;
import org.gitools.core.persistence.PersistenceException;
import org.gitools.core.persistence._DEPRECATED.FileSuffixes;
import org.gitools.core.persistence.formats.AbstractResourceFormat;
import org.gitools.utils.csv.CSVReader;
import org.gitools.utils.csv.RawCsvWriter;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class AnnotationMatrixFormat extends AbstractResourceFormat<AnnotationMatrix> {


    public AnnotationMatrixFormat() {
        super(FileSuffixes.ANNOTATION_MATRIX, AnnotationMatrix.class);
    }

    @NotNull
    @Override
    protected AnnotationMatrix readResource(@NotNull IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException {

        AnnotationMatrix matrix = new AnnotationMatrix();

        try {
            InputStream in = resourceLocator.openInputStream();
            CSVReader parser = new CSVReader(new InputStreamReader(in));

            // Header
            String[] headers = parser.readNext();

            // Annotations
            String[] fields;
            while ((fields = parser.readNext()) != null) {
                for (int i = 1; (i < headers.length) && (i < fields.length); i++) {
                    matrix.setAnnotation(fields[0], headers[i], fields[i]);
                }
            }

            in.close();
        } catch (Exception e) {
            throw new PersistenceException(e);
        }

        return matrix;
    }


    @Override
    protected void writeResource(IResourceLocator resourceLocator, AnnotationMatrix resource, IProgressMonitor progressMonitor) throws PersistenceException {
        try {
            OutputStream out = resourceLocator.openOutputStream();
            RawCsvWriter writer = new RawCsvWriter(new OutputStreamWriter(out), '\t', '"');

            // Header
            List<String> labels = new ArrayList<String>();
            labels.addAll(resource.getLabels());
            writer.writePropertyList("identifier", labels);

            // Annotations
            String[] annotations = new String[labels.size()];
            for (String identifier : resource.getIdentifiers()) {
                for (int i = 0; i < labels.size(); i++) {
                    annotations[i] = resource.getAnnotation(identifier, labels.get(i));
                }
                writer.writePropertyList(identifier, annotations);
            }

            writer.close();
            out.close();
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }
}
