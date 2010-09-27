package org.gitools.analysis.htest.enrichment;

import cern.colt.function.DoubleFunction;
import java.util.Date;
import org.gitools.matrix.model.IMatrix;

import org.gitools.matrix.model.ObjectMatrix;
import org.gitools.matrix.model.element.BeanElementAdapter;
import org.gitools.stats.mtc.MTC;
import org.gitools.stats.test.Test;
import org.gitools.stats.test.factory.TestFactory;
import org.gitools.stats.test.results.CommonResult;
import org.gitools.threads.ThreadManager;
import org.gitools.threads.ThreadQueue;
import org.gitools.threads.ThreadSlot;

import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.ObjectFactory1D;
import cern.colt.matrix.ObjectMatrix1D;
import cern.jet.math.Functions;
import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.analysis.AnalysisException;
import org.gitools.analysis.htest.HtestProcessor;
import org.gitools.matrix.MatrixUtils;
import org.gitools.model.ModuleMap;
import org.gitools.stats.mtc.MTCFactory;

/* Notes:
 * 'cond' is an abbreviation for condition.
 */

public class EnrichmentProcessor extends HtestProcessor {

	private class RunSlot extends ThreadSlot {
		public DoubleMatrix1D population;
		public Test test;
		
		public RunSlot(ThreadQueue threadQueue) {
			super(threadQueue);
			population = null;
			test = null;
		}
	}

	private EnrichmentAnalysis analysis;
	
	public EnrichmentProcessor(EnrichmentAnalysis analysis) {
		
		this.analysis = analysis;
	}
	
	public void run(IProgressMonitor monitor) throws AnalysisException, InterruptedException {
		
		Date startTime = new Date();
		
		TestFactory testFactory = 
			TestFactory.createFactory(analysis.getTestConfig());

		IMatrix dataMatrix = analysis.getData();

		final int numConditions = dataMatrix.getColumnCount();
		final int numRows = dataMatrix.getRowCount();

		String[] labels = new String[numRows];
		for (int i = 0; i < labels.length; i++)
			labels[i] = dataMatrix.getRowLabel(i);

		ModuleMap mmap = analysis.getModuleMap();
		mmap = mmap.remap(labels,
				analysis.getMinModuleSize(),
				analysis.getMaxModuleSize());

		//DoubleMatrix2D data = null;
		ObjectMatrix1D conditions = ObjectFactory1D.dense.make(dataMatrix.getColumnCount());
		for (int i = 0; i < numConditions; i++)
			conditions.setQuick(i, dataMatrix.getColumnLabel(i));

		ObjectMatrix1D modules = ObjectFactory1D.dense.make(mmap.getModuleNames());
		
		int[][] moduleItemIndices = mmap.getAllItemIndices();
		
		final int numModules = modules.size();
		
		monitor.begin("Running enrichment analysis...", numConditions);
		
		Test test = testFactory.create();
		
		final ObjectMatrix resultsMatrix = new ObjectMatrix();
		
		resultsMatrix.setColumns(conditions);
		resultsMatrix.setRows(modules);
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
		
		for (int condIndex = 0; condIndex < numConditions && !monitor.isCancelled(); condIndex++) {
			
			//final String condName = conditions.getQuick(condIndex).toString();
			final String condName = dataMatrix.getColumnLabel(condIndex);
			
			//final DoubleMatrix1D condItems = data.viewRow(condIndex);
			final DoubleMatrix1D condItems = DoubleFactory1D.dense.make(numRows);
			for (int i = 0; i < numRows; i++) {
				double value = MatrixUtils.doubleValue(
						dataMatrix.getCellValue(i, condIndex, 0));

				condItems.setQuick(i, value);
			}

			DoubleMatrix1D population = condItems.viewSelection(notNaNProc);
			
			final IProgressMonitor condMonitor = monitor.subtask();
			
			condMonitor.begin("Condition " + condName + "...", numModules);
			
			for (int moduleIndex = 0; moduleIndex < numModules && !monitor.isCancelled(); moduleIndex++) {

				final String moduleName = modules.getQuick(moduleIndex).toString();
				final int[] itemIndices = moduleItemIndices[moduleIndex];
				
				final RunSlot slot = (RunSlot) threadQueue.take();
				
				if (slot.population != population) {
					slot.population = population;
					slot.test = testFactory.create();
					slot.test.processPopulation(condName, population);
				}
				
				final int condIdx = condIndex;
				final int moduleIdx = moduleIndex;
				
				slot.execute(new Runnable() {
					@Override public void run() {
						CommonResult result = null;
						try {
							int moduleSize = (int) condItems
									.viewSelection(itemIndices)
									.aggregate(
										Functions.plus,
										new DoubleFunction() {
											@Override public double apply(double d) {
												return Double.isNaN(d) ? 0 : 1; } });

							if (moduleSize >= minModuleSize	&& moduleSize <= maxModuleSize)
								result = slot.test.processTest(
										condName, condItems,
										moduleName, itemIndices);
						}
						catch (Throwable cause) {
							cause.printStackTrace();
						}

						try {
							resultsMatrix.setCell(moduleIdx, condIdx, result);
						}
						catch (Throwable cause) {
							cause.printStackTrace();
						}
					}
				});
				
				condMonitor.worked(1);
			}

			condMonitor.end();
			monitor.worked(1);
		}
		
		ThreadManager.shutdown(monitor);

		if (monitor.isCancelled())
			return;

		/* Multiple test correction */
		
		MTC mtc = MTCFactory.createFromName(analysis.getMtc());

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
