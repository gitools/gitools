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
package org.gitools.ui.settings.decorators;

import com.thoughtworks.xstream.XStream;
import org.apache.commons.io.FileUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.regex.Pattern;


public class DecoratorArchivePersistance {

    private transient static XStream xstream;

    private static final String userPath = System.getProperty("user.home", ".");

    private static final String CONFIG_PATH = userPath + File.separator + ".gitools";

    private static final String configFileName = "scales-2.x.xml";

    private static final String configFile = CONFIG_PATH + File.separator + configFileName;

    public DecoratorArchivePersistance() {


    }

    public void save(DecoratorArchive archive) {
        File path = new File(CONFIG_PATH);
        if (!path.exists()) {
            path.mkdirs();
        }

        try {
            FileWriter writer = new FileWriter(configFile);

            writer.write("<!-- scales.xml version " + DecoratorArchive.VERSION + " -->\n");

            JAXBContext context = JAXBContext.newInstance(DecoratorArchive.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(archive, writer);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DecoratorArchive load() {
        DecoratorArchive decoratorArchive;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(configFile));

            String firstLine = reader.readLine();

            if (!firstLine.matches("(.*)" + Pattern.quote("version " + DecoratorArchive.VERSION) + "(.*)")) {
                System.err.println("Current scales.xml file not compatible with current version.");
                reader.close();
                FileUtils.copyFile(new File(configFile), new File(configFile + ".backup"));
                decoratorArchive = new DecoratorArchive();
                decoratorArchive.add(decoratorArchive.getDefaultElementDecoratros());
                save(decoratorArchive);
                System.err.println("Created scales file with defaults and there is a backup copy on " + configFileName + ".backup");

            } else {
                JAXBContext context = JAXBContext.newInstance(DecoratorArchive.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                decoratorArchive = (DecoratorArchive) unmarshaller.unmarshal(reader);
                reader.close();
            }


        } catch (FileNotFoundException e) {
            System.err.println("Settings file doesn't exist: " + configFile);
            System.err.println("Created scales file with defaults.");
            decoratorArchive = new DecoratorArchive();
            decoratorArchive.add(decoratorArchive.getDefaultElementDecoratros());
            save(decoratorArchive);
        } catch (Exception e) {
            decoratorArchive = new DecoratorArchive();
            e.printStackTrace();
        }
        return decoratorArchive;
    }
}
