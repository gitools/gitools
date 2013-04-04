/*
 * #%L
 * gitools-core
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
package org.gitools.idtype;

import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

public class IdTypeManager
{

    @XmlRootElement(name = "idTypes")
    private static class Document
    {
        @NotNull
        @XmlElement(name = "idType")
        public List<IdType> idTypes = new ArrayList<IdType>(0);
    }

    private static IdTypeManager instance;

    public static IdTypeManager getDefault()
    {
        if (instance == null)
        {
            instance = createDefaultInstance();
        }
        return instance;
    }

    @NotNull
    private static IdTypeManager createDefaultInstance()
    {
        List<IdType> idTypes = new ArrayList<IdType>();
        Reader reader = null;
        try
        {
            JAXBContext context = JAXBContext.newInstance(Document.class);
            Unmarshaller u = context.createUnmarshaller();

            reader = new InputStreamReader(
                    IdTypeManager.class.getResourceAsStream(
                            "/idtypes.xml"));

            Document doc = (Document) u.unmarshal(reader);
            idTypes.addAll(doc.idTypes);
        } catch (JAXBException ex)
        {
            LoggerFactory.getLogger(IdTypeManager.class).error(ex.toString());
        } finally
        {
            try
            {
                if (reader != null)
                {
                    reader.close();
                }
            } catch (IOException ex)
            {
            }
        }
        return new IdTypeManager(idTypes);
    }

    private List<IdType> idTypes;
    private Map<String, IdType> idTypesMap;
    private IdType defaultIdType;

    private IdTypeManager(@NotNull List<IdType> idTypes)
    {
        this.idTypes = idTypes;

        this.idTypesMap = new HashMap<String, IdType>();
        for (IdType type : idTypes)
            idTypesMap.put(type.getKey(), type);

        defaultIdType = idTypesMap.get(IdTypeNames.NOT_SPECIFIED_KEY);
        if (defaultIdType == null)
        {
            defaultIdType = new IdType(
                    IdTypeNames.NOT_SPECIFIED_KEY,
                    IdTypeNames.NOT_SPECIFIED_TITLE);
            this.idTypes.add(defaultIdType);
            this.idTypesMap.put(defaultIdType.getKey(), defaultIdType);
        }
    }

    public List<IdType> getIdTypes()
    {
        return Collections.unmodifiableList(idTypes);
    }

    public IdType getIdType(String idTypeKey)
    {
        return idTypesMap.get(idTypeKey);
    }

    public IdType getDefaultIdType()
    {
        return defaultIdType;
    }

    public List<UrlLink> getLinks(String idTypeKey)
    {
        IdType type = idTypesMap.get(idTypeKey);
        if (type == null)
        {
            return new ArrayList<UrlLink>(0);
        }
        else
        {
            return Collections.unmodifiableList(type.getLinks());
        }
    }
}
