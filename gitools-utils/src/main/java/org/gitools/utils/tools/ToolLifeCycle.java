/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.utils.tools;

import org.gitools.utils.tools.exception.ToolException;
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
