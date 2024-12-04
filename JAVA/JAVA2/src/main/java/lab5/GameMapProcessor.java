package lab5;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.IntStream;

public class GameMapProcessor {
    public static void main(String[] args) {
        Path inputPath = Paths.get("input.txt");
        Path outputPath = Paths.get("output.txt");

        try {
            // 读取输入文件并处理符号
            String processedMap = new String(Files.readAllBytes(inputPath), StandardCharsets.UTF_8)
                    .codePoints()
                    .mapToObj(codePoint -> {
                        switch (codePoint) {
                            case 0x007E:
                                return "\u2744";
                            case 0x1F332:
                                return "\u2B1C";
                            case 0x26F0:
                            default:
                                return new String(Character.toChars(codePoint));
                        }
                    })
                    .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                    .toString();

            byte[] outputBytes = processedMap.getBytes(StandardCharsets.UTF_16);
            Files.write(outputPath, outputBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}