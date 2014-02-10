/*
 * #%L
 * gitools-ui-app
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
package org.gitools.ui.app.fileimport.wizard.csv;

import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.utils.csv.CSVReader;
import org.gitools.utils.progressmonitor.NullProgressMonitor;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CsvReader implements JobRunnable, Closeable {

    public static String TAB = "Tab";
    public static String COMMA = "Comma";
    public static String[] SEPARATORS = new String[] { CsvReader.TAB, CsvReader.COMMA };

    private final IResourceLocator locator;
    private String separator = TAB;
    private CSVReader reader = null;
    private List<CsvHeader> headers;

    public CsvReader(IResourceLocator locator) {
        super();
        this.locator = locator;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    @Override
    public void run(IProgressMonitor monitor) {
        monitor.begin("Opening [" + locator.getName() + "]", 0);
        CSVReader reader = null;
        try {
            reader = new CSVReader(new InputStreamReader(locator.openInputStream(monitor)), (COMMA.equals(separator) ? ',' : '\t'));

            // Load headers
            String[] line = reader.readNext();
            headers = new ArrayList<>(line.length);
            for (int i=0; i < line.length; i++) {
                headers.add(new CsvHeader(line[i], i, 0));
            }
            reader.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(reader);
        }
        monitor.end();
        // Read the header


    }

    public String[] readNext() throws IOException {

        if (reader==null) {
            reader = new CSVReader(new InputStreamReader(locator.openInputStream(NullProgressMonitor.get())), (COMMA.equals(separator) ? ',' : '\t'));
        }

        return reader.readNext();
    }

    public List<CsvHeader> getHeaders() {
        return headers;
    }

    public IResourceLocator getLocator() {
        return locator;
    }

    public int getLastRowNum() {
        //TODO
        return 0;
    }


    @Override
    public void close() throws IOException {
        if (reader!=null) {
            reader.close();
        }
    }
}
