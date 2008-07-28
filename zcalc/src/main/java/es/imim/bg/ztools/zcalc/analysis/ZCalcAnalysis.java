package es.imim.bg.ztools.zcalc.analysis;

import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.ObjectFactory2D;
import cern.colt.matrix.ObjectMatrix2D;

import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.threads.ThreadManager;
import es.imim.bg.ztools.zcalc.method.ZCalcMethod;
import es.imim.bg.ztools.zcalc.method.factory.ZCalcMethodFactory;

public class ZCalcAnalysis {
	
	private class RunSlot {
		public int propIndex;
		public ZCalcMethod method;
		public RunSlot() {
			propIndex = -1;
			method = null;
		}
	}

	private String name;
	
	// Analysis input
	protected String[] propNames;
	protected String[] itemNames;
	
	protected DoubleMatrix2D data;
	
	protected String[] groupNames;
	protected int[][] groupItemIndices;
	
	// Analysis method factory
	protected ZCalcMethodFactory methodFactory;
	
	// Analysis output
	protected String[] resultNames;
	protected ObjectMatrix2D results;
	
	protected Date startTime;
	protected long elapsedTime;
	
	public ZCalcAnalysis(
			String analysisName, 
			String[] propNames,
			String[] itemNames,			
			DoubleMatrix2D data,
			String[] groupNames,			
			int[][] groupItemIndices,
			ZCalcMethodFactory methodFactory
			) {
		
		this.name = analysisName;
		this.propNames = propNames;
		this.itemNames = itemNames;
		
		this.data = data;
		
		this.groupNames = groupNames;
		this.groupItemIndices = groupItemIndices;
		
		this.methodFactory = methodFactory;
	}
	
	public void run(ProgressMonitor monitor) throws InterruptedException {
		
		/*final int maxProcs = ThreadManager.getNumThreads();
		final Semaphore sem = new Semaphore(maxProcs);
		final ExecutorService executor = Executors.newCachedThreadPool();*/
		
		startTime = new Date();
		
		final int numProperties = data.rows();
		final int numGroups = groupNames.length;
		
		monitor.begin("ZCalc analysis...", numProperties * numGroups);
	
		resultNames = methodFactory.create().getResultNames();
		results = ObjectFactory2D.dense.make(numGroups, numProperties);
		
		int numProcs = ThreadManager.getNumThreads();
		//final ExecutorService executor = ThreadManager.getExecutor();
		final ExecutorService executor = Executors.newCachedThreadPool();
		
		final ArrayBlockingQueue<RunSlot> queue = 
			new ArrayBlockingQueue<RunSlot>(numProcs);
		
		for (int i = 0; i < numProcs; i++)
			try {
				queue.put(new RunSlot());
			} catch (InterruptedException e) {
				monitor.debug("InterruptedException while initializing run queue: " + e.getLocalizedMessage());
			}
			
		for (int propIndex = 0; propIndex < numProperties; propIndex++) {
			
			final String propName = propNames[propIndex];
			
			final DoubleMatrix1D propItems = data.viewRow(propIndex);
			
			final ProgressMonitor propMonitor = monitor.subtask();
			
			propMonitor.begin("Property " + propName + "...", numGroups);
			
			for (int groupIndex = 0; groupIndex < numGroups; groupIndex++) {

				final int propIdx = propIndex;
				final int groupIdx = groupIndex;
				
				final String groupName = groupNames[groupIdx];
				final int[] itemIndices = groupItemIndices[groupIdx];

				RunSlot slot = null;
				try {
					slot = queue.take();
				} catch (InterruptedException e) {
					monitor.debug("InterruptedException while retrieving a free slot from the run queue: " + e.getLocalizedMessage());
					throw e;
				}
				
				if (slot.propIndex != propIndex) {
					slot.propIndex = propIndex;
					slot.method = methodFactory.create();
					slot.method.startCondition(propName, propItems);
				}

				final RunSlot runSlot = slot;
				
				executor.execute(new Runnable() {
					public void run() {
						results.setQuick(groupIdx, propIdx, 
							runSlot.method.processGroup(
								propName, propItems,
								groupName, itemIndices));
						
						queue.offer(runSlot);
					}
				});
				
				propMonitor.worked(1);
				monitor.worked(1);
			}

			propMonitor.end();
			monitor.worked(1);
		}
		
		//ThreadManager.shutdown(); //FIXME
		
		executor.shutdown();
		while (!executor.isTerminated())
			executor.awaitTermination(1, TimeUnit.SECONDS);
		
		elapsedTime = new Date().getTime() - startTime.getTime();
		
		monitor.end();
	}

	// Getters
	
	public String getName() {
		return name;
	}

	/*public ZCalcMethod getMethod() {
		return method;
	}*/
	
	public ZCalcMethodFactory getMethodFactory() {
		return methodFactory;
	}

	public String[] getPropNames() {
		return propNames;
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

	public Date getStartTime() {
		return startTime;
	}

	public long getElapsedTime() {
		return elapsedTime;
	}

}
