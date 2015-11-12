@XmlSchema(
    namespace = "http://www.opsource.net/simpleapp",
    elementFormDefault = XmlNsForm.QUALIFIED,
    xmlns = {
        @XmlNs(prefix="xs2", namespaceURI="http://www.opsource.net/simpleapp")
    }
)
package model;
import javax.xml.bind.annotation.*;