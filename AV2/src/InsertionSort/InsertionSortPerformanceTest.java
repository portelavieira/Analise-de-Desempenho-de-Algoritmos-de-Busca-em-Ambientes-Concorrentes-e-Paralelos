package InsertionSort;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class InsertionSortPerformanceTest {

    public static void main(String[] args) {
        int[] tamanhosArray = {1000, 5000, 10000, 20000, 50000}; // Tamanhos variados para o array
        int[] numThreads = {1, 2, 4, 8}; // Diferentes configurações de threads

        try (FileWriter writer = new FileWriter("InsertionSort_resultados.csv")) {
            writer.write("Execucao,Tamanho,Threads,Tempo (ms)\n");

            for (int tamanho : tamanhosArray) {
                for (int threads : numThreads) {
                    for (int execucao = 1; execucao <= 5; execucao++) {
                        int[] array = gerarArrayAleatorio(tamanho);
                        
                        // Teste Serial
                        int[] arraySerial = Arrays.copyOf(array, array.length);
                        long tempoSerial = medirTempoInsertionSortSerial(arraySerial);
                        writer.write("Serial," + tamanho + ",1," + tempoSerial + "\n");
                        
                        // Teste Paralelo
                        int[] arrayParalelo = Arrays.copyOf(array, array.length);
                        long tempoParalelo = medirTempoInsertionSortParalelo(arrayParalelo, threads);
                        writer.write("Paralelo," + tamanho + "," + threads + "," + tempoParalelo + "\n");
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo CSV: " + e.getMessage());
        }
    }

    private static int[] gerarArrayAleatorio(int tamanho) {
        Random random = new Random();
        int[] array = new int[tamanho];
        for (int i = 0; i < tamanho; i++) {
            array[i] = random.nextInt(10000); // Valores aleatórios entre 0 e 9999
        }
        return array;
    }

    private static long medirTempoInsertionSortSerial(int[] array) {
        long inicio = System.currentTimeMillis();
        InsertionSort.sort(array);
        return System.currentTimeMillis() - inicio;
    }

    private static long medirTempoInsertionSortParalelo(int[] array, int threads) {
        long inicio = System.currentTimeMillis();
        InsertionSortParallel.parallelSort(array, threads);
        return System.currentTimeMillis() - inicio;
    }
}
