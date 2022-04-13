package com.umky.pdftoepub.epubmodel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

/**
 * REQUIRED third child of package, following manifest.
 * The order of the itemref elements defines the default reading order of the given Rendition.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class EPUBSpine {

    //1 or more
    @XmlElement(name = "opf:itemref",required = true)
    private List<EPUBSpineItemref> itemrefList = new ArrayList<>();

    public List<EPUBSpineItemref> getItemrefList() {
        return itemrefList;
    }

    public void addItemref(EPUBSpineItemref itemref) {
        itemrefList.add(itemref);
    }
}
