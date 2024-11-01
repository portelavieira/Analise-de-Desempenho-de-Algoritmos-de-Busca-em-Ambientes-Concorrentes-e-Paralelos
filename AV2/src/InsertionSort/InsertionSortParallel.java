package InsertionSort;
import java.util.concurrent.*;

public class InsertionSortParallel {
    public static void sort(int[] array, int numThreads) {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        int chunkSize = array.length / numThreads;
        Future<?>[] futures = new Future<?>[numThreads];

        for (int i = 0; i < numThreads; i++) {
            final int start = i * chunkSize;
            final int end = (i == numThreads - 1) ? array.length : start + chunkSize;
            futures[i] = executor.submit(() -> insertionSortPartial(array, start, end));
        }

        try {
            for (Future<?> future : futures) {
                future.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }

        // Realiza a fusão final, se necessário
        // mergeSortedChunks(array, numThreads, chunkSize);
    }

    private static void insertionSortPartial(int[] array, int start, int end) {
        for (int i = start + 1; i < end; i++) {
            int key = array[i];
            int j = i - 1;
            while (j >= start && array[j] > key) {
                array[j + 1] = array[j];
                j = j - 1;
            }
            array[j + 1] = key;
        }
    }
}
