package MergeSort;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class MergeSortPerformanceTest {
    private static final int[] SIZES = {1000, 5000, 10000, 50000, 100000}; // Tamanhos variados
    private static final int[] THREAD_COUNTS = {1, 2, 4, 8}; // Diferentes números de threads
    private static final int SAMPLES = 5; // Amostras para média

    public static void main(String[] args) {
        String outputFile = "MergeSort_resultados.csv";

        try (FileWriter writer = new FileWriter(outputFile)) {
            writer.write("Execucao,Tamanho,Threads,Tempo (ms)\n");

            // Realiza testes de performance para cada algoritmo
            for (int size : SIZES) {
                int[] originalArray = generateRandomArray(size);

                // Testes seriais
                for (int i = 0; i < SAMPLES; i++) {
                    int[] array = Arrays.copyOf(originalArray, originalArray.length);
                    long time = measureSerial(array);
                    writer.write("Serial," + size + ",1," + time + "\n");
                }

                // Testes paralelos
                for (int threads : THREAD_COUNTS) {
                    for (int i = 0; i < SAMPLES; i++) {
                        int[] array = Arrays.copyOf(originalArray, originalArray.length);
                        long time = measureParallel(array, threads);
                        writer.write("Parallel," + size + "," + threads + "," + time + "\n");
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int[] generateRandomArray(int size) {
        Random random = new Random();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(10000); // Valores de exemplo
        }
        return array;
    }

    private static long measureSerial(int[] array) {
        long start = System.currentTimeMillis();
        MergeSort.sort(array); // Substituir pelo método serial do algoritmo específico
        return System.currentTimeMillis() - start;
    }

    private static long measureParallel(int[] array, int threads) {
        ForkJoinPool pool = new ForkJoinPool(threads);
        long start = System.currentTimeMillis();
        pool.invoke(new MergeSortParallel(array, 0, array.length - 1)); // Método paralelo
        pool.shutdown();
        pool.shutdown();
        return System.currentTimeMillis() - start;
    }
}
