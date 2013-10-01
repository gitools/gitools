/*
 * #%L
 * gitools-ui-app
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
package org.gitools.ui.fileimport.wizard.excel;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * @noinspection ALL
 */
public class ExcelHeader implements Serializable {

    private final int pos;
    private final int type;
    private final String label;

    public ExcelHeader(String label, int pos, int type) {
        this.label = label;
        this.pos = pos;
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public int getPos() {
        return pos;
    }

    public int getType() {
        return type;
    }

    public String toString() {
        return label;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ExcelHeader that = (ExcelHeader) o;

        if (pos != that.pos) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return pos;
    }
}
