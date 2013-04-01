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
package org.gitools.biomart.restful.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;


@XmlAccessorType(XmlAccessType.FIELD)
public class AttributeDescription extends AbstractDescription
{

    @XmlAttribute
    private String datasetLink;

    @XmlAttribute
    private String source;

    @XmlAttribute
    private String homepageURL;

    @XmlAttribute
    private String linkoutURL;

    @XmlAttribute
    private String imageURL;

    @XmlAttribute
    private String maxLength;

    @XmlAttribute
    private String pointerDataset;

    @XmlAttribute
    private String pointerInterface;

    @XmlAttribute
    private String pointerAttribute;

    @XmlAttribute
    private String pointerFilter;

    @XmlAttribute
    private String checkForNulls;

    @XmlAttribute
    private String pipeDisplay;


    public String getCheckForNulls()
    {
        return checkForNulls;
    }

    public void setCheckForNulls(String checkForNulls)
    {
        this.checkForNulls = checkForNulls;
    }

    public String getDatasetLink()
    {
        return datasetLink;
    }

    public void setDatasetLink(String datasetLink)
    {
        this.datasetLink = datasetLink;
    }

    public String getHomepageURL()
    {
        return homepageURL;
    }

    public void setHomepageURL(String homepageURL)
    {
        this.homepageURL = homepageURL;
    }

    public String getImageURL()
    {
        return imageURL;
    }

    public void setImageURL(String imageURL)
    {
        this.imageURL = imageURL;
    }

    public String getLinkoutURL()
    {
        return linkoutURL;
    }

    public void setLinkoutURL(String linkoutURL)
    {
        this.linkoutURL = linkoutURL;
    }

    public String getMaxLength()
    {
        return maxLength;
    }

    public void setMaxLength(String maxLength)
    {
        this.maxLength = maxLength;
    }

    public String getPipeDisplay()
    {
        return pipeDisplay;
    }

    public void setPipeDisplay(String pipeDisplay)
    {
        this.pipeDisplay = pipeDisplay;
    }

    public String getPointerAttribute()
    {
        return pointerAttribute;
    }

    public void setPointerAttribute(String pointerAttribute)
    {
        this.pointerAttribute = pointerAttribute;
    }

    public String getPointerDataset()
    {
        return pointerDataset;
    }

    public void setPointerDataset(String pointerDataset)
    {
        this.pointerDataset = pointerDataset;
    }

    public String getPointerFilter()
    {
        return pointerFilter;
    }

    public void setPointerFilter(String pointerFilter)
    {
        this.pointerFilter = pointerFilter;
    }

    public String getPointerInterface()
    {
        return pointerInterface;
    }

    public void setPointerInterface(String pointerInterface)
    {
        this.pointerInterface = pointerInterface;
    }

    public String getSource()
    {
        return source;
    }

    public void setSource(String source)
    {
        this.source = source;
    }

}
