package org.example.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileManager {

    public static void createFile(byte[] bytes, String path, String fileName){
        try {
            new File(path).mkdirs();
            BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(path + "\\" + fileName));
            stream.write(bytes);
            stream.close();
            copyFile(path + "\\" + fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void copyFile(String path){
        java.nio.file.Path destFile = Paths.get(path);
        SeekableByteChannel destFileChannel;
        try {
            destFileChannel = Files.newByteChannel(destFile);
            destFileChannel.close();
            Files.copy(destFile, destFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
