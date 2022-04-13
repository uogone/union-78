package com.umky.pdftoepub.epubmodel;

/**
 * 各个文件相对路径
 */
public interface EPUBFileStructure {
    String MIMETYPE = "mimetype";

    String CONTAINER = "META-INF/container.xml";

    String PACKAGE = "EPUB/package.opf";

    String XHTML_RESOURCE_PREFIX = "EPUB/xhtml/";

    String NAVIGATION = XHTML_RESOURCE_PREFIX+"nav.xhtml";

    String IMAGE_PREFIX = "EPUB/img/";
}
