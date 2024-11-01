package MergeSort;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;

public class ParallelMergeSort {
    public static void mergeSort(int[] array) {
        ForkJoinPool pool = new ForkJoinPool(); // Cria um pool de threads ForkJoin
        pool.invoke(new MergeSortTask(array, 0, array.length - 1)); // Executa a tarefa inicial
        pool.shutdown();
    }

    private static class MergeSortTask extends RecursiveAction {
        private final int[] array;
        private final int inicio;
        private final int fim;

        public MergeSortTask(int[] array, int inicio, int fim) {
            this.array = array;
            this.inicio = inicio;
            this.fim = fim;
        }

        @Override
        protected void compute() {
            if (inicio < fim) {
                int meio = (inicio + fim) / 2; // Calcula o meio do array

                // Realiza as chamadas recursivas paralelamente (fork)
                invokeAll(new MergeSortTask(array, inicio, meio),
                          new MergeSortTask(array, meio + 1, fim));

                // Intercala os subvetores ordenados (join)
                merge(array, inicio, meio, fim);
            }
        }

        private void merge(int[] array, int inicio, int meio, int fim) {
            int[] temp = new int[fim - inicio + 1];
            int i = inicio, j = meio + 1, k = 0;

            while (i <= meio && j <= fim) {
                if (array[i] <= array[j]) {
                    temp[k++] = array[i++];
                } else {
                    temp[k++] = array[j++];
                }
            }

            while (i <= meio) temp[k++] = array[i++];
            while (j <= fim) temp[k++] = array[j++];

            System.arraycopy(temp, 0, array, inicio, temp.length);
        }
    }

    // Exemplo de uso
    public static void main(String[] args) {
        int[] meuArray = {14, 7, 3, 12, 9, 11, 6, 2};
        MergeSortTask task = new MergeSortTask(meuArray, 0, meuArray.length - 1);
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(task); // Executa o Merge Sort paralelamente

        // Exibe o array ordenado
        System.out.print("Array ordenado: ");
        for (int num : meuArray) {
            System.out.print(num + " ");
        }
    }
}
