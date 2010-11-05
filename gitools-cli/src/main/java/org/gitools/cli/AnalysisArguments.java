/*
 *  Copyright 2010 chris.
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

import java.util.ArrayList;
import java.util.List;
import org.kohsuke.args4j.Option;

public class AnalysisArguments extends GitoolsArguments {

	@Option(name = "-N", aliases = "-name", metaVar = "<name>",
			usage = "Analysis name. A folder with this name will be created\n" +
			"in the workdir. (default: unnamed).")
	public String analysisName = "unnamed";

	@Option(name = "-T", aliases="-title", metaVar = "<title>",
			usage = "Set the analysis title. (default: analysis name)")
	public String analysisTitle;

	@Option(name = "-notes", metaVar = "<notes>",
			usage = "Set analysis description and notes.")
	public String analysisNotes;

	@Option(name = "-A", aliases="-attribute", metaVar = "<name=value>",
			usage = "Define an analysis attribute.")
	public List<String> analysisAttributes = new ArrayList<String>(0);

	@Option(name = "-w", aliases = "-workdir", metaVar = "<dir>",
			usage = "Working directory (default: current dir).")
	public String workdir = System.getProperty("user.dir");
}
