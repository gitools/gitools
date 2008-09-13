package es.imim.bg.ztools.oncoz;

import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;

import cern.colt.function.DoubleProcedure;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.model.Results;
import es.imim.bg.ztools.test.Test;
import es.imim.bg.ztools.test.factory.TestFactory;
import es.imim.bg.ztools.test.results.Result;
import es.imim.bg.ztools.threads.ThreadManager;

public class OncozAnalysis {

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
	
	public OncozAnalysis(Analysis analysis) {
		
		this.analysis = analysis;
	}
	
	public void run(ProgressMonitor monitor) throws InterruptedException {
		
		Date startTime = new Date();
	
		TestFactory testFactory = analysis.getTestFactory();
		String[] paramNames = testFactory.create().getResultNames();
		final int numParams = paramNames.length;
		
		String[] itemNames = analysis.getData().getRowNames();
		DoubleMatrix2D data = analysis.getData().getData();
		
		String[] moduleNames = analysis.getModules().getModuleNames();
		int[][] moduleItemIndices = analysis.getModules().getItemIndices();
		
		final int numColumns = data.columns();
		final int numItems = data.rows();
		final int numModules = moduleNames.length;
		
		monitor.begin("Oncoz analysis...", numItems * numModules);
	
		final Results results = new Results();
		results.setColNames(moduleNames);
		results.setRowNames(itemNames);
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
		
		for (int moduleIndex = 0; moduleIndex < numModules; moduleIndex++) {
		
			final int moduleIdx = moduleIndex;
			
			final String moduleName = moduleNames[moduleIndex];
			
			final int[] itemIndices = moduleItemIndices[moduleIndex];
			
			final ProgressMonitor condMonitor = monitor.subtask();
			
			condMonitor.begin("Module " + moduleName + "...", numItems);
			
			final DoubleMatrix1D population = data 
				.viewSelection(null, itemIndices)
				.like1D(numColumns * numItems) //FIXME numColumns ?
				.viewSelection(notNaNProc);
			
			for (int itemIndex = 0; itemIndex < numItems; itemIndex++) {

				final int itemIdx = itemIndex;
				
				final String itemName = itemNames[itemIndex];
				
				final DoubleMatrix1D itemValues = data.viewRow(itemIndex);
				
				final RunSlot slot = takeSlot(monitor, queue);
				
				if (slot.population != population) {
					slot.population = population;
					slot.test = testFactory.create();
					slot.test.processPopulation(moduleName, population);
				}

				executor.execute(new Runnable() {
					public void run() {
						//FIXME: It will be fixed, itemValues has to be filtered 
						//for the group and item indices related to all population.
						
						Result result = slot.test.processTest(
								moduleName, itemValues,
								itemName, itemIndices);
						
						double[] values = result.getValues();
						
						for (int paramIdx = 0; paramIdx < numParams; paramIdx++)
							results.setDataValue(moduleIdx, itemIdx, paramIdx, values[paramIdx]);
						
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
