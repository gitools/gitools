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
package org.gitools.utils;

import com.thoughtworks.xstream.XStream;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Code obtained from http://weblogs.java.net/blog/emcmanus/archive/2007/04/cloning_java_ob.html
 */
public class SerialClone
{

    @NotNull
    public static <T> T clone(T x)
    {
        try
        {
            return cloneX(x);
        } catch (IOException e)
        {
            throw new IllegalArgumentException(e);
        } catch (ClassNotFoundException e)
        {
            throw new IllegalArgumentException(e);
        }
    }

    @NotNull
    private static <T> T cloneX(T x) throws IOException, ClassNotFoundException
    {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        CloneOutput cout = new CloneOutput(bout);
        cout.writeObject(x);
        byte[] bytes = bout.toByteArray();

        ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
        CloneInput cin = new CloneInput(bin, cout);
        @SuppressWarnings("unchecked")  // thanks to Bas de Bakker for the tip!
                T clone = (T) cin.readObject();
        return clone;
    }

    private static class CloneOutput extends ObjectOutputStream
    {

        @NotNull
        Queue<Class<?>> classQueue = new LinkedList<Class<?>>();

        CloneOutput(OutputStream out) throws IOException
        {
            super(out);
        }

        @Override
        protected void annotateClass(Class<?> c)
        {
            classQueue.add(c);
        }

        @Override
        protected void annotateProxyClass(Class<?> c)
        {
            classQueue.add(c);
        }
    }

    private static class CloneInput extends ObjectInputStream
    {

        private final CloneOutput output;

        CloneInput(InputStream in, CloneOutput output) throws IOException
        {
            super(in);
            this.output = output;
        }

        @Override
        protected Class<?> resolveClass(@NotNull ObjectStreamClass osc)
                throws IOException, ClassNotFoundException
        {
            Class<?> c = output.classQueue.poll();
            String expected = osc.getName();
            String found = (c == null) ? null : c.getName();
            if (!expected.equals(found))
            {
                throw new InvalidClassException("Classes desynchronized: "
                        + "found " + found + " when expecting " + expected);
            }
            return c;
        }

        @Override
        protected Class<?> resolveProxyClass(String[] interfaceNames)
                throws IOException, ClassNotFoundException
        {
            return output.classQueue.poll();
        }
    }

    public static <T> T xclone(T x)
    {
        XStream xstream = new XStream();
        String xml = xstream.toXML(x);
        return (T) xstream.fromXML(xml);
    }
}
