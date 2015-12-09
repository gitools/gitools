package org.gitools.api.matrix;

import java.util.List;

/**
 * Created by mpschr on 09.12.15.
 */
public interface IFunctionParameter<T> {
    boolean validate(T parameter);

    boolean validate();

    T getParameterValue();

    void setParameterValue(T parameterValue);

    boolean hasChoices();

    List<T> getChoices();

    void setChoices(List<T> choices);

    void setChoices(T[] choices);

    String getDescription();

    void setDescription(String description);
}
