
package org.gitools.biomart.restful.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Filter", propOrder = {
    "name",
    "value",
	"excluded"
})
public class Filter {

    @XmlAttribute(required = true)
    protected String name;

    @XmlAttribute
    protected String value;

	@XmlAttribute
    protected int excluded;

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

	public int getExcluded() {
		return excluded;
	}

	public void setExcluded(int excluded) {
		this.excluded = excluded;
	}
}
