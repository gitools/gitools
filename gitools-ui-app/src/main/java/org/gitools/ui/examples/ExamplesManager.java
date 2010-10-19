/*
 *  Copyright 2010 cperez.
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

package org.gitools.ui.examples;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.io.File;
import java.net.URL;
import java.security.CodeSource;

public class ExamplesManager {

	private static ExamplesManager instance;

	public static ExamplesManager getDefault() {
		if (instance == null)
			instance = new ExamplesManager();

		return instance;
	}

	private ExamplesManager() {
	}

	public File resolvePath(String exampleId, IProgressMonitor monitor) {
		File file = null;

		CodeSource sc = getClass().getProtectionDomain().getCodeSource();
		if (sc == null)
			return null; // TODO download

		URL loc = sc.getLocation();
		if (!loc.getProtocol().equals("file"))
			return null; // TODO download

		String locPath = loc.getPath();
		if (locPath.endsWith(".jar")) {
			int pos = locPath.lastIndexOf(File.separatorChar);
			if (pos >= 0)
				locPath = locPath.substring(0, pos);
		}
		
		System.out.println("locPath = " + locPath);

		String[] paths = new String[] {
			"examples",
			"../examples",
			"../../examples",
			"../../../examples"
		};

		for (String path : paths) {
			file = new File(locPath, path);
			System.out.println(file.getAbsoluteFile());
			
			if (file.exists()) {
				file = new File(file, exampleId);
				if (!file.exists())
					continue;

				try {
					file = file.getCanonicalFile();
					return file;
				}
				catch (Exception ex) {
					file = null;
				}
			}
		}

		return null;
	}
}
