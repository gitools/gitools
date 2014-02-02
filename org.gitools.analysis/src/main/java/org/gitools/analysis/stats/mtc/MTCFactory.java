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
package org.gitools.analysis.stats.mtc;

import org.gitools.api.matrix.IMatrixFunction;

import java.util.HashMap;
import java.util.Map;

public class MTCFactory {

    private static final Map<String, Class<? extends MTC>> nameMap = new HashMap<>();

    private static final Map<Class<? extends MTC>, String> classMap = new HashMap<>();

    static {
        nameMap.put(Bonferroni.SHORT_NAME, Bonferroni.class);
        nameMap.put(BenjaminiHochbergFdr.SHORT_NAME, BenjaminiHochbergFdr.class);

        for (Map.Entry<String, Class<? extends MTC>> e : nameMap.entrySet())
            classMap.put(e.getValue(), e.getKey());
    }

    public static MTC createFromName(String name) {
        MTC mtc = null;
        try {
            Class<? extends MTC> cls = nameMap.get(name);
            mtc = cls.newInstance();
        } catch (Exception ex) {
            return null;
        }
        return mtc;
    }

    public static IMatrixFunction<Double, Double> createFunction(MTC mtc) {

        if (mtc instanceof Bonferroni) {
            return new BonferroniMtcFunction();
        }

        return new BenjaminiHochbergFdrMtcFunction();
    }
}
