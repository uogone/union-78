package com.umky.pdftoepub.epubmodel;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

/**
 * [REQUIRED] second child of EPUBPackage, following metadata.
 * All Publication Resources MUST be referenced from the manifest,
 * regardless of whether they are Local or Remote Resources.
 * Exactly one item MUST be declared as the EPUB Navigation Document using the nav property.
 */
public class EPUBManifest {

    private List<EPUBManifestItem> itemList = new ArrayList<>();;

    //item element [1 or more]
    @XmlElement(name = "opf:item",required = true)
    public List<EPUBManifestItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<EPUBManifestItem> items) {
        this.itemList = items;
    }

    public void addItem(EPUBManifestItem item) {
        itemList.add(item);
    }
}
