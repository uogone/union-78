package com.umky.pdftoepub.converter;

import com.umky.pdftoepub.util.EPUBUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class PDFConverterTest {
    /**
     * 测试文件:
     *      毛泽东选集1-7  静火版.pdf
     *      Java开发手册-alibaba.pdf
     */
    @Test
    public void testConvert() throws Exception {
        InputStream is = this.getClass().getClassLoader()
                .getResourceAsStream("毛泽东选集1-7  静火版.pdf");
        PDFConverter converter = new PDFToEPUB();
        converter.convert(is,new FileOutputStream("./temp/毛泽东选集1-7  静火版.epub"));
    }

    @Test
    public void check() {
        EPUBUtils.check(new File("./temp/毛泽东选集1-7  静火版.epub"));
    }
}