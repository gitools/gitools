/*
 * #%L
 * gitools-ui-platform
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
package org.gitools.ui.platform;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.Properties;

/**
 * The tag ${} is used to introduce substitutable parameters so it
 * expands properties with ${} described in a property file; it is possible to
 * have different sections with the same names as:<br>
 * <p/>
 * global_name1=z1 <br>
 * sec_name=v      <br>
 * x1=2            <br>
 * y1=0            <br>
 * [section_1]     <br>
 * x1=${x1}        <br>
 * [section2]      <br>
 * x1=zxc          <br>
 * [${sec_name}]   <br>
 * ...             <br>
 *
 * @author Serguei Eremenko
 * @version 1.0
 *          <p/>
 *          http://articles.techrepublic.com.com/5100-10878_11-1049526.html#
 */
public class PropertiesExpansion extends Properties
{

    /**
     * Creates an empty property list with no default values.
     */
    public PropertiesExpansion()
    {
        super();
    }

    /**
     * Creates an empty property list with the specified defaults.
     *
     * @param def the defaults.
     */
    public PropertiesExpansion(Properties def)
    {
        super(def);
    }

    /**
     * Searches for the property with the specified key in this property list
     * and substitutes any ${} occurrence that may be in the key.
     *
     * @param key the property key.
     * @return the value in this property list with the specified key value or
     *         null if not found.
     */
    public String getProperty(String key)
    {
        return replace(super.getProperty(replace(key)));
    }

    /**
     * Searches for the property within the specified section and with the
     * specified key in this property list and substitutes any ${} occurrence
     * that may be in the key.
     *
     * @param section the property section.
     * @param key     the property key.
     * @return the value in this property list within the specified section and
     *         with the specified key value or null if not found.
     */
    public String getProperty(String section, String key)
    {
        Properties p = (Properties) get(replace(section));
        if (p == null)
        {
            return null;
        }
        return replace(p.getProperty(replace(key)));
    }

    /**
     * @param section the property section.
     * @param key     the property key.
     * @param def     the default value.
     * @return the value in this property list within the specified section and
     *         with the specified key value or this default value.
     */
    public String getProperty(String section, String key, String def)
    {
        String s = getProperty(section, key);
        if (s == null)
        {
            s = def;
        }
        return replace(s);
    }

    /**
     * Casts a found property to its integer value
     *
     * @param prop the Properties to be searched in
     * @param key  the property key
     * @param def  the default value
     * @return the found value cast to integer or the default value
     * @see #getProperty
     */
    public static int getInt(Properties prop, String key, int def)
    {
        int out = def;
        try
        {
            out = getInt(prop, key);
        } catch (RuntimeException e)
        {
        }
        return out;
    }

    /**
     * @see #getProperty
     */
    public static int getInt(Properties prop, String key)
    {
        String s = prop.getProperty(key);
        if (s == null)
        {
            throw new NoSuchElementException("No key " + key + " found");
        }
        return Integer.parseInt(s);
    }

    public static long getLong(Properties prop, String key, long def)
    {
        long out = def;
        try
        {
            out = getLong(prop, key);
        } catch (RuntimeException e)
        {
        }
        return out;
    }

    public static long getLong(Properties prop, String key)
    {
        String s = prop.getProperty(key);
        if (s == null)
        {
            throw new NoSuchElementException("No key " + key + " found");
        }
        return Long.parseLong(s);
    }

    public static float getFloat(Properties prop, String key, float def)
    {
        float out = def;
        try
        {
            out = getFloat(prop, key);
        } catch (RuntimeException e)
        {
        }
        return out;
    }

    public static float getFloat(Properties prop, String key)
    {
        String s = prop.getProperty(key);
        if (s == null)
        {
            throw new NoSuchElementException("No key " + key + " found");
        }
        return Float.parseFloat(s);
    }

    public static double getDouble(Properties prop, String key, double def)
    {
        double out = def;
        try
        {
            out = getDouble(prop, key);
        } catch (RuntimeException e)
        {
        }
        return out;
    }

    public static double getDouble(Properties prop, String key)
    {
        String s = prop.getProperty(key);
        if (s == null)
        {
            throw new NoSuchElementException("No key " + key + " found");
        }
        return Double.parseDouble(s);
    }

    public static boolean getBoolean(Properties prop, String key, boolean def)
    {
        boolean out = def;
        try
        {
            out = getBoolean(prop, key);
        } catch (RuntimeException e)
        {
        }
        return out;
    }

