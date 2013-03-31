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

package org.gitools.utils.tools.args;

import java.util.List;

import org.kohsuke.args4j.Option;

public class BaseArguments {

	@Option(name = "-loglevel", aliases = "-ll", metaVar = "<PACKAGE=LEVEL>",
			usage = "Define the log level for the package.")
	public List<String> loglevel;
	
	@Option(name = "-help", aliases = "-h", usage = "Show available options.")
	public boolean help;
	
	@Option(name = "-err-log",
		usage = "Specify the file where error logs will be saved.\n" +
		"If it is '-' the standard error stream will be used.")
	public String errorLog;
}
