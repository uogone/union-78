package com.umky.pdftoepub.util;

import com.adobe.epubcheck.api.EpubCheck;

import java.io.File;

public class EPUBUtils {
    public static void check(File file) {
        EpubCheck checker = new EpubCheck(file);
        checker.validate();
    }
}
