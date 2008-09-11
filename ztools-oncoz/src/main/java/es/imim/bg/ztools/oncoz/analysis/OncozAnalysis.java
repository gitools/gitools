package es.imim.bg.ztools.oncoz.analysis;

import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;

import cern.colt.function.DoubleProcedure;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.ObjectFactory2D;
import cern.colt.matrix.ObjectMatrix2D;
import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.test.Test;
import es.imim.bg.ztools.test.factory.TestFactory;
import es.imim.bg.ztools.threads.ThreadManager;

public class OncozAnalysis extends Analysis {

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
	
	// Analysis input
	protected String[] condNames;
	protected String[] itemNames;
	
	protected DoubleMatrix2D data;
	
	protected String[] groupNames;
	protected int[][] groupItemIndices;
	
	// Analysis test factory
	protected TestFactory testFactory;
	
	// Analysis output
	protected String[] resultNames;
	protected ObjectMatrix2D results;
	
	public OncozAnalysis(
			String analysisName, 
			String[] condNames,
			String[] itemNames,			
			DoubleMatrix2D data,
			String[] groupNames,			
			int[][] groupItemIndices,
			TestFactory testFactory) {
		
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
		
		final int numCond = data.columns();
		final int numItems = data.rows();
		final int numGroups = groupNames.length;
		
		monitor.begin("Oncoz analysis...", numItems * numGroups);
	
		resultNames = testFactory.create().getResultNames();
		results = ObjectFactory2D.dense.make(numItems, numGroups);
		
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
		
		for (int groupIndex = 0; groupIndex < numGroups; groupIndex++) {
		
			final int groupIdx = groupIndex;
			
			final String groupName = groupNames[groupIndex];
			
			final int[] itemIndices = groupItemIndices[groupIndex];
			
			final ProgressMonitor condMonitor = monitor.subtask();
			
			condMonitor.begin("Group " + groupName + "...", numItems);
			
			final DoubleMatrix1D population = data 
				.viewSelection(null, itemIndices)
				.like1D(numCond * numItems)
				.viewSelection(notNaNProc);
			
			for (int itemIndex = 0; itemIndex < numItems; itemIndex++) {

				final int itemIdx = itemIndex;
				
				final String itemName = itemNames[itemIndex];
				
				final DoubleMatrix1D itemValues = data.viewRow(itemIndex);
				
				final RunSlot slot = takeSlot(monitor, queue);
				
				if (slot.population != population) {
					slot.population = population;
					slot.test = testFactory.create();
					slot.test.processPopulation(groupName, population);
				}

				executor.execute(new Runnable() {
					public void run() {
						results.setQuick(itemIdx, groupIdx, 
							slot.test.processTest(
								groupName, itemValues,
								itemName, itemIndices)); //FIX: It will be fixed, itemValues has to be filtered for the group and item indices related to all population.
						
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

	public TestFactory getMethodFactory() {
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
