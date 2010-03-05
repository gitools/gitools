package org.gitools.ui.settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Settings {

	public static final String DEFAULT_INTOGEN_URL = "http://www.intogen.org";
	public static final String DEFAULT_INTOGEN_ONCOMODULES_URL = DEFAULT_INTOGEN_URL + "/oncomodules";
	public static final String DEFAULT_INTOGEN_DATA_URL = DEFAULT_INTOGEN_URL + "/oncodata";
	
	private static final String userPath = 
		System.getProperty("user.home", ".");
	
	private static final String configPath =
		userPath + File.separator + ".gitools";
	
	private static final String configFileName = "ui.xml";
	
	private static final String configFile = 
		configPath + File.separator + configFileName;
	
	private static final String defaultWorkspacePath =
		configPath + File.separator + "workspace";

	private static Settings instance;
	
	public static Settings getDefault() {
		if (instance == null) {
            instance = load();
        }
        return instance;
	}

	private static Settings load() {
        Settings settings = null;
        try {
            Reader reader = new FileReader(configFile);
            JAXBContext context = JAXBContext.newInstance(Settings.class);
            Unmarshaller u = context.createUnmarshaller();
            settings = (Settings) u.unmarshal(reader);

            reader.close();
        } catch (FileNotFoundException e) {
            System.err.println("Settings file doesn't exist: " + configFile);
            System.err.println("Created one with defaults.");
            settings = new Settings();
            settings.save();
        }
        catch (Exception e) {
            e.printStackTrace(); //TODO Deberia lanzar una excepción?
            settings = new Settings();
        }
        return settings;
    }

	private String lastPath = userPath;
	private String lastImportPath = userPath;
	private String lastExportPath = userPath;
	private String lastWorkPath = userPath;
	private String lastDataPath = userPath;
	private String lastMapPath = userPath;
	private String lastFilterPath = userPath;
	private String workspacePath = defaultWorkspacePath;
	private String intogenOncomodulesUrl = DEFAULT_INTOGEN_ONCOMODULES_URL;
	private String intogenDataUrl = DEFAULT_INTOGEN_DATA_URL;

	private Settings() {
	}
	
	public void save() {
        File path = new File(configPath);
        if (!path.exists())
            path.mkdirs();

        try {
            FileWriter writer = new FileWriter(configFile);

            JAXBContext context = JAXBContext.newInstance(Settings.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(this, writer);

            writer.close();
        } catch (Exception e) {
                e.printStackTrace();
        }
    }

	public String getLastPath() {
		return lastPath;
	}

	public void setLastPath(String lastPath) {
		this.lastPath = lastPath;
	}

	public String getLastImportPath() {
		return lastImportPath;
	}

	public void setLastImportPath(String lastImportPath) {
		this.lastImportPath = lastImportPath;
	}

	public String getLastExportPath() {
		return lastExportPath;
	}

	public void setLastExportPath(String lastExportPath) {
		this.lastExportPath = lastExportPath;
	}
	
	public String getLastWorkPath() {
		return lastWorkPath;
	}

	public void setLastWorkPath(String lastWorkPath) {
		this.lastWorkPath = lastWorkPath;
	}
	
	public String getLastDataPath() {
		return lastDataPath;
	}

	public void setLastDataPath(String lastDataPath) {
		this.lastDataPath = lastDataPath;
	}
	
	public String getLastMapPath() {
		return lastMapPath;
	}

	public void setLastMapPath(String lastMapPath) {
		this.lastMapPath = lastMapPath;
	}

	public String getLastFilterPath() {
		return lastFilterPath;
	}

	public void setLastFilterPath(String lastFilterPath) {
		this.lastFilterPath = lastFilterPath;
	}

	public String getWorkspacePath() {
		return workspacePath;
	}
	
	public void setWorkspacePath(String workspacePath) {
		this.workspacePath = workspacePath;
	}

	public String getIntogenOncomodulesUrl() {
		return intogenOncomodulesUrl;
	}

	public void setIntogenOncomodulesUrl(String intogenOncomodulesUrl) {
		this.intogenOncomodulesUrl = intogenOncomodulesUrl;
	}

	public String getIntogenDataUrl() {
		return intogenDataUrl;
	}

	public void setIntogenDataUrl(String intogenDataUrl) {
		this.intogenDataUrl = intogenDataUrl;
	}
}
