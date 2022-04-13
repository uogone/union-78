package com.umky.pdftoepub.util;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class DOMUtils {

    public static Document createDocument() throws ParserConfigurationException {
        DocumentBuilderFactory dBuilderFactory =DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder =dBuilderFactory.newDocumentBuilder();
        return dBuilder.newDocument();
    }

}
