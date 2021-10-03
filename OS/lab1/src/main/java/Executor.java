import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.function.Function;

public class Executor extends Thread {
    private final PipedOutputStream pipedOutputStream;
    private final PipedInputStream pipedInputStream;
    private final int sleepTime;
    private final Function<Integer, String> func;
    private final int value;
    private String failedMessage;

    public Executor(int sleepTime, Function<Integer, String> func, int value) throws IOException {
        this.pipedInputStream = new PipedInputStream();
        this.pipedOutputStream = new PipedOutputStream(pipedInputStream);
        this.sleepTime = sleepTime;
        this.func = func;
        this.value = value;
    }

    public void run() {
        try {
            Thread.sleep(sleepTime);

            byte[] x = func.apply(value).getBytes(StandardCharsets.UTF_8);
            if (x.length == 0) {
                failedMessage = "Empty result";
            } else {
                pipedOutputStream.write(x);
            }
        } catch (InterruptedException e) {
            failedMessage = "Interrupted while sleeping";
        } catch (IOException e) {
            failedMessage = "Pipe error";
        } catch (IllegalArgumentException e) {
            failedMessage = "Incorrect argument";
        } catch (StringIndexOutOfBoundsException e) {
            failedMessage = "Out of range";
        }
    }

    public String readFromPiped() throws IOException {
        StringBuilder res = new StringBuilder();
        Scanner scanner = new Scanner(pipedInputStream);
        while (scanner.hasNextLine()) {
            res.append(scanner.nextLine());
        }

        return res.toString();
    }

    public boolean failed() {
        return failedMessage != null;
    }

    public String getFailedMessage() {
        return failedMessage;
    }
}