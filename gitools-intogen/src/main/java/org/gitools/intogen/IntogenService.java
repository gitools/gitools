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

package org.gitools.intogen;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.gitools.persistence.PersistenceUtils;


public class IntogenService {

	private static IntogenService service;

	private IntogenService() {
	}

	public static final IntogenService getDefault() {
		if (service == null) {
			service = new IntogenService();
		}

		return service;
	}

	public void queryOncomodulesFromPOST(
			File file,
			String prefix,
			URL action,
			Properties properties,
			IProgressMonitor monitor) throws IntogenServiceException {

		try {
			monitor.begin("Querying for data ...", 1);

			// URL connection channel.
			URLConnection urlConn = action.openConnection();

			urlConn.setDoInput(true); // Let the run-time system (RTS) know that we want input.
			urlConn.setDoOutput(true); // Let the RTS know that we want to do output.
			urlConn.setUseCaches(false); // No caching, we want the real thing.
			urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); // Specify the content type.

			// Send POST output.
			DataOutputStream printout = new DataOutputStream(urlConn.getOutputStream());

			StringBuilder content = new StringBuilder();
			boolean first = true;
			for (Map.Entry entry : properties.entrySet()) {
				if (!first)
					content.append('&');
				first = false;
				
				content.append(entry.getKey()).append('=');
				content.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
			}

			printout.writeBytes(content.toString());
			printout.flush();
			printout.close();

			monitor.end();

			monitor.begin("Downloading data ...", 1);

			// Get response data.
			ZipInputStream zin = new ZipInputStream(urlConn.getInputStream());

			ZipEntry ze;
			while ((ze = zin.getNextEntry()) != null) {
				IProgressMonitor mnt = monitor.subtask();

				String name = ze.getName();
				if (name.equals("modulemap.csv"))
					name = prefix + ".oncomodules.gz";
				else if (name.equals("oncomodules.csv"))
					name = prefix + ".annotations.gz";

				mnt.begin("Extracting " + name + " ...", (int) ze.getSize());

				File outFile = new File(file, name);
				if (!outFile.getParentFile().exists())
					outFile.getParentFile().mkdirs();
				
				OutputStream fout = PersistenceUtils.openOutputStream(outFile);

				final int BUFFER_SIZE = 4 * 1024;
				byte[] data = new byte[BUFFER_SIZE];
				int count;
				while ((count = zin.read(data, 0, BUFFER_SIZE)) != -1) {
					fout.write(data, 0, count);
					mnt.worked(count);
				}
				zin.closeEntry();
				fout.close();

				mnt.end();
			}
			
			zin.close();

			monitor.end();
		}
		catch (IOException ex) {
			throw new IntogenServiceException(ex);
		}
	}
}
