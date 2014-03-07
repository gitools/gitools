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
package org.gitools.analysis.stats.test;

import org.gitools.analysis.stats.test.results.CommonResult;

public abstract class AbstractTest implements Test {

    private String name;
    private Class<? extends CommonResult> resultClass;

    protected AbstractTest(String name, Class<? extends CommonResult> resultClass) {
        this.name = name;
        this.resultClass = resultClass;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<? extends CommonResult> getResultClass() {
        return resultClass;
    }

    @Override
    public void processPopulation(Iterable<Double> population) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CommonResult processTest(Iterable<Double> values) {
        throw new UnsupportedOperationException();
    }
}
