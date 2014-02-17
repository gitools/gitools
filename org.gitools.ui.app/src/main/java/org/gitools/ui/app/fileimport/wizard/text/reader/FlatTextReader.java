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
package org.gitools.ui.app.fileimport.wizard.text.reader;

import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.utils.progressmonitor.NullProgressMonitor;
import org.gitools.utils.text.*;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.gitools.utils.text.Separator.COMMA;
import static org.gitools.utils.text.Separator.TAB;

public class FlatTextReader implements Closeable {

    private final IResourceLocator locator;
    private CSVReader reader = null;
    private List<FileHeader> headers;
    private int previewLength = 20;
    private List<List<FileField>> preview;
    private ReaderProfile readerProfile;
    private ReaderAssistant readerAssistant;
    private boolean previewMode;
    private String[] currentLine;


    public FlatTextReader(IResourceLocator locator, boolean previewMode, ReaderProfile profile) {
        super();
        this.locator = locator;
        this.readerProfile = profile == null ? new TableReaderProfile() : profile;
        this.previewMode = previewMode;
        guessSeparator(locator);
        loadHead(new NullProgressMonitor());
        if (profile == null) {
            this.readerProfile = guessDataLayout();
        }
        setAssistant(readerProfile);
    }

    public FlatTextReader(IResourceLocator locator, boolean previewMode) {
        this(locator, previewMode, null);
    }

    private ReaderProfile guessDataLayout() {

        // if top left field is empty, layout is probably a matrix
        if (getFileHeaders().get(0).getLabel().equals("")) {
            return MatrixReaderProfile.fromProfile(readerProfile);
        } else {
            return readerProfile;
        }
    }

    private void guessSeparator(IResourceLocator locator) {
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
                loadHead(new NullProgressMonitor());
                if (headers.size() < recordOfFields) {
                    readerProfile.setSeparator(oldSep);
                } else {
                    recordOfFields = headers.size();
                }
            }
        }
    }

    private void setAssistant(ReaderProfile profile) {
        this.readerAssistant = profile.getLayout().equals(ReaderProfile.MATRIX) ?
                new MatrixReaderAssistant(this) : new TableReaderAssistant(this);
    }


    public void loadHead(IProgressMonitor monitor) {
        monitor.begin("Opening [" + locator.getName() + "]", 0);
        reader = null;
        try {
            reader = newCSVReader(monitor);


            // Load file headers
            String[] line = reader.readNext();
            headers = new ArrayList<>(line.length);
            for (int i = 0; i < line.length; i++) {
                headers.add(new FileHeader(line[i], i, 0));
            }

            //Load preview
            if (previewLength > 0 && previewMode) {
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
                reader.close();
            }

            if (readerAssistant != null) {
                readerAssistant.update();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        monitor.end();
    }

    private CSVReader newCSVReader(IProgressMonitor monitor) throws IOException {
        CSVReader reader = new CSVReader(
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

    public boolean readNext() throws IOException {

        if (reader == null) {
            loadHead(new NullProgressMonitor());
        }

        currentLine = reader.readNext();
        if (currentLine == null) {
            return false;
        }
        return true;
    }

    public List<FileHeader> getFileHeaders() {
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
        setAssistant(readerProfile);
    }

    public ReaderAssistant getReaderAssistant() {
        return readerAssistant;
    }

    @Override
    public void close() throws IOException {
        if (reader != null) {
            reader.close();
        }
    }

    public void setPreviewMode(boolean previewMode) {
        this.previewMode = previewMode;
        loadHead(new NullProgressMonitor());
    }

    public String[] getCurrentLine() {
        return currentLine;
    }
}
