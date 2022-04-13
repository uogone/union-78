package com.umky.pdftoepub.epubmodel;

import com.umky.pdftoepub.epubmodel.resource.XHTMLContentResource;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class EPUBContainer{

    //mimetype
    public static final byte[] MIMETYPE = "application/epub+zip".getBytes(StandardCharsets.US_ASCII);

    //META-INF/container.xml
    public static final byte[] CONTAINER_XML = (
            "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
            "<container version=\"1.0\" xmlns=\"urn:oasis:names:tc:opendocument:xmlns:container\">\n" +
            "    <rootfiles>\n" +
            "        <rootfile full-path=\"EPUB/package.opf\"\n" +
            "            media-type=\"application/oebps-package+xml\"/>\n" +
            "    </rootfiles>\n" +
            "</container>").getBytes(StandardCharsets.UTF_8);

    private final EPUBPackage epubPackage = new EPUBPackage();

    private EPUBNavigation epubNavigation;

    private final List<XHTMLContentResource> xhtmlContentResourceList = new ArrayList<>();

    private final String uuid = UUID.randomUUID().toString();

    public EPUBContainer() {
        this.epubPackage.getMetadata().getIdentifier().setValue("urn:uuid:"+uuid);
    }

    public void toZipOutputStream(ZipOutputStream out) throws IOException, JAXBException, TransformerException {
        ZipEntry entry = new ZipEntry(EPUBFileStructure.MIMETYPE);
        entry.setMethod(ZipEntry.STORED);
        entry.setSize(20);
        entry.setCompressedSize(20);
        entry.setCrc(0x2CAB616F); // pre-computed
        out.putNextEntry(entry);
        out.write(MIMETYPE);
        out.closeEntry();

        if(epubNavigation != null) {
            EPUBManifestItem navItem = new EPUBManifestItem("nav", "xhtml/nav.xhtml", "application/xhtml+xml");
            navItem.setProperties("nav");
            this.getEpubPackage().getManifest().getItemList().add(0,navItem);
            writeEntry(out,EPUBFileStructure.NAVIGATION,epubNavigation.transformToBytes());
        }

        writeEntry(out,EPUBFileStructure.CONTAINER,CONTAINER_XML);
        writeEntry(out,EPUBFileStructure.PACKAGE,this.epubPackage.transformToBytes());

        for(XHTMLContentResource resource:xhtmlContentResourceList) {
            writeEntry(out,EPUBFileStructure.XHTML_RESOURCE_PREFIX+resource.getResourceName()+".xhtml",resource.transformToBytes());
        }
        out.close();
    }

    private void writeEntry(ZipOutputStream out,String name,byte[] bytes) throws IOException {
        out.putNextEntry(new ZipEntry(name));
        out.write(bytes);
        out.closeEntry();
    }

    public String getUuid() {
        return uuid;
    }

    public EPUBPackage getEpubPackage() {
        return epubPackage;
    }

    public EPUBNavigation getEpubNavigation() {
        return epubNavigation;
    }

    public List<XHTMLContentResource> getXhtmlContentResourceList() {
        return xhtmlContentResourceList;
    }

    public void setEpubNavigation(EPUBNavigation epubNavigation) {
        this.epubNavigation = epubNavigation;
    }

    public void addXHTMLResource(XHTMLContentResource resource) {
        String name = resource.getResourceName();
        xhtmlContentResourceList.add(resource);
        epubPackage.getManifest().addItem(new EPUBManifestItem(name,"xhtml/"+name+".xhtml","application/xhtml+xml"));
        epubPackage.getSpine().addItemref(new EPUBSpineItemref(name));
    }
}
