import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;
import java.util.function.Function;

public class PipeWrapper extends Thread {
    private final PipedOutputStream pipedOutputStream;
    private final PipedInputStream pipedInputStream;
    private final int sleepTime;
    private final Function<Integer, String> func;
    private final int value;
    private String failedMessage;
    private long startTime;
    public String name;

    public PipeWrapper(String name, int sleepTime, Function<Integer, String> func, int value) throws IOException {
        this.pipedInputStream = new PipedInputStream();
        this.pipedOutputStream = new PipedOutputStream(pipedInputStream);
        this.sleepTime = sleepTime;
        this.func = func;
        this.value = value;
        this.name = name;
    }

    @Override
    public synchronized void start() {
        startTime = System.currentTimeMillis();
        super.start();
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

    public String readFromPiped() {
        StringBuilder res = null;
        try {
            while (pipedInputStream.available() != 0) {
                if (res == null) {
                    res = new StringBuilder();
                }
                int size = pipedInputStream.available();
                while (size > 0) {
                    byte[] arr = new byte[size];
                    int readed = pipedInputStream.read(arr);
                    byte[] readedArray = Arrays.copyOf(arr, readed);
                    res.append(new String(readedArray));
                    size -= readed;
                }
            }
        } catch (IOException e){
            failedMessage = "Pipe read error";
            res = null;
        }
        if (res == null) return null;
        long endTime = System.currentTimeMillis();
        System.out.println(name + " execution time: " + (endTime - startTime) + "ms. " + res);
        return res.toString();
    }

    public boolean failed() {
        return failedMessage != null;
    }

    public String getFailedMessage() {
        return failedMessage;
    }
}