    public static boolean getBoolean(Properties prop, String key)
    {
        String s = prop.getProperty(key);
        if (s == null)
        {
            throw new NoSuchElementException("No key " + key + " found");
        }
        return new Boolean(s).booleanValue();
    }

    public int getInt(String key, int def)
    {
        int out = def;
        try
        {
            out = getInt(key);
        } catch (RuntimeException e)
        {
        }
        return out;
    }

    public int getInt(String key)
    {
        String s = getProperty(key);
        if (s == null)
        {
            throw new NoSuchElementException("No key " + key + " found");
        }
        return Integer.parseInt(s);
    }

    public long getLong(String key, long def)
    {
        long out = def;
        try
        {
            out = getLong(key);
        } catch (RuntimeException e)
        {
        }
        return out;
    }

    public long getLong(String key)
    {
        String s = getProperty(key);
        if (s == null)
        {
            throw new NoSuchElementException("No key " + key + " found");
        }
        return Long.parseLong(s);
    }

    public float getFloat(String key, float def)
    {
        float out = def;
        try
        {
            out = getFloat(key);
        } catch (RuntimeException e)
        {
        }
        return out;
    }

    public float getFloat(String key)
    {
        String s = getProperty(key);
        if (s == null)
        {
            throw new NoSuchElementException("No key " + key + " found");
        }
        return Float.parseFloat(s);
    }

    public double getDouble(String key, double def)
    {
        double out = def;
        try
        {
            out = getDouble(key);
        } catch (RuntimeException e)
        {
        }
        return out;
    }

    public double getDouble(String key)
    {
        String s = getProperty(key);
        if (s == null)
        {
            throw new NoSuchElementException("No key " + key + " found");
        }
        return Double.parseDouble(s);
    }

    public boolean getBoolean(String key, boolean def)
    {
        boolean out = def;
        try
        {
            out = getBoolean(key);
        } catch (RuntimeException e)
        {
        }
        return out;
    }

    public boolean getBoolean(String key)
    {
        String s = getProperty(key);
        if (s == null)
        {
            throw new NoSuchElementException("No key " + key + " found");
        }
        return new Boolean(s).booleanValue();
    }

    /**
     * @param key   the key to be placed into this property list.
     * @param value the value corresponding to key.
     * @return the previous value of the specified key in this property list,
     *         or null if it did not have one.
     */
    public Object setProperty(String key, String value)
    {
        return super.setProperty(replace(key), replace(value));
    }

    /**
     * @param section the section where the key to be placed into this property
     *                list, the section is created if the property list did not have one.
     * @param key     the key to be placed into this property list.
     * @param value   the value corresponding to key.
     * @return the previous value of the specified key in this property list,
     *         or null if it did not have one.
     */
    public Object setProperty(String section, String key, String value)
    {
        Properties p = (Properties) get(replace(section));
        if (p == null)
        {
            p = new Properties();
            put(replace(section), p);
        }
        return p.setProperty(key, replace(value));
    }

    /**
     * @return an enumeration of the sections in this property list.
     */
    public synchronized Enumeration sections()
    {
        final int len = keySet().size();
        final String[] all = (String[]) keySet().toArray(new String[len]);
        return new Enumeration()
        {

            public boolean hasMoreElements()
            {
                next = null;
                while (i < len)
                {
                    String key = all[i++];
                    if (get(key) instanceof Properties)
                    {
                        next = key;
                        break;
                    }
                    else
                    {
                        next = null;
                    }
                }
                return next != null;
            }

            public Object nextElement()
            {
                if (next == null)
                {
                    throw new NoSuchElementException();
                }
                return next;
            }

            private Object next = null;
            private int i = 0;
        };
    }

    /**
     * @param section the section name where to enumerate the keys from.
     * @return an enumeration of the section keys in this property list or
     *         empty enumeration if there is no such section or the section
     *         does not contain any keys.
     */
    public synchronized Enumeration sectionKeys(String section)
    {
        Properties p = (Properties) get(replace(section));
        Enumeration en = null;
        if (p == null)
        {
            en = new Enumeration()
            {

                public boolean hasMoreElements()
                {
                    return false;
                }

                public Object nextElement()
                {
                    throw new NoSuchElementException();
                }
            };
        }
        else
        {
            en = p.keys();
        }
        return en;
    }

