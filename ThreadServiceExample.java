import java.util.concurrent.*;

public class ThreadServiceExample {

    public static void main(String[] args) {
        // Create a thread pool with fixed number of threads
        ExecutorService executor = Executors.newFixedThreadPool(5);

        // Input parameters
        int[] inputs = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

        // Submit tasks to the executor
        for (int input : inputs) {
            executor.submit(() -> {
                // Perform some computation based on input
                String result = compute(input);
                System.out.println(result);
                return result;
            });
        }

        // Shut down the executor when all tasks are completed
        executor.shutdown();

        try {
            // Wait until all tasks are completed or timeout after 10 seconds
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Method to perform computation based on input
    private static String compute(int input) {
        // Replace this with your actual computation
        return "Result of task with input " + input;
    }
}
