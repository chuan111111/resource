package lab5;

import java.io.*;

public class FileTypeParser {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java FileTypeParser <filename>");
            return;
        }

        String filename = args[0];
        System.out.println(filename);
        try {
            byte[] header = new byte[4];
            FileInputStream fileInputStream = new FileInputStream(filename);
            fileInputStream.read(header);
            fileInputStream.close();

            String fileType = determineFileType(header);
            System.out.printf("Filename: %s File Header(Hex): [%s] File Type: %s%n",
                    filename, bytesToHex(header), fileType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String determineFileType(byte[] header) {
        switch (bytesToHex(header)) {
            case "89504E47":
                return "png";
            case "504B0304":
                return "zip or jar";
            case "CAFEBABE":
                return "class";
            default:
                return "unknown";
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString().toUpperCase();
    }
}