package es.imim.bg.ztools.cli.zcalc;

import org.kohsuke.args4j.Option;

import es.imim.bg.progressmonitor.NullProgressMonitor;
import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.progressmonitor.StreamProgressMonitor;
import es.imim.bg.ztools.cli.AnalysisTool;
import es.imim.bg.ztools.cli.RequiredArgumentException;
import es.imim.bg.ztools.cli.Tool;
import es.imim.bg.ztools.cli.ToolException;
import es.imim.bg.ztools.cli.oncoz.OncozTool;
import es.imim.bg.ztools.zcalc.ZCalcCommand;

public class ZcalcTool extends AnalysisTool implements Tool {

	@Option(name = "-c", aliases = "-class", usage = "File with mappings between items and modules.", metaVar = "<file>")
	public String groupsFile;

	@Option(name = "-min", aliases = "-min-mod-size", usage = "Discard all modules that have\nless items than <min> (default: 20)", metaVar = "<min>")
	private int minModuleSize = 20;

	@Option(name = "-max", aliases = "-max-mod-size", usage = "Discard all modules that have\nmore items than <max> (default: no limit)", metaVar = "<max>")
	private int maxModuleSize = Integer.MAX_VALUE;

	@Override
	public int run(Object argsObject) 
			throws RequiredArgumentException, ToolException {
		
		ZcalcTool args = (ZcalcTool) argsObject;
		
		processArgs(args);
		
		if (groupsFile == null)
        	throw new RequiredArgumentException("Groups file has to be specified.");
        
        if (minModuleSize < 1)
        	minModuleSize = 1;
        if (maxModuleSize < minModuleSize)
        	maxModuleSize = minModuleSize;
        
        ZCalcCommand cmd = new ZCalcCommand(
        		analysisName, testName, samplingNumSamples, 
        		dataFile, groupsFile, 
        		minModuleSize, maxModuleSize,
        		workdir, outputFormat, true);
        
        ProgressMonitor monitor = !args.quiet ? 
			new StreamProgressMonitor(System.out, verbose, debug)
			: new NullProgressMonitor();
        
        try {
			cmd.run(monitor);
		} catch (Exception e) {
			throw new ToolException(e);
		}
        
        return 0;
	}
}