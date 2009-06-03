package es.imim.bg.ztools.cli.analysis;

import org.kohsuke.args4j.Option;

import es.imim.bg.progressmonitor.NullProgressMonitor;
import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.progressmonitor.StreamProgressMonitor;
import es.imim.bg.ztools.cli.InvalidArgumentException;
import es.imim.bg.ztools.cli.RequiredArgumentException;
import es.imim.bg.ztools.cli.CliTool;
import es.imim.bg.ztools.cli.CliToolException;
import es.imim.bg.ztools.commands.OncozCommand;

public class OncozCliTool extends AnalysisCliTool implements CliTool {

	@Option(name = "-c", aliases = "-class", usage = "File with mappings between columns and sets.", metaVar = "<file>")
	public String groupsFile;

	@Option(name = "-min", aliases = "-min-set-size", usage = "Discard all sets that have\nless columns than <min> (default: 20)", metaVar = "<min>")
	private int minSetSize = 20;

	@Option(name = "-max", aliases = "-max-set-size", usage = "Discard all sets that have\nmore columns than <max> (default: no limit)", metaVar = "<max>")
	private int maxSetSize = Integer.MAX_VALUE;

	@Override
	public void validateArguments(Object argsObject) 
			throws RequiredArgumentException, InvalidArgumentException {
		
		super.validateArguments(argsObject);
		
		//OncozTool args = (OncozTool) argsObject;
		
		/*if (groupsFile == null)
        	throw new RequiredArgumentException("Groups file has to be specified.");*/
        
		if (minSetSize < 1)
        	minSetSize = 1;
        if (maxSetSize < minSetSize)
        	maxSetSize = minSetSize;
	}
	
	@Override
	public int run(Object argsObject) 
			throws CliToolException {
		
		OncozCliTool args = (OncozCliTool) argsObject;
        
        OncozCommand cmd = new OncozCommand(
        		analysisName, testName, samplingNumSamples, 
        		dataFile, binaryCutoffFilter, 
        		groupsFile, minSetSize, maxSetSize,
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
