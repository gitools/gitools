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
package org.gitools.analysis.stats.test.results;

import org.gitools.analysis._DEPRECATED.matrix.model.matrix.element.LayerDef;

public class FisherResult extends CommonResult {

    private int a;
    private int b;
    private int c;
    private int d;

    public FisherResult() {
        super(0, 0.0, 0.0, 0.0);
        a = b = c = d = 0;
    }

    public FisherResult(int n, double leftPvalue, double rightPvalue, double twoTailPvalue, int a, int b, int c, int d) {

        super(n, leftPvalue, rightPvalue, twoTailPvalue);

        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    @LayerDef(id = "a", name = "a", description = "Number of positive events that belongs to the module")
    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    @LayerDef(id = "b", name = "b", description = "Number of no positive events that belongs to the module")
    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    @LayerDef(id = "c", name = "c", description = "Number of positive events that don't belong to the module")
    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }

    @LayerDef(id = "d", name = "d", description = "Number of no positive events that don't belong to the module")
    public int getD() {
        return d;
    }

    public void setD(int d) {
        this.d = d;
    }
}
