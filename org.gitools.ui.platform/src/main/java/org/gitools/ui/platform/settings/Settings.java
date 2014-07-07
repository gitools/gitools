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
package org.gitools.ui.platform.settings;

import com.google.common.base.Strings;
import com.jgoodies.binding.beans.Model;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Settings extends Model {

    private static final String userPath = System.getProperty("user.home", ".");
    public static final String CONFIG_PATH = userPath + File.separator + ".gitools";
    private static final String configFileName = "ui.xml";
    private static final String configFile = CONFIG_PATH + File.separator + configFileName;
    private static Settings instance;

    private static final int DEFAULT_EDITOR_TAB_LENGTH = 20;
    private static final String DEFAULT_IGV_URL = "http://127.0.0.1:60151";
    private static final String DEFAULT_WELCOME_URL = "http://www.gitools.org/welcome";
    private static final int DEFAULT_SVG_BODY_LIMIT = 50000;

    public static final String PROPERTY_USAGE_STATS = "allowUsageStatistics";
    public static final String PROPERTY_TIPS = "showTipsAtStartup";
    public static final String PROPERTY_PORT_ENABLED = "portEnabled";
    public static final String PROPERTY_PORT = "defaultPort";
    public static final String PROPERTY_IGV_ENABLED = "showIGVLink";
    public static final String PROPERTY_IGV_URL = "igvUrl";
    public static final String PROPERTY_AUTHOR_NAME = "authorName";
    public static final String PROPERTY_AUTHOR_EMAIL = "authorEmail";
    public static final String PROPERTY_RECENT_FILES_NUMBER = "recentFilesNumber";
    public static final String PROPERTY_PROXY_ENABLED = "proxyEnabled";
    public static final String PROPERTY_PROXY_PORT = "proxyPort";
    public static final String PROPERTY_PROXY_HOST = "proxyHost";


    private String authorName = System.getProperty("user.name");
    private String authorEmail = "";
    private String lastFilterPath = userPath;
    private String lastAnnotationPath = userPath;
    private String lastMapPath = userPath;
    private String lastDataPath = userPath;
    private String lastWorkPath = userPath;
    private String lastExportPath = userPath;
    private String lastImportPath = userPath;
    private String lastPath = userPath;
    @XmlElementWrapper(name = "recentFiles")
    @XmlElement(name = "file")
    private List<String> recentFiles = new ArrayList<>();
    private int recentFilesNumber = 5;

    private String version;
    private String uuid;
    private int editorTabLength = DEFAULT_EDITOR_TAB_LENGTH;

    // Editable parameters
    private boolean allowUsageStatistics = true;
    private String statisticsConsentmentVersion = "";
    private boolean showTipsAtStartup = true;
    private boolean showMutualExclusionProgress = false;
    private String welcomeUrl = DEFAULT_WELCOME_URL;

    // Port parameters
    private boolean portEnabled = true;
    private int defaultPort = 50151;

    // IGV parameters
    private boolean showIGVLink = true;
    private String igvUrl = DEFAULT_IGV_URL;

    // Preview features
    private boolean previewFeatures = false;

    // SVG body bitmap limit
    private int svgBodyLimit = DEFAULT_SVG_BODY_LIMIT;

    // Proxy settings
    private boolean proxyEnabled = false;
    private String proxyHost = "";
    private int proxyPort = 8080;


    private Settings() {
    }

    public static Settings get() {
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
            e.printStackTrace();
            settings = new Settings();
        }
        return settings;
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

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
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

    public boolean isShowTipsAtStartup() {
        return showTipsAtStartup;
    }

    public void setShowTipsAtStartup(boolean showTipsAtStartup) {
        boolean old = this.showTipsAtStartup;
        this.showTipsAtStartup = showTipsAtStartup;
        firePropertyChange(PROPERTY_TIPS, old, showTipsAtStartup);
    }

    public boolean isPortEnabled() {
        return portEnabled;
    }

    public void setPortEnabled(boolean portEnabled) {
        this.portEnabled = portEnabled;
    }

    public Integer getDefaultPort() {
        return defaultPort;
    }

    public void setDefaultPort(Integer defaultPort) {
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

    public int getSvgBodyLimit() {
        return svgBodyLimit;
    }

    public void setSvgBodyLimit(int svgBodyLimit) {
        this.svgBodyLimit = svgBodyLimit;
    }

    public boolean isAllowUsageStatistics() {
        return allowUsageStatistics;
    }

    public void setAllowUsageStatistics(boolean allowUsageStatistics) {
        this.allowUsageStatistics = allowUsageStatistics;
    }

    public String getStatisticsConsentmentVersion() {
        return statisticsConsentmentVersion;
    }

    public boolean isProxyEnabled() {
        return proxyEnabled;
    }

    public void setProxyEnabled(boolean proxyEnabled) {
        this.proxyEnabled = proxyEnabled;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public void setStatisticsConsentmentVersion(String statisticsConsentmentVersion) {
        this.statisticsConsentmentVersion = statisticsConsentmentVersion;
    }

    public String getWelcomeUrl() {
        return welcomeUrl;
    }

    public void setWelcomeUrl(String welcomeUrl) {
        this.welcomeUrl = welcomeUrl;
    }

    public String getUuid() {

        if (Strings.isNullOrEmpty(uuid) && isAllowUsageStatistics()) {
            uuid = UUID.randomUUID().toString();
        }

        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void addRecentFile(String fileName) {
        if (recentFiles.contains(fileName)) {
            recentFiles.remove(fileName);
        }

        recentFiles.add(fileName);

        while (recentFiles.size() > recentFilesNumber) {
            recentFiles.remove(0);
        }
    }

    public void setRecentFiles(List<String> recentFiles) {
        this.recentFiles = recentFiles;
    }

    /**
     * Returns a list of up to 5 last .heatmap files accessed
     * in revers order (most recent last)
     *
     * @return
     */
    public List<String> getRecentFiles() {
        return recentFiles;
    }

    public int getRecentFilesNumber() {
        return recentFilesNumber;
    }

    public void setRecentFilesNumber(int recentFilesNumber) {
        this.recentFilesNumber = recentFilesNumber;
    }

    public Proxy getProxy() {

        if (isProxyEnabled()) {

            // Update system proxy
            System.getProperties().put("http.proxyHost", getProxyHost());
            System.getProperties().put("http.proxyPort", getProxyPort());

            return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(getProxyHost(), getProxyPort()));
        }

        // Update system proxy
        System.getProperties().put("http.proxyHost", "");
        System.getProperties().put("http.proxyPort", "");

        return Proxy.NO_PROXY;
    }
}
