/*
 * #%L
 * org.gitools.matrix
 * %%
 * Copyright (C) 2013 - 2016 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.matrix.transform.parameters;


import com.jgoodies.binding.beans.Model;
import org.gitools.api.matrix.IFunctionParameter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractFunctionParameter<T> extends Model implements IFunctionParameter<T> {

    boolean allowNull = false;
    String description = "";

    List<T> choices;

    public static final String PROPERTY_PARAMETER_VALUE = "parameterValue";
    T parameterValue;

    public AbstractFunctionParameter() {
    }

    @Override
    public boolean validate(T parameter) {

        if(!allowNull && (parameter == null)) {
            return false;
        } else if (hasChoices() && !choices.contains(parameter)) {
            return false;
        }

        return true;
    };

    @Override
    public boolean validate() {
        return validate(parameterValue);
    }

    @Override
    public T getParameterValue() {
        return parameterValue;
    }

    @Override
    public void setParameterValue(T parameterValue) {
        final T oldValue = this.parameterValue;
        this.parameterValue = parameterValue;
        firePropertyChange(PROPERTY_PARAMETER_VALUE, oldValue, parameterValue);
    }

    @Override
    public boolean hasChoices() {
        return choices != null && choices.size() > 0;
    }

    @Override
    public List<T> getChoices() {
        return choices;
    }

    @Override
    public void setChoices(List<T> choices) {
        this.choices = choices;
        parameterValue = choices.get(0);
    }

    @Override
    public void setChoices(T[] choices) {
        ArrayList<T> choicesList = new ArrayList<>();
        Collections.addAll(choicesList, choices);
        setChoices(choicesList);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

}