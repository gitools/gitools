package es.imim.bg.ztools.cli;

import java.io.PrintStream;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import es.imim.bg.progressmonitor.NullProgressMonitor;
import es.imim.bg.ztools.threads.ThreadManager;

public class Main {

	private static final String appName = 
		Main.class.getPackage().getImplementationTitle();
	
	private static final String versionString = 
		Main.class.getPackage().getImplementationVersion();
	
	public static void main(String[] sargs) {
		
		if (sargs.length < 1) {
			System.err.println("The tool you want to run must be specified.");
			System.exit(-1);
		}
		
		String[] delegatedArgs = new String[sargs.length - 1];
		System.arraycopy(sargs, 1, delegatedArgs, 0, sargs.length - 1);
		
		String toolName = sargs[0];
		
		Class<? extends Tool> toolClass = null;
		
		if (toolName.equals("zcalc"))
			//es.imim.bg.ztools.cli.zcalc.Main.main(delegatedArgs);
			toolClass = es.imim.bg.ztools.cli.zcalc.ZcalcTool.class;
		else if (toolName.equals("oncoz"))
			//es.imim.bg.ztools.cli.oncoz.Main.main(delegatedArgs);
			toolClass = es.imim.bg.ztools.cli.oncoz.OncozTool.class;
		else {
			System.err.println("The tool you want to run doesn't exit: " + toolName);
			System.exit(-1);
		}
		
		Tool tool = null;
		try {
			tool = toolClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		BasicArguments args = (BasicArguments) tool;
		
		CmdLineParser parser = new CmdLineParser(args);

        parser.setUsageWidth(80);

        try {
            parser.parseArgument(delegatedArgs);
        } 
        catch(CmdLineException e ) {
        	System.err.println(e.getMessage());
            printUsage(System.err, parser, toolName);
            System.exit(-1);
        }
        
        if (args.version)
        	printVersion();
        
        if (args.help) {
        	printUsage(System.out, parser, toolName);
        	System.exit(0);
        }
        
		ThreadManager.setNumThreads(args.maxProcs);
		
		int code = 0;
		try {
			code = tool.run(args);
		} 
		catch (RequiredArgumentException e) {
			System.err.println(e.getMessage());
			printUsage(System.err, parser, toolName);
			System.exit(-1);
		} 
		catch (ToolException e) {
			if (args != null && args.debug)
				e.printStackTrace();
			else
				System.err.println("ERROR: " + e.toString());
			
            System.exit(-1);
		}
		
		ThreadManager.shutdown(new NullProgressMonitor());
		
		System.out.println();
		System.exit(code);
	}
	
	private static void printVersion() {
		System.out.println(appName + " version " + versionString);
		System.out.println("Written by Christian Perez-Llamas <christian.perez@upf.edu>");
	}

	private static void printUsage(PrintStream out, CmdLineParser parser, String toolName) {
        System.err.println("Usage: " + toolName + " [options]");
        parser.printUsage(System.err);
        System.err.println();
	}
}
