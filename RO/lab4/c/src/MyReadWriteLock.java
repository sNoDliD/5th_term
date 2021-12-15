package main;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class MyReadWriteLock {
    private AtomicInteger readSemaphore = new AtomicInteger();
    private AtomicInteger writeSemaphore = new AtomicInteger();

    public MyReadWriteLock() {
        readSemaphore.set(0);
        writeSemaphore.set(0);
    }

    public void ReadLock() {
        while (writeSemaphore.get() > 0);
        readSemaphore.incrementAndGet();
    }
    public void ReadUnlock() {
        readSemaphore.decrementAndGet();
    }
    public void WriteLock() {
        while (readSemaphore.get() > 0);
        while (writeSemaphore.get() > 0);
        writeSemaphore.incrementAndGet();
    }
    public void WriteUnlock() {
        writeSemaphore.decrementAndGet();
    }
}