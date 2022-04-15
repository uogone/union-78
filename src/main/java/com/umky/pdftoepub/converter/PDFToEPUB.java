package com.umky.pdftoepub.converter;

import com.umky.pdftoepub.epubmodel.EPUBContainer;
import com.umky.pdftoepub.epubmodel.EPUBMetadata;
import com.umky.pdftoepub.epubmodel.EPUBNavigation;
import com.umky.pdftoepub.epubmodel.resource.XHTMLContentResource;
import com.umky.pdftoepub.util.StringUtils;
import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDNamedDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;

public class PDFToEPUB implements PDFConverter{

    private static final Logger logger = LoggerFactory.getLogger(PDFToEPUB.class);

    private PDDocument document;

    private EPUBContainer container;

    private List<PDOutlineItem> outlineItemList;

    private EPUBNavigation navigation;

    private int chapterCount = 1;

    @Override
    public void convert(InputStream in, OutputStream out) throws IOException {
        try(RandomAccessRead rar = new RandomAccessBuffer(in)) {
            PDFParser pdfParser = new PDFParser(rar);
            pdfParser.parse();
            this.document = pdfParser.getPDDocument();
        } catch (IOException e) {
            throw new IOException("parse file to PDDocument by Pdf-box failed. " + e.getLocalizedMessage());
        }

        if(document!=null) {
            this.container = new EPUBContainer();

            extractMetadata();
            extractOutline();
            try {
                extractContent();
            } catch (IOException e) {
                throw new IOException("extract pdf content failed.");
            }

            try{
                container.toZipOutputStream(new ZipOutputStream(out));
            } catch (Exception e) {
                throw new IOException("output to zip failed.");
            }
            try {
                document.close();
            } catch (IOException e) {
                logger.warn(document + " failed to close.",e);
            }
        }
    }

    private void extractMetadata() {
        EPUBMetadata metadata = this.container.getEpubPackage().getMetadata();
        PDDocumentInformation documentInformation = document.getDocumentInformation();
        PDDocumentCatalog catalog = document.getDocumentCatalog();

        if(catalog.getLanguage()!=null) {
            metadata.setLanguage(catalog.getLanguage());
        }
        if(documentInformation.getTitle()!=null) {
            metadata.setTitle(documentInformation.getTitle());
        }
        if(documentInformation.getModificationDate()!=null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
            metadata.setLastModifiedDate(format.format(documentInformation.getModificationDate().getTime()));
        }
    }

    private void extractOutline() {
        PDDocumentCatalog catalog = document.getDocumentCatalog();
        PDDocumentOutline outline = catalog.getDocumentOutline();

        if(outline == null) {
            return;
        }

        navigation = new EPUBNavigation();
        outlineItemList = new ArrayList<>();

        container.setEpubNavigation(navigation);
        dfs(outline.getFirstChild(),navigation.getTocNavOl());
    }

    private void dfs(PDOutlineItem item, Element ol) {
        if(item==null) return;

        Element li = navigation.createLi("Cap"+ chapterCount++ +".xhtml", item.getTitle());

        ol.appendChild(li);
        outlineItemList.add(item);
        if(item.hasChildren()) {
            Element ol1 = navigation.getDocument().createElement("ol");
            li.appendChild(ol1);
            dfs(item.getFirstChild(),ol1);
        }
        dfs(item.getNextSibling(),ol);
    }

    private void extractContent() throws IOException{
        PDFTextStripper stripper = new PDFTextStripper();

        stripper.setLineSeparator("");
        stripper.setParagraphEnd("\n");

        //没有目录，直接提取每一页的文本
        if(outlineItemList == null || outlineItemList.get(0).getDestination() instanceof PDNamedDestination) {
            int numberOfPages = document.getNumberOfPages();

            for(int i=1;i<numberOfPages+1;i++) {
                stripper.setStartPage(i);
                stripper.setEndPage(i);
                String text = stripper.getText(document);
                List<String> list = trimParagraphs(text.split(stripper.getParagraphEnd()));
                if(list.size() == 0) {
                    continue;
                }
                XHTMLContentResource xpage = new XHTMLContentResource();
                list.remove(0);
                xpage.addParagraphs(list);
                xpage.setResourceName("Cap"+i);
                container.addXHTMLResource(xpage);
            }
        }
        else if(outlineItemList.get(0).getDestination() instanceof PDPageDestination){
            PDPageTree pageTree = document.getPages();
            int capCount = 1;

            for(int i=0;i<outlineItemList.size();i++) {
                PDPageDestination curDest = (PDPageDestination) outlineItemList.get(i).getDestination();
                PDPageDestination nextDest=null;
                //最后一个目录项特殊处理
                if(i<outlineItemList.size()-1) {
                    nextDest = (PDPageDestination) outlineItemList.get(i+1).getDestination();
                }

                //indexOf:0-based
                stripper.setStartPage(pageTree.indexOf(curDest.getPage()) + 1);
                if(i<outlineItemList.size()-1) {
                    stripper.setEndPage(pageTree.indexOf(nextDest.getPage()));
                }
                else {
                    stripper.setEndPage(document.getNumberOfPages());
                }

                String text = stripper.getText(document);
                String title = outlineItemList.get(i).getTitle();
                List<String> list = trimParagraphs(text.split(stripper.getParagraphEnd()));

                if(list.size() == 0) {
                    continue;
                }
                else if(list.size() > 1) {
                    list.remove(title);
                }
                XHTMLContentResource xpage = new XHTMLContentResource();
                xpage.addParagraphs(list);
                xpage.setResourceName("Cap"+capCount++);
                xpage.setTitle(title);
                container.addXHTMLResource(xpage);
            }
        }
    }

    private List<String> trimParagraphs(String... paragraphs) {
        List<String> list = new ArrayList<>();

        for (String p : paragraphs) {
            String s = p.trim();
            if(s.isEmpty() || s.length()<=5 && StringUtils.isDigit(s)) {
                continue;
            }
            list.add(s);
        }
        return list;
    }
}
