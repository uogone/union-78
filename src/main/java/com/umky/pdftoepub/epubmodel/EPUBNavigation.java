package com.umky.pdftoepub.epubmodel;

import com.umky.pdftoepub.util.DOMUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;

public class EPUBNavigation {

    private Document document;

    private Element html;

    private Element head;

    private Element body;

    private Element tocNav;

    private Element tocNavOl;

    public EPUBNavigation() {
        try {
            this.document = DOMUtils.createDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        if(document!=null) {
            this.html = document.createElement("html");
            html.setAttribute("xmlns","http://www.w3.org/1999/xhtml");
            html.setAttribute("xmlns:epub","http://www.idpf.org/2007/ops");
            this.document.appendChild(html);

            this.head = document.createElement("head");
            Element title = document.createElement("title");
            title.setTextContent("Table of Contents");
            head.appendChild(title);
            this.html.appendChild(head);

            this.body = document.createElement("body");
            this.tocNav = document.createElement("nav");
            tocNav.setAttribute("epub:type","toc");
            Element tocNavTitle = document.createElement("h1");
            tocNavTitle.setTextContent("Table of Contents");
            this.tocNavOl = document.createElement("ol");
            tocNav.appendChild(tocNavTitle);
            tocNav.appendChild(tocNavOl);
            body.appendChild(tocNav);
            this.html.appendChild(body);
        }
    }

    public byte[] transformToBytes() throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Transformer transformer = factory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT,"yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        transformer.transform(new DOMSource(this.document),new StreamResult(out));
        return out.toByteArray();
    }

    public void addTocNavLi(String href,String aTagText) {
        Element li = document.createElement("li");
        Element a = document.createElement("a");
        a.setAttribute("href",href);
        a.setTextContent(aTagText);
        li.appendChild(a);
        this.tocNavOl.appendChild(li);
    }

    public Document getDocument() {
        return this.document;
    }

    public Element getTocNavOl() {
        return tocNavOl;
    }

    public Element createLi(String href, String text) {
        Element li = document.createElement("li");
        Element a = document.createElement("a");

        a.setAttribute("href",href);
        a.setTextContent(text);
        li.appendChild(a);
        return li;
    }

}
