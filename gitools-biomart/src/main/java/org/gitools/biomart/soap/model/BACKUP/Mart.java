
package org.gitools.biomart.soap.model.BACKUP;
 
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for mart complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="mart">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="displayName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="database" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="host" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="path" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="port" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="visible" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="default" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="serverVirtualSchema" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="includeDatasets" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="martUser" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="redirect" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mart", propOrder = {
    "name",
    "displayName",
    "database",
    "host",
    "path",
    "port",
    "visible",
    "_default",
    "serverVirtualSchema",
    "includeDatasets",
    "martUser",
    "redirect"
})
public class Mart {

    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected String displayName;
    @XmlElement(required = true)
    protected String database;
    @XmlElement(required = true)
    protected String host;
    @XmlElement(required = true)
    protected String path;
    @XmlElement(required = true)
    protected String port;
    protected int visible;
    @XmlElement(name = "default")
    protected int _default;
    @XmlElement(required = true)
    protected String serverVirtualSchema;
    @XmlElement(required = true)
    protected String includeDatasets;
    @XmlElement(required = true)
    protected String martUser;
    protected int redirect;

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

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the displayName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the value of the displayName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisplayName(String value) {
        this.displayName = value;
    }

    /**
     * Gets the value of the database property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatabase() {
        return database;
    }

    /**
     * Sets the value of the database property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatabase(String value) {
        this.database = value;
    }

    /**
     * Gets the value of the host property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets the value of the host property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHost(String value) {
        this.host = value;
    }

    /**
     * Gets the value of the path property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the value of the path property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPath(String value) {
        this.path = value;
    }

    /**
     * Gets the value of the port property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPort() {
        return port;
    }

    /**
     * Sets the value of the port property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPort(String value) {
        this.port = value;
    }

    /**
     * Gets the value of the visible property.
     * 
     */
    public int getVisible() {
        return visible;
    }

    /**
     * Sets the value of the visible property.
     * 
     */
    public void setVisible(int value) {
        this.visible = value;
    }

    /**
     * Gets the value of the default property.
     * 
     */
    public int getDefault() {
        return _default;
    }

    /**
     * Sets the value of the default property.
     * 
     */
    public void setDefault(int value) {
        this._default = value;
    }

    /**
     * Gets the value of the serverVirtualSchema property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServerVirtualSchema() {
        return serverVirtualSchema;
    }

    /**
     * Sets the value of the serverVirtualSchema property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServerVirtualSchema(String value) {
        this.serverVirtualSchema = value;
    }

    /**
     * Gets the value of the includeDatasets property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIncludeDatasets() {
        return includeDatasets;
    }

    /**
     * Sets the value of the includeDatasets property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIncludeDatasets(String value) {
        this.includeDatasets = value;
    }

    /**
     * Gets the value of the martUser property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMartUser() {
        return martUser;
    }

    /**
     * Sets the value of the martUser property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMartUser(String value) {
        this.martUser = value;
    }

    /**
     * Gets the value of the redirect property.
     * 
     */
    public int getRedirect() {
        return redirect;
    }

    /**
     * Sets the value of the redirect property.
     * 
     */
    public void setRedirect(int value) {
        this.redirect = value;
    }

}
