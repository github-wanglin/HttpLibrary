package com.wl.library.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class IOUtil {


    public static byte[] readBytesFromInputStream(BufferedInputStream in, int length) throws IOException {
        int readSize;
        byte[] bytes= new byte[length];
        long length_tmp = length;
        long index = 0;
        while ((readSize = in.read(bytes, (int) index, (int) length_tmp)) != -1) {
            length_tmp -= readSize;
            if (length_tmp == 0) {
                break;
            }
            index = index + readSize;
        }
        return bytes;
    }

    public static byte[] toByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

}