    /**
     * Overrides the Hashtable's put method and its use is strongly discouraged.
     *
     * @see java.util.Hashtable#put
     * @see #setProperty
     */
    public Object put(Object key, Object val)
    {
        if (key == null || val == null)
        {
            throw new NullPointerException();
        }
        Object o = null;
        if (key instanceof String && val instanceof String)
        {
            String s = (String) key;
            int i = s.indexOf(startSecSep);
            if (i == 0)
            {
                curSec = replace(s.substring(i + 1, s.indexOf(endSecSep, i + 2)));
                Properties p = (Properties) get(curSec);
                if (p == null)
                {
                    p = new Properties();
                    o = super.put(curSec, p);
                }
            }
            else
            {
                Properties p = (Properties) get(curSec);
                if (p != null)
                {
                    o = p.put(key, val);
                }
                else
                {
                    o = super.put(key, val);
                }
            }
        }
        else
        {
            o = super.put(key, val);
        }
        return o;
    }

    /**
     * Reads a property list (key and element pairs) from the input stream.
     *
     * @param is the input stream.
     */
    public synchronized void load(InputStream is) throws IOException
    {
        curSec = "";
        super.load(is);
        curSec = "";
        replaceAll(this);
    }

    /**
     * Writes this property list (key and element pairs) in this Properties
     * table to the output stream in a format suitable for loading into a
     * Properties table using the load method.
     *
     * @param os     an output stream.
     * @param header a description of the property list.
     */
    public synchronized void store(OutputStream os, String header)
            throws IOException
    {
        Properties p = null;
        for (Enumeration en = keys(); en.hasMoreElements(); )
        {
            String k = (String) en.nextElement();
            Object v = get(k);
            if (v instanceof String)
            {
                if (p == null)
                {
                    p = new Properties();
                }
                p.put(k, v);
            }
        }
        if (p != null)
        {
            p.store(os, header);
        }
        for (Enumeration en = keys(); en.hasMoreElements(); )
        {
            String k = (String) en.nextElement();
            Object v = get(k);
            if (v instanceof Properties)
            {
                byte[] b = (startSecSep + k + endSecSep + "\r\n").getBytes();
                os.write(b, 0, b.length);
                os.flush();
                ((Properties) v).store(os, null);
            }
        }
    }

    /**
     * @param in   the string where substitutable parameters ${} will be replaces
     *             with values contained in the hashtable of keys
     * @param keys the hashtable of keys
     * @return the substituted string
     */
    public static String replace(String in, Hashtable keys)
    {
        if (in == null)
        {
            return null;
        }
        if (keys == null)
        {
            throw new NullPointerException("Keys source is null");
        }
        StringBuffer out = new StringBuffer();
        int index = 0;
        int i = 0;
        String key = null;
        while ((index = in.indexOf(startTag, i)) > -1)
        {
            key = in.substring(index + 2, in.indexOf(endTag, index + 3));
            out.append(in.substring(i, index));
            if (keys.containsKey(key))
            {
                out.append(keys.get(key));
            }
            else
            {
                out.append(startTag).append(key).append(endTag);
            }
            i = index + 3 + key.length();
        }
        return out.append(in.substring(i)).toString();
    }

    /**
     * @param in the key which string value to be replaced with values from
     *           this property list
     */
    protected String replace(String in)
    {
        if (in == null)
        {
            return null;
        }
        StringBuffer out = new StringBuffer();
        int index = 0;
        int i = 0;
        String key = null;
        while ((index = in.indexOf(startTag, i)) > -1)
        {
            key = in.substring(index + 2, in.indexOf(endTag, index + 3));
            out.append(in.substring(i, index));
            String val = super.getProperty(key);
            if (val != null)
            {
                // be careful here
                out.append(replace(val));
            }
            else
            {
                out.append(startTag).append(key).append(endTag);
            }
            i = index + 3 + key.length();
        }
        return out.append(in.substring(i)).toString();
    }

    /**
     * Replaces all occurrences of the substitution tag in the given Properties.
     */
    protected void replaceAll(Properties p)
    {
        for (Enumeration en = p.keys(); en.hasMoreElements(); )
        {
            String k = (String) en.nextElement();
            Object v = p.get(k);
            if (v instanceof String)
            {
                String nv = replace((String) v);
                p.put(k, nv);
            }
            if (v instanceof Properties)
            {
                replaceAll((Properties) v);
            }
        }
    }

    protected static char startSecSep = '[';
    protected static char endSecSep = ']';
    protected static String startTag = "${";
    protected static String endTag = "}";
    private String curSec = "";
}
