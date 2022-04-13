package com.umky.pdftoepub.epubmodel;

import org.junit.Test;

import javax.xml.bind.JAXBException;

public class EPUBPackageTest {
    @Test
    public void testCreate() throws JAXBException {
        EPUBPackage epubPackage = new EPUBPackage();

        EPUBMetadata metadata = new EPUBMetadata();
        metadata.setCreator("Umky");
        metadata.setDate("2022年3月27日16:30:59");
        //epubPackage.setMetadata(metadata);

        EPUBManifest manifest = new EPUBManifest();
        for(int i=0;i<3;i++) {
            EPUBManifestItem item = new EPUBManifestItem();
            item.setId(i+"");
            item.setHref("href"+i);
            item.setMediaType("application/txt");
            manifest.addItem(item);
        }
        //epubPackage.setManifest(manifest);

        //epubPackage.setSpine(new EPUBSpine());

        epubPackage.transformToBytes();
    }
}
