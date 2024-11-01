package InsertionSort;
import java.io.FileWriter;
import java.io.IOException;

public class InsertionSortPerformanceTest {
    public static void main(String[] args) {
        int[] dataSizes = {1000, 5000, 10000, 50000, 100000};
        int[] numThreads = {1, 2, 4, 8, 16};

        try (FileWriter csvWriter = new FileWriter("InsertionSort_resultados.csv")) {
            csvWriter.append("ArraySize,SerialTime,ParallelTime\n");

            for (int size : dataSizes) {
                int[] array = generateArray(size);

                // Teste Serial
                long serialTime = measureInsertionSortSerial(array.clone());
                csvWriter.append(size + "," + serialTime + ",");

                // Teste Paralelo (usando diferentes números de threads)
                for (int threads : numThreads) {
                    long parallelTime = measureInsertionSortParallel(array.clone(), threads);
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

    private static long measureInsertionSortSerial(int[] array) {
        long start = System.currentTimeMillis();
        InsertionSort.sort(array);
        return System.currentTimeMillis() - start;
    }

    private static long measureInsertionSortParallel(int[] array, int threads) {
        // Paralelização para o Insertion Sort é incomum, mas pode ser simulada aqui como exemplo
        long start = System.currentTimeMillis();
        InsertionSortParallel.sort(array, threads);
        return System.currentTimeMillis() - start;
    }
}
