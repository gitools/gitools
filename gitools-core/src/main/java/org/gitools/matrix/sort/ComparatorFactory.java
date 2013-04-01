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
package org.gitools.matrix.sort;

import java.text.Collator;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ComparatorFactory
{

    private static final Map<Class<? extends Object>, Comparator> list =
            new HashMap<Class<? extends Object>, Comparator>();

    static
    {
        list.put(Float.class, new Comparator<Float>()
        {
            @Override
            public int compare(Float o1, Float o2)
            {
                return (int) (o1 - o2);
            }
        });

        list.put(Double.class, new Comparator<Double>()
        {
            @Override
            public int compare(Double o1, Double o2)
            {
                return (int) (o1 - o2);
            }
        });

        list.put(Integer.class, new Comparator<Integer>()
        {
            @Override
            public int compare(Integer o1, Integer o2)
            {
                return o1 - o2;
            }
        });

        list.put(Long.class, new Comparator<Long>()
        {
            @Override
            public int compare(Long o1, Long o2)
            {
                return (int) (o1 - o2);
            }
        });

        list.put(String.class, new Comparator<String>()
        {
            @Override
            public int compare(String o1, String o2)
            {
                Collator collator = Collator.getInstance();
                return collator.compare(o1, o2);
            }
        });
    }

    public Comparator create(Class<? extends Object> cls)
    {
        return list.get(cls);
    }
}
