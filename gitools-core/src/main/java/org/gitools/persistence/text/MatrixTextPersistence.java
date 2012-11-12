/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.persistence.text;

import edu.upf.bg.csv.CSVReader;
import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.io.File;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.DataFormatException;
import org.gitools.datafilters.ValueTranslator;
import org.gitools.matrix.model.BaseMatrix;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.PersistenceUtils;

public abstract class MatrixTextPersistence<T extends BaseMatrix>
		extends BaseMatrixPersistence<T> {

    protected boolean isBinaryValues() {
		if (getProperties().containsKey(BINARY_VALUES))
			return (Boolean) getProperties().get(BINARY_VALUES);
		else
			return false;
	}

	protected abstract T createEntity();
	
	public T read(
			File file,
			ValueTranslator filt,
			IProgressMonitor monitor)
			throws PersistenceException {

		monitor.begin("Loading matrix...", 1);
		monitor.info("File: " + file.getAbsolutePath());

		T matrix = createEntity();

		readMetadata(file, matrix, monitor);

		readData(file, matrix, filt, null, null, monitor);

		monitor.end();

		return matrix;
	}

	public void readMetadata(
			File file,
			T matrix,
			IProgressMonitor monitor)
			throws PersistenceException {

		monitor.begin("Reading names ...", 1);

		Reader reader = null;
		try {
			reader = PersistenceUtils.openReader(file);
		} catch (Exception e) {
			throw new PersistenceException("Error opening file: " + file.getName(), e);
		}

		CSVReader parser = new CSVReader(reader);

		// Read header

		try {
			final String[] header = parser.readNext();
			if (header.length < 2)
				throw new DataFormatException("At least 2 columns expected.");

			// Read datafile name and column names

			matrix.setTitle(header[0]);

			int numColumns = header.length - 1;

			String[] columnNames = new String[numColumns];
			System.arraycopy(header, 1, columnNames, 0, numColumns);
			matrix.setColumns(columnNames);

			// Read row names

			String[] populationLabels = getPopulationLabels();
			final Set<String> popLabelsSet = populationLabels != null ?
				new HashSet<String>(Arrays.asList(populationLabels)) : null;

			String[] fields;

			final List<String> names = new ArrayList<String>();
			while ((fields = parser.readNext()) != null) {
				if (popLabelsSet == null || popLabelsSet.contains(fields[0]))
					names.add(fields[0]);
			}

			// Incorporate background names

			if (populationLabels != null) {
				final Set<String> nameSet = new HashSet<String>(names);
				for (String name : populationLabels) {
					if (!nameSet.contains(name)) {
						names.add(name);
						nameSet.add(name);
					}
				}
			}

			String[] rowNames = names.toArray(new String[names.size()]);
			matrix.setRows(rowNames);

			reader.close();
		}
		catch (Exception e) {
			throw new PersistenceException(e);
		}

		monitor.info(matrix.getColumns().cardinality() + " columns and "
				+ matrix.getRows().cardinality() + " rows");

		monitor.end();
	}

	public void readData(
			File file,
			T matrix,
			ValueTranslator valueTranslator,
			int[] columnsOrder,
			int[] rowsOrder,
			IProgressMonitor monitor)
			throws PersistenceException {

		monitor.begin("Reading data ...", 1);

		int numColumns = matrix.getColumns().cardinality();
		int numRows = matrix.getRows().cardinality();

		String[] columnNames = matrix.getColumnStrings();
		String[] rowNames = matrix.getRowStrings();

		// Sort column names ordered by columnsOrder

		final String[] finalColumnNames = new String[numColumns];
		for (int i = 0; i < numColumns; i++) {
			int colidx = columnsOrder != null ? columnsOrder[i] : i;
			finalColumnNames[colidx] = columnNames[i];
		}
		columnNames = finalColumnNames;

		// Read rows names and data ordered by rowsOrder

		String[] populationLabels = getPopulationLabels();
		final Set<String> popLabelsSet = populationLabels != null ?
			new HashSet<String>(Arrays.asList(populationLabels)) : null;

		Reader reader;
		try {
			reader = PersistenceUtils.openReader(file);
		} catch (Exception e) {
			throw new PersistenceException("Error opening resource: " + file.getName(), e);
		}

		try {
			CSVReader parser = new CSVReader(reader);

			parser.readNext(); // discard header

			matrix.makeCells(numRows, numColumns);

			String[] fields;
			int row = 0;

			while ((fields = parser.readNext()) != null) {
				if (popLabelsSet != null && !popLabelsSet.contains(fields[0]))
					continue;

				int rowidx = rowsOrder != null ? rowsOrder[row] : row;

				if (rowidx >= 0) {
					rowNames[rowidx] = fields[0];

					int col = 0;

					while (col < numColumns && col < fields.length - 1) {

						int colidx = columnsOrder != null ? columnsOrder[col] : col;

						Double value = (Double) valueTranslator.stringToValue(fields[col + 1]);

						matrix.setCellValue(rowidx, colidx, 0, value);
						col++;
					}

					// fill the rest of the columns with NaNs
					while (col < numColumns) {
						int colidx = columnsOrder != null ? columnsOrder[col] : col;
						matrix.setCellValue(rowidx, colidx, 0, Double.NaN);
						col++;
					}
				}
				row++;
			}

			// Fill the rest of population rows with background value

			Double backgroundValue = getBackgroundValue();
			while (row < numRows) {
				for (int col = 0; col < numColumns; col++)
					matrix.setCellValue(row, col, 0, backgroundValue);

				row++;
			}

			reader.close();
		}
		catch (Exception e) {
			throw new PersistenceException(e);
		}

		monitor.end();
	}

	public void write(
			File file,
			T matrix,
			ValueTranslator valueTranslator,
			IProgressMonitor monitor)
			throws PersistenceException {

		monitor.begin("Saving matrix...", matrix.getColumnCount());
		monitor.info("File: " + file.getAbsolutePath());

		Writer writer;
		try {
			writer = PersistenceUtils.openWriter(file);
		} catch (Exception e) {
			throw new PersistenceException("Error opening resource: " + file.getName(), e);
		}

		PrintWriter pw = new PrintWriter(writer);

		int numCols = matrix.getColumnCount();

		final String[] colNames = matrix.getColumnStrings();

		pw.print(matrix.getTitle() != null ? matrix.getTitle() : "");

		for (int i = 0; i < numCols; i++) {
			final String name = i < colNames.length ? colNames[i] : "";
			pw.print('\t');
			pw.print(name);
		}

		pw.print('\n');

		int numRows = matrix.getRowCount();

		final String[] rowNames = matrix.getRowStrings();

		for (int i = 0; i < numRows; i++) {
			final String name = i < rowNames.length ? rowNames[i] : "";

			pw.print(name);

			for (int j = 0; j < numCols; j++) {
				pw.print('\t');
				pw.print(valueTranslator.valueToString(
						matrix.getCellValue(i, j, 0)));
			}

			pw.print('\n');

			monitor.worked(1);
		}

		try {
			writer.close();
		} catch (Exception e) {
			throw new PersistenceException("Error closing file: " + file.getName(), e);
		}

		monitor.end();
	}
}
