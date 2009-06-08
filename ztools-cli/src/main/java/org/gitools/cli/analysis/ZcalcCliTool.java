package org.gitools.cli.analysis;

import org.gitools.cli.CliTool;
import org.gitools.cli.CliToolException;
import org.gitools.cli.InvalidArgumentException;
import org.gitools.cli.RequiredArgumentException;
import org.kohsuke.args4j.Option;

import edu.upf.bg.progressmonitor.NullProgressMonitor;
import edu.upf.bg.progressmonitor.ProgressMonitor;
import edu.upf.bg.progressmonitor.StreamProgressMonitor;
import org.gitools.commands.ZCalcCommand;

public class ZcalcCliTool extends AnalysisCliTool implements CliTool {

	@Option(name = "-c", aliases = "-class", usage = "File with mappings between items and modules.", metaVar = "<file>")
	public String groupsFile;

	@Option(name = "-min", aliases = "-min-mod-size", usage = "Discard all modules that have\nless items than <min> (default: 20)", metaVar = "<min>")
	private int minModuleSize = 20;

	@Option(name = "-max", aliases = "-max-mod-size", usage = "Discard all modules that have\nmore items than <max> (default: no limit)", metaVar = "<max>")
	private int maxModuleSize = Integer.MAX_VALUE;

	@Option(name = "-omi", aliases = "-only-mapped-items", 
			usage = "Consider only items having a mapping for the background.\n" +
					"(default: all items in data file will be considered)")
	private boolean discardNonMappedItems = false;
	
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
        		dataFile, binaryCutoffFilter,
        		groupsFile, minModuleSize, maxModuleSize,
        		!discardNonMappedItems,
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