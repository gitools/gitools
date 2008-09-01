package es.imim.bg.ztools.zcalc.analysis;

import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;

import cern.colt.function.DoubleProcedure;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.ObjectFactory2D;
import cern.colt.matrix.ObjectMatrix2D;

import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.analysis.Analysis;
import es.imim.bg.ztools.test.ZCalcTest;
import es.imim.bg.ztools.test.factory.ZCalcTestFactory;
import es.imim.bg.ztools.threads.ThreadManager;

/* Notes:
 * 'cond' is an abbreviation for condition.
 */

public class ZCalcAnalysis extends Analysis {
	
	protected static final DoubleProcedure notNaNProc = 
		new DoubleProcedure() {
			public boolean apply(double element) {
				return !Double.isNaN(element);
			}
		};
		
	private class RunSlot {
		public DoubleMatrix1D population;
		public ZCalcTest test;
		public RunSlot() {
			population = null;
			test = null;
		}
	}
	
	// Analysis input
	protected String[] condNames;
	protected String[] itemNames;
	
	protected DoubleMatrix2D data;
	
	protected String[] groupNames;
	protected int[][] groupItemIndices;
	
	// Analysis test factory
	protected ZCalcTestFactory testFactory;
	
	// Analysis output
	protected String[] resultNames;
	protected ObjectMatrix2D results;
	
	public ZCalcAnalysis(
			String analysisName, 
			String[] condNames,
			String[] itemNames,			
			DoubleMatrix2D data,
			String[] groupNames,			
			int[][] groupItemIndices,
			ZCalcTestFactory testFactory
			) {
		
		this.name = analysisName;
		this.condNames = condNames;
		this.itemNames = itemNames;
		
		this.data = data;
		
		this.groupNames = groupNames;
		this.groupItemIndices = groupItemIndices;
		
		this.testFactory = testFactory;
	}
	
	public void run(ProgressMonitor monitor) throws InterruptedException {
		
		startTime = new Date();
		
		// transpose data
		monitor.debug("Transposing data...");
		DoubleMatrix2D data = this.data.viewDice().copy();
		
		final int numConditions = data.rows();
		final int numGroups = groupNames.length;
		
		monitor.begin("ZCalc analysis...", numConditions * numGroups);
		
		resultNames = testFactory.create().getResultNames();
		results = ObjectFactory2D.dense.make(numGroups, numConditions);
		
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
			
			condMonitor.begin("Condition " + condName + "...", numGroups);
			
			for (int groupIndex = 0; groupIndex < numGroups; groupIndex++) {

				final int condIdx = condIndex;
				final int groupIdx = groupIndex;
				
				final String groupName = groupNames[groupIdx];
				final int[] itemIndices = groupItemIndices[groupIdx];
					
				final RunSlot slot = takeSlot(monitor, queue);
				
				if (slot.population != population) {
					slot.population = population;
					slot.test = testFactory.create();
					slot.test.processPopulation(condName, population);
				}

				executor.execute(new Runnable() {
					public void run() {
						results.setQuick(groupIdx, condIdx, 
							slot.test.processTest(
								condName, condItems,
								groupName, itemIndices));
						
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
		
		elapsedTime = new Date().getTime() - startTime.getTime();
		
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
	
	// Getters

	public ZCalcTestFactory getTestFactory() {
		return testFactory;
	}

	public String[] getCondNames() {
		return condNames;
	}
	
	public String[] getItemNames() {
		return itemNames;
	}
	
	public DoubleMatrix2D getData() {
		return data;
	}
	
	public String[] getGroupNames() {
		return groupNames;
	}
	
	public int[][] getGroupItemIndices() {
		return groupItemIndices;
	}
	
	public String[] getResultNames() {
		return resultNames;
	}
	
	public ObjectMatrix2D getResults() {
		return results;
	}

}
