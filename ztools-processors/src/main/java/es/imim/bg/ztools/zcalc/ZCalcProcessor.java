package es.imim.bg.ztools.zcalc;

import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;

import cern.colt.function.DoubleProcedure;
import cern.colt.matrix.DoubleFactory3D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.DoubleMatrix3D;
import cern.colt.matrix.ObjectFactory2D;
import cern.colt.matrix.ObjectMatrix2D;

import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.model.Results;
import es.imim.bg.ztools.test.Test;
import es.imim.bg.ztools.test.factory.TestFactory;
import es.imim.bg.ztools.test.results.Result;
import es.imim.bg.ztools.threads.ThreadManager;

/* Notes:
 * 'cond' is an abbreviation for condition.
 */

public class ZCalcProcessor {
	
	protected static final DoubleProcedure notNaNProc = 
		new DoubleProcedure() {
			public boolean apply(double element) {
				return !Double.isNaN(element);
			}
		};
		
	private class RunSlot {
		public DoubleMatrix1D population;
		public Test test;
		public RunSlot() {
			population = null;
			test = null;
		}
	}

	private Analysis analysis;
	
	public ZCalcProcessor(
			/*String analysisName, 
			String[] condNames,
			String[] itemNames,			
			DoubleMatrix2D data,
			String[] groupNames,			
			int[][] groupItemIndices,
			TestFactory testFactory*/
			Analysis analysis
			) {
		
		/*this.name = analysisName;
		this.condNames = condNames;
		this.itemNames = itemNames;
		
		this.data = data;
		
		this.groupNames = groupNames;
		this.groupItemIndices = groupItemIndices;
		
		this.testFactory = testFactory;*/
		
		this.analysis = analysis;
	}
	
	public void run(ProgressMonitor monitor) throws InterruptedException {
		
		Date startTime = new Date();
		
		TestFactory testFactory = analysis.getTestFactory();
		String[] paramNames = testFactory.create().getResultNames();
		final int numParams = paramNames.length;
		
		String[] condNames = analysis.getData().getColNames();
		
		monitor.debug("Transposing data...");
		DoubleMatrix2D data = analysis.getData().getData().viewDice().copy();
		
		String[] moduleNames = analysis.getModules().getModuleNames();
		int[][] moduleItemIndices = analysis.getModules().getItemIndices();
		
		final int numConditions = data.rows();
		final int numModules = moduleNames.length;
		
		monitor.begin("ZCalc analysis...", numConditions * numModules);
		
		final Results results = new Results();
		results.setColNames(condNames);
		results.setRowNames(moduleNames);
		results.setParamNames(paramNames);
		results.createData();
		
		int numProcs = ThreadManager.getNumThreads();
		final ExecutorService executor = ThreadManager.getExecutor();
		
		final ArrayBlockingQueue<RunSlot> queue = 
			new ArrayBlockingQueue<RunSlot>(numProcs);
		
		for (int i = 0; i < numProcs; i++)
			try {
				queue.put(new RunSlot());
			} catch (InterruptedException e) {
				monitor.debug("InterruptedException while initializing run queue: " + e.getLocalizedMessage());
			}

		/* Test analysis */
		
		for (int condIndex = 0; condIndex < numConditions; condIndex++) {
			
			final String condName = condNames[condIndex];
			
			final DoubleMatrix1D condItems = data.viewRow(condIndex);
			
			DoubleMatrix1D population = condItems.viewSelection(notNaNProc);
			
			final ProgressMonitor condMonitor = monitor.subtask();
			
			condMonitor.begin("Condition " + condName + "...", numModules);
			
			for (int moduleIndex = 0; moduleIndex < numModules; moduleIndex++) {

				final int condIdx = condIndex;
				final int moduleIdx = moduleIndex;
				
				final String moduleName = moduleNames[moduleIdx];
				final int[] itemIndices = moduleItemIndices[moduleIdx];
					
				final RunSlot slot = takeSlot(monitor, queue);
				
				if (slot.population != population) {
					slot.population = population;
					slot.test = testFactory.create();
					slot.test.processPopulation(condName, population);
				}

				executor.execute(new Runnable() {
					public void run() { 
						Result result = slot.test.processTest(
								condName, condItems,
								moduleName, itemIndices);
						
						double[] values = result.getValues();
						
						for (int paramIdx = 0; paramIdx < numParams; paramIdx++)
							results.setDataValue(condIdx, moduleIdx, paramIdx, values[paramIdx]);
						
						queue.offer(slot);
					}
				});
				
				condMonitor.worked(1);
				monitor.worked(1);
			}

			condMonitor.end();
		}
		
		/* Multiple test correction */
		
		// TODO
		
		ThreadManager.shutdown(monitor);

		analysis.setStartTime(startTime);
		analysis.setElapsedTime(new Date().getTime() - startTime.getTime());
		
		analysis.setResults(results);
		
		monitor.end();
	}

	private RunSlot takeSlot(
			ProgressMonitor monitor,
			final ArrayBlockingQueue<RunSlot> queue) throws InterruptedException {
		
		RunSlot slot = null;
		try {
			slot = queue.take();
		} catch (InterruptedException e) {
			monitor.debug("InterruptedException while retrieving a free slot from the run queue: " + e.getLocalizedMessage());
			throw e;
		}
		return slot;
	}

}
