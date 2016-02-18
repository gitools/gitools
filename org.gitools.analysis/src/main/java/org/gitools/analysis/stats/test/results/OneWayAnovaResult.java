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

import org.gitools.matrix.model.matrix.element.LayerDef;


public class OneWayAnovaResult extends SimpleResult {

    private int minN;
    private int groups;
    private double Fvalue;

    public OneWayAnovaResult() {
        super(0, 0.0);
        minN = 0;
        groups = 0;
        Fvalue = 0.0;
    }

    public OneWayAnovaResult(int N, int minN, int groups,
                             double twoTailPvalue,
                             double Fvalue) {

        super(N, twoTailPvalue);
        this.minN = minN;
        this.Fvalue = Fvalue;
        this.groups = groups;
    }

    @LayerDef(id = "minN",
            name = "Min N",
            description = "Group with smallest N",
            groups = {SimpleResult.TEST_DETAILS_GROUP, LayerDef.ALL_DATA_GROUP})
    public int getMinN() {
        return minN;
    }

    public void setMinN(int minN) {
        this.minN = minN;
    }

    @LayerDef(id = "groups",
            name = "Groups",
            description = "Number of groups with data considered for test",
            groups = {SimpleResult.TEST_DETAILS_GROUP, LayerDef.ALL_DATA_GROUP})
    public int getGroups() {
        return groups;
    }

    public void setGroups(int groups) {
        this.groups = groups;
    }


    @LayerDef(id = "F-value",
            name = "F-value",
            description = "F-Value of the one-way ANOVA test",
            groups = {RESULTS_GROUP, LayerDef.ALL_DATA_GROUP})
    public double getFvalue() {
        return Fvalue;
    }

    public void setFvalue(double Fvalue) { this.Fvalue = Fvalue; }
}
