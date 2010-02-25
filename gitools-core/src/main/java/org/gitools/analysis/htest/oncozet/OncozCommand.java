package org.gitools.analysis.htest.oncozet;

import java.io.File;

import org.gitools.datafilters.ValueTranslator;
import org.gitools.model.ModuleMap;
import org.gitools.matrix.model.DoubleMatrix;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.text.DoubleMatrixTextPersistence;
import org.gitools.persistence.text.ModuleMapTextSimplePersistence;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.analysis.htest.HtestCommand;
import org.gitools.persistence.MimeTypes;
import org.gitools.persistence.text.DoubleBinaryMatrixTextPersistence;
import org.gitools.persistence.text.MatrixTextPersistence;
import org.gitools.persistence.xml.OncozAnalysisXmlPersistence;

public class OncozCommand extends HtestCommand {

	protected String setsMime;
	protected String setsFile;

	public OncozCommand(
			OncozAnalysis analysis,
			String dataMime,
			String dataFile,
			String setsMime,
			String setsFile,
			String workdir,
			String fileName) {
		
		super(analysis, dataMime, dataFile,
				workdir, fileName);

		this.setsFile = setsFile;
	}

	public String getSetsFile() {
		return setsFile;
	}

	public void setSetsFile(String setsFile) {
		this.setsFile = setsFile;
	}

	@Override
	public void run(IProgressMonitor monitor) 
			throws PersistenceException, InterruptedException {
		
		final OncozAnalysis oncozAnalysis = (OncozAnalysis) analysis;
		
		// Load data and modules
		
		monitor.begin("Loading ...", 1);
		monitor.info("Data: " + dataPath);
		monitor.info("Sets: " + setsFile);
		
		DoubleMatrix doubleMatrix = new DoubleMatrix();
		ModuleMap setsMap = new ModuleMap();

		loadDataAndModules(
				doubleMatrix, setsMap,
				dataMime, dataPath,
				createValueTranslator(analysis),
				setsFile,
				oncozAnalysis.getMinSetSize(),
				oncozAnalysis.getMaxSetSize(),
				true, monitor.subtask());

		oncozAnalysis.setDataMatrix(doubleMatrix);
		oncozAnalysis.setSetsMap(setsMap);

		monitor.end();
		
		OncozProcessor processor = new OncozProcessor(oncozAnalysis);
		
		processor.run(monitor);
		
		// Save analysis
		
		save(oncozAnalysis, monitor);
	}

	private void loadDataAndModules(
			DoubleMatrix matrix, ModuleMap moduleMap,
			String dataFileMime, String dataFileName,
			ValueTranslator valueTranslator,
			String setsFileName, int minSetsSize, int maxSetsSize,
			boolean includeNonMappedItems,
			IProgressMonitor monitor)
			throws PersistenceException {
		
		// Load metadata
		
		File dataFile = new File(dataFileName);
		
		MatrixTextPersistence dmPersistence = null;

		if (dataFileMime.equals(MimeTypes.DOUBLE_MATRIX))
			dmPersistence = new DoubleMatrixTextPersistence();
		else if (dataFileMime.equals(MimeTypes.DOUBLE_BINARY_MATRIX))
			dmPersistence = new DoubleBinaryMatrixTextPersistence();
		else
			throw new PersistenceException("Unsupported mime type: " + dataFileMime);

		dmPersistence.readMetadata(dataFile, matrix, monitor);
		
		// Load sets
		
		if (setsFileName != null) {
			File file = new File(setsFileName);
			moduleMap.setTitle(file.getName());
			
			ModuleMapTextSimplePersistence moduleMapTextSimplePersistence = new ModuleMapTextSimplePersistence(file);
			moduleMapTextSimplePersistence.load(
				moduleMap,
				minSetsSize,
				maxSetsSize,
				matrix.getColumnStrings(),
				includeNonMappedItems,
				monitor);
		}
		else {
			String[] names = matrix.getColumnStrings();
			moduleMap.setItemNames(names);
			moduleMap.setModuleNames(new String[] {"all"});
			int num = names.length;
			int[][] indices = new int[1][num];
			for (int i = 0; i < num; i++)
				indices[0][i] = i;
			moduleMap.setAllItemIndices(indices);
		}
		
		// Load data
		
		dmPersistence.readData(
				dataFile,
				matrix,
				valueTranslator,
				null, //DEPRECATED moduleMap.getItemsOrder(),
				null,
				monitor);		
	}

	private void save(final OncozAnalysis analysis, IProgressMonitor monitor) throws PersistenceException {

		File workdirFile = new File(workdir);
		if (!workdirFile.exists())
			workdirFile.mkdirs();

		File file = new File(workdirFile, fileName);
		OncozAnalysisXmlPersistence p = new OncozAnalysisXmlPersistence();
		p.setRecursivePersistence(true);
		p.write(file, analysis, monitor);
	}
}
