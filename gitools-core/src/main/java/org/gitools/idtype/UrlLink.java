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

import org.gitools.utils.textpatt.TextPattern;
import org.gitools.utils.textpatt.TextPatternXmlAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @noinspection ALL
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class UrlLink {

    @XmlAttribute
    private String name;

    @XmlValue
    @XmlJavaTypeAdapter(TextPatternXmlAdapter.class)
    private TextPattern pattern;

    public UrlLink() {
    }

    public UrlLink(String name, String pattern) {
        this.name = name;
        this.pattern = new TextPattern(pattern);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TextPattern getPattern() {
        return pattern;
    }

    public void setPattern(TextPattern pattern) {
        this.pattern = pattern;
    }
}
