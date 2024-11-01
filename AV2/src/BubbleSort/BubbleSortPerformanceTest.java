package BubbleSort;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class BubbleSortPerformanceTest {
    private static final int NUM_RUNS = 5;

    public static void main(String[] args) throws InterruptedException, IOException {
        int[] sizes = {1000, 2000, 5000, 10000, 20000}; // Tamanhos do array para testar
        
        // Escrita do cabeçalho do arquivo CSV
        try (FileWriter writer = new FileWriter("BubbleSort_resultados.csv")) {
            writer.append("Array Size,Serial Time (ms),Parallel Time (ms),Speedup\n");
            
            for (int size : sizes) {
                System.out.println("Array Size: " + size);
                
                long serialTime = testSerialBubbleSort(size);
                long parallelTime = testParallelBubbleSort(size);
                double speedup = (double) serialTime / parallelTime;

                // Imprime os resultados no console
                System.out.println("Serial Bubble Sort Time: " + serialTime + " ms");
                System.out.println("Parallel Bubble Sort Time: " + parallelTime + " ms");
                System.out.println("Speedup: " + speedup);
                System.out.println();

                // Grava os resultados no arquivo CSV
                writer.append(size + "," + serialTime + "," + parallelTime + "," + speedup + "\n");
            }
        }
    }

    private static long testSerialBubbleSort(int size) {
        long totalTime = 0;
        for (int i = 0; i < NUM_RUNS; i++) {
            int[] array = generateRandomArray(size);
            long startTime = System.currentTimeMillis();
            BubbleSortSerial.bubbleSort(array);
            totalTime += System.currentTimeMillis() - startTime;
        }
        return totalTime / NUM_RUNS;
    }

    private static long testParallelBubbleSort(int size) throws InterruptedException {
        long totalTime = 0;
        for (int i = 0; i < NUM_RUNS; i++) {
            int[] array = generateRandomArray(size);
            long startTime = System.currentTimeMillis();
            ParallelBubbleSort.parallelBubbleSort(array);
            totalTime += System.currentTimeMillis() - startTime;
        }
        return totalTime / NUM_RUNS;
    }

    private static int[] generateRandomArray(int size) {
        Random rand = new Random();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = rand.nextInt(10000); // Gera números aleatórios entre 0 e 9999
        }
        return array;
    }
}
