package practice.lab2;
import java.io.*;
import java.util.*;

public class TopWords {
    public static void main(String[] args) {
        String filePath = "alice.txt"; // 文件路径
        Map<String, Integer> wordCountMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // 将文本转换为小写单词数组
                String[] words = line.toLowerCase().split("\\W+");
                for (String word : words) {
                    if (!word.isEmpty()) {
                        wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 将Map转换为List，并根据计数排序
        List<Map.Entry<String, Integer>> list = new ArrayList<>(wordCountMap.entrySet());
        list.sort(Map.Entry.<String, Integer>comparingByValue().reversed());

        // 打印前5个单词
        System.out.println("Word : Count");
        for (int i = 0; i < Math.min(5, list.size()); i++) {
            System.out.println(list.get(i).getKey() + " : " + list.get(i).getValue());
        }
    }

}