package org.gitools.cli.analysis;

import org.kohsuke.args4j.Option;

import edu.upf.bg.progressmonitor.NullProgressMonitor;
import edu.upf.bg.progressmonitor.IProgressMonitor;
import edu.upf.bg.progressmonitor.StreamProgressMonitor;

import org.gitools.cli.CliTool;
import org.gitools.cli.CliToolException;
import org.gitools.cli.InvalidArgumentException;
import org.gitools.cli.RequiredArgumentException;
import org.gitools.analysis.htest.oncozet.OncozCommand;

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
        		dataFile, binaryCutoffParser, 
        		groupsFile, minSetSize, maxSetSize,
        		workdir, outputFormat, true);
        
        IProgressMonitor monitor = !args.quiet ? 
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
