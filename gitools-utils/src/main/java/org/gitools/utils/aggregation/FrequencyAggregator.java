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
package org.gitools.utils.aggregation;

import org.gitools.api.analysis.IAggregator;

/**
 * Multiplication
 */
public class FrequencyAggregator extends AbstractAggregator {

    public final static IAggregator INSTANCE = new FrequencyAggregator();

    private FrequencyAggregator() {
    }

    @Override
    public double aggregate(double[] data) {

        double length = (double) data.length;
        double events = 0.0;


        for (double d : data) {
            if (!(Double.isNaN(d) || d == 0)) {
                events += 1;
            }
        }

        if (events == 0.0 || length == 0.0) {
            return 0.0;
        } else {
            return events / length;
        }
    }


    @Override
    public String toString() {
        return "Frequency (Non-zero)";
    }
}
