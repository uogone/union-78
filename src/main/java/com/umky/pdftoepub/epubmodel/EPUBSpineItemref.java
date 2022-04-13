package com.umky.pdftoepub.epubmodel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class EPUBSpineItemref {

    @XmlAttribute(required = true)
    private String idref;

    public EPUBSpineItemref() {
    }

    public EPUBSpineItemref(String idref) {
        this.idref = idref;
    }

    public String getIdref() {
        return idref;
    }

    public void setIdref(String idref) {
        this.idref = idref;
    }
}
