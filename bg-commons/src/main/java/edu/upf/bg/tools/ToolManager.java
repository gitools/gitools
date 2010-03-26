package edu.upf.bg.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Date;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import edu.upf.bg.tools.args.BaseArguments;
import edu.upf.bg.tools.exception.ToolException;
import edu.upf.bg.tools.exception.ToolValidationException;

public class ToolManager {

	protected ToolSet toolSet;

	protected String appName;
	protected String appVersion;
	
	private int usageWidth;
	
	protected PrintStream outputStream = System.out;
	protected PrintStream errorStream = System.err;
	
	public ToolManager(ToolSet toolSet, String appName, String appVersion) {
		this.toolSet = toolSet;
		this.appName = appName;
		this.appVersion = appVersion;
		this.usageWidth = 120;
	}
	
	public ToolSet getToolSet() {
		return toolSet;
	}
	
	public void setToolSet(ToolSet toolSet) {
		this.toolSet = toolSet;
	}
	
	public String getAppName() {
		return appName;
	}
	
	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	public String getAppVersion() {
		return appVersion;
	}
	
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	
	public int getUsageWidth() {
		return usageWidth;
	}
	
	public void setUsageWidth(int usageWidth) {
		this.usageWidth = usageWidth;
	}
	
	public PrintStream getOutputStream() {
		return outputStream;
	}
	
	public void setOutputStream(PrintStream outputStream) {
		this.outputStream = outputStream;
	}
	
	public PrintStream getErrorStream() {
		return errorStream;
	}
	
	public void setErrorStream(PrintStream errorStream) {
		this.errorStream = errorStream;
	}
	
	/** Launches a tool from the raw java arguments.*/
	public int launch(String[] args) throws ToolException {
		if (args.length < 1) {
			printUsage(errorStream);
			return -1;
		}
		
		final String toolName = args[0];
		
		if (toolName.equalsIgnoreCase("list")) {
			printAvailableTools(outputStream, toolSet);
			return 0;
		}
		else if (toolName.equalsIgnoreCase("help")) {
			if (args.length < 2) {
				printUsage(outputStream);
				return 0;
			}
			
			printToolUsage(args[1], appName);
			return 0;
		}
		
		final String[] toolArgs = new String[args.length - 1];
		System.arraycopy(args, 1, toolArgs, 0, toolArgs.length);
		
		return launch(toolName, toolArgs);
	}
	
	/** Launches a tool from its name and the arguments passed to it */
	public int launch(String name, String[] args) 
			throws ToolException {
		
		// look for the tool
		
		ToolDescriptor toolDesc = getToolDescriptorFromName(name);
		if (toolDesc == null)
			throw new ToolException("There isn't any tool named '" + name + "'");
		
		ToolLifeCycle tool;
		try {
			final Class<? extends ToolLifeCycle> lifeCycleClass = 
				toolDesc.getLifeCycleClass();
			
			if (lifeCycleClass == null)
				throw new ToolException("LifeCycle class not defined.");
			
			tool = lifeCycleClass.newInstance();
		} catch (Exception e) {
			throw new ToolException(e);
		}
		
		// initialize
		
		tool.initialize();
		
		// prepare arguments
		
		final Class<?> argsClass = toolDesc.getArgumentsClass();
		if (argsClass == null)
			throw new ToolException("Tool '" + name + "' doesn't define an arguments class");
		
		Object argsObject;
		try {
			argsObject = argsClass.newInstance();
		} catch (Exception e) {
			throw new ToolException(e);
		}
		
		// parse arguments
		
		CmdLineParser parser = new CmdLineParser(argsObject);
        parser.setUsageWidth(usageWidth);

        try {
            parser.parseArgument(args);
        }
        catch (CmdLineException e ) {
        	
        	logExceptionError(toolDesc, args, e, errorStream, null);
        	
			System.exit(-1);
        }
        
        String logName = null;
        
        // look for known basic arguments
        
        if (argsObject instanceof BaseArguments) {
        	final BaseArguments bargs = (BaseArguments) argsObject;
        	if (bargs.help) {
        		tool.printUsage(outputStream, appName, toolDesc, parser);
    			System.exit(0);
        	}
        	if (bargs.errorLog != null)
        		logName = bargs.errorLog;
        }
        
        try {
        	tool.validate(argsObject);
        }
        /*catch (ToolUsageException e) {
			usage(name, appName);
			System.exit(0);
		}*/
        catch (ToolValidationException e) {
			errorStream.println(e.getLocalizedMessage());
			return -1;
		}
        
        // run tool
        
        int code = 0;
        
        try {
        	tool.run(argsObject);
        }
        catch (ToolException e) {
			code = -1;

			logExceptionError(toolDesc, args, e, errorStream, logName);
		}
        finally {
        	// uninitialize
        	
        	try {
        		tool.uninitialize();
        	}
        	catch (Exception e) {
        		// Nothing
			}
        }
        
        return code;
	}

