package com.umky.pdftoepub.epubmodel.resource;

import com.umky.pdftoepub.epubmodel.xhtml.tag.Meta;
import com.umky.pdftoepub.util.MarshallerUtils;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.*;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@XmlRootElement(name = "html",namespace = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"head","body"})
public class XHTMLContentResource {

    @XmlTransient
    private String resourceName;

    @XmlAttribute
    private final String xmlns = "http://www.w3.org/1999/xhtml";

    private final Head head = new Head();

    private final Body body = new Body();

    @XmlAccessorType(XmlAccessType.FIELD)
    private static class Head {

        @XmlElement(name = "meta",required = true)
        public final Meta contentTypeMeta = new Meta();

        @XmlElement(required = true)
        public String title;

        public Head() {
            contentTypeMeta.setHttpEquiv("Content-Type");
            contentTypeMeta.setContent("text/html; charset=utf-8");
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    private static class Body {

        public String h3;

        @XmlElement(name = "p")
        public final List<String> paragraphs = new ArrayList<>();

    }

    public void setTitle(String title) {
        this.head.title = title;
        this.body.h3=title;
    }

    public byte[] transformToBytes() throws JAXBException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Marshaller jaxbMarshaller = MarshallerUtils.newInstance(this.getClass());
        jaxbMarshaller.marshal(this,out);
        return out.toByteArray();
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getTitle() {
        return this.head.title;
    }

    public void addParagraphs(String... paragraphs) {
        this.body.paragraphs.addAll(Arrays.asList(paragraphs));
    }

    public void addParagraphs(List<String> list) {
        this.body.paragraphs.addAll(list);
    }

    public String getContent() {
        StringBuilder content= new StringBuilder();
        for(String s:this.body.paragraphs) {
            content.append(s)
                    .append("\n");
        }
        return content.toString();
    }

}
