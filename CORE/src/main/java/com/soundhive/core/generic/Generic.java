package com.soundhive.core.generic;

import com.soundhive.core.response.Response;

import java.io.*;

public class Generic {
    public static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is;
        OutputStream os;

        is = new FileInputStream(source);
        os = new FileOutputStream(dest);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
        is.close();
        os.close();
    }

    public static <T> Response<T> secureResponseCast(Response<?> response) {//TODO use inheritance to make response system more consistent
        try {
            return (Response<T>) response;
        } catch (ClassCastException e) {
            return new Response<>(response.getStatus(), response.getMessage(), response.getException());
        }
    }
}
