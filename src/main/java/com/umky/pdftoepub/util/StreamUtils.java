package com.umky.pdftoepub.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtils {

    /**
     * 将输入流转为字节数组
     *
     * @return 字节数组，最大长度：Integer.MAX_VALUE-8
     */
    public static byte[] toByteArray(InputStream is) throws IOException {
        byte[] buffer=new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int reads=is.read(buffer,0,1024);

        while(reads!=-1) {
            baos.write(buffer,0,reads);
            reads=is.read(buffer,0,1024);
        }
        return baos.toByteArray();
    }
}
