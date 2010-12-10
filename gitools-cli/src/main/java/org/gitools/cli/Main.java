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

package org.gitools.cli;

import edu.upf.bg.tools.exception.ToolException;
import java.io.PrintStream;

import org.kohsuke.args4j.CmdLineParser;

import edu.upf.bg.tools.ToolManager;
import edu.upf.bg.tools.ToolSet;
import edu.upf.bg.tools.XmlToolSetResource;
import java.io.InputStreamReader;
import org.gitools.persistence.PersistenceInitialization;

public class Main {

	private static final String appName = 
		Main.class.getPackage().getImplementationTitle();
	
	private static final String versionString = 
		Main.class.getPackage().getImplementationVersion();
	
	public static void main(String[] args) throws ToolException {

		// Initialize file formats
		PersistenceInitialization.registerFormats();
		
		final ToolSet toolSet = XmlToolSetResource.load(
				new InputStreamReader(Main.class.getClassLoader()
					.getResourceAsStream("gitools-cli.xml")));

		final ToolManager toolManager = new ToolManager(toolSet, appName, versionString);
		int code = toolManager.launch(args);
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
