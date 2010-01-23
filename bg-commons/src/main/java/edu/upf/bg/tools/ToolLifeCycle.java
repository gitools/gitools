package edu.upf.bg.tools;

import edu.upf.bg.tools.exception.ToolException;

/**
 * Interface implemented by each tool.
 */
public interface ToolLifeCycle {
	
	/**
	 * Initialize tool.
	 * 
	 * @throws ToolException
	 */
	void initialize() throws ToolException;
	
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
}
