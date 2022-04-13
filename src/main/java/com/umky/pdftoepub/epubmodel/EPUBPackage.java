package com.umky.pdftoepub.epubmodel;

import com.umky.pdftoepub.util.MarshallerUtils;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.*;
import java.io.ByteArrayOutputStream;

@XmlRootElement(name = "opf:package")
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(propOrder={"metadata","manifest","spine"})
public class EPUBPackage{

    @XmlAttribute(name = "unique-identifier",required = true)
    private final String uniqueIdentifier = "pub-id";

    @XmlAttribute(required = true)
    private final String version = "3.0";

    //REQUIRED first child of the package
    @XmlElement(name = "opf:metadata",required = true)
    private final EPUBMetadata metadata = new EPUBMetadata();

    //[REQUIRED] second child of EPUBPackage, following metadata.
    @XmlElement(name = "opf:manifest",required = true)
    private final EPUBManifest manifest = new EPUBManifest();

    //REQUIRED third child of package, following manifest.
    @XmlElement(name = "opf:spine",required = true)
    private final EPUBSpine spine = new EPUBSpine();

    public EPUBPackage() {
        this.metadata.getIdentifier().setId(this.uniqueIdentifier);
    }

    public byte[] transformToBytes() throws JAXBException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Marshaller jaxbMarshaller = MarshallerUtils.newInstance(this.getClass());
        jaxbMarshaller.marshal(this,out);
        return out.toByteArray();
    }

    public EPUBSpine getSpine() {
        return spine;
    }

    public EPUBMetadata getMetadata() {
        return metadata;
    }

    public EPUBManifest getManifest() {
        return manifest;
    }
}
