import java.util.InputMismatchException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Manager manager = new Manager();
        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.println("Input x: ");
            int x;
            try {
                x = input.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Bye bye");
                break;
            }
            long startTime = System.currentTimeMillis();
            try {
                PipeWrapper[] pipes = {
                        new PipeWrapper("f", 4600, "Hello"::repeat),
                        new PipeWrapper("g", 1500, x1 -> " World!" + "abcdefg".charAt(x1)),
                        new PipeWrapper("h", 3750, String::valueOf),
                };
                String res = manager.run(x, pipes);
                System.out.println(res);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            long endTime = System.currentTimeMillis();
            System.out.println("Total execution time: " + (endTime - startTime) + "ms");
        }
    }
}
