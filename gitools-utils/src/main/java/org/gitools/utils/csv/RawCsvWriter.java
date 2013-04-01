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
package org.gitools.utils.csv;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

public class RawCsvWriter
{

    private PrintWriter out;
    private char separator;
    private char quote;

    public RawCsvWriter(Writer writer, char separator, char quote)
    {
        this.out = new PrintWriter(writer);
        this.separator = separator;
        this.quote = quote;
    }

    public void writeNewLine()
    {
        out.println();
    }

    public void writeValue(String value)
    {
        out.print(value);
    }

    public void writeQuotedValue(String value)
    {
        out.print(quote);
        out.print(value);
        out.print(quote);
    }

    public void writeProperty(String name, String value)
    {
        out.print(name);
        out.print(separator);
        writeQuotedValue(value);
        out.println();
    }

    public void writePropertyList(String name, String[] values)
    {
        out.print(name);
        if (values.length > 0)
        {
            for (String value : values)
            {
                out.print(separator);
                writeQuotedValue(value);
            }
        }
        out.println();
    }

    public void writePropertyList(String name, List<String> values)
    {
        out.print(name);
        if (values.size() > 0)
        {
            for (String value : values)
            {
                out.print(separator);
                writeQuotedValue(value);
            }
        }
        out.println();
    }

    public void writeSeparator()
    {
        out.print(separator);
    }

    public void write(String raw)
    {
        out.print(raw);
    }

    public void close()
    {
        out.close();
    }
}
