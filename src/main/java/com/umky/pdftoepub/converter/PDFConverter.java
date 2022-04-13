package com.umky.pdftoepub.converter;

import java.io.InputStream;
import java.io.OutputStream;

public interface PDFConverter {

    public void convert(InputStream in, OutputStream out) throws Exception;

}
