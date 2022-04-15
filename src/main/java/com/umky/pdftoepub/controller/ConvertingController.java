package com.umky.pdftoepub.controller;

import com.umky.pdftoepub.converter.PDFToEPUB;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
public class ConvertingController {

    private static final Logger log = LoggerFactory.getLogger(ConvertingController.class);

    private static final ExecutorService threadPool = Executors.newFixedThreadPool(10);

    /**
     * @param files  pdf files
     * @return json string
     */
    @PostMapping("/upload")
    public String uploadPdf(@RequestParam("files") MultipartFile[] files) throws JSONException, InterruptedException {
        JSONObject json = new JSONObject();

        for (MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString();
            String fName = file.getOriginalFilename();
            AtomicBoolean appearedException = new AtomicBoolean(false);

            try {
                InputStream is = file.getInputStream();
                OutputStream os = new FileOutputStream("./" + uuid);
                threadPool.submit(() -> {
                    try {
                        log.info("begin to convert " + fName);
                        new PDFToEPUB().convert(is,os);
                        log.info("succeed to convert " + fName);
                    } catch (IOException e) {
                        appearedException.set(true);
                        log.error("failed to convert pdf file " + fName,e);
                    }
                    synchronized (appearedException) {
                        appearedException.notify();
                    }
                });
            } catch (IOException e) {
                appearedException.set(true);
                log.error("failed to upload pdf file" + fName,e);
            }

            synchronized (appearedException) {
                appearedException.wait();
            }
            if(appearedException.get()) {
                json.put(fName.replace(".pdf",".epub"),"retry");
            }
            else {
                json.put(fName.replace(".pdf",".epub"),uuid);
            }
        }
        return json.toString();
    }

    @GetMapping("/download/{name}/{uuid}")
    public ResponseEntity<InputStreamResource> downloadEpub(@PathVariable String uuid,@PathVariable String name) throws IOException {
        String filePath = "./" + uuid;
        FileSystemResource file = new FileSystemResource(filePath);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment; filename=" + URLEncoder.encode(name,"UTF-8"));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(file.getInputStream()));
    }

}
