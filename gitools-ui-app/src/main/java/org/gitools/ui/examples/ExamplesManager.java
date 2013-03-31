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

package org.gitools.ui.examples;

import org.gitools.utils.progressmonitor.IProgressMonitor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.CodeSource;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.gitools.ui.settings.Settings;

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
			return resolveDownloadedPath(exampleId, monitor);

		URL loc = sc.getLocation();
		if (!loc.getProtocol().equals("file"))
			return resolveDownloadedPath(exampleId, monitor);

		String locPath = loc.getPath();
		if (locPath.endsWith(".jar")) {
			int pos = locPath.lastIndexOf(File.separatorChar);
			if (pos >= 0)
				locPath = locPath.substring(0, pos);
		}
		
		//System.out.println("locPath = " + locPath);

		String[] paths = new String[] {
			"examples",
			"../examples",
			"../../examples",
			"../../../examples"
		};

		for (String path : paths) {
			file = new File(locPath, path);
			//System.out.println(file.getAbsoluteFile());
			
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

		return resolveDownloadedPath(exampleId, monitor);
	}

	private static final String EXAMPLES_BASE_URL = "http://webstart.gitools.org/default/examples";

	private File resolveDownloadedPath(String exampleId, IProgressMonitor monitor) {
		File dstBasePath = new File(Settings.CONFIG_PATH + File.separator + "examples");
		if (!dstBasePath.exists())
			dstBasePath.mkdirs();

		String srcTimestamp = getRemoteTimestamp(EXAMPLES_BASE_URL, exampleId, monitor);

		String dstTimestamp = "00000000000000";
		File examplePath = new File(dstBasePath, exampleId);
		if (examplePath.exists()) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			dstTimestamp = sdf.format(new Date(examplePath.lastModified()));
		}

		if (srcTimestamp.compareTo(dstTimestamp) > 0)
			downloadExample(dstBasePath, exampleId, monitor);

		return examplePath;
	}

	private String getRemoteTimestamp(String srcBaseUrl, String exampleId, IProgressMonitor monitor) {
		try {
			URL url = new URL(EXAMPLES_BASE_URL + "/" + exampleId + ".timestamp");
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String timestamp = reader.readLine();
			if (timestamp.length() < 14) {
				char[] fill = new char[timestamp.length() - 14];
				Arrays.fill(fill, '0');
				timestamp += new String(fill);
			}
			return timestamp;
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	private void downloadExample(File dstBasePath, String exampleId, IProgressMonitor monitor) {
		try {
			monitor.begin("Downloading ...", 1);

			URL url = new URL(EXAMPLES_BASE_URL + "/" + exampleId + ".zip");

			ZipInputStream zin = new ZipInputStream(url.openStream());

			ZipEntry ze;
			while ((ze = zin.getNextEntry()) != null) {
				IProgressMonitor mon = monitor.subtask();

				//long totalKb = ze.getSize() / 1024;

				String name = ze.getName();

				mon.begin("Extracting " + name + " ...", (int) ze.getSize());

				File outFile = new File(dstBasePath, name);

				if (ze.isDirectory()) {
					if (!outFile.exists()) {
						outFile.mkdirs();
					}
				} else {
					if (!outFile.getParentFile().exists()) {
						outFile.getParentFile().mkdirs();
					}

					OutputStream fout = new FileOutputStream(outFile);

					final int BUFFER_SIZE = 4 * 1024;
					byte[] data = new byte[BUFFER_SIZE];
					int partial = 0;
					int count;
					while ((count = zin.read(data, 0, BUFFER_SIZE)) != -1) {
						fout.write(data, 0, count);
						partial += count;
						mon.info((partial / 1024) + " Kb read");
						mon.worked(count);
					}

					fout.close();
				}

				zin.closeEntry();
				mon.end();
			}

			zin.close();

			monitor.end();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
