package com.umky.pdftoepub.epubmodel.resource;

import com.umky.pdftoepub.util.MarshallerUtils;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class XHTMLContentResourceTest {

    @Test
    public void testNewInstance() {
        XHTMLContentResource resource = new XHTMLContentResource();

        resource.setTitle("Test Title");
        resource.addParagraphs("P1...","P2...");
        try {
            Marshaller marshaller = MarshallerUtils.newInstance(resource.getClass());
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT,true);
            marshaller.marshal(resource,System.out);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
