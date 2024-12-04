package lab6;

import java.io.*;
import java.nio.file.*;
import java.util.zip.*;

public class SourceCodeCounter {
    public static void main(String[] args) {
        String zipFilePath = "lib/src.zip"; // 请替换为实际路径
        int count = 0;
        try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry = zipIn.getNextEntry();
            while (entry != null) {
                String fileName = entry.getName();
                if (fileName.startsWith("java/io/") || fileName.startsWith("java/nio/")) {
                    if (fileName.endsWith(".java")) {
                        count++;
                        System.out.println(fileName);
                    }
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("In .zip: # of .java files in java.io/java.nio packages: " + count);
    }
}