package MergeSort;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;

public class MergeSortPerformanceTest {
    public static void main(String[] args) {
        int[] dataSizes = {1000, 5000, 10000, 50000, 100000}; // Exemplo de tamanhos de arrays
        int[] numThreads = {1, 2, 4, 8, 16}; // Variedade de threads para o teste paralelo

        try (FileWriter csvWriter = new FileWriter("MergeSort_resultados.csv")) {
            csvWriter.append("ArraySize,SerialTime,ParallelTime\n");

            for (int size : dataSizes) {
                int[] array = generateArray(size);

                // Teste Serial
                long serialTime = measureMergeSortSerial(array.clone());
                csvWriter.append(size + "," + serialTime + ",");
                
                // Teste Paralelo para diferentes números de threads
                for (int threads : numThreads) {
                    long parallelTime = measureMergeSortParallel(array.clone(), threads);
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

    private static long measureMergeSortSerial(int[] array) {
        long start = System.currentTimeMillis();
        MergeSort.sort(array);
        return System.currentTimeMillis() - start;
    }

    private static long measureMergeSortParallel(int[] array, int threads) {
        ForkJoinPool pool = new ForkJoinPool(threads);
        long start = System.currentTimeMillis();
        pool.invoke(new MergeSortParallel(array, 0, array.length - 1));
        return System.currentTimeMillis() - start;
    }
}
