package BubbleSort;
import java.util.Arrays;

public class ParallelBubbleSort {
    private static final int NUM_THREADS = 4; // Número de threads paralelas (representa N-1 nós)
    
    public static void parallelBubbleSort(int[] array) throws InterruptedException {
        int length = array.length;
        int partSize = length / NUM_THREADS; // Tamanho de cada parte para os threads
        Thread[] threads = new Thread[NUM_THREADS];
        int[][] parts = new int[NUM_THREADS][];
        
        // Dividir o array em partes e ordenar cada parte em threads separadas
        for (int i = 0; i < NUM_THREADS; i++) {
            int start = i * partSize;
            int end = (i == NUM_THREADS - 1) ? length : start + partSize;
            parts[i] = Arrays.copyOfRange(array, start, end);

            threads[i] = new Thread(new BubbleSortTask(parts[i]));
            threads[i].start(); // Inicia a ordenação paralela
        }

        // Aguarda todos os threads finalizarem a ordenação de suas partes
        for (Thread thread : threads) {
            thread.join();
        }

        // Nó 0: fusão global das partes ordenadas
        mergeParts(array, parts);
    }

    // Classe que realiza o Bubble Sort em uma parte do array
    private static class BubbleSortTask implements Runnable {
        private final int[] array;

        public BubbleSortTask(int[] array) {
            this.array = array;
        }

        @Override
        public void run() {
            bubbleSort(array);
        }

        private void bubbleSort(int[] arr) {
            boolean swapped;
            int n = arr.length;
            do {
                swapped = false;
                for (int i = 0; i < n - 1; i++) {
                    if (arr[i] > arr[i + 1]) {
                        int temp = arr[i];
                        arr[i] = arr[i + 1];
                        arr[i + 1] = temp;
                        swapped = true;
                    }
                }
                n--; // Reduz o limite do Bubble Sort
            } while (swapped);
        }
    }

    // Função para realizar a fusão das partes ordenadas no array original
    private static void mergeParts(int[] array, int[][] parts) {
        int[] indices = new int[parts.length];
        int arrayIndex = 0;

        while (arrayIndex < array.length) {
            int minIndex = -1;
            int minValue = Integer.MAX_VALUE;

            // Encontra o menor valor atual entre as partes
            for (int i = 0; i < parts.length; i++) {
                if (indices[i] < parts[i].length && parts[i][indices[i]] < minValue) {
                    minIndex = i;
                    minValue = parts[i][indices[i]];
                }
            }

            // Adiciona o menor valor ao array e move o índice da parte correspondente
            array[arrayIndex++] = minValue;
            indices[minIndex]++;
        }
    }

    // Função principal para testar o Bubble Sort paralelo
    public static void main(String[] args) throws InterruptedException {
        int[] array = {14, 7, 3, 12, 9, 11, 6, 2, 15, 5, 10, 1, 4, 8, 13};
        
        System.out.println("Array original: " + Arrays.toString(array));
        
        parallelBubbleSort(array);
        
        System.out.println("Array ordenado: " + Arrays.toString(array));
    }
}
