package org.gitools.ui.fileimport.wizard.excel;

import java.io.Serializable;

public class ExcelHeader implements Serializable {

    private int pos;
    private int type;
    private String label;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExcelHeader that = (ExcelHeader) o;

        if (pos != that.pos) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return pos;
    }
}
