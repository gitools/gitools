package es.imim.bg.ztools.cli.analysis;

import org.kohsuke.args4j.Option;

import es.imim.bg.progressmonitor.NullProgressMonitor;
import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.progressmonitor.StreamProgressMonitor;
import es.imim.bg.ztools.cli.InvalidArgumentException;
import es.imim.bg.ztools.cli.RequiredArgumentException;
import es.imim.bg.ztools.cli.CliTool;
import es.imim.bg.ztools.cli.CliToolException;
import es.imim.bg.ztools.commands.ZCalcCommand;

public class ZcalcCliTool extends AnalysisCliTool implements CliTool {

	@Option(name = "-c", aliases = "-class", usage = "File with mappings between items and modules.", metaVar = "<file>")
	public String groupsFile;

	@Option(name = "-min", aliases = "-min-mod-size", usage = "Discard all modules that have\nless items than <min> (default: 20)", metaVar = "<min>")
	private int minModuleSize = 20;

	@Option(name = "-max", aliases = "-max-mod-size", usage = "Discard all modules that have\nmore items than <max> (default: no limit)", metaVar = "<max>")
	private int maxModuleSize = Integer.MAX_VALUE;

	@Override
	public void validateArguments(Object argsObject) 
			throws RequiredArgumentException, InvalidArgumentException {
		
		super.validateArguments(argsObject);
		
		//ZcalcTool args = (ZcalcTool) argsObject;
		
		if (groupsFile == null)
        	throw new RequiredArgumentException("Groups file has to be specified.");
        
        if (minModuleSize < 1)
        	minModuleSize = 1;
        if (maxModuleSize < minModuleSize)
        	maxModuleSize = minModuleSize;
	}
	
	@Override
	public int run(Object argsObject) 
			throws CliToolException {
		
		ZcalcCliTool args = (ZcalcCliTool) argsObject;
        
        ZCalcCommand cmd = new ZCalcCommand(
        		analysisName, testName, samplingNumSamples, 
        		dataFile, binCutoffFilter,
        		groupsFile, minModuleSize, maxModuleSize,
        		workdir, outputFormat, true);
        
        ProgressMonitor monitor = !args.quiet ? 
			new StreamProgressMonitor(System.out, verbose, debug)
			: new NullProgressMonitor();
        
        try {
			cmd.run(monitor);
		} catch (Exception e) {
			throw new CliToolException(e);
		}
        
        return 0;
	}
}