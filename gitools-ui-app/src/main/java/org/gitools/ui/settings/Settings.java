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
    public static final String DEFAULT_IGV_URL = "http://127.0.0.1:60151";
	
	private static final String userPath = 
		System.getProperty("user.home", ".");
	
	public static final String CONFIG_PATH =
		userPath + File.separator + ".gitools";
	
	private static final String configFileName = "ui.xml";
	
	private static final String configFile = 
		CONFIG_PATH + File.separator + configFileName;
	
	private static final String defaultWorkspacePath =
		CONFIG_PATH + File.separator + "workspace";

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

	private String version;
	
	private String lastPath = userPath;
	private String lastImportPath = userPath;
	private String lastExportPath = userPath;
	private String lastWorkPath = userPath;
	private String lastDataPath = userPath;
	private String lastMapPath = userPath;
	private String lastAnnotationPath = userPath;
	private String lastFilterPath = userPath;
	private String workspacePath = defaultWorkspacePath;
	private String intogenOncomodulesUrl = DEFAULT_INTOGEN_ONCOMODULES_URL;
	private String intogenDataUrl = DEFAULT_INTOGEN_DATA_URL;

	private boolean showEnrichmentExamplePage = true;
	private boolean showOncodriveExamplePage = true;
	private boolean showCorrelationExamplePage = true;
	private boolean showOverlapExamplePage = true;
	private boolean showCombinationExamplePage = true;

    // Port parameters
    private boolean portEnabled = false;
    private int defaultPort = 50151;

    // IGV parameters
    private boolean showIGVLink = false;
    private String igvUrl = DEFAULT_IGV_URL;
    private int igvTimeout = 3000;

    

	private Settings() {
	}
	
	public void save() {
        File path = new File(CONFIG_PATH);
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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
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

	public String getLastAnnotationPath() {
		return lastAnnotationPath;
	}

	public void setLastAnnotationPath(String lastAnnotationPath) {
		this.lastAnnotationPath = lastAnnotationPath;
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

	public boolean isShowEnrichmentExamplePage() {
		return showEnrichmentExamplePage;
	}

	public void setShowEnrichmentExamplePage(boolean showEnrichmentExamplePage) {
		this.showEnrichmentExamplePage = showEnrichmentExamplePage;
	}

	public boolean isShowOncodriveExamplePage() {
		return showOncodriveExamplePage;
	}

	public void setShowOncodriveExamplePage(boolean showOncodriveExamplePage) {
		this.showOncodriveExamplePage = showOncodriveExamplePage;
	}

	public boolean isShowCorrelationExamplePage() {
		return showCorrelationExamplePage;
	}

	public void setShowCorrelationExamplePage(boolean showCorrelationExamplePage) {
		this.showCorrelationExamplePage = showCorrelationExamplePage;
	}

	public boolean isShowCombinationExamplePage() {
		return showCombinationExamplePage;
	}

	public void setShowCombinationExamplePage(boolean showCombinationExamplePage) {
		this.showCombinationExamplePage = showCombinationExamplePage;
	}

	public boolean isShowOverlapExamplePage() {
		return showOverlapExamplePage;
	}

	public void setShowOverlapExamplePage(boolean showOverlapExamplePage) {
		this.showOverlapExamplePage = showOverlapExamplePage;
	}

    public boolean isPortEnabled() {
        return portEnabled;
    }

    public void setPortEnabled(boolean portEnabled) {
        this.portEnabled = portEnabled;
    }

    public int getDefaultPort() {
        return defaultPort;
    }

    public void setDefaultPort(int defaultPort) {
        this.defaultPort = defaultPort;
    }

    public String getIgvUrl() {
        return igvUrl;
    }

    public void setIgvUrl(String igvUrl) {
        this.igvUrl = igvUrl;
    }

    public int getIgvTimeout() {
        return igvTimeout;
    }

    public void setIgvTimeout(int igvTimeout) {
        this.igvTimeout = igvTimeout;
    }

    public boolean isShowIGVLink() {
        return showIGVLink;
    }

    public void setShowIGVLink(boolean showIGVLink) {
        this.showIGVLink = showIGVLink;
    }
}
