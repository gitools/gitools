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
package org.gitools.ui.fileimport.wizard.excel;

import org.apache.commons.io.FilenameUtils;
import org.gitools.ui.commands.CommandLoadFile;
import org.gitools.utils.csv.RawCsvWriter;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CommandConvertAndLoadExcelFile extends CommandLoadFile {

    private final int columns;
    private final int rows;
    private final List<Integer> values;
    private final ExcelReader reader;

    public CommandConvertAndLoadExcelFile(int columns, int rows, List<Integer> values, @NotNull ExcelReader reader) {
        super(createTdmFile(reader).getAbsolutePath());

        this.columns = columns;
        this.rows = rows;
        this.values = values;
        this.reader = reader;
    }

    @NotNull
    private static File createTdmFile(@NotNull ExcelReader reader) {
        File excelFile = reader.getFile();
        return new File(excelFile.getParent(), FilenameUtils.getBaseName(excelFile.getName()) + ".tdm");
    }

    @Override
    public void execute(@NotNull IProgressMonitor monitor) throws CommandException {

        monitor.begin("Converting excel file", reader.getLastRowNum());

        File tdmFile = createTdmFile(reader);
        RawCsvWriter out = null;

        try {
            if (!tdmFile.exists()) {
                tdmFile.createNewFile();
            }
            out = new RawCsvWriter(new FileWriter(tdmFile), '\t', '"');
        } catch (IOException e) {
            throw new CommandException("Error opening file '" + tdmFile.getName() + "'");
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
                tdmFile.delete();
                return;
            }
        }
        out.close();

        super.execute(monitor);
    }
}
