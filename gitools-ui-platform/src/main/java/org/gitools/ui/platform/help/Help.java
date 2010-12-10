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

package org.gitools.ui.platform.help;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.gitools.ui.platform.PropertiesExpansion;

public abstract class Help {

	private static Help instance;

	public static class UrlMap {
		private Pattern pattern;
		private String url;

		public UrlMap(Pattern pattern, String url) {
			this.pattern = pattern;
			this.url = url;
		}

		public Pattern getPattern() {
			return pattern;
		}

		public String getUrl() {
			return url;
		}
	}

	protected PropertiesExpansion properties;
	protected List<UrlMap> urlMap;

	public Help() {
		this(new Properties(), new ArrayList<UrlMap>());
	}

	protected Help(Properties properties, List<UrlMap> urlMap) {
		this.properties = new PropertiesExpansion(properties);
		this.urlMap = urlMap;
	}

	public static Help getDefault() {
		if (instance == null)
			instance = new DesktopNavigatorHelp();
		
		return instance;
	}

	public void loadProperties(InputStream in) throws IOException {
		properties.load(in);
		in.close();
	}

	public void loadUrlMap(InputStream in) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line;
		int lineNum = 1;
		while ((line = br.readLine()) != null) {
			line = line.trim();
			if (!line.isEmpty()) {
				String[] fields = line.split("\t");

				if (fields.length == 2)
					urlMap.add(new UrlMap(Pattern.compile(fields[0]), fields[1]));
				else
					throw new Exception("Error reading help url mappings:" +
							" Two columns expected at line " + lineNum);
			}
			lineNum++;
		}
		br.close();
	}

	protected URL getHelpUrl(HelpContext context) throws MalformedURLException {
		String id = context.getId();
		String urlStr = null; // FIXME Use a default url
		for (UrlMap map : urlMap) {
			Matcher matcher = map.getPattern().matcher(id);
			if (matcher.matches()) {
				Properties p = new Properties(properties);
				for (int i = 0; i < matcher.groupCount(); i++)
					p.setProperty("" + i, matcher.group(i));
				urlStr = expandPattern(properties, map.getUrl());
				//System.out.println(map.getPattern().pattern() + " -> " + urlStr + " (" + map.getUrl() + ")");
				break;
			}
		}

		return new URL(urlStr);
	}

	public abstract void showHelp(HelpContext context) throws HelpException;

	private String expandPattern(
			Properties properties,
			String pattern) {

		final StringBuilder output = new StringBuilder();
		final StringBuilder var = new StringBuilder();

		char state = 'C';

		int pos = 0;

		while (pos < pattern.length()) {

			char ch = pattern.charAt(pos++);

			switch (state) {
			case 'C': // copying normal characters
				if (ch == '$')
					state = '$';
				else
					output.append(ch);
				break;

			case '$': // start of variable
				if (ch == '{')
					state = 'V';
				else {
					output.append('$').append(ch);
					state = 'C';
				}
				break;

			case 'V': // reading name of variable
				if (ch == '}')
					state = 'X';
				else
					var.append(ch);
				break;

			case 'X': // expand variable
				output.append(properties.getProperty(var.toString()));
				var.setLength(0);
				pos--;
				state = 'C';
				break;
			}
		}

		switch (state) {
		case '$': output.append('$'); break;
		case 'V': output.append("${").append(var); break;
		case 'X':
			output.append(properties.getProperty(var.toString()));
			break;
		}

		return output.toString();
	}
}
