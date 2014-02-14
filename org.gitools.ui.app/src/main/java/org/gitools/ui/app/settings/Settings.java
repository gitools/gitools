/*
 * #%L
 * gitools-ui-app
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.ui.app.settings;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Settings {

    private static final String DEFAULT_INTOGEN_URL = "http://www.intogen.org";
    private static final String DEFAULT_INTOGEN_ONCOMODULES_URL = DEFAULT_INTOGEN_URL + "/oncomodules";
    private static final String DEFAULT_INTOGEN_DATA_URL = DEFAULT_INTOGEN_URL + "/oncodata";

    private static final int DEFAULT_EDITOR_TAB_LENGTH = 20;

    private static final String DEFAULT_IGV_URL = "http://127.0.0.1:60151";

    private static final String userPath = System.getProperty("user.home", ".");

    public static final String CONFIG_PATH = userPath + File.separator + ".gitools";

    private static final String configFileName = "ui.xml";

    private static final String configFile = CONFIG_PATH + File.separator + configFileName;


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
        } catch (Exception e) {
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
    private String intogenOncomodulesUrl = DEFAULT_INTOGEN_ONCOMODULES_URL;
    private String intogenDataUrl = DEFAULT_INTOGEN_DATA_URL;


    private int editorTabLength = DEFAULT_EDITOR_TAB_LENGTH;

    private boolean showEnrichmentExamplePage = true;
    private boolean showOncodriveExamplePage = true;
    private boolean showCorrelationExamplePage = true;
    private boolean showOverlapExamplePage = true;
    private boolean showCombinationExamplePage = true;
    private boolean showTipsAtStartup = true;
    private boolean showMutualExclusionProgress = false;


    // Port parameters
    private boolean portEnabled = true;
    private int defaultPort = 50151;

    // IGV parameters
    private boolean showIGVLink = true;
    private String igvUrl = DEFAULT_IGV_URL;

    // Preview features
    private boolean previewFeatures = false;


    private Settings() {
    }

    public void save() {
        File path = new File(CONFIG_PATH);
        if (!path.exists()) {
            path.mkdirs();
        }

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

    /**
     * @noinspection UnusedDeclaration
     */
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


    public boolean isShowTipsAtStartup() {
        return showTipsAtStartup;
    }

    public void setShowTipsAtStartup(boolean showTipsAtStartup) {
        this.showTipsAtStartup = showTipsAtStartup;
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

    public boolean isShowIGVLink() {
        return showIGVLink;
    }

    public void setShowIGVLink(boolean showIGVLink) {
        this.showIGVLink = showIGVLink;
    }

    public boolean isPreviewFeatures() {
        return previewFeatures;
    }

    public void setPreviewFeatures(boolean previewFeatures) {
        this.previewFeatures = previewFeatures;
    }

    public int getEditorTabLength() {
        return editorTabLength;
    }

    public void setEditorTabLength(int editorTabLength) {
        this.editorTabLength = editorTabLength;
    }

    public boolean isShowMutualExclusionProgress() {
        return showMutualExclusionProgress;
    }

    public void setShowMutualExclusionProgress(boolean showMutualExclusionProgress) {
        this.showMutualExclusionProgress = showMutualExclusionProgress;
    }
}
