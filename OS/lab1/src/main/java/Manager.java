import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class Manager {

    public String run(int x) throws InterruptedException, IOException {
        Function<Integer, String> f = "Hello"::repeat;
        Function<Integer, String> g = x1 -> " World!" + "abcdefg".charAt(x1);
        AtomicReference<String> res1 = new AtomicReference<>();
        AtomicReference<String> res2 = new AtomicReference<>();
        Executor exe1 = new Executor(3800, f, x);
        Executor exe2 = new Executor(900, g, x);

        Thread pipeReader1 = new Thread(() -> {
            long startTime = System.currentTimeMillis();
            try {
                res1.set(exe1.readFromPiped());
                System.out.println("Out1: " + res1);
            } catch (IOException ignored) {
            }
            long endTime = System.currentTimeMillis();
            System.out.println("f execution time: " + (endTime - startTime) + "ms");
        });

        Thread pipeReader2 = new Thread(() -> {
            long startTime = System.currentTimeMillis();
            try {
                res2.set(exe2.readFromPiped());
                System.out.println("Out2: " + res2);
            } catch (IOException ignored) {
            }
            long endTime = System.currentTimeMillis();
            System.out.println("g execution time: " + (endTime - startTime) + "ms");
        });

        AtomicBoolean stopped = new AtomicBoolean(false);
        Thread stopper = new Thread(() -> {
            while (!stopped.get()) {
                String line = new Scanner(System.in).nextLine();
                if (line.toLowerCase(Locale.ROOT).equals("stop")) {
                    stopped.set(true);
                }
            }
        });
        stopper.setDaemon(true);

        Runnable interruptAll = () -> {
            stopped.set(true);
            exe1.interrupt();
            exe2.interrupt();
            pipeReader1.interrupt();
            pipeReader2.interrupt();
        };
        stopper.start();
        pipeReader1.start();
        pipeReader2.start();
        exe1.start();
        exe2.start();

        do {
            if (stopped.get()) {
                interruptAll.run();
                throw new InterruptedException("tasks was stopped");
            }
            if (exe1.failed()) {
                String error = exe1.getFailedMessage();
                interruptAll.run();
                throw new InterruptedException("f failed: " + error);
            }
            if (exe1.failed()) {
                String error = exe2.getFailedMessage();
                interruptAll.run();
                throw new InterruptedException("g failed: " + error);
            }
        } while (res1.get() == null || res2.get() == null);

        exe1.join();
        exe2.join();
        pipeReader1.join();
        pipeReader2.join();
        return operation(res1.get(), res2.get());
    }

    public static String operation(String a, String b){
        return "f+g: " + a + " " + b;
    }
}
