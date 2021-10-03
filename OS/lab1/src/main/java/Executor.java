
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Executor extends Thread {
    private final PipedOutputStream pipedOutputStream;
    public PipedInputStream pipedInputStream;
    final int sleepTime;
    private Foo func;
    int value;
    public String failedMessage = "";

    public Executor(int sleepTime, Foo func, int value) throws IOException {
        pipedInputStream = new PipedInputStream();
        pipedOutputStream=new PipedOutputStream(pipedInputStream);
        this.sleepTime = sleepTime;
        this.func = func;
        this.value = value;
    }
    public void run() {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            failedMessage = "Interrupted while sleeping";
            return;
        }
        try {

            var x = func.method(value).getBytes(StandardCharsets.UTF_8);
            if (x.length == 0){
                failedMessage = "Empty result";
                return;
            }
            pipedOutputStream.write(x);
        } catch (IOException e) {
            failedMessage = "Pipe error";
        } catch (IllegalArgumentException e) {
            failedMessage = "Incorrect argument";
        } catch (StringIndexOutOfBoundsException e) {
            failedMessage = "Out of range";
        }
    }
    public String readFromPiped() throws IOException {
        try {
            byte first = (byte) pipedInputStream.read();
            int total = pipedInputStream.available() + 1;
            byte[] res = new byte[total];
            res[0] = first;
            for (int i = 1; i < total; i++) {
                res[i] = (byte) pipedInputStream.read();
            }
            return new String(res);
        }
        catch (IOException e) {
            failedMessage = "Pipe error";
            throw e;
        }
    }
}