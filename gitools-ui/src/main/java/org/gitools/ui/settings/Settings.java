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
	private String lastExportPath = userPath;
	private String lastWorkPath = userPath;
	private String lastDataPath = userPath;
	private String lastMapPath = userPath;
	private String workspacePath = defaultWorkspacePath;

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
		return lastPath; //props.getProperty("last-path", userPath);
	}

	public void setLastPath(String lastPath) {
		this.lastPath = lastPath; //props.setProperty("last-path", lastPath);
	}

	public String getLastExportPath() {
		return lastExportPath; //props.getProperty("last-export-path", getLastPath());
	}

	public void setLastExportPath(String lastExportPath) {
		this.lastExportPath = lastExportPath; //props.setProperty("last-export-path", lastExportPath);
	}
	
	public String getLastWorkPath() {
		return lastWorkPath; //props.getProperty("last-work-path", getLastPath());
	}

	public void setLastWorkPath(String lastWorkPath) {
		this.lastWorkPath = lastPath; //props.setProperty("last-work-path", lastWorkPath);
	}
	
	public String getLastDataPath() {
		return lastDataPath; //props.getProperty("last-data-path", getLastPath());
	}

	public void setLastDataPath(String lastDataPath) {
		this.lastDataPath = lastDataPath; //props.setProperty("last-data-path", lastDataPath);
	}
	
	public String getLastMapPath() {
		return lastMapPath; //props.getProperty("last-map-path", getLastPath());
	}

	public void setLastMapPath(String lastMapPath) {
		this.lastMapPath = lastMapPath; //props.setProperty("last-map-path", lastMapPath);
	}
	
	public String getWorkspacePath() {
		return workspacePath; //props.getProperty("workspace", workspacePath);
	}
	
	public void setWorkspacePath(String workspacePath) {
		this.workspacePath = workspacePath; //props.setProperty("workspace", workspacePath);
	}
}
