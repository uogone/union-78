package com.umky.pdftoepub.util;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class StreamUtilsTest {
    @Test
    public void testToByteArr() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("Python编程：从入门到实践.pdf");
        byte[] bytes = StreamUtils.toByteArray(is);

        is.close();
        System.out.println(bytes.length);
    }
}
