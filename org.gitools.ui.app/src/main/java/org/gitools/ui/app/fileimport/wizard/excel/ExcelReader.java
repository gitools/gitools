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

import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.ui.platform.progress.JobRunnable;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
            Workbook workbook = WorkbookFactory.create(fis);
            this.sheet = workbook.getSheetAt(0);

        } catch (IOException | InvalidFormatException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(fis);
        }

        // Read the header
        Row header = sheet.getRow(0);
        Row firstRow = sheet.getRow(1);
        headers = rowToHeader(header, firstRow);

    }

    private List<ExcelHeader> rowToHeader(Row header, Row firstRow) {

        Cell cell;
        Cell firstCell;
        List<ExcelHeader> headers = new ArrayList<>();

        if (header != null) {

            for (int i = 0; i < header.getLastCellNum(); i++) {
                cell = header.getCell(i);
                firstCell = firstRow.getCell(i);
                if (cell != null) {
                    int cellType = (firstCell == null ? Cell.CELL_TYPE_BLANK : firstCell.getCellType());
                    headers.add(new ExcelHeader(cell.toString(), i, cellType));
                }
            }
        }
        return headers;

    }

    public List<ExcelHeader> getHeaders() {
        return headers;
    }


    public String getValue(int rowPos, int colPos) {

        Row row = sheet.getRow(rowPos);
        if (row == null) {
            return null;
        }

        Cell cell = row.getCell(colPos);
        if (cell == null) {
            return null;
        }

        return cell.toString();
    }

    public IResourceLocator getLocator() {
        return locator;
    }

    public int getLastRowNum() {
        return sheet.getLastRowNum();
    }


}
