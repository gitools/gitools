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

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @noinspection ALL
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class IdType
{

    @XmlAttribute
    private String key;

    @XmlElement
    private String title;

    @XmlElement(name = "link")
    private List<UrlLink> links = new ArrayList<UrlLink>(0);

    public IdType()
    {
        this(null, null);
    }

    public IdType(String key, String title)
    {
        this.key = key;
        this.title = title;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public List<UrlLink> getLinks()
    {
        return links;
    }

    public void setLinks(List<UrlLink> links)
    {
        this.links = links;
    }

    @Override
    public String toString()
    {
        return title;
    }
}
