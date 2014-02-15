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
package org.gitools.ui.app.fileimport.wizard.text;

import org.apache.commons.io.IOUtils;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.utils.progressmonitor.NullProgressMonitor;
import org.gitools.utils.text.*;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.gitools.utils.text.Separator.COMMA;
import static org.gitools.utils.text.Separator.TAB;

public class FlatTextReader implements JobRunnable, Closeable {

    private final IResourceLocator locator;
    private CSVReader reader = null;
    private List<FileHeader> headers;
    private int previewLength = 20;
    private List<List<FileField>> preview;

    private ReaderProfile readerProfile;

    public FlatTextReader(IResourceLocator locator, ReaderProfile profile) {
        super();
        this.locator = locator;
        this.readerProfile = profile;

        // Choose COMMA separator if it's a CSV
        if ("csv".equalsIgnoreCase(locator.getExtension())) {
            readerProfile.setSeparator(COMMA);
        } else if ("tsv".equalsIgnoreCase(locator.getExtension()) || "tab".equalsIgnoreCase(locator.getExtension())) {
            readerProfile.setSeparator(TAB);
        } else {
            // try to guess:
            int recordOfFields = 0;
            for (Separator sep : Separator.values()) {
                Separator oldSep = readerProfile.getSeparator();
                readerProfile.setSeparator(sep);
                run(new NullProgressMonitor());
                if (headers.size() < recordOfFields) {
                    readerProfile.setSeparator(oldSep);
                } else {
                    recordOfFields = headers.size();
                }
            }
        }
    }


    @Override
    public void run(IProgressMonitor monitor) {
        monitor.begin("Opening [" + locator.getName() + "]", 0);
        CSVReader reader = null;
        try {
            reader = newCSVReader(monitor);


            // Load headers
            String[] line = reader.readNext();
            headers = new ArrayList<>(line.length);
            for (int i=0; i < line.length; i++) {
                headers.add(new FileHeader(line[i], i, 0));
            }

            //Load preview
            if (previewLength > 0) {
                preview = new ArrayList<>();
                for (int i = 0; i < previewLength; i++) {
                    line = reader.readNext();
                    if (line != null) {
                        ArrayList<FileField> fields = new ArrayList<FileField>(line.length);
                        for (int j = 0; j < line.length; j++) {
                            fields.add(new FileField(line[j], j, 0, i));
                        }
                        preview.add(fields);
                    } else {
                        previewLength = i;
                    }

                }
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

    private CSVReader newCSVReader(IProgressMonitor monitor) throws IOException {
        reader = new CSVReader(
                new InputStreamReader(locator.openInputStream(monitor)),
                readerProfile.getSeparator().getChar(),
                CSVParser.DEFAULT_QUOTE_CHARACTER,
                CSVParser.DEFAULT_ESCAPE_CHARACTER,
                readerProfile.getCommentChar(),
                readerProfile.getSkipLines(),
                CSVParser.DEFAULT_STRICT_QUOTES,
                CSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE);
        return reader;
    }

    public String[] readNext() throws IOException {

        if (reader==null) {
            reader = newCSVReader(new NullProgressMonitor());
        }

        return reader.readNext();
    }

    public List<FileHeader> getHeaders() {
        return headers;
    }

    public List<List<FileField>> getPreview() {
        return preview;
    }

    public IResourceLocator getLocator() {
        return locator;
    }

    public int getLastRowNum() {
        //TODO
        return 0;
    }

    public ReaderProfile getReaderProfile() {
        return readerProfile;
    }

    public void setReaderProfile(ReaderProfile readerProfile) {
        this.readerProfile = readerProfile;
    }

    @Override
    public void close() throws IOException {
        if (reader!=null) {
            reader.close();
        }
    }
}
