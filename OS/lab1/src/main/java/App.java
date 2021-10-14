import java.util.InputMismatchException;
import java.util.Scanner;

import os.lab1.compfuncs.basic.Concatenation;

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
                        new PipeWrapper("f", 0, (integer -> {
                            try {
                                return Concatenation.trialF(integer).orElse("");
                            }  catch (final Exception e) {
                                throw new RuntimeException(e);
                            }
                        })),
                        new PipeWrapper("g", 0, (integer -> {
                            try {
                                return Concatenation.trialG(integer).orElse("");
                            }  catch (final Exception e) {
                                throw new RuntimeException(e);
                            }
                        })),
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
