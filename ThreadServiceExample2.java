import java.util.concurrent.*;

public class ThreadServiceExample2 {

    public static void main(String[] args) {
        // Create a thread pool with fixed number of threads
        ExecutorService executor = Executors.newFixedThreadPool(5);

        // Input parameters
        int[] inputs = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

        // Future results
        Future<String>[] results = new Future[inputs.length];

        // Submit tasks to the executor
        for (int i = 0; i < inputs.length; i++) {
            final int input = inputs[i];
            results[i] = executor.submit(() -> {
                // Perform some computation based on input
                return compute(input);
            });
        }

        // Shut down the executor when all tasks are completed
        executor.shutdown();

        try {
            // Wait until all tasks are completed or timeout after 10 seconds
            executor.awaitTermination(10, TimeUnit.SECONDS);

            // Sum the results
            int sum = 0;
            for (Future<String> result : results) {
                sum += Integer.parseInt(result.get());
            }

            // Print the sum
            System.out.println("Sum of results: " + sum);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    // Method to perform computation based on input
    private static String compute(int input) {
        // Replace this with your actual computation
        return String.valueOf(input);
    }
}
