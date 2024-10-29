package InsertionSort;
// InsertionSortPerformanceTest.java
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class InsertionSortPerformanceTest {

    public static void main(String[] args) {
        int[] dataSizes = {1000, 5000, 10000, 50000, 100000}; // Diferentes tamanhos de array para testes
        int numThreads[] = {1, 2, 4, 8}; // Diferentes números de threads para teste paralelo
        int samples = 5; // Número de amostras por configuração

        try (FileWriter writer = new FileWriter("insertion_sort_performance_results.csv")) {
            writer.write("Data Size,Threads,Sample,Time (ms),Mode\n");

            for (int dataSize : dataSizes) {
                int[] array = generateRandomArray(dataSize, 1000); // Gera array de teste com valores no intervalo 0-999

                // Teste serial
                for (int i = 0; i < samples; i++) {
                    int[] arrayCopy = Arrays.copyOf(array, array.length);
                    long startTime = System.currentTimeMillis();
                    InsertionSort.insertionSort(arrayCopy); // Executa Insertion Sort Serial
                    long endTime = System.currentTimeMillis();
                    long duration = endTime - startTime;

                    writer.write(dataSize + ",1," + (i + 1) + "," + duration + ",Serial\n");
                }

                // Teste paralelo
                for (int threadCount : numThreads) {
                    for (int i = 0; i < samples; i++) {
                        int[] arrayCopy = Arrays.copyOf(array, array.length);
                        long startTime = System.currentTimeMillis();

                        ParallelInsertionSort insertionSortTask = new ParallelInsertionSort(arrayCopy, threadCount);
                        insertionSortTask.insertionSort();

                        long endTime = System.currentTimeMillis();
                        long duration = endTime - startTime;

                        writer.write(dataSize + "," + threadCount + "," + (i + 1) + "," + duration + ",Parallel\n");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Gera um array de inteiros aleatórios no intervalo [0, range)
    private static int[] generateRandomArray(int size, int range) {
        Random random = new Random();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(range); // Inteiros aleatórios de 0 até (range - 1)
        }
        return array;
    }
}
