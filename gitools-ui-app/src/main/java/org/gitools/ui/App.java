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

package org.gitools.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileEntryParser;
import org.gitools.obo.OBOEvent;
import org.gitools.obo.OBOEventTypes;
import org.gitools.obo.OBOStreamReader;

public class App implements FTPFileEntryParser, OBOEventTypes {

	public static class Entity {
		private String name;
		public Entity(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
	}

	public static void main(String[] args) {
		try {
			URL url = new URL("ftp://ftp.geneontology.org/pub/go/ontology/gene_ontology.obo");
			FTPClient ftp = new FTPClient();
			ftp.connect("ftp.geneontology.org");
			ftp.login("anonymous", "");
			ftp.enterLocalPassiveMode();
			InputStream is = ftp.retrieveFileStream("/pub/go/ontology/gene_ontology.obo");
			BufferedReader r = new BufferedReader(new InputStreamReader(is));
			OBOStreamReader oboReader = new OBOStreamReader(r);

			char state = '0';

			OBOEvent evt = oboReader.nextEvent();
			while (evt != null) {
				switch (state) {
					case '0':
						while (evt != null && evt.getType() != STANZA_START)
							evt = oboReader.nextEvent();

						// TODO ...
				}
			}
			r.close();
			ftp.disconnect();
		} catch (IOException ex) {
			Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private static int count = 0;

	@Override
	public FTPFile parseFTPEntry(String string) {
		System.out.println("parseEntry: " + string);
		FTPFile f = new FTPFile();
		f.setName(string);
		return f;
	}

	@Override
	public String readNextEntry(BufferedReader reader) throws IOException {
		String line = null;
		if (count == 0) {
			while ((line = reader.readLine()) != null)
				System.out.println(line);
		}
		return count++ < 10 ? "entry" + count : null;
	}

	@Override
	public List<String> preParse(List<String> list) {
		System.out.println(list.toString());
		return list;
	}
}
