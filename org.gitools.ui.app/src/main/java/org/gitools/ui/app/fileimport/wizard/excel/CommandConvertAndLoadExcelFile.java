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
package org.gitools.ui.app.fileimport.wizard.excel;

import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.resource.IResource;
import org.gitools.api.resource.IResourceFormat;
import org.gitools.api.ApplicationContext;
import org.gitools.matrix.format.TdmMatrixFormat;
import org.gitools.persistence.locators.UrlResourceLocator;
import org.gitools.ui.app.commands.CommandLoadFile;
import org.gitools.utils.csv.RawCsvWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CancellationException;

public class CommandConvertAndLoadExcelFile extends CommandLoadFile {

    private final int columns;
    private final int rows;
    private final List<Integer> values;
    private final ExcelReader reader;

    public CommandConvertAndLoadExcelFile(int columns, int rows, List<Integer> values, ExcelReader reader) {
        super(reader.getLocator().getURL().toString());

        this.columns = columns;
        this.rows = rows;
        this.values = values;
        this.reader = reader;
    }

    @Override
    public boolean isConfigurable() {
        return false;
    }

    @Override
    protected IResource loadResource(IProgressMonitor monitor) {
        monitor.begin("Converting excel file", reader.getLastRowNum());
        File tmpFile;
        RawCsvWriter out;

        try {
            tmpFile = File.createTempFile("gitools", reader.getLocator().getBaseName() + ".tdm");
            out = new RawCsvWriter(new FileWriter(tmpFile), '\t', '"');
        } catch (IOException e) {
            throw new RuntimeException("Error opening temporal file");
        }

        // Write headers
        out.writeQuotedValue("column");
        out.writeSeparator();
        out.writeQuotedValue("row");
        for (int v : values) {
            out.writeSeparator();
            out.writeQuotedValue(reader.getValue(0, v));
        }
        out.writeNewLine();

        // Write rows
        for (int r = 1; r <= reader.getLastRowNum(); r++) {
            out.writeQuotedValue(reader.getValue(r, columns));
            out.writeSeparator();
            out.writeQuotedValue(reader.getValue(r, rows));

            for (int v : values) {
                out.writeSeparator();
                String value = reader.getValue(r, v);
                out.writeValue((value != null ? value : "-"));
            }
            out.writeNewLine();
            monitor.worked(r);

            if (monitor.isCancelled()) {
                out.close();
                tmpFile.delete();
                throw new CancellationException();
            }
        }
        out.close();
        setExitStatus(0);

        IResourceFormat<IMatrix> format = ApplicationContext.getPersistenceManager().getFormat(TdmMatrixFormat.EXSTENSION, IMatrix.class);
        IMatrix matrix = ApplicationContext.getPersistenceManager().load(new UrlResourceLocator(tmpFile), format, monitor);
        matrix.setLocator(null);
        return matrix;
    }
}
