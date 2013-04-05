/*
 * #%L
 * gitools-utils
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
package org.gitools.utils.xml.adapter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.awt.*;

public class ColorXmlAdapter extends XmlAdapter<String, Color>
{

    @NotNull
    @Override
    public String marshal(@NotNull Color v) throws Exception
    {
        String rgb = Integer.toHexString(v.getRGB());
        rgb = rgb.substring(2, rgb.length());
        return "#" + rgb;
    }

    @Nullable
    @Override
    public Color unmarshal(@NotNull String v) throws Exception
    {
        try
        {
            Color c = Color.decode(v);
            return c;
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

}
