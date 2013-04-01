/*
 * #%L
 * gitools-biomart
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
package org.gitools.biomart.settings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.List;

public class BiomartSourceManager
{

    private static Logger logger = LoggerFactory.getLogger(BiomartSourceManager.class);

    private static final String userPath = System.getProperty("user.home", ".");
    private static final String configPath = userPath + File.separator + ".gitools";
    private static final String configFileName = "biomart-sources.xml";
    private static final String configFile = configPath + File.separator + configFileName;

    private static BiomartSourceManager instance;
    private static BiomartSources biomartSources = new BiomartSources();

    public static BiomartSourceManager getDefault()
    {
        if (instance == null)
        {

            Reader reader;
            try
            {
                File file = new File(configFile);

                reader = file.exists() ? new FileReader(configFile)
                        : new InputStreamReader(BiomartSourceManager.class.getResourceAsStream("/biomart-sources.xml"));

                instance = load(reader);
                reader.close();
            } catch (Exception ex)
            {
                logger.error("Error in biomart user configuration file");
            } finally
            {
                if (instance == null || instance.getSources().size() == 0)
                {
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

    private static BiomartSourceManager load(Reader reader)
    {
        BiomartSourceManager settings = new BiomartSourceManager();
        try
        {
            JAXBContext context = JAXBContext.newInstance(BiomartSources.class);
            Unmarshaller u = context.createUnmarshaller();
            settings.addSources((BiomartSources) u.unmarshal(reader));
            reader.close();

        } catch (FileNotFoundException e)
        {
            System.err.println("Biomart settings file doesn't exist: " + configFile);
            System.err.println("Created one with defaults.");
            settings = new BiomartSourceManager();
            settings.save();
        } catch (Exception e)
        {
            e.printStackTrace(); //TODO Deberia lanzar una excepción?
            settings = new BiomartSourceManager();
        }
        return settings;
    }

    private BiomartSourceManager()
    {
    }

    public void save()
    {
        File path = new File(configPath);
        if (!path.exists())
        {
            path.mkdirs();
        }

        try
        {
            FileWriter writer = new FileWriter(configFile);

            JAXBContext context = JAXBContext.newInstance(BiomartSources.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(this, writer);

            writer.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public List<BiomartSource> getSources()
    {
        return biomartSources.getSources();
    }

    public void setSources(List<BiomartSource> sources)
    {
        biomartSources.setSources(sources);
    }

    private void addSource(BiomartSource source)
    {
        biomartSources.getSources().add(source);
    }

    private void addSources(BiomartSources sources)
    {
        biomartSources.getSources().addAll(sources.getSources());
    }

}
