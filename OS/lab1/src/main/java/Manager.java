import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;
import java.util.function.Function;

public class Manager {

    public String run(int x) throws InterruptedException, IOException {
        Function<Integer, String> f = "Hello"::repeat;
        Function<Integer, String> g = x1 -> " World!" + "abcdefg".charAt(x1);

        PipeWrapper pipe1 = new PipeWrapper("f", 4600, f, x);
        PipeWrapper pipe2 = new PipeWrapper("g", 1500, g, x);

        boolean stopped = false;

        Runnable interruptAll = () -> {
            pipe1.interrupt();
            pipe2.interrupt();
        };
        pipe1.start();
        pipe2.start();
        String res1 = null, res2 = null;
        do {
            String tmp;
            tmp = pipe1.readFromPiped();

            if (tmp != null){
                res1 = tmp;
            }
            tmp = pipe2.readFromPiped();
            if (tmp != null){
                res2 = tmp;
            }

            if (stopped) {
                interruptAll.run();
                throw new InterruptedException("tasks was stopped");
            }
            if (pipe1.failed()) {
                String error = pipe1.getFailedMessage();
                interruptAll.run();
                throw new InterruptedException(pipe1.name + " failed: " + error);
            }
            if (pipe1.failed()) {
                String error = pipe2.getFailedMessage();
                interruptAll.run();
                throw new InterruptedException(pipe1.name + " failed: " + error);
            }
            if(System.in.available() != 0){
                String line = new Scanner(System.in).nextLine();
                if (line.toLowerCase(Locale.ROOT).equals("stop")) {
                    stopped = true;
                } else {
                    System.out.println("Unknown command: " + line);
                }
            }
        } while (res1 == null || res2 == null);

        pipe1.join();
        pipe2.join();
        return operation(res1, res2);
    }

    public static String operation(String a, String b){
        return "f+g: " + a + " " + b;
    }
}
