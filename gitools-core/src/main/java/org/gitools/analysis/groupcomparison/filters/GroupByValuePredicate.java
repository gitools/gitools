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
package org.gitools.analysis.groupcomparison.filters;

import org.gitools.api.matrix.position.IMatrixPosition;
import org.gitools.api.matrix.position.IMatrixPredicate;
import org.gitools.core.matrix.filter.DataIntegrationCriteria;
import org.gitools.utils.operators.Operator;

import java.util.ArrayList;
import java.util.List;

public class GroupByValuePredicate implements IMatrixPredicate<Double> {

    private List<DataIntegrationCriteria> criteriaList;
    private Double nullValue;

    public GroupByValuePredicate(List<DataIntegrationCriteria> criteriaList, Double nullValue) {
        this.criteriaList = criteriaList;
        this.nullValue = nullValue;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (DataIntegrationCriteria c : criteriaList) {
            if (first) {
                first = false;
            } else {
                sb.append(" ");
            }
            sb.append(c.toString());
        }
        return sb.toString();
    }

    public boolean apply(Double value, IMatrixPosition position) {

/*
        Double cutoffValue = position.getMatrix().get(cutoffLayer, position);
        if (cutoffValue == null) {
            cutoffValue = nullValue;
        }
        return (cutoffValue != null) && (binaryCutoff.apply(cutoffValue) == 1.0);
    }     */
        ArrayList<Boolean> ORs = new ArrayList<Boolean>();

        for (DataIntegrationCriteria dic : criteriaList) {

            double matrixValue = (double) position.getMatrix().get(dic.getLayer(), position);
            boolean evaluatedCondition = dic.getComparator().compare(matrixValue, dic.getValue());

            if (dic.getOperator().equals(Operator.EMPTY)) {
                ORs.add(evaluatedCondition);
            } else if (dic.getOperator().equals(Operator.OR)) {
                ORs.add(evaluatedCondition);
            } else {
                Boolean lastOr;
                lastOr = ORs.get(ORs.size() - 1);
                ORs.remove(lastOr);
                ORs.add(dic.getOperator().evaluate(lastOr, evaluatedCondition));
            }
        }
        for (Boolean or : ORs)
            if (or)
                return true;
        return false;

    }

}
