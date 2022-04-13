package com.umky.pdftoepub.epubmodel;

import org.junit.Test;

import javax.xml.transform.TransformerException;

public class EPUBNavigationTest {

    @Test
    public void testCrateObject() throws TransformerException {
        EPUBNavigation navigation = new EPUBNavigation();
        navigation.addTocNavLi("test.xhtml","test li");
        System.out.println(new String(navigation.transformToBytes()));
    }
}
