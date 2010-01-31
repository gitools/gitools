package org.gitools.persistence.text;

import java.io.File;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.DataFormatException;

import org.apache.commons.csv.CSVParser;
import org.gitools.matrix.model.ObjectMatrix;
import org.gitools.matrix.model.element.ArrayElementAdapter;
import org.gitools.matrix.model.element.ArrayElementFactory;
import org.gitools.matrix.model.element.BeanElementAdapter;
import org.gitools.matrix.model.element.BeanElementFactory;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.matrix.model.element.IElementFactory;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.persistence.AbstractEntityPersistence;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.PersistenceUtils;
import org.gitools.stats.test.results.BinomialResult;
import org.gitools.stats.test.results.CombinationResult;
import org.gitools.stats.test.results.CommonResult;
import org.gitools.stats.test.results.FisherResult;
import org.gitools.stats.test.results.ZScoreResult;
import org.gitools.utils.CSVStrategies;

import cern.colt.matrix.ObjectFactory1D;
import cern.colt.matrix.ObjectMatrix1D;
import edu.upf.bg.csv.RawCsvWriter;
import edu.upf.bg.progressmonitor.IProgressMonitor;

public class ObjectMatrixTextPersistence
		extends AbstractEntityPersistence<ObjectMatrix> {

	private static final long serialVersionUID = 3487889255829878181L;

	/* This information will be used to infer the element class
	 * to use when loading an old tabulated file 
	 * using only its headers */
	private static Map<String, Class<?>> elementClasses = new HashMap<String, Class<?>>();
	static {
		Class<?>[] classes = new Class<?>[] {
			ZScoreResult.class,
			BinomialResult.class,
			FisherResult.class,
			CombinationResult.class,
			CommonResult.class
		};
		
		for (Class<?> elementClass : classes) {
			IElementAdapter adapter = 
				new BeanElementAdapter(elementClass);

			elementClasses.put(
					getElementClassId(adapter.getProperties()), 
					elementClass);
		}
	}
	
	private static String getElementClassId(List<IElementAttribute> properties) {
		String[] ids = new String[properties.size()];
		for (int i = 0; i < properties.size(); i++)
			ids[i] = properties.get(i).getId();
		
		return getElementClassId(ids);
	}
	
	private static String getElementClassId(String[] ids) {
		Arrays.sort(ids);
		
		StringBuilder sb = new StringBuilder();
		for (String id : ids)
			sb.append(':').append(id);
		return sb.toString();
	}
	
	public ObjectMatrixTextPersistence() {
	}

	@Override
	public ObjectMatrix read(
			File file,
			IProgressMonitor monitor) 
			throws PersistenceException {
		
		ObjectMatrix resultsMatrix = new ObjectMatrix();
		read(file, resultsMatrix, monitor);
		return resultsMatrix;
	}
	
	public void read(
			File file,
			ObjectMatrix resultsMatrix, 
			IProgressMonitor monitor) 
			throws PersistenceException {
		
		monitor.begin("Loading results ...", 1);
		monitor.info("File: " + file.getAbsolutePath());
		
		try {
			Reader reader = PersistenceUtils.openReader(file);
			
			CSVParser parser = new CSVParser(reader, CSVStrategies.TSV);
			
			String[] line = parser.getLine();
			
			// read header
			if (line.length < 3)
				throw new DataFormatException("Almost 3 columns expected.");
			
			int numParams = line.length - 2;
			String[] paramNames = new String[numParams];
			System.arraycopy(line, 2, paramNames, 0, line.length - 2);
			
			String[] ids = new String[numParams];
			System.arraycopy(line, 2, ids, 0, line.length - 2);
			
			// infer element class and create corresponding adapter and factory
			Class<?> elementClass = elementClasses.get(
					getElementClassId(ids));
			
			IElementAdapter elementAdapter = null;
			IElementFactory elementFactory = null;
			if (elementClass == null) {
				elementAdapter = new ArrayElementAdapter(paramNames);
				elementFactory = new ArrayElementFactory(paramNames.length);
			}
			else {
				elementAdapter = new BeanElementAdapter(elementClass);
				elementFactory = new BeanElementFactory(elementClass);
			}
			
			Map<String, Integer> attrIdmap = new HashMap<String, Integer>();
			int index = 0;
			for (IElementAttribute attr : elementAdapter.getProperties())
				attrIdmap.put(attr.getId(), index++);
			
			// read body
			Map<String, Integer> columnMap = new HashMap<String, Integer>();
			Map<String, Integer> rowMap = new HashMap<String, Integer>();
			List<Object[]> list = new ArrayList<Object[]>();
			
			while ((line = parser.getLine()) != null) {
				final String columnName = line[0];
				final String rowName = line[1];
				
				Integer columnIndex = columnMap.get(columnName);
				if (columnIndex == null) {
					columnIndex = columnMap.size();
					columnMap.put(columnName, columnIndex);
				}
				
				Integer rowIndex = rowMap.get(rowName);
				if (rowIndex == null) {
					rowIndex = rowMap.size();
					rowMap.put(rowName, rowIndex);
				}
				
				Object element = elementFactory.create();
	
				for (int i = 2; i < line.length; i++) {
					final int pix = attrIdmap.get(paramNames[i - 2]);
					
					Object value = parsePropertyValue(
							elementAdapter.getProperty(pix), line[i]);
	
					elementAdapter.setValue(element, pix, value);
				}
				
				list.add(new Object[] {
						new int[] { columnIndex, rowIndex }, element });
			}
			
			int numColumns = columnMap.size();
			int numRows = rowMap.size();
			
			ObjectMatrix1D columns = ObjectFactory1D.dense.make(numColumns);
			for (Entry<String, Integer> entry : columnMap.entrySet())
				columns.setQuick(entry.getValue(), entry.getKey());
			
			ObjectMatrix1D rows = ObjectFactory1D.dense.make(numRows);
			for (Entry<String, Integer> entry : rowMap.entrySet())
				rows.setQuick(entry.getValue(), entry.getKey());
			
			resultsMatrix.setColumns(columns);
			resultsMatrix.setRows(rows);
			resultsMatrix.makeCells();
			
			resultsMatrix.setCellAdapter(elementAdapter);
			
			for (Object[] result : list) {
				int[] coord = (int[]) result[0];
				final int columnIndex = coord[0];
				final int rowIndex = coord[1];
				
				Object element = result[1];			
				resultsMatrix.setCell(rowIndex, columnIndex, element);
			}
		}
		catch (Exception e) {
			throw new PersistenceException(e);
		}
		
		monitor.end();
	}
	
	//FIXME: We need a ValueParserFactory
	private Object parsePropertyValue(IElementAttribute property, String string) {
		
		final Class<?> propertyClass = property.getValueClass();
		
		Object value = null;
		try {
			if (propertyClass.equals(double.class)
					|| propertyClass.equals(Double.class))
				value = Double.parseDouble(string);
			else if (propertyClass.equals(float.class)
					|| propertyClass.equals(Float.class))
				value = Double.parseDouble(string);
			else if (propertyClass.equals(int.class)
					|| propertyClass.equals(Integer.class))
				value = Integer.parseInt(string);
			else if (propertyClass.equals(long.class)
					|| propertyClass.equals(Long.class))
				value = Long.parseLong(string);
			else if (propertyClass.isEnum()) {
				Object[] cts = propertyClass.getEnumConstants();
				for (Object o : cts)
					if (o.toString().equals(string))
						value = o;
			}
			else
				value = string;
		}
		catch (Exception e) {
			if (propertyClass.equals(double.class)
					|| propertyClass.equals(Double.class))
				value = Double.NaN;
			else if (propertyClass.equals(float.class)
					|| propertyClass.equals(Float.class))
				value = Float.NaN;
			else if (propertyClass.equals(int.class)
					|| propertyClass.equals(Integer.class))
				value = new Integer(0);
			else if (propertyClass.equals(long.class)
					|| propertyClass.equals(Long.class))
				value = new Long(0);
			else if (propertyClass.isEnum())
				value = string;
		}
		return value;
	}

	@Override
	public void write(
			File file,
			ObjectMatrix results, 
			IProgressMonitor monitor) 
			throws PersistenceException {
		
		write(file, results, true, monitor);
	}
	
	public void write(
			File file,
			ObjectMatrix results, 
			boolean orderByColumn,
			IProgressMonitor monitor) 
			throws PersistenceException {

		monitor.begin("Saving results...", results.getRowCount() * results.getColumnCount());
		monitor.info("File: " + file.getAbsolutePath());

		try {
			Writer writer = PersistenceUtils.openWriter(file);
			//String cellsPath = new File(basePath, prefix + ".cells.tsv.gz").getAbsolutePath();
			writeCells(writer, results, orderByColumn, monitor);
			writer.close();
		}
		catch (Exception e) {
			throw new PersistenceException(e);
		}
		finally {
			monitor.end();
		}
	}
	
	public void writeCells(Writer writer, ObjectMatrix resultsMatrix, boolean orderByColumn, IProgressMonitor monitor) {
		
		RawCsvWriter out = new RawCsvWriter(writer, 
				CSVStrategies.TSV.getDelimiter(),
				CSVStrategies.TSV.getEncapsulator());
		
		out.writeQuotedValue("column");
		out.writeSeparator();
		out.writeQuotedValue("row");
		
		for (IElementAttribute prop : resultsMatrix.getCellAdapter().getProperties()) {
			out.writeSeparator();
			out.writeQuotedValue(prop.getId());
		}
		
		out.writeNewLine();

		int numColumns = resultsMatrix.getColumnCount();
		int numRows = resultsMatrix.getRowCount();
		
		if (orderByColumn) {
			for (int colIndex = 0; colIndex < numColumns; colIndex++)
				for (int rowIndex = 0; rowIndex < numRows; rowIndex++)
					writeLine(out, resultsMatrix, colIndex, rowIndex, monitor);
		}
		else {
			for (int rowIndex = 0; rowIndex < numRows; rowIndex++)
				for (int colIndex = 0; colIndex < numColumns; colIndex++)
					writeLine(out, resultsMatrix, colIndex, rowIndex, monitor);
		}
	}

	private void writeLine(
			RawCsvWriter out, 
			ObjectMatrix resultsMatrix,
			int colIndex, int rowIndex,
			IProgressMonitor monitor) {
		
		final String colName = resultsMatrix.getColumn(colIndex).toString();
		final String rowName = resultsMatrix.getRow(rowIndex).toString();
		
		out.writeQuotedValue(colName);
		out.writeSeparator();
		out.writeQuotedValue(rowName);
		
		Object element = resultsMatrix.getCell(rowIndex, colIndex);
		
		IElementAdapter cellsFacade = resultsMatrix.getCellAdapter();
		
		int numProperties = cellsFacade.getPropertyCount();
		
		for (int propIndex = 0; propIndex < numProperties; propIndex++) {
			out.writeSeparator();
			
			Object value = cellsFacade.getValue(element, propIndex);
			if (value instanceof Double) {
				Double v = (Double) value;
				if (Double.isNaN(v))
					out.writeValue("-");
				else
					out.writeValue(v.toString());
			}
			else
				out.writeQuotedValue(value.toString());
		}
		
		out.writeNewLine();

		monitor.worked(1);
	}
}