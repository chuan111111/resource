package lab8;

import java.io.*;
import java.nio.file.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class FirstFileSearch {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter keyword (e.g. volatile):");
        String keyword = scanner.nextLine();
        scanner.close();

        ExecutorService executor = Executors.newCachedThreadPool();
        List<Callable<Object>> tasks = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get("src/main/resources/src"))) {
            paths.forEach(path -> {
                if (Files.isRegularFile(path)) {
                    tasks.add(() -> searchWordInFile(path, keyword));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        Instant startTime = Instant.now();
        try {
            Object result = executor.invokeAny(tasks);
            System.out.println("Found the first file that contains " + keyword + ": " + result);
        } catch (NoSuchElementException e) {
            System.out.println("Keyword not found in any file.");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }

        Instant endTime = Instant.now();
        System.out.println("Time elapsed: " + Duration.between(startTime, endTime).toMillis() + " ms\n");

        if (executor instanceof ThreadPoolExecutor) {
            System.out.println("Largest pool size: " + ((ThreadPoolExecutor) executor).getLargestPoolSize());
        }
    }

    private static Object searchWordInFile(Path path, String word) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(word)) {
                    return path.toString();
                }
            }
        }
        if (Thread.currentThread().isInterrupted()) {
            System.out.println("Search in " + path + " canceled.");
        }
        throw new NoSuchElementException("Keyword not found in " + path);
    }
}