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
package org.gitools.stats.test.results;

import org.gitools.matrix.model.element.AttributeDef;

import java.io.Serializable;

public class CommonResult implements Serializable
{

    public int N;
    public double leftPvalue;
    public double rightPvalue;
    public double twoTailPvalue;
    public double corrLeftPvalue;
    public double corrRightPvalue;
    public double corrTwoTailPvalue;

    public CommonResult(
            int N,
            double leftPvalue, double rightPvalue,
            double twoTailPvalue)
    {

        this.leftPvalue = leftPvalue;
        this.N = N;
        this.rightPvalue = rightPvalue;
        this.twoTailPvalue = twoTailPvalue;
    }

    @AttributeDef(id = "N", name = "N", description = "Number of elements")
    public int getN()
    {
        return N;
    }

    public void setN(int n)
    {
        N = n;
    }

    @AttributeDef(id = "left-p-value", name = "Left P-Value", description = "P-Value for alternative hipothesis lower than")
    public double getLeftPvalue()
    {
        return leftPvalue;
    }

    public void setLeftPvalue(double leftPvalue)
    {
        this.leftPvalue = leftPvalue;
    }

    @AttributeDef(id = "right-p-value", name = "Right P-Value", description = "P-Value for alternative hipothesis greater than")
    public double getRightPvalue()
    {
        return rightPvalue;
    }

    public void setRightPvalue(double rightPvalue)
    {
        this.rightPvalue = rightPvalue;
    }

    @AttributeDef(id = "two-tail-p-value", name = "Two tail P-Value", description = "P-Value for alternative hipothesis different than")
    public double getTwoTailPvalue()
    {
        return twoTailPvalue;
    }

    public void setTwoTailPvalue(double twoTailPvalue)
    {
        this.twoTailPvalue = twoTailPvalue;
    }

    @AttributeDef(id = "corrected-left-p-value", name = "Corrected left P-Value", description = "Corrected P-Value for alternative hipothesis lower than")
    public double getCorrLeftPvalue()
    {
        return corrLeftPvalue;
    }

    public void setCorrLeftPvalue(double corrLeftPvalue)
    {
        this.corrLeftPvalue = corrLeftPvalue;
    }

    @AttributeDef(id = "corrected-right-p-value", name = "Corrected right P-Value", description = "Corrected P-Value for alternative hipothesis greater than")
    public double getCorrRightPvalue()
    {
        return corrRightPvalue;
    }

    public void setCorrRightPvalue(double corrRightPvalue)
    {
        this.corrRightPvalue = corrRightPvalue;
    }

    @AttributeDef(id = "corrected-two-tail-p-value", name = "Corrected two tail P-Value", description = "Corrected P-Value for alternative hipothesis different than")
    public double getCorrTwoTailPvalue()
    {
        return corrTwoTailPvalue;
    }

    public void setCorrTwoTailPvalue(double corrTwoTailPvalue)
    {
        this.corrTwoTailPvalue = corrTwoTailPvalue;
    }
}
