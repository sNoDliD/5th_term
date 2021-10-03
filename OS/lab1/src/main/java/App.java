public class App {
    public static void main(String[] args) {
        Manager manager = new Manager();
        var x = 6;
        long startTime = System.currentTimeMillis();
        try {
            var res = manager.run(x);
            System.out.println(res);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime) + "ms");
    }
}
