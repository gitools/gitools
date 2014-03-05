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
package org.gitools.analysis.stats.calc;

public class OnesCountStatistic implements Statistic {

    @Override
    public String getName() {
        return "count-of-ones";
    }

    @Override
    public Double calc(Iterable<Double> values) {

        Double count = 0.0;
        int size = 0;

        for (Double value : values) {
            size++;
            count += (value == 1.0 ? 1.0 : 0.0);
        }

        if (size == 0) {
            return null;
        }

        return count;
    }

}
