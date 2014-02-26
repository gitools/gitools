package org.gitools.heatmap;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Bookmark implements Serializable {

    String name;
    List<String> rows;
    List<String> columns;

    public Bookmark(String name, List<String> rows, List<String> columns) {
        this.name = name;
        this.rows = rows;
        this.columns = columns;
    }


    public String getName() {
        return name;
    }

    public List<String> getRows() {
        return rows;
    }

    public List<String> getColumns() {
        return columns;
    }

}
