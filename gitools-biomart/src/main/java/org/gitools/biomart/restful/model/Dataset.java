
package org.gitools.biomart.restful.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Dataset", propOrder = {
    "name",
    "interface"
})
public class Dataset {

    @XmlAttribute(required = true)
    protected String name;

	@XmlAttribute(name = "interface")
	protected String _interface;

    @XmlElement(name = "Filter")
    protected List<Filter> filter;

    @XmlElement(name = "Attribute", required = true)
    protected List<Attribute> attribute;

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

	public String getInterface() {
		return _interface;
	}

	public void setInterface(String _interface) {
		this._interface = _interface;
	}

    public List<Filter> getFilter() {
        if (filter == null) {
            filter = new ArrayList<Filter>();
        }
        return this.filter;
    }

    public List<Attribute> getAttribute() {
        if (attribute == null) {
            attribute = new ArrayList<Attribute>();
        }
        return this.attribute;
    }
}
