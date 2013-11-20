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

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExcelReader {

    private final File file;


    private Sheet sheet = null;

    private DataFormatter formatter = null;

    private FormulaEvaluator evaluator = null;
    private List<ExcelHeader> headers;


    public ExcelReader(File file) {
        super();
        this.file = file;
    }

    public void init() throws Exception {

        // Open the workbook
        openWorkbook(file);

        // Read the header
        Row header = sheet.getRow(0);
        Row firstRow = sheet.getRow(1);
        headers = rowToHeader(header, firstRow);

    }

    private void openWorkbook(File file) throws IOException, InvalidFormatException {
        FileInputStream fis = null;
        try {
            System.out.println("Opening workbook [" + file.getName() + "]");

            fis = new FileInputStream(file);

            Workbook workbook = WorkbookFactory.create(fis);
            this.sheet = workbook.getSheetAt(0);
            this.evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            this.formatter = new DataFormatter(Locale.ENGLISH, true);

        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }


    private String cellToString(Cell cell) {
        if (cell.getCellType() != Cell.CELL_TYPE_FORMULA) {
            return this.formatter.formatCellValue(cell);
        } else {
            return this.formatter.formatCellValue(cell, this.evaluator);
        }
    }


    private List<ExcelHeader> rowToHeader(Row header, Row firstRow) {

        Cell cell = null;
        Cell firstCell = null;
        int lastCellNum = 0;
        List<ExcelHeader> headers = new ArrayList<ExcelHeader>();

        if (header != null) {

            lastCellNum = header.getLastCellNum();
            for (int i = 0; i <= lastCellNum; i++) {
                cell = header.getCell(i);
                firstCell = firstRow.getCell(i);
                if (cell != null) {
                    int cellType = (firstCell == null ? Cell.CELL_TYPE_BLANK : firstCell.getCellType());
                    headers.add(new ExcelHeader(cellToString(cell), i, cellType));
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

        return cellToString(cell);
    }

    public File getFile() {
        return file;
    }

    public int getLastRowNum() {
        return sheet.getLastRowNum();
    }
}
