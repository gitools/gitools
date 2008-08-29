package es.imim.bg.ztools.oncoz.analysis;

import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.ObjectFactory1D;
import cern.colt.matrix.ObjectMatrix1D;
import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.analysis.Analysis;
import es.imim.bg.ztools.threads.ThreadManager;
import es.imim.bg.ztools.zcalc.test.ZCalcTest;
import es.imim.bg.ztools.zcalc.test.factory.ZCalcTestFactory;

public class OncozAnalysis extends Analysis {

	private class RunSlot {
		public ZCalcTest test;
		public RunSlot() {
			test = null;
		}
	}
	
	// Analysis input
	protected String[] condNames;
	protected String[] itemNames;
	
	protected DoubleMatrix2D data;
	
	// Analysis test factory
	protected ZCalcTestFactory testFactory;
	
	// Analysis output
	protected String[] resultNames;
	protected ObjectMatrix1D results;
	
	public OncozAnalysis(
			String analysisName, 
			String[] condNames,
			String[] itemNames,			
			DoubleMatrix2D data,
			ZCalcTestFactory methodFactory) {
		
		this.name = analysisName;
		this.condNames = condNames;
		this.itemNames = itemNames;
		
		this.data = data;
		
		this.testFactory = methodFactory;
	}
	
	public void run(ProgressMonitor monitor) throws InterruptedException {
		
		startTime = new Date();
		
		final int numCond = data.columns();
		final int numItems = data.rows();
		
		monitor.begin("Oncoz analysis...", numItems);
	
		final DoubleMatrix1D population = data.like1D(numCond * numItems);
		
		resultNames = testFactory.create().getResultNames();
		results = ObjectFactory1D.dense.make(numItems);
		
		int numProcs = ThreadManager.getNumThreads();
		final ExecutorService executor = ThreadManager.getExecutor();
		
		final ArrayBlockingQueue<RunSlot> queue = 
			new ArrayBlockingQueue<RunSlot>(numProcs);
		
		for (int i = 0; i < numProcs; i++)
			try {
				RunSlot slot = new RunSlot();
				slot.test = testFactory.create();
				slot.test.processPopulation("", population);
				queue.put(slot);
			} catch (InterruptedException e) {
				monitor.debug("InterruptedException while initializing run queue: " + e.getLocalizedMessage());
			}
		
		/* Test analysis */
		
		for (int itemIndex = 0; itemIndex < numItems; itemIndex++) {
			
			final String itemName = itemNames[itemIndex];
			
			final DoubleMatrix1D itemValues = data.viewRow(itemIndex);
			
			RunSlot slot = null;
			try {
				slot = queue.take();
			} catch (InterruptedException e) {
				monitor.debug("InterruptedException while retrieving a free slot from the run queue: " + e.getLocalizedMessage());
				throw e;
			}
			
			final int itemIdx = itemIndex;
			final RunSlot runSlot = slot;
			
			executor.execute(new Runnable() {
				public void run() {
					results.setQuick(itemIdx, 
						runSlot.test.processTest(
							"", itemValues,
							itemName, null));
					
					queue.offer(runSlot);
				}
			});
			
			monitor.worked(1);
		}
		
		/* Multiple test correction */
		
		// TODO
		
		ThreadManager.shutdown(monitor);
		
		elapsedTime = new Date().getTime() - startTime.getTime();
		
		monitor.end();
	}

	// Getters

	public ZCalcTestFactory getMethodFactory() {
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
	
	public String[] getResultNames() {
		return resultNames;
	}
	
	public ObjectMatrix1D getResults() {
		return results;
	}
}
