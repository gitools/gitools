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
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Properties;


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

	public void queryOncomodulesFromPOST(File file,
			URL action,
			Properties properties,
			IProgressMonitor monitor) {

		DataInputStream input;

		try {
			// URL connection channel.
			URLConnection urlConn = action.openConnection();

			urlConn.setDoInput(true); // Let the run-time system (RTS) know that we want input.
			urlConn.setDoOutput(true); // Let the RTS know that we want to do output.
			urlConn.setUseCaches(false); // No caching, we want the real thing.
			urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); // Specify the content type.

			// Send POST output.
			DataOutputStream printout = new DataOutputStream(urlConn.getOutputStream());

			StringBuilder content = new StringBuilder();
			for (Map.Entry entry : properties.entrySet()) {
				content.append(entry.getKey()).append('=');
				content.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
			}

			printout.writeBytes(content.toString());
			printout.flush();
			printout.close();

			// Get response data.
			input = new DataInputStream(urlConn.getInputStream());
			String str;
			while (null != ((str = input.readLine()))) {
				System.out.println(str);
			}
			input.close();
		}
		catch (IOException ex) {
			
		}

	}
}
