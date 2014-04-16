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
package org.gitools.matrix.format;

import com.google.common.collect.Iterables;
import edu.upf.bg.mtabix.MTabixConfig;
import edu.upf.bg.mtabix.MTabixIndex;
import org.apache.commons.io.IOUtils;
import org.gitools.api.PersistenceException;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.matrix.model.MatrixLayer;
import org.gitools.matrix.model.MatrixLayers;
import org.gitools.matrix.model.hashmatrix.HashMatrix;
import org.gitools.utils.readers.text.CSVReader;
import org.gitools.utils.readers.text.RawFlatTextWriter;
import org.gitools.utils.translators.DoubleTranslator;

import javax.enterprise.context.ApplicationScoped;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.gitools.api.matrix.MatrixDimensionKey.COLUMNS;
import static org.gitools.api.matrix.MatrixDimensionKey.ROWS;

@ApplicationScoped
public class TdmMatrixFormat extends AbstractMatrixFormat {

    public static final String EXSTENSION = "tdm";

    public TdmMatrixFormat() {
        super(EXSTENSION);
    }

    @Override
    public boolean isDefaultExtension() {
        return true;
    }

    @Override
    protected IMatrix readResource(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException {

        try {

            MTabixIndex index = readMtabixIndex(resourceLocator, progressMonitor);

            if (index != null) {
                index.checkMD5();
                //TODO Use mtabix index
            }

            InputStream in = resourceLocator.openInputStream(progressMonitor);
            CSVReader parser = new CSVReader(new InputStreamReader(in));

            String[] header = parser.readNext();
            if (header.length < 3) {
                throw new PersistenceException("At least 3 fields expected on one line.");
            }

            MatrixLayer<Double> layers[] = new MatrixLayer[header.length - 2];
            for (int i = 2; i < header.length; i++) {
                layers[i - 2] = new MatrixLayer<>(header[i], Double.class);
            }

            HashMatrix resultsMatrix = new HashMatrix(new MatrixLayers<MatrixLayer>(layers), ROWS, COLUMNS);

            // read body
            String fields[];
            while ((fields = parser.readNext()) != null) {

                if (progressMonitor.isCancelled()) {
                    throw new CancellationException();
                }

                checkLine(fields, header, parser.getLineNumber());

                final String columnId = fields[0];
                final String rowId = fields[1];

                for (int i = 2; i < fields.length; i++) {
                    Double value = DoubleTranslator.get().stringToValue(fields[i]);
                    resultsMatrix.set(layers[i - 2], value, rowId, columnId);
                }
            }

            in.close();

            return resultsMatrix;

        } catch (Exception e) {
            throw new PersistenceException(e);
        }

    }

    private MTabixIndex readMtabixIndex(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws IOException, URISyntaxException {

        // Check if we are using mtabix
        URL dataURL = resourceLocator.getURL();
        if ("file".equals(dataURL.getProtocol())) {

            URL indexURL = null;

            if (!dataURL.getPath().endsWith("zip")) {
                IResourceLocator mtabix = resourceLocator.getReferenceLocator(resourceLocator.getName() + ".gz.mtabix");
                indexURL = mtabix.getURL();
            } else {
                ZipFile zipFile = new ZipFile(new File(dataURL.toURI()));
                ZipEntry entry = zipFile.getEntry(resourceLocator.getName() + ".gz.mtabix");

                if (entry == null) {
                    return null;
                }

                // Copy index to a temporal file
                File indexFile = File.createTempFile("gitools-cache-", "zip_mtabix");
                indexFile.deleteOnExit();
                IOUtils.copy(zipFile.getInputStream(entry), new FileOutputStream(indexFile));
                indexURL = indexFile.toURL();

                // Copy data to a temporal file
                File dataFile = File.createTempFile("gitools-cache-", "zip_bgz");
                dataFile.deleteOnExit();
                InputStream dataStream = resourceLocator.openInputStream(progressMonitor);
                IOUtils.copy(dataStream, new FileOutputStream(dataFile));
                dataURL = dataFile.toURL();

            }

            MTabixConfig mtabixConfig = new MTabixConfig(new File(dataURL.toURI()), new File(indexURL.toURI()));
            MTabixIndex index = new MTabixIndex(mtabixConfig);
            index.loadIndex();

            return index;
        }

        return null;
    }



    @Override
    protected void writeResource(IResourceLocator resourceLocator, IMatrix results, IProgressMonitor monitor) throws PersistenceException {

        monitor.begin("Saving results...", results.getRows().size());

        try {
            OutputStream out = resourceLocator.openOutputStream();
            Writer writer = new OutputStreamWriter(out);
            writeCells(writer, results, monitor);
            writer.close();

            writeMtabixIndex(resourceLocator);

        } catch (Exception e) {
            throw new PersistenceException(e);
        }


    }

    private void writeMtabixIndex(IResourceLocator resourceLocator) throws URISyntaxException, IOException, NoSuchAlgorithmException {

        URL dataURL = resourceLocator.getURL();
        if ("file".equals(dataURL.getProtocol())) {

            if (dataURL.getPath().endsWith("zip")) {
                //TODO
            } else {
                IResourceLocator mtabix = resourceLocator.getReferenceLocator(resourceLocator.getName() + ".gz.mtabix");
                URL indexURL = mtabix.getURL();

                MTabixConfig mtabixConfig = new MTabixConfig(new File(dataURL.toURI()), new File(indexURL.toURI()));
                MTabixIndex index = new MTabixIndex(mtabixConfig);
                index.buildIndex();
            }
        }

    }

    private void writeCells(Writer writer, IMatrix resultsMatrix, IProgressMonitor progressMonitor) {

        RawFlatTextWriter out = new RawFlatTextWriter(writer, '\t', '"');

        out.writeQuotedValue("column");
        out.writeSeparator();
        out.writeQuotedValue("row");

        for (IMatrixLayer layer : resultsMatrix.getLayers()) {
            out.writeSeparator();
            out.writeQuotedValue(layer.getId());
        }

        out.writeNewLine();

        // Sort columns (MTabix requires the labels sorted)
        List<String> columns = new ArrayList<>(resultsMatrix.getColumns().size());
        Iterables.addAll(columns, resultsMatrix.getColumns());
        Collections.sort(columns);

        // Sort rows (MTabix requires the labels sorted)
        List<String> rows = new ArrayList<>(resultsMatrix.getRows().size());
        Iterables.addAll(rows, resultsMatrix.getRows());
        Collections.sort(rows);

        for (String row : rows) {
            for (String column : columns) {
                writeLine(out, resultsMatrix, column, row, progressMonitor);
            }
            progressMonitor.worked(1);
        }

    }

    private void writeLine(RawFlatTextWriter out, IMatrix resultsMatrix, String column, String row, IProgressMonitor progressMonitor) {

        out.writeQuotedValue(column);
        out.writeSeparator();
        out.writeQuotedValue(row);

        for (IMatrixLayer layer : resultsMatrix.getLayers()) {
            out.writeSeparator();

            Object value = resultsMatrix.get(layer, row, column);
            if (value instanceof Double) {
                Double v = (Double) value;
                out.write(DoubleTranslator.get().valueToString(v));
            } else if (value instanceof Integer) {
                out.writeValue(value.toString());
            } else if (value != null) {
                out.writeQuotedValue(value.toString());
            } else {
                out.writeValue("-");
            }
        }

        out.writeNewLine();
    }

    @Deprecated
    public static String[] readHeader(File file) throws PersistenceException {

        String[] matrixHeaders = null;
        try {
            Reader reader = openReader(file);

            CSVReader parser = new CSVReader(reader);

            String[] line = parser.readNext();

            // read header
            if (line.length < 3) {
                throw new PersistenceException("At least 3 columns expected.");
            }

            int numAttributes = line.length - 2;
            matrixHeaders = new String[numAttributes];
            System.arraycopy(line, 2, matrixHeaders, 0, numAttributes);
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
        return matrixHeaders;
    }

    private static Reader openReader(File path) throws IOException {
        if (path == null) {
            return null;
        }

        if (path.getName().endsWith(".gz")) {
            return new InputStreamReader(new GZIPInputStream(new FileInputStream(path)));
        } else {
            return new BufferedReader(new FileReader(path));
        }
    }

}