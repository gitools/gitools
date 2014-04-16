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

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.commons.io.IOUtils;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.ui.platform.progress.JobRunnable;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExcelReader implements JobRunnable {

    private final IResourceLocator locator;

    private Sheet sheet = null;

    private List<ExcelHeader> headers;

    public ExcelReader(IResourceLocator locator) {
        super();
        this.locator = locator;
    }

    @Override
    public void run(IProgressMonitor monitor) {
        // Open the workbook
        monitor.begin("Opening workbook [" + locator.getName() + "]", 0);
        InputStream fis = null;
        try {
            fis = locator.openInputStream(monitor);
            Workbook workbook = Workbook.getWorkbook(fis);
            this.sheet = workbook.getSheet(0);

        } catch (BiffException | IOException e ) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(fis);
        }

        // Read the header
        Cell[] header = sheet.getRow(0);
        Cell[] firstRow = sheet.getRow(1);
        headers = rowToHeader(header, firstRow);

    }

    private List<ExcelHeader> rowToHeader(Cell[] header, Cell[] firstRow) {

        Cell cell;
        Cell firstCell;
        List<ExcelHeader> headers = new ArrayList<>();

        if (header != null) {

            for (int i = 0; i < header.length; i++) {
                cell = header[i];
                firstCell = firstRow[i];
                if (cell != null) {
                    CellType cellType = (firstCell == null ? CellType.EMPTY : firstCell.getType());
                    headers.add(new ExcelHeader(cell.getContents(), i, cellType));
                }
            }
        }
        return headers;

    }

    public List<ExcelHeader> getHeaders() {
        return headers;
    }


    public String getValue(int rowPos, int colPos) {

        Cell[] row = sheet.getRow(rowPos);
        if (row == null) {
            return null;
        }

        Cell cell = row[colPos];
        if (cell == null) {
            return null;
        }

        return cell.getContents();
    }

    public IResourceLocator getLocator() {
        return locator;
    }

    public int getLastRowNum() {
        return sheet.getRows();
    }


}
