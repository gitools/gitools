package org.gitools.biomart.settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class BiomartSourceManager {

	private static final String userPath = System.getProperty("user.home", ".");
	private static final String configPath = userPath + File.separator + ".gitools";
	private static final String configFileName = "biomart-sources.xml";
	private static final String configFile = configPath + File.separator + configFileName;

	private static BiomartSourceManager instance;
	private static BiomartSources biomartSources = new BiomartSources();

	public static BiomartSourceManager getDefault() {
		if (instance == null) {
			//Reader reader = new FileReader(configFile);
			try {
				InputStream in = BiomartSourceManager.class.getResourceAsStream("biomart-sources.xml");
				Reader reader = new InputStreamReader(in);
				instance = load(reader);
				reader.close();
			}
			catch (Exception ex) {
				BiomartSource src = new BiomartSource();
				src.setName("Biomart Central Portal");
				src.setDescription("BioMart Central Portal");
				src.setVersion("0.7");
				src.setRestUrl("http://www.biomart.org/biomart/martservice");
				src.setWsdlUrl("http://www.biomart.org:80/biomart/martwsdl");
				src.setNamespace("http://www.biomart.org:80/MartServiceSoap");
				src.setPortName("BioMartSoapPort");
				src.setServiceName("BioMartSoapService");
				instance = new BiomartSourceManager();
				instance.addSource(src);
			}
		}
		return instance;
	}

	private static BiomartSourceManager load(Reader reader) {
		BiomartSourceManager settings = null;
		try {
			JAXBContext context = JAXBContext.newInstance(BiomartSources.class);
			Unmarshaller u = context.createUnmarshaller();
			biomartSources = (BiomartSources) u.unmarshal(reader);
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
}
