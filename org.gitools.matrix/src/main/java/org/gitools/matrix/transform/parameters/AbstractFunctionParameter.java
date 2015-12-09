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