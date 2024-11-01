package QuickSort;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;

public class QuickSortPerformanceTest {
    public static void main(String[] args) {
        int[] dataSizes = {1000, 5000, 10000, 50000, 100000};
        int[] numThreads = {1, 2, 4, 8, 16};

        try (FileWriter csvWriter = new FileWriter("QuickSort_resultados.csv")) {
            csvWriter.append("ArraySize,SerialTime,ParallelTime\n");

            for (int size : dataSizes) {
                int[] array = generateArray(size);

                // Teste Serial
                long serialTime = measureQuickSortSerial(array.clone());
                csvWriter.append(size + "," + serialTime + ",");

                // Teste Paralelo para diferentes números de threads
                for (int threads : numThreads) {
                    long parallelTime = measureQuickSortParallel(array.clone(), threads);
                    csvWriter.append(parallelTime + (threads == numThreads[numThreads.length - 1] ? "\n" : ","));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int[] generateArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = (int) (Math.random() * size);
        }
        return array;
    }

    private static long measureQuickSortSerial(int[] array) {
        long start = System.currentTimeMillis();
        QuickSort.sort(array);
        return System.currentTimeMillis() - start;
    }

    private static long measureQuickSortParallel(int[] array, int threads) {
        ForkJoinPool pool = new ForkJoinPool(threads);
        long start = System.currentTimeMillis();
        pool.invoke(new QuickSortParallel(array, 0, array.length - 1));
        return System.currentTimeMillis() - start;
    }
}
