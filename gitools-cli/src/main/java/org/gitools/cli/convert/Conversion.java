/*
 * #%L
 * gitools-cli
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
package org.gitools.cli.convert;

class Conversion
{

    public String src;
    public String dst;

    public ConversionDelegate delegate;

    public Conversion(String src, String dst)
    {
        this(src, dst, null);
    }

    public Conversion(String src, String dst, ConversionDelegate delegate)
    {
        this.src = src;
        this.dst = dst;
        this.delegate = delegate;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        else if (!(obj instanceof Conversion))
        {
            return false;
        }
        Conversion o = (Conversion) obj;
        return o.src.equals(this.src) && o.dst.equals(this.dst);
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 89 * hash + (this.src != null ? this.src.hashCode() : 0);
        hash = 89 * hash + (this.dst != null ? this.dst.hashCode() : 0);
        return hash;
    }
}
