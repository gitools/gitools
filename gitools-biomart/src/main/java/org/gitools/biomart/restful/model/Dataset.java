
package org.gitools.biomart.restful.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;


@XmlAccessorType(XmlAccessType.FIELD)
public class Dataset {

    @XmlAttribute(required = true)
    protected String name;

	@XmlAttribute(name = "interface")
	protected String _interface;

    @XmlElement(name = "Filter")
    protected List<Filter> filter = new ArrayList<Filter>(0);

	@XmlElement(name = "ValueFilter")
    protected List<Filter> valueFilter = new ArrayList<Filter>(0);

    @XmlElement(name = "Attribute", required = true)
    protected List<Attribute> attribute = new ArrayList<Attribute>(0);

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
        return this.filter;
    }

	public List<Filter> getValueFilter() {
		return valueFilter;
	}
	
    public List<Attribute> getAttribute() {
        return this.attribute;
    }
}
