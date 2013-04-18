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
package org.gitools.utils.tools;

import org.gitools.utils.tools.exception.ToolException;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.ArrayList;

/**
 * @noinspection ALL
 */
public class XmlToolSetResource {

    private final File file;

    public XmlToolSetResource(File file) {
        this.file = file;
    }

    @Nullable
    public ToolSet load() throws ToolException {
        try {
            return load(new FileReader(file));
        } catch (Exception e) {
            throw new ToolException(e);
        }
    }

    public void save(ToolSet toolSet) throws ToolException {
        try {
            save(toolSet, new FileWriter(file));
        } catch (Exception e) {
            throw new ToolException(e);
        }
    }

    @Nullable
    public static ToolSet load(Reader reader) throws ToolException {
        ToolSet toolSet = null;
        try {
            JAXBContext context = JAXBContext.newInstance(ToolSet.class, ToolDescriptor.class, ArrayList.class);

            Unmarshaller u = context.createUnmarshaller();
            toolSet = (ToolSet) u.unmarshal(reader);
        } catch (Exception e) {
            throw new ToolException(e);
        }
        return toolSet;
    }

    private static void save(ToolSet toolSet, Writer writer) throws ToolException {
        try {
            JAXBContext context = JAXBContext.newInstance(ToolSet.class);

            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            m.marshal(toolSet, writer);
        } catch (Exception e) {
            throw new ToolException(e);
        }
    }
}
