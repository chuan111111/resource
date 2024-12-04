package lab7;

import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
public class BenchmarkData {
    public AccountSync accountSync = new AccountSync();
    public AccountLock accountLock = new AccountLock();
}