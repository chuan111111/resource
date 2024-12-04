package lab6;

import java.io.*;
import java.nio.file.*;
import java.util.Enumeration;
import java.util.jar.*;

public class ByteCodeCounter {
    public static void main(String[] args) {
        String jarFilePath = "lib/rt.jar"; // 请替换为实际路径
        int count = 0;
        try (JarFile jarFile = new JarFile(jarFilePath)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String fileName = entry.getName();
                if (fileName.startsWith("java/io/") || fileName.startsWith("java/nio/")) {
                    if (fileName.endsWith(".class")) {
                        count++;
                        System.out.println(fileName);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("In .jar: # of .class files in java.io/java.nio packages: " + count);
    }
}