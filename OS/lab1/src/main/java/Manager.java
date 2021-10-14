import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class Manager {

    public String run(int x, PipeWrapper[] pipes) throws InterruptedException, IOException {
        Runnable interruptAll = () -> {
            for (PipeWrapper pipe : pipes) {
                pipe.interrupt();
            }
        };
        for (PipeWrapper pipe : pipes) {
            pipe.setValue(x);
            pipe.start();
        }
        String[] res = new String[pipes.length];
        do {
            for (int i = 0; i < pipes.length; i++) {
                String tmp = pipes[i].readFromPiped();
                if (tmp != null) {
                    res[i] = tmp;
                }
            }

            for (PipeWrapper pipe : pipes) {
                if (pipe.failed()) {
                    String error = pipe.getFailedMessage();
                    interruptAll.run();
                    throw new InterruptedException(pipe.name + " failed: " + error);
                }
            }
            if (System.in.available() != 0) {
                String line = new Scanner(System.in).nextLine();
                if (line.toLowerCase(Locale.ROOT).equals("stop")) {
                    interruptAll.run();
                    throw new InterruptedException("tasks for " + x + " was stopped");
                } else {
                    System.out.println("Unknown command: " + line);
                }
            }
        } while (Arrays.asList(res).contains(null));

        for (PipeWrapper pipe : pipes) {
            pipe.join();
        }
        return operation(res);
    }

    private String operation(String[] res) {
        StringBuilder result = new StringBuilder();
        for (String re : res) {
            result.append(re);
        }
        return result.toString();
    }
}
