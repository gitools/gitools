package edu.upf.bg.tools;

import edu.upf.bg.tools.exception.ToolException;
import java.io.PrintStream;
import org.kohsuke.args4j.CmdLineParser;

/**
 * Interface implemented by each tool.
 */
public interface ToolLifeCycle<Context> {
	
	/**
	 * Initialize tool.
	 * 
	 * @throws ToolException
	 */
	void initialize(Context context) throws ToolException;
	
	/**
	 * Validate arguments. 
	 * If there is any error throw an exception otherwise return.
	 * 
	 * @param argsObject Parsed command line arguments
	 * @throws ToolException
	 */
	void validate(Object argsObject) throws ToolException;
	
	/**
	 * It's the entry point of the tool in the case that
	 * arg4j arguments object is used.
	 * 
	 * @param argsObject Parsed command line arguments
	 * @throws ToolException
	 */
	void run(Object argsObject) throws ToolException;
	
	/**
	 * Undo initialization, if needed.
	 * @throws ToolException
	 */
	void uninitialize() throws ToolException;

	/**
	 * Print tool usage
	 * 
	 * @param appName command line name
	 * @param toolDesc tool description
	 * @param parser command line parser
	 */
	public void printUsage(PrintStream outputStream, String appName, ToolDescriptor toolDesc, CmdLineParser parser);
}
