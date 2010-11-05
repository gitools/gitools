
package org.gitools.biomart.restful.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
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
	
	// Only in case of radio components since
	// a query in xml with this type of components is different
	@XmlTransient
    protected Boolean radio=false;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */

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
	
    /**
     * Gets the value of the value property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
	public Boolean getRadio() {
		return radio;
	}


    /**
     * Sets the value of the value property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
	public void setRadio(Boolean radio) {
		this.radio = radio;
	}

	public int getExcluded() {
		return excluded;
	}

	public void setExcluded(int excluded) {
		this.excluded = excluded;
	}

}
