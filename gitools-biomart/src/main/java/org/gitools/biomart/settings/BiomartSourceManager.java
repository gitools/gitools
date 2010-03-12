package org.gitools.biomart.settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class BiomartSourceManager {

    private static final String userPath =
            System.getProperty("user.home", ".");
    private static final String configPath =
            userPath + File.separator + ".gitools";
    private static final String configFileName = "biomartSources.xml";
    private static final String configFile =
            configPath + File.separator + configFileName;

    private static BiomartSourceManager instance;
    private static BiomartSources bs = null;

    public static BiomartSourceManager getDefault() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    private static BiomartSourceManager load() {

        BiomartSourceManager settings = null;
        try {
            Reader reader = new FileReader(configFile);
            JAXBContext context = JAXBContext.newInstance(BiomartSources.class);
            Unmarshaller u = context.createUnmarshaller();
            bs = (BiomartSources) u.unmarshal(reader);

            reader.close();
        } catch (FileNotFoundException e) {
            System.err.println("Settings file doesn't exist: " + configFile);
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

    public static BiomartSources getBiomartListSrc() {
        return bs;
    }

    public static void setBiomartListSrc(BiomartSources sc) {
        BiomartSourceManager.bs = sc;
    }


}
