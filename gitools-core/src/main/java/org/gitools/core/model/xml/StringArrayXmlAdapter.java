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
package org.gitools.core.model.xml;

import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.ArrayList;
import java.util.List;

public class StringArrayXmlAdapter extends XmlAdapter<String, List<String>> {

    private static final String elemSeparator = ",";

    @NotNull
    @Override
    public String marshal(@NotNull List<String> v) throws Exception {

        StringBuilder output = new StringBuilder();

        if (v.size() > 0) {

            int i = 0;

            while (i < v.size() - 1) {
                output.append(v.get(i).replace(",", "\\c")).append(elemSeparator);
                i++;
            }
            output.append(v.get(i));
        }
        return output.toString();
    }

    @NotNull
    @Override
    public List<String> unmarshal(@NotNull String v) throws Exception {

        String values[] = v.split(elemSeparator);
        List<String> result = new ArrayList<>(values.length);

        for (String value : values) {
            result.add(value.replace("\\c", ",").trim());
        }

        return result;
    }

}