	private void logExceptionError(
			ToolDescriptor toolDesc,
			String[] args,
			Throwable e, 
			PrintStream err,
			String logName) {
		
		err.println("ERROR " + getErrorLine(e));
		
		Throwable cause = e.getCause();
		while (cause != null) {
			err.println("  Caused by " + getErrorLine(cause));
			cause = cause.getCause();
		}
		
		try {
			File file = null;
			Writer writer = null;
			if (logName == null) {
				file = File.createTempFile(
						"intogen-error-report-" + toolDesc.getName() + "-", ".txt");
				writer = new BufferedWriter(new FileWriter(file));
			}
			else {
				if (logName.equals("-"))
					writer = new OutputStreamWriter(errorStream);
				else {
					file = new File(logName);
					writer = new FileWriter(file);
				}
			}
			
			PrintWriter pw = new PrintWriter(writer);
			pw.println("Error executing tool '" + toolDesc.getName() + "':");
			pw.print("\n  Arguments:");
			for (String arg : args)
				if (arg.indexOf(' ') != -1)
					pw.print(" \"" + arg + "\"");
				else
					pw.print(" " + arg);
			pw.println();
			pw.println("  User: " + System.getProperty("user.name", "unknown"));
			pw.println("  User dir: " + System.getProperty("user.dir", "unknown"));
			pw.println("  Java version: " + System.getProperty("java.version", "unknown"));
			pw.println("  Java HOME: " + System.getProperty("java.home", "unknown"));
			pw.println("  Java CLASSPATH: " + System.getProperty("java.class.path", "unknown"));
			pw.println("  Date/Time: " + new Date());
			pw.println();
			e.printStackTrace(pw);
			pw.close();
			
			if (file != null) {
				err.println("\nAn error report has been generated on:");
				err.println("\t" + file.getAbsolutePath());
			}
		} catch (Exception e1) {
		}
	}

	private String getErrorLine(Throwable t) {
		final StringBuilder sb = new StringBuilder();
		sb.append(t.getClass().getSimpleName());
		final String msg = t.getMessage();
		if (msg != null)
			sb.append(": " + msg);
		return sb.toString();
	}

	public void printToolUsage(String name, String appName) throws ToolException {

		ToolDescriptor toolDesc = getToolDescriptorFromName(name);
		if (toolDesc == null)
			throw new ToolException("There isn't any tool named '" + name + "'");

		ToolLifeCycle tool;
		try {
			tool = (ToolLifeCycle) toolDesc.getLifeCycleClass().newInstance();
		} catch (Exception e) {
			throw new ToolException(e);
		}

		tool.initialize();

		final Class<?> argsClass = toolDesc.getArgumentsClass();
		if (argsClass == null)
			throw new ToolException("Tool '" + name + "' doesn't define an arguments class");

		Object argsObject;
		try {
			argsObject = argsClass.newInstance();
		} catch (Exception e) {
			throw new ToolException(e);
		}

		CmdLineParser parser = new CmdLineParser(argsObject);
        parser.setUsageWidth(usageWidth);

		tool.printUsage(outputStream, appName, toolDesc, parser);
	}

	private void printUsage(PrintStream out) {
		out.println(appName + " version " + appVersion);
		out.println("\nUsage: " + appName + " <tool-name> <tool-arguments>\n");
		out.println("To get the list of availables tools write:");
		out.println("\n" + appName + " list");
		out.println("\nTo get help for a tool write:");
		out.println("\n" + appName + " help <tool-name>");
		out.println();
	}

	private void printAvailableTools(PrintStream out, ToolSet toolSet) {
		out.println("The list of available tools:\n");
		for (ToolDescriptor toolDesc : toolSet.getToolDescriptors()) {
			final String name = toolDesc.getName();
			out.print(name);
			for (int i = 0; i < 30 - name.length(); i++)
				out.print('.');
			out.println(toolDesc.getDescription());
		}
	}
	
	private ToolDescriptor getToolDescriptorFromName(String name) {
		for (ToolDescriptor desc : toolSet.getToolDescriptors())
			if (desc.getName().equalsIgnoreCase(name))
				return desc;
		return null;
	}
}
