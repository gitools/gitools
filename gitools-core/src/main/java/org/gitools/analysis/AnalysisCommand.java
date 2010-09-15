package org.gitools.analysis;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.BaseMatrix;
import org.gitools.matrix.model.DoubleBinaryMatrix;
import org.gitools.matrix.model.IMatrix;
import org.gitools.model.ModuleMap;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.PersistenceManager;

public abstract class AnalysisCommand {

	protected String workdir;

	protected String fileName;

	public AnalysisCommand(String workdir, String fileName) {
		this.workdir = workdir;
		this.fileName = fileName;
	}

	public String getWorkdir() {
		return workdir;
	}

	public void setWorkdir(String workdir) {
		this.workdir = workdir;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public abstract void run(IProgressMonitor monitor) throws AnalysisException;

	protected BaseMatrix loadDataMatrix(
			File file, String mime,
			Properties props, IProgressMonitor monitor) throws PersistenceException {

		Object obj = PersistenceManager.getDefault()
				.load(file, mime, props, monitor);

		BaseMatrix matrix = null;
		if (obj instanceof BaseMatrix)
			matrix = (BaseMatrix) obj;
		else if (obj instanceof ModuleMap)
			matrix = moduleMapToMatrix((ModuleMap) obj);
		else
			throw new PersistenceException("Invalid MIME type for data: " + mime);

		return matrix;
	}

	protected ModuleMap loadModuleMap(
			File file, String mime,
			Properties props, IProgressMonitor monitor) throws PersistenceException {

		Object obj = (ModuleMap) PersistenceManager.getDefault()
				.load(file, mime, props, monitor);

		ModuleMap moduleMap = null;
		if (obj instanceof BaseMatrix)
			moduleMap = matrixToModuleMap((BaseMatrix) obj);
		else if (obj instanceof ModuleMap)
			moduleMap = (ModuleMap) obj;
		else
			throw new PersistenceException("Invalid MIME type for modules: " + mime);

		return moduleMap;
	}

	protected BaseMatrix moduleMapToMatrix(ModuleMap mmap) {
		DoubleBinaryMatrix matrix = new DoubleBinaryMatrix();
		String[] columns = mmap.getModuleNames();
		String[] rows = mmap.getItemNames();
		matrix.setColumns(columns);
		matrix.setRows(rows);
		matrix.makeCells(rows.length, columns.length);
		for (int col = 0; col < mmap.getModuleCount(); col++)
			for (int row : mmap.getItemIndices(col))
				matrix.setCellValue(row, col, 0, 1.0);
		return matrix;
	}

	protected ModuleMap matrixToModuleMap(IMatrix matrix) {
		String[] itemNames = new String[matrix.getRowCount()];
		for (int i = 0; i < matrix.getRowCount(); i++)
			itemNames[i] = matrix.getRowLabel(i);

		String[] modNames = new String[matrix.getColumnCount()];
		for (int i = 0; i < matrix.getColumnCount(); i++)
			modNames[i] = matrix.getColumnLabel(i);

		ModuleMap map = new ModuleMap();
		map.setItemNames(itemNames);
		map.setModuleNames(modNames);

		int[][] mapIndices = new int[matrix.getColumnCount()][];
		for (int col = 0; col < matrix.getColumnCount(); col++) {
			List<Integer> indexList = new ArrayList<Integer>();
			for (int row = 0; row < matrix.getRowCount(); row++) {
				double value = MatrixUtils.doubleValue(matrix.getCellValue(row, col, 0));
				if (value == 1.0)
					indexList.add(row);
			}
			int[] indexArray = new int[indexList.size()];
			for (int i = 0; i < indexList.size(); i++)
				indexArray[i] = indexList.get(i);
			mapIndices[col] = indexArray;
		}

		map.setAllItemIndices(mapIndices);

		return map;
	}
}
