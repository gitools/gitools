package es.imim.bg.ztools.processors;

import java.util.Date;

import cern.colt.function.DoubleProcedure;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.ObjectFactory1D;
import cern.colt.matrix.ObjectMatrix1D;
import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.model.ResultsMatrix;
import es.imim.bg.ztools.stats.mtc.BenjaminiHochbergFdr;
import es.imim.bg.ztools.table.element.bean.BeanElementAdapter;
import es.imim.bg.ztools.table.element.string.StringElementAdapter;
import es.imim.bg.ztools.test.Test;
import es.imim.bg.ztools.test.factory.TestFactory;
import es.imim.bg.ztools.test.results.CommonResult;
import es.imim.bg.ztools.threads.ThreadManager;
import es.imim.bg.ztools.threads.ThreadQueue;
import es.imim.bg.ztools.threads.ThreadSlot;

public class OncozProcessor extends AbstractProcessor {

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
	
	public OncozProcessor(Analysis analysis) {
		
		this.analysis = analysis;
	}
	
	public void run(ProgressMonitor monitor) throws InterruptedException {
		
		Date startTime = new Date();
	
		TestFactory testFactory = 
			TestFactory.createFactory(analysis.getToolConfig());
		
		//String[] paramNames = testFactory.create().getResultNames();
		//final int numParams = paramNames.length;
		
		ObjectMatrix1D items = ObjectFactory1D.dense.make(
				analysis.getDataTable().getRowNames());
		
		DoubleMatrix2D data = analysis.getDataTable().getData();
		
		ObjectMatrix1D modules = ObjectFactory1D.dense.make(
				analysis.getModuleMap().getModuleNames());
		
		int[][] moduleItemIndices = analysis.getModuleMap().getItemIndices();
		
		final int numColumns = data.columns();
		final int numItems = data.rows();
		final int numModules = modules.size();
		
		monitor.begin("oncoz analysis...", numItems * numModules);
	
		Test test = testFactory.create();
		
		final ResultsMatrix resultsMatrix = new ResultsMatrix();
		
		resultsMatrix.setColumns(modules);
		resultsMatrix.setRows(items);
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
		
		for (int moduleIndex = 0; moduleIndex < numModules; moduleIndex++) {
		
			final int moduleIdx = moduleIndex;
			
			final String moduleName = modules.getQuick(moduleIndex).toString();
			
			final int[] itemIndices = moduleItemIndices[moduleIndex];
			
			final ProgressMonitor condMonitor = monitor.subtask();
			
			condMonitor.begin("Module " + moduleName + "...", numItems);
			
			final DoubleMatrix2D pop2d = data 
				.viewSelection(null, itemIndices);
			
			DoubleMatrix1D population = pop2d 
				.like1D(numColumns * numItems);
				//FIXME !!!!
				//.viewSelection(notNaNProc);
			
			int k = 0;
			for (int i = 0; i < pop2d.rows(); i++)
				for (int j = 0; j < pop2d.columns(); j++)
					population.setQuick(k++,
							pop2d.getQuick(i, j));
			
			population = population.viewSelection(notNaNProc); //FIXME
			
			for (int itemIndex = 0; itemIndex < numItems; itemIndex++) {

				final int itemIdx = itemIndex;
				
				final String itemName = items.getQuick(itemIndex).toString();
				
				final DoubleMatrix1D itemValues = data.viewRow(itemIndex);
				
				final RunSlot slot = (RunSlot) threadQueue.take();
				
				if (slot.population != population) {
					slot.population = population;
					slot.test = testFactory.create();
					slot.test.processPopulation(moduleName, population);
				}

				slot.execute(new Runnable() {
					public void run() {
						//FIXME: It will be fixed, itemValues has to be filtered 
						//for the group and item indices related to all population.
						
						CommonResult result = slot.test.processTest(
								moduleName, itemValues,
								itemName, itemIndices);
						
						resultsMatrix.setCell(itemIdx, moduleIdx, result);
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
