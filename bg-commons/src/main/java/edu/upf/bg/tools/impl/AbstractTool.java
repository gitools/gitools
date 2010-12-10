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

package edu.upf.bg.tools.impl;

import edu.upf.bg.tools.ToolDescriptor;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.upf.bg.tools.ToolLifeCycle;
import edu.upf.bg.tools.args.BaseArguments;
import edu.upf.bg.tools.exception.ToolException;
import edu.upf.bg.tools.exception.ToolUsageException;
import edu.upf.bg.tools.exception.ToolValidationException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.kohsuke.args4j.CmdLineParser;

public abstract class AbstractTool<Context> implements ToolLifeCycle<Context> {

	private Context context;

	public Context getContext() {
		return context;
	}
	
	@Override
	public void initialize(Context context) throws ToolException {
		this.context = context;
	}

	@Override
	public void validate(Object argsObject) throws ToolException {
		if (!(argsObject instanceof BaseArguments))
			return;
		
		BaseArguments args = (BaseArguments) argsObject;
		if (args.loglevel != null) {
			Pattern pat = Pattern.compile("^(.*)=(.*)$");
			
			for (String loglevel : args.loglevel) {
				Matcher mat = pat.matcher(loglevel);
				if (!mat.matches() || mat.groupCount() != 2)
					throw new ToolValidationException("Invalid -loglevel argument: " + loglevel);
				
				final String pkg = mat.group(1);
				final String levelName = mat.group(2);
				if (pkg == null || levelName == null)
					throw new ToolValidationException("Invalid -loglevel package: " + loglevel);

				final Level level = Level.toLevel(levelName);
				if (level == null)
					throw new ToolValidationException("Invalid -loglevel level name: " + loglevel);
				
				Logger.getLogger(pkg).setLevel(level);
			}
		}
		
		if (args.help)
			throw new ToolUsageException();
	}
	
	@Override
	public void run(Object argsObject) throws ToolException {
		/*if (!(argsObject instanceof BaseArguments))
			return;
		
		BaseArguments args = (BaseArguments) argsObject;*/
	}

	@Override
	public void uninitialize() throws ToolException {
	}

	@Override
	public void printUsage(PrintStream outputStream, String appName, ToolDescriptor toolDesc, CmdLineParser parser) {
		outputStream.print(
        		toolDesc.getName() + " usage:\n\t" +
        		appName + " " + toolDesc.getName());

        parser.printSingleLineUsage(outputStream);

        outputStream.println("\n");

        parser.printUsage(outputStream);
		
        outputStream.println();
	}
}
