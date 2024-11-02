package BubbleSort;
import java.util.Arrays;

public class ParallelBubbleSort {
    private static final int NUM_THREADS = 4; // número de threads paralelas (representa N-1 nós)

    public static void parallelBubbleSort(int[] array) throws InterruptedException { 
        int length = array.length; //armazena o tamanho da array (parâmetro)
        int partSize = length / NUM_THREADS; // calcula o tamanho de cada parte a ser ordenada por uma thread
        Thread[] threads = new Thread[NUM_THREADS]; // cria um array de threads
        int[][] parts = new int[NUM_THREADS][]; // cria um array de subarrays para cada parte a ser ordenada

        for (int i = 0; i < NUM_THREADS; i++) { // divide o array e cria threads para cada parte
            int start = i * partSize; // define o índice inicial da parte atual
            int end = (i == NUM_THREADS - 1) ? length : start + partSize; // define o índice final da parte (última parte pode ser maior)
            parts[i] = Arrays.copyOfRange(array, start, end); // copia a parte do array original para o subarray

            threads[i] = new Thread(new BubbleSortTask(parts[i])); // cria uma nova thread para ordenar a parte com BubbleSortTask
            threads[i].start(); // inicia a thread
        }

        for (Thread thread : threads) { // espera que todas as threads terminem a execução
            thread.join();
        }

        mergeParts(array, parts); // combina as partes ordenadas em um único array
    }

    // Classe que realiza o Bubble Sort em uma parte do array
    private static class BubbleSortTask implements Runnable { 
        private final int[] array; // array que será ordenado pela tarefa

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
                for (int i = 0; i < n - 1; i++) { // loop para comparar e trocar elementos adjacentes
                    if (arr[i] > arr[i + 1]) { // verifica se o elemento atual é maior que o próximo
                        int temp = arr[i]; // troca os elementos
                        arr[i] = arr[i + 1];
                        arr[i + 1] = temp;
                        swapped = true; // define swapped como verdadeiro para indicar que houve uma troca
                    }
                }
                n--; // reduz o tamanho do array para ignorar o último elemento ordenado
            } while (swapped); // repete até que nenhuma troca ocorra
        }
    }

    // Função para realizar a fusão das partes ordenadas no array original
    private static void mergeParts(int[] array, int[][] parts) {
        int[] indices = new int[parts.length]; // array de índices para acompanhar a posição em cada subarray
        int arrayIndex = 0; // índice do array principal para onde os elementos serão mesclados

        while (arrayIndex < array.length) { // loop para mesclar todos os elementos nos subarrays
            int minIndex = -1; // índice do menor valor encontrado nas partes
            int minValue = Integer.MAX_VALUE; // inicializa minValue com o maior valor possível

            for (int i = 0; i < parts.length; i++) { // loop para encontrar o menor valor entre as partes
                if (indices[i] < parts[i].length && parts[i][indices[i]] < minValue) { // verifica se há elementos restantes e encontra o menor valor
                    minIndex = i;
                    minValue = parts[i][indices[i]]; // atualiza o índice e o valor mínimos
                }
            }

            array[arrayIndex++] = minValue; // coloca o menor valor no array principal
            indices[minIndex]++; // avança o índice da parte de onde veio o menor valor
        }
    }
}