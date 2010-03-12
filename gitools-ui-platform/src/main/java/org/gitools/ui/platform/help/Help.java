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

package org.gitools.ui.platform.help;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public abstract class Help {

	private static final String HELP_MAP = "/help/map.properties";

	private static Help instance;

	protected Properties urlMap = new Properties();

	protected Help(Properties urlMap) {
		this.urlMap = urlMap;
	}

	public static Help getDefault() {
		if (instance == null) {
			Properties urlMap = new Properties();
			URL res = Help.class.getClassLoader().getResource(HELP_MAP);
			try {
				if (res != null) {
					InputStream in = res.openStream();
					urlMap.load(in);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			instance = new DesktopNavigatorHelp(urlMap);
		}
		
		return instance;
	}

	protected URL getHelpUrl(HelpContext context) throws MalformedURLException {
		String id = context.getId();
		String urlStr = urlMap.getProperty(id);
		if (urlStr == null)
			urlStr = id;
		return new URL(urlStr);
	}

	public abstract void showHelp(HelpContext context) throws HelpException;
}
