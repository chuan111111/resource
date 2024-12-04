package lab8;
import java.io.*;
import java.nio.file.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class WordCountTask {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter keyword (e.g. volatile):");
        String keyword = scanner.nextLine();
        scanner.close();

        ExecutorService executor = Executors.newCachedThreadPool();
        List<Callable<Integer>> tasks = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get("src/main/resources/src"))) {
            paths.forEach(path -> {
                if (Files.isRegularFile(path)) {
                    tasks.add(() -> countWordOccurrences(path, keyword));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        Instant startTime = Instant.now();
        List<Future<Integer>> results;
        try {
            results = executor.invokeAll(tasks);
            int totalOccurrences = 0;
            for (Future<Integer> result : results) {
                int i = result.get();
                totalOccurrences += i;
            }
            System.out.println("Occurrences of " + keyword + ": " + totalOccurrences);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }

        Instant endTime = Instant.now();
        System.out.println("Time elapsed: " + Duration.between(startTime, endTime).toMillis() + " ms\n");
    }

    private static int countWordOccurrences(Path path, String word) throws IOException {
        int count = 0;
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("\\W+");
                for (String w : words) {
                    if (w.equals(word)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
}