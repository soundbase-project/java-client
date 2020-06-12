package com.soundhive.core.generic;

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
}
