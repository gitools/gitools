
package org.gitools.biomart.restful.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Attribute", propOrder = {
    "name"
})
public class Attribute {

    @XmlAttribute(required = true)
    protected String name;

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

}
