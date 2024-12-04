package lab6;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class CompareJavaAndClassFiles {
    private static final String SRC_ZIP = "lib/src.zip";
    private static final String RT_JAR = "lib/rt.jar";

    public static void main(String[] args) throws IOException {
        Set<String> javaFiles = new HashSet<>();
        Set<String> classFiles = new HashSet<>();


        try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(SRC_ZIP))) {
            ZipEntry entry = zipIn.getNextEntry();
            while (entry != null) {
                String fileName = entry.getName();
                if (fileName.startsWith("java/io/") || fileName.startsWith("java/nio/")) {
                    if (fileName.endsWith(".java")) {
                        javaFiles.add(toQualifiedName(fileName));
                    }
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
        }


        try (JarFile jarFile = new JarFile(RT_JAR)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String fileName = entry.getName();
                if (fileName.startsWith("java/io/") || fileName.startsWith("java/nio/")) {
                    if (fileName.endsWith(".class")) {
                        classFiles.add(toQualifiedName(fileName));
                    }
                }
            }
        }


        int innerClassFilesCount = (int) classFiles.stream()
                .filter(className -> className.contains("$"))
                .count();


        Set<String> classFilesWithoutInnerClasses = new HashSet<>(classFiles);
        classFilesWithoutInnerClasses.removeIf(className -> className.contains("$"));


        int matchingFilesCount = 0;
        for (String javaFile : javaFiles) {
            String correspondingClassFile = javaFile.replace(".java", ".class");
            if (classFilesWithoutInnerClasses.contains(correspondingClassFile)) {
                matchingFilesCount++;
            }
        }


        Set<String> javaWithoutClass = new HashSet<>();
        for (String javaFile : javaFiles) {
            String correspondingClassFile = javaFile.replace(".java", ".class");
            if (!classFilesWithoutInnerClasses.contains(correspondingClassFile)) {
                javaWithoutClass.add(javaFile);
            }
        }


        Set<String> classWithoutJava = new HashSet<>();
        for (String classFile : classFilesWithoutInnerClasses) {
            String correspondingJavaFile = classFile.replace(".class", ".java");
            if (!javaFiles.contains(correspondingJavaFile)) {
                classWithoutJava.add(classFile);
            }
        }


        System.out.println("# of .class files for inner classes: " + innerClassFilesCount);
        System.out.println("# of .java files with corresponding .class: " + matchingFilesCount);
        System.out.println("# of .java without its .class: " + javaWithoutClass.size());
        for (String javaFile : javaWithoutClass) {
            System.out.println(javaFile.replace('.', '/'));
        }
        System.out.println("# of .class without its .java: " + classWithoutJava.size());
        for (String classFile : classWithoutJava) {
            System.out.println(classFile.replace('.', '/'));
        }
    }


    private static String toQualifiedName(String path) {
        return path.replace('/', '.').replace('\\', '.').replace(".class", "").replace(".java", "");
    }
}