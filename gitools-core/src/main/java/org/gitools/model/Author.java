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
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"name", "surnames", "contribution", "affiliation"})
public class Author
        implements Serializable
{

    private static final long serialVersionUID = -1132892392275362936L;

    private String name;

    /**
     * Composed surnames should be considered as one *
     */
    private String surnames;

    /**
     * The affiliation this author belongs to *
     */
    private Affiliation affiliation;

    /**
     * True if this author should be considered as a corresponding author*
     */
    boolean contribution;


    public Author()
    {

    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSurnames()
    {
        return surnames;
    }

    public void setSurnames(String surnames)
    {
        this.surnames = surnames;
    }

    public void setAffiliation(Affiliation affiliation)
    {
        this.affiliation = affiliation;
    }

    public Affiliation getAffiliation()
    {
        return affiliation;
    }

}
