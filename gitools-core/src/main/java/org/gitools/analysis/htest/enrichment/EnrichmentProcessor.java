package org.gitools.analysis.htest.enrichment;

import java.util.Date;
import org.gitools.matrix.model.IMatrix;

import org.gitools.model.Analysis;
import org.gitools.matrix.model.ObjectMatrix;
import org.gitools.matrix.model.element.BeanElementAdapter;
import org.gitools.stats.mtc.BenjaminiHochbergFdr;
import org.gitools.stats.mtc.MTC;
import org.gitools.stats.test.Test;
import org.gitools.stats.test.factory.TestFactory;
import org.gitools.stats.test.results.CommonResult;
import org.gitools.threads.ThreadManager;
import org.gitools.threads.ThreadQueue;
import org.gitools.threads.ThreadSlot;

import cern.colt.function.DoubleProcedure;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.ObjectFactory1D;
import cern.colt.matrix.ObjectMatrix1D;
import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.analysis.htest.AbstractProcessor;
import org.gitools.matrix.model.DoubleMatrix;
import org.gitools.model.ModuleMap;
import org.gitools.stats.mtc.MTCFactory;

/* Notes:
 * 'cond' is an abbreviation for condition.
 */

public class EnrichmentProcessor extends AbstractProcessor {
	
	protected static final DoubleProcedure notNaNProc = 
		new DoubleProcedure() {
			@Override
			public boolean apply(double element) {
				return !Double.isNaN(element);
			}
		};

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
	
	public void run(IProgressMonitor monitor) throws InterruptedException {
		
		Date startTime = new Date();
		
		TestFactory testFactory = 
			TestFactory.createFactory(analysis.getTestConfig());
		//String[] paramNames = testFactory.create().getResultNames();
		//final int numParams = paramNames.length;

		IMatrix dataMatrix = analysis.getDataMatrix();
		if (!(dataMatrix instanceof DoubleMatrix))
			throw new RuntimeException("This processor only works with DoubleMatrix data. "
					+ dataMatrix.getClass().getSimpleName() + " found instead.");

		String[] labels = new String[dataMatrix.getRowCount()];
		for (int i = 0; i < labels.length; i++)
			labels[i] = dataMatrix.getRowLabel(i);

		ModuleMap mmap = analysis.getModuleMap();
		mmap = mmap.remap(labels,
				analysis.getMinModuleSize(), analysis.getMaxModuleSize());

		DoubleMatrix doubleMatrix = (DoubleMatrix) dataMatrix;

		ObjectMatrix1D conditions = doubleMatrix.getColumns().copy();
		
		DoubleMatrix2D data = doubleMatrix.getCells().viewDice().copy();
		
		ObjectMatrix1D modules = ObjectFactory1D.dense.make(mmap.getModuleNames());
		
		int[][] moduleItemIndices = mmap.getAllItemIndices();
		
		final int numConditions = data.rows();
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

		/* Test analysis */
		
		for (int condIndex = 0; condIndex < numConditions; condIndex++) {
			
			final String condName = conditions.getQuick(condIndex).toString();
			
			final DoubleMatrix1D condItems = data.viewRow(condIndex);
			
			DoubleMatrix1D population = condItems.viewSelection(notNaNProc);
			
			final IProgressMonitor condMonitor = monitor.subtask();
			
			condMonitor.begin("Condition " + condName + "...", numModules);
			
			for (int moduleIndex = 0; moduleIndex < numModules; moduleIndex++) {

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
						CommonResult result = slot.test.processTest(
								condName, condItems,
								moduleName, itemIndices);
						
						resultsMatrix.setCell(moduleIdx, condIdx, result);
					}
				});
				
				condMonitor.worked(1);
			}

			condMonitor.end();
			monitor.worked(1);
		}
		
		ThreadManager.shutdown(monitor);

		/* Multiple test correction */
		
		MTC mtc = MTCFactory.createFromName(analysis.getMtc());

		multipleTestCorrection(
				resultsMatrix, 
				mtc,
				monitor.subtask());

		analysis.setStartTime(startTime);
		analysis.setElapsedTime(new Date().getTime() - startTime.getTime());
		
		analysis.setResultsMatrix(resultsMatrix);
		
		monitor.end();
	}
}
