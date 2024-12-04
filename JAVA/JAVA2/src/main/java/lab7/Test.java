package lab7;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.results.BenchmarkResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Benchmark)
public class Test {

    private ExecutorService service;

    @Setup(Level.Iteration)
    public void setUp() {
        service = Executors.newFixedThreadPool(100);
    }

    @TearDown
    public void tearDown() {
        service.shutdown();
        try {
            if (!service.awaitTermination(1, TimeUnit.MINUTES)) {
                service.shutdownNow();
            }
        } catch (InterruptedException e) {
            service.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @BenchmarkMode(Mode.AverageTime)
    public void testAccountSync(BenchmarkData data) throws InterruptedException {
        for (int i = 1; i <= 100; i++) {
            service.execute(new DepositThread(data.accountSync, 10));
        }
        service.shutdown();
        service.awaitTermination(1, TimeUnit.MINUTES);
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @BenchmarkMode(Mode.AverageTime)
    public void testAccountLock(BenchmarkData data) throws InterruptedException {
        for (int i = 1; i <= 100; i++) {
            service.execute(new DepositThread(data.accountLock, 10));
        }
        service.shutdown();
        service.awaitTermination(1, TimeUnit.MINUTES);
    }

    public static void main(String[] args) throws Exception {
        // Run the benchmarks
        Options opt = new OptionsBuilder()
                .include(Test.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(opt).run();
    }
}