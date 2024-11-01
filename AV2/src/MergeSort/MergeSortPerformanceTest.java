package MergeSort;
// MergeSortPerformanceTest.java
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class MergeSortPerformanceTest {

    public static void main(String[] args) {
        int[] dataSizes = {1000, 5000, 10000, 50000, 100000}; // Diferentes tamanhos de array para testes
        int samples = 5; // Número de amostras por configuração

        try (FileWriter writer = new FileWriter("MergeSort_resultados.csv")) {
            writer.write("Data Size,Sample,Time (ms),Mode\n");

            for (int dataSize : dataSizes) {
                int[] array = generateRandomArray(dataSize, 1000); // Gera array de teste com valores no intervalo 0-999

                // Teste serial
                for (int i = 0; i < samples; i++) {
                    int[] arrayCopy = Arrays.copyOf(array, array.length);
                    long startTime = System.currentTimeMillis();
                    MergeSort.mergeSort(arrayCopy); // Executa Merge Sort Serial
                    long endTime = System.currentTimeMillis();
                    long duration = endTime - startTime;

                    writer.write(dataSize + "," + (i + 1) + "," + duration + ",Serial\n");
                }

                // Teste Fork-Join
                for (int i = 0; i < samples; i++) {
                    int[] arrayCopy = Arrays.copyOf(array, array.length);
                    long startTime = System.currentTimeMillis();

                    ParallelMergeSort.mergeSort(arrayCopy); // Executa Merge Sort com Fork-Join

                    long endTime = System.currentTimeMillis();
                    long duration = endTime - startTime;

                    writer.write(dataSize + "," + (i + 1) + "," + duration + ",Paralelo\n");
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
