package org.gitools.tool.processors;

import java.util.Date;

import org.gitools.model.Analysis;
import org.gitools.model.table.ObjectMatrix;
import org.gitools.model.table.element.basic.StringElementAdapter;
import org.gitools.model.table.element.bean.BeanElementAdapter;
import org.gitools.stats.mtc.BenjaminiHochbergFdr;
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

import edu.upf.bg.progressmonitor.ProgressMonitor;

/* Notes:
 * 'cond' is an abbreviation for condition.
 */

public class ZCalcProcessor extends AbstractProcessor {
	
	protected static final DoubleProcedure notNaNProc = 
		new DoubleProcedure() {
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

	private Analysis analysis;
	
	public ZCalcProcessor(
			Analysis analysis) {
		
		this.analysis = analysis;
	}
	
	public void run(ProgressMonitor monitor) throws InterruptedException {
		
		Date startTime = new Date();
		
		TestFactory testFactory = 
			TestFactory.createFactory(analysis.getToolConfig());
		
		//String[] paramNames = testFactory.create().getResultNames();
		//final int numParams = paramNames.length;
		
		ObjectMatrix1D conditions = ObjectFactory1D.dense.make(
				analysis.getDataTable().getColNames());
		
		monitor.debug("Transposing data...");
		DoubleMatrix2D data = analysis.getDataTable().getData().viewDice().copy();
		
		ObjectMatrix1D modules = ObjectFactory1D.dense.make(
				analysis.getModuleMap().getModuleNames());
		
		int[][] moduleItemIndices = analysis.getModuleMap().getItemIndices();
		
		final int numConditions = data.rows();
		final int numModules = modules.size();
		
		monitor.begin("zetcalc analysis...", numConditions * numModules);
		
		Test test = testFactory.create();
		
		final ObjectMatrix resultsMatrix = new ObjectMatrix();
		
		resultsMatrix.setColumns(conditions);
		resultsMatrix.setRows(modules);
		resultsMatrix.makeData();
		
		resultsMatrix.setRowAdapter(new StringElementAdapter());
		resultsMatrix.setColumnAdapter(new StringElementAdapter());
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
			
			final ProgressMonitor condMonitor = monitor.subtask();
			
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
					public void run() { 
						CommonResult result = slot.test.processTest(
								condName, condItems,
								moduleName, itemIndices);
						
						resultsMatrix.setCell(moduleIdx, condIdx, result);
					}
				});
				
				condMonitor.worked(1);
				monitor.worked(1);
			}

			condMonitor.end();
		}
		
		ThreadManager.shutdown(monitor);
		
		/* Multiple test correction */
		
		multipleTestCorrection(
				resultsMatrix, 
				new BenjaminiHochbergFdr(), //TODO It should be configurable 
				monitor.subtask());

		analysis.setStartTime(startTime);
		analysis.setElapsedTime(new Date().getTime() - startTime.getTime());
		
		analysis.setResults(resultsMatrix);
		
		monitor.end();
	}
}
