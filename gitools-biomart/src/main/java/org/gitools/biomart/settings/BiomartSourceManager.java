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

package org.gitools.biomart.settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BiomartSourceManager {

	private static Logger logger = LoggerFactory.getLogger(BiomartSourceManager.class);
	
	private static final String userPath = System.getProperty("user.home", ".");
	private static final String configPath = userPath + File.separator + ".gitools";
	private static final String configFileName = "biomart-sources.xml";
	private static final String configFile = configPath + File.separator + configFileName;

	private static BiomartSourceManager instance;
	private static BiomartSources biomartSources = new BiomartSources();

	public static BiomartSourceManager getDefault() {
		if (instance == null) {

			Reader reader;
			try {
				File file = new File(configFile);

				reader = file.exists() ? new FileReader(configFile)
						: new InputStreamReader(BiomartSourceManager.class.getResourceAsStream("/biomart-sources.xml"));

				instance = load(reader);
				reader.close();
			}
			catch (Exception ex) {
				logger.error("Error in biomart user configuration file");
			}
			finally {
				if (instance == null || instance.getSources().size() == 0) {
					logger.error("Instance not initialised. Loading default configuration");
					BiomartSource src = new BiomartSource();
					src.setName("Biomart Central Portal");
					src.setDescription("BioMart Central Portal");
					src.setVersion("0.7");
					src.setRestPath("/biomart/martservice");
					src.setWsdlPath("/biomart/martwsdl");
					src.setHost("www.biomart.org");
					src.setPort("80");

					instance = new BiomartSourceManager();
					instance.addSource(src);
				}
			}
		}
		return instance;
	}

	private static BiomartSourceManager load(Reader reader) {
		BiomartSourceManager settings = new BiomartSourceManager();
		try {
			JAXBContext context = JAXBContext.newInstance(BiomartSources.class);
			Unmarshaller u = context.createUnmarshaller();
			settings.addSources((BiomartSources) u.unmarshal(reader));
			reader.close();

		} catch (FileNotFoundException e) {
			System.err.println("Biomart settings file doesn't exist: " + configFile);
			System.err.println("Created one with defaults.");
			settings = new BiomartSourceManager();
			settings.save();
		} catch (Exception e) {
			e.printStackTrace(); //TODO Deberia lanzar una excepción?
			settings = new BiomartSourceManager();
		}
		return settings;
	}

	private BiomartSourceManager() {
	}

	public void save() {
		File path = new File(configPath);
		if (!path.exists()) {
			path.mkdirs();
		}

		try {
			FileWriter writer = new FileWriter(configFile);

			JAXBContext context = JAXBContext.newInstance(BiomartSources.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(this, writer);

			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<BiomartSource> getSources() {
		return biomartSources.getSources();
	}

	public void setSources(List<BiomartSource> sources) {
		biomartSources.setSources(sources);
	}

	private void addSource(BiomartSource source) {
		biomartSources.getSources().add(source);
	}

	private void addSources(BiomartSources sources) {
		biomartSources.getSources().addAll(sources.getSources());
	}

}
