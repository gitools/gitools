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

import org.gitools.api.matrix.IMatrixPosition;
import org.gitools.api.matrix.IMatrixPredicate;
import org.gitools.matrix.filter.DataIntegrationCriteria;
import org.gitools.utils.operators.Operator;

import java.util.ArrayList;
import java.util.List;

public class GroupByValuePredicate implements IMatrixPredicate<Double> {

    private List<DataIntegrationCriteria> criteriaList;

    public GroupByValuePredicate(List<DataIntegrationCriteria> criteriaList) {
        this.criteriaList = criteriaList;
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

        ArrayList<Boolean> ORs = new ArrayList<Boolean>();

        for (DataIntegrationCriteria dic : criteriaList) {

            Object v = position.getMatrix().get(dic.getLayer(), position);
            if (v == null && dic.getNullConversion() == null) {
                return false;
            }

            double matrixValue = (v == null) ? dic.getNullConversion() : (double) v;
            boolean evaluatedCondition = dic.getComparator().compare(matrixValue, dic.getCutoffValue());

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
