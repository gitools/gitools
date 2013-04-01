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
package org.gitools.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
public class Publication
        implements Serializable
{

    private static final long serialVersionUID = 4040601803383233010L;

    private String title;
    private String authors;
    private String abstr;
    private String journal;
    private String pubmed;
    private String url;

    public Publication()
    {
    }

    public Publication(String title, String authors, String abstr,
                       String journal, String pubmed, String url)
    {
        this.title = title;
        this.authors = authors;
        this.abstr = abstr;
        this.journal = journal;
        this.pubmed = pubmed;
        this.url = url;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getAuthors()
    {
        return authors;
    }

    public void setAuthors(String authors)
    {
        this.authors = authors;
    }

    public String getAbstract()
    {
        return abstr;
    }

    public void setAbstract(String abstr)
    {
        this.abstr = abstr;
    }

    public String getJournal()
    {
        return journal;
    }

    public void setJournal(String journal)
    {
        this.journal = journal;
    }

    public String getPubmed()
    {
        return pubmed;
    }

    public void setPubmed(String pubmed)
    {
        this.pubmed = pubmed;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }
}
