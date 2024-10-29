package CountingSort;
// ParallelCountingSort.java
public class ParallelCountingSort {
    private int[] arr;
    private int range, numThreads;

    public ParallelCountingSort(int[] arr, int range, int numThreads) {
        this.arr = arr;
        this.range = range;
        this.numThreads = numThreads;
    }

    public void countingSort() {
        int[] count = new int[range];
        int[][] localCounts = new int[numThreads][range];
        
        // Fase 1: Contagem local em threads paralelas
        Thread[] threads = new Thread[numThreads];
        int chunkSize = arr.length / numThreads;

        for (int t = 0; t < numThreads; t++) {
            final int threadId = t;
            threads[t] = new Thread(() -> {
                int start = threadId * chunkSize;
                int end = (threadId == numThreads - 1) ? arr.length : start + chunkSize;
                
                for (int i = start; i < end; i++) {
                    localCounts[threadId][arr[i]]++;
                }
            });
            threads[t].start();
        }

        // Aguarda todas as threads completarem a contagem local
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Fase 2: Combina contagens locais em uma contagem global
        for (int t = 0; t < numThreads; t++) {
            for (int i = 0; i < range; i++) {
                count[i] += localCounts[t][i];
            }
        }

        // Fase 3: Modifica o array de contagem para obter as posições finais dos elementos
        for (int i = 1; i < range; i++) {
            count[i] += count[i - 1];
        }

        // Fase 4: Constrói o array de saída em paralelo
        int[] output = new int[arr.length];
        threads = new Thread[numThreads];
        
        for (int t = 0; t < numThreads; t++) {
            final int threadId = t;
            threads[t] = new Thread(() -> {
                int start = threadId * chunkSize;
                int end = (threadId == numThreads - 1) ? arr.length : start + chunkSize;
                
                for (int i = end - 1; i >= start; i--) {
                    int position = --count[arr[i]];
                    output[position] = arr[i];
                }
            });
            threads[t].start();
        }

        // Aguarda todas as threads completarem a construção do array de saída
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Copia o array de saída para o array de entrada
        System.arraycopy(output, 0, arr, 0, arr.length);
    }
}
