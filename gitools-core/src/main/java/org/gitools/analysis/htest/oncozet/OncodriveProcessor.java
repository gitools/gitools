package org.gitools.analysis.htest.oncozet;

import cern.colt.matrix.DoubleFactory1D;
import java.util.Date;

import org.gitools.matrix.model.ObjectMatrix;
import org.gitools.matrix.model.element.BeanElementAdapter;
import org.gitools.stats.test.Test;
import org.gitools.stats.test.factory.TestFactory;
import org.gitools.stats.test.results.CommonResult;
import org.gitools.threads.ThreadManager;
import org.gitools.threads.ThreadQueue;
import org.gitools.threads.ThreadSlot;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.ObjectFactory1D;
import cern.colt.matrix.ObjectMatrix1D;
import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.util.Arrays;
import org.gitools.analysis.htest.HtestProcessor;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.IMatrix;
import org.gitools.model.ModuleMap;
import org.gitools.stats.mtc.MTCFactory;
import org.gitools.stats.mtc.MTC;

public class OncodriveProcessor extends HtestProcessor {
		
	private class RunSlot extends ThreadSlot {
		public DoubleMatrix1D population;
		public Test test;
		public RunSlot(ThreadQueue threadQueue) {
			super(threadQueue);
			population = null;
			test = null;
		}
	}
	
	private OncodriveAnalysis analysis;
	
	public OncodriveProcessor(OncodriveAnalysis analysis) {
		
		this.analysis = analysis;
	}
	
	public void run(IProgressMonitor monitor) throws InterruptedException {
		
		Date startTime = new Date();
	
		TestFactory testFactory = 
			TestFactory.createFactory(analysis.getTestConfig());

		IMatrix dataMatrix = analysis.getData();
		/*if (!(dataMatrix instanceof DoubleMatrix))
			throw new RuntimeException("This processor only works with DoubleMatrix data. "
					+ dataMatrix.getClass().getSimpleName() + " found instead.");*/

		String[] labels = new String[dataMatrix.getColumnCount()];
		for (int i = 0; i < labels.length; i++)
			labels[i] = dataMatrix.getColumnLabel(i);

		ModuleMap smap = analysis.getModuleMap();
		if (smap != null)
			smap = smap.remap(labels,
					analysis.getMinModuleSize(),
					analysis.getMaxModuleSize());
		else {
			smap = new ModuleMap();
			smap.setModuleNames(new String[] {"all columns"});
			smap.setItemNames(labels);
			int[] indices = new int[dataMatrix.getColumnCount()];
			for (int i = 0; i < indices.length; i++)
				indices[i] = i;
			smap.setAllItemIndices(new int[][] {indices});
		}

		final int numRows = dataMatrix.getRowCount();
		ObjectMatrix1D rowLabels = ObjectFactory1D.dense.make(numRows);
		for (int i = 0; i < numRows; i++)
			rowLabels.setQuick(i, dataMatrix.getRowLabel(i));
		
		ObjectMatrix1D moduleLabels = ObjectFactory1D.dense.make(smap.getModuleNames());
		
		int[][] moduleColumnIndices = smap.getAllItemIndices();

		final int numModules = moduleLabels.size();
		
		monitor.begin("Running oncodrive analysis...", numRows * numModules);
	
		Test test = testFactory.create();
		
		final ObjectMatrix resultsMatrix = new ObjectMatrix();
		
		resultsMatrix.setColumns(moduleLabels);
		resultsMatrix.setRows(rowLabels);
		resultsMatrix.makeCells();
		
		resultsMatrix.setCellAdapter(
				new BeanElementAdapter(test.getResultClass()));
		
		int numProcs = ThreadManager.getNumThreads();
		
		ThreadQueue threadQueue = new ThreadQueue(numProcs);
		
		for (int i = 0; i < numProcs; i++)
			try {
				threadQueue.put(new RunSlot(threadQueue));
			} catch (InterruptedException e) {
				monitor.debug("InterruptedException while initializing run queue: " + e.getLocalizedMessage());
			}

		final int minModuleSize = analysis.getMinModuleSize();
		final int maxModuleSize = analysis.getMaxModuleSize();

		/* Test analysis */

		for (int moduleIndex = 0; moduleIndex < numModules; moduleIndex++) {
		
			final int moduleIdx = moduleIndex;
			
			final String moduleName = moduleLabels.getQuick(moduleIndex).toString();
			
			final int[] columnIndices = moduleColumnIndices[moduleIndex];
			
			final IProgressMonitor condMonitor = monitor.subtask();
			
			final int numColumns = columnIndices.length;

			if (numColumns >= minModuleSize && numColumns <= maxModuleSize) {

				condMonitor.begin("Module " + moduleName + "...", numRows);

				DoubleMatrix1D population = DoubleFactory1D.dense.make(numColumns * numRows);

				int k = 0;
				for (int i = 0; i < numRows; i++)
					for (int j = 0; j < numColumns; j++)
						population.setQuick(k++,
								MatrixUtils.doubleValue(
									dataMatrix.getCellValue(j, columnIndices[j], 0)));

				population = population.viewSelection(notNaNProc);

				final DoubleMatrix1D itemValues = DoubleFactory1D.dense.make(numColumns);

				for (int itemIndex = 0; itemIndex < numRows; itemIndex++) {

					final int itemIdx = itemIndex;

					final String itemName = rowLabels.getQuick(itemIndex).toString();

					/* TODO This is inefficient, it is being used all the matrix row
					 * because of the way the test.processTest() works, but only some
					 * columns are being processed. We should take only the values
					 * corresponding to the selected columns for this module.
					 */

					for (int j = 0; j < dataMatrix.getColumnCount(); j++)
						itemValues.setQuick(j,
								MatrixUtils.doubleValue(
									dataMatrix.getCellValue(itemIndex, j, 0)));

					final RunSlot slot = (RunSlot) threadQueue.take();

					if (slot.population != population) {
						slot.population = population;
						slot.test = testFactory.create();
						slot.test.processPopulation(moduleName, population);
					}

					slot.execute(new Runnable() {
						@Override public void run() {
							CommonResult result = null;
							try {
								result = slot.test.processTest(
										moduleName, itemValues,
										itemName, columnIndices);
							}
							catch (Throwable cause) {
								cause.printStackTrace();
							}

							try {
								resultsMatrix.setCell(itemIdx, moduleIdx, result);
							}
							catch (Throwable cause) {
								cause.printStackTrace();
							}
						}
					});

					condMonitor.worked(1);
				}
			}
			else
				condMonitor.begin("Module " + moduleName + " discarded.", 1);

			condMonitor.end();

			monitor.worked(1);
		}
		
		ThreadManager.shutdown(monitor);
		
		/* Multiple test correction */
		
		MTC mtc =
				MTCFactory.createFromName(analysis.getMtc());

		multipleTestCorrection(
				resultsMatrix,
				mtc,
				monitor.subtask());
		
		analysis.setStartTime(startTime);
		analysis.setElapsedTime(new Date().getTime() - startTime.getTime());
		
		analysis.setResults(resultsMatrix);
		
		monitor.end();
	}
}
