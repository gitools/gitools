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
package org.gitools.matrix.model.matrix;

import org.gitools.utils.textpatt.TextPattern.VariableValueResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnnotationResolver implements VariableValueResolver
{

    private static final String DEFAULT_NA = "";

    private final AnnotationMatrix am;

    private String label;
    private int annRow;

    private final String na;

    public AnnotationResolver(AnnotationMatrix am)
    {
        this(am, null);
    }

    public AnnotationResolver(AnnotationMatrix am, String label)
    {
        this(am, label, DEFAULT_NA);
    }

    public AnnotationResolver(AnnotationMatrix am, @Nullable String label, String na)
    {
        this.am = am;
        if (label != null)
        {
            setLabel(label);
        }
        this.na = na;
    }

    final void setLabel(String label)
    {
        this.label = label;

        if (am != null)
        {
            annRow = am.internalRowIndex(label);
        }
    }

    @Override
    public String resolveValue(@NotNull String variableName)
    {
        if (variableName.equalsIgnoreCase("id"))
        {
            return label;
        }

        if (annRow == -1)
        {
            return na;
        }

        int annCol = am != null ? am.internalColumnIndex(variableName) : -1;
        if (annCol == -1)
        {
            return "${" + variableName + "}";
        }

        return am.getCellAsString(annRow, annCol);
    }
}
