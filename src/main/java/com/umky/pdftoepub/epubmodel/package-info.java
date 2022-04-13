@XmlSchema(
        elementFormDefault= XmlNsForm.QUALIFIED,
        xmlns={ @XmlNs(prefix="opf", namespaceURI="http://www.idpf.org/2007/opf"),
        @XmlNs(prefix="dc", namespaceURI="http://purl.org/dc/elements/1.1/"),
        @XmlNs(prefix = "epub",namespaceURI = "http://www.idpf.org/2007/ops")
        }
)
package com.umky.pdftoepub.epubmodel;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;