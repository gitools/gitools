package org.gitools.matrix.transform.parameters;


import org.gitools.utils.cutoffcmp.CutoffCmp;

public class CutoffCmpParameter extends AbstractFunctionParameter<CutoffCmp> {

    @Override
    public boolean validate(CutoffCmp parameter) {

        super.validate(parameter);

        return true;
    }

}
