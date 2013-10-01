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

import org.apache.commons.lang.StringUtils;
import org.gitools.core.matrix.model.matrix.AnnotationMatrix;
import org.gitools.core.persistence.IResourceLocator;
import org.gitools.core.persistence.PersistenceException;
import org.gitools.core.persistence.formats.AbstractResourceFormat;
import org.gitools.core.persistence.formats.FileSuffixes;
import org.gitools.utils.csv.CSVParser;
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
            InputStream in = resourceLocator.openInputStream(progressMonitor);

            // A reader that don't skip the comments.
            CSVReader parser = new CSVReader(
                    new InputStreamReader(in),
                    CSVParser.DEFAULT_SEPARATOR,
                    CSVParser.DEFAULT_QUOTE_CHARACTER,
                    CSVParser.DEFAULT_ESCAPE_CHARACTER,
                    '\n',
                    0,
                    CSVParser.DEFAULT_STRICT_QUOTES,
                    CSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE
            );

            // Header
            String[] headers = parser.readNext();
            while (headers[0].length() > 0 && headers[0].charAt(0) == '#') {
                headers = parser.readNext();
            }

            // Annotations
            String[] fields;
            while ((fields = parser.readNext()) != null) {

                if (StringUtils.isEmpty(fields[0])) {
                    continue;
                }

                // Annotation metadata
                if (fields[0].charAt(0) == '#') {
                    for (int i = 1; (i < headers.length) && (i < fields.length); i++) {
                        String key = fields[0].substring(1).trim();
                        matrix.setAnnotationMetadata(key, headers[i], fields[i]);
                    }
                } else {
                    for (int i = 1; (i < headers.length) && (i < fields.length); i++) {
                        matrix.setAnnotation(fields[0], headers[i], fields[i]);
                    }
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
            List<String> labels = new ArrayList<>();
            labels.addAll(resource.getLabels());
            writer.writePropertyList("identifier", labels);

            // Annotation metadata
            String[] annotations = new String[labels.size()];
            for (String key : resource.getMetadataKeys()) {
                for (int i = 0; i < labels.size(); i++) {
                    annotations[i] = resource.getAnnotationMetadata(key, labels.get(i));
                }
                writer.writePropertyList('#' + key, annotations);
            }

            // Annotations
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
