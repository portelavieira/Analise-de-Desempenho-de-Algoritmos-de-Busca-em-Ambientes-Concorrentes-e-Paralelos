package InsertionSort;
// ParallelInsertionSort.java
public class ParallelInsertionSort {
    private int[] arr;
    private int numThreads;

    public ParallelInsertionSort(int[] arr, int numThreads) {
        this.arr = arr;
        this.numThreads = numThreads;
    }

    public void insertionSort() {
        Thread[] threads = new Thread[numThreads];
        int chunkSize = arr.length / numThreads;

        // Cria threads para ordenar seções do array em paralelo
        for (int t = 0; t < numThreads; t++) {
            final int start = t * chunkSize;
            final int end = (t == numThreads - 1) ? arr.length : start + chunkSize;

            threads[t] = new Thread(() -> {
                insertionSortSection(arr, start, end);
            });
            threads[t].start();
        }

        // Aguarda todas as threads concluírem a ordenação
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Mescla as seções ordenadas
        mergeSections();
    }

    // Função para ordenar uma seção do array usando Insertion Sort
    private void insertionSortSection(int[] arr, int start, int end) {
        for (int i = start + 1; i < end; i++) {
            int key = arr[i];
            int j = i - 1;

            while (j >= start && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    // Função para mesclar seções ordenadas do array
    private void mergeSections() {
        int[] temp = new int[arr.length];
        System.arraycopy(arr, 0, temp, 0, arr.length);
        int chunkSize = arr.length / numThreads;

        for (int t = 1; t < numThreads; t++) {
            int start1 = 0;
            int end1 = (t * chunkSize) - 1;
            int start2 = t * chunkSize;
            int end2 = (t == numThreads - 1) ? arr.length - 1 : start2 + chunkSize - 1;

            int index = 0;
            int k = start1, j = start2;
            while (k <= end1 && j <= end2) {
                if (temp[k] <= temp[j]) {
                    arr[index++] = temp[k++];
                } else {
                    arr[index++] = temp[j++];
                }
            }
            while (k <= end1) arr[index++] = temp[k++];
            while (j <= end2) arr[index++] = temp[j++];
        }
    }
}
