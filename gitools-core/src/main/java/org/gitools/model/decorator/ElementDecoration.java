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
package org.gitools.model.decorator;

import java.awt.*;
import java.io.Serializable;

public class ElementDecoration implements Serializable
{

    private static final long serialVersionUID = 5204451046972665249L;

    public enum TextAlignment
    {
        left, right, center
    }

    protected String text;
    public TextAlignment textAlign;
    protected String toolTip;
    protected Color fgColor;
    protected Color bgColor;

    public ElementDecoration()
    {
        reset();
    }

    public void reset()
    {
        this.text = "";
        this.textAlign = TextAlignment.left;
        this.toolTip = "";
        this.fgColor = Color.BLACK;
        this.bgColor = Color.WHITE;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public TextAlignment getTextAlign()
    {
        return textAlign;
    }

    public void setTextAlign(TextAlignment textAlign)
    {
        this.textAlign = textAlign;
    }

    public String getToolTip()
    {
        return toolTip;
    }

    public void setToolTip(String toolTip)
    {
        this.toolTip = toolTip;
    }

    public Color getFgColor()
    {
        return fgColor;
    }

    public void setFgColor(Color fgColor)
    {
        this.fgColor = fgColor;
    }

    public Color getBgColor()
    {
        return bgColor;
    }

    public void setBgColor(Color bgColor)
    {
        this.bgColor = bgColor;
    }
}
