package org.gitools.core.matrix.model.iterable;

import org.gitools.core.matrix.model.IMatrixDimension;
import org.gitools.core.matrix.model.IMatrixPredicate;
import org.gitools.core.matrix.model.IMatrixPosition;

import java.util.HashSet;
import java.util.Set;


public class IdentifiersPredicate<T> implements IMatrixPredicate<T> {

    private IMatrixDimension iterationDimension;
    private Set<String> identifiers;

    public IdentifiersPredicate(IMatrixDimension iterationDimension, Iterable<String> identifiers) {

        this.iterationDimension = iterationDimension;
        this.identifiers = new HashSet<>();

        for (String identifier : identifiers) {
            this.identifiers.add(identifier);
        }

    }

    @Override
    public boolean apply(T value, IMatrixPosition position) {
        return identifiers.contains(position.get(iterationDimension));
    }
}
