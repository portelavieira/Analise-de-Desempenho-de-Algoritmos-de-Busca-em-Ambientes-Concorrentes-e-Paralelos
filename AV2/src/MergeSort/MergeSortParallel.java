package MergeSort;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;

public class MergeSortParallel extends RecursiveAction {
    private int[] array; // array a ser ordenado
    private int left; // índice inicial da porção do array a ser ordenada
    private int right; // índice final da porção do array a ser ordenada

    public MergeSortParallel(int[] array, int left, int right) {
        this.array = array;
        this.left = left;
        this.right = right;
    }

    @Override
    protected void compute() {
        if (left < right) { // verifica se a porção do array tem mais de um elemento para dividir
            int mid = (left + right) / 2; // calcula o ponto médio para dividir o array
            MergeSortParallel leftTask = new MergeSortParallel(array, left, mid); // cria uma tarefa para a metade esquerda
            MergeSortParallel rightTask = new MergeSortParallel(array, mid + 1, right); // cria uma tarefa para a metade direita
            invokeAll(leftTask, rightTask); // executa ambas as tarefas em paralelo
            merge(array, left, mid, right); // mescla as duas partes ordenadas
        }
    }

    private void merge(int[] array, int left, int mid, int right) {
        int[] temp = new int[right - left + 1]; // array temporário para armazenar a mescla
        int i = left, j = mid + 1, k = 0; // índices para percorrer as metades e o array temporário

        while (i <= mid && j <= right) { // mescla os elementos das duas metades ordenadas
            temp[k++] = (array[i] <= array[j]) ? array[i++] : array[j++]; // adiciona o menor elemento ao temp
        }
        while (i <= mid) temp[k++] = array[i++]; // copia elementos restantes da primeira metade
        while (j <= right) temp[k++] = array[j++]; // copia elementos restantes da segunda metade

        System.arraycopy(temp, 0, array, left, temp.length); // copia os elementos mesclados de volta ao array original
    }

    public static void sort(int[] array, int numThreads) {
        ForkJoinPool pool = new ForkJoinPool(numThreads); // cria um pool de threads com o número especificado
        pool.invoke(new MergeSortParallel(array, 0, array.length - 1)); // inicia a tarefa de ordenação no pool
        pool.close();
    }
}
