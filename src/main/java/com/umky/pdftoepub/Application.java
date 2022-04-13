package com.umky.pdftoepub;

import com.umky.pdftoepub.converter.PDFToEPUB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    private static final ExecutorService threadPool = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws Exception {
        if(args.length < 1) {
            throw new IllegalArgumentException("第一个参数应该为pdf文件路径或目录");
        }

        File file = new File(args[0]);

        if(!file.exists()) {
            throw new FileNotFoundException(args[0]);
        }
        else if(file.isDirectory()) {
            File[] children = file.listFiles();
            for (File f : children) {
                if(f.getName().endsWith(".pdf")) {
                    threadPool.submit(() -> {
                        String s = f.getAbsolutePath();
                        File epubFile = new File(s.substring(0,s.length()-4) + ".epub");
                        try {
                            logger.info("开始转换：" + f.getName());
                            new PDFToEPUB().convert(new FileInputStream(f),new FileOutputStream(epubFile));
                            logger.info("成功转换：" + f.getName() + "，epub文件路径：" + epubFile.getAbsolutePath());
                        } catch (IOException | TransformerException | JAXBException e) {
                            logger.error("转换失败：" + f.getName()+ " error:" + e.getMessage());
                            epubFile.delete();
                            e.printStackTrace();
                        }
                    });
                }
            }
        }
        else{
            if(!args[0].endsWith(".pdf")) {
                throw new Exception("not a pdf file");
            }

            String s = file.getAbsolutePath();
            File epubFile = new File(s.substring(0,s.length()-4) + ".epub");
            try {
                logger.info("开始转换：" + file.getName());
                new PDFToEPUB().convert(new FileInputStream(file),new FileOutputStream(epubFile));
                logger.info("成功转换：" + file.getName() + "，epub文件路径：" + epubFile.getAbsolutePath());
            } catch (IOException | TransformerException | JAXBException e) {
                logger.error("转换失败：" + file.getName()+ "error:" + e.getMessage());
                epubFile.delete();
                e.printStackTrace();
            }
        }
        threadPool.shutdown();
    }
}
