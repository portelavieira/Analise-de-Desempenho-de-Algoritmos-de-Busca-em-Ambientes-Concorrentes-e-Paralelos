package InsertionSort;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;

public class InsertionSortParallel extends RecursiveAction { 
    private static final int THRESHOLD = 1000; // define o limiar de tamanho do array
    private int[] array; // array a ser ordenado
    private int start, end; // índices de início e fim da porção do array a ser ordenada

    public InsertionSortParallel(int[] array, int start, int end) { 
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected void compute() { // método que define a tarefa para a ordenação
        if (end - start <= THRESHOLD) { // verifica se o tamanho da porção está abaixo do limiar para usar inserção direta
            insertionSort(array, start, end); // ordena a porção do array usando Insertion Sort
        } else {
            int mid = (start + end) / 2; // calcula o ponto médio para dividir o array em duas partes
            InsertionSortParallel leftTask = new InsertionSortParallel(array, start, mid); // cria a tarefa para a metade esquerda
            InsertionSortParallel rightTask = new InsertionSortParallel(array, mid + 1, end); // cria a tarefa para a metade direita

            invokeAll(leftTask, rightTask); // executa ambas as tarefas em paralelo
            merge(array, start, mid, end); // mescla as duas partes ordenadas
        }
    }

    private void insertionSort(int[] array, int start, int end) {
        for (int i = start + 1; i <= end; i++) { // percorre o array a partir do segundo elemento da sublista
            int key = array[i]; // define o elemento a ser inserido
            int j = i - 1;

            while (j >= start && array[j] > key) { // desloca elementos maiores que a chave para a direita
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = key; // insere a chave na posição correta
        }
    }

    private void merge(int[] array, int start, int mid, int end) {
        int[] temp = new int[end - start + 1]; // array temporário para armazenar o resultado da mescla
        int i = start, j = mid + 1, k = 0; // índices para as metades esquerda e direita e o array temporário

        while (i <= mid && j <= end) { // mescla os elementos das duas partes ordenadas
            if (array[i] <= array[j]) {
                temp[k++] = array[i++];
            } else {
                temp[k++] = array[j++];
            }
        }

        while (i <= mid) temp[k++] = array[i++]; // copia os elementos restantes da primeira metade
        while (j <= end) temp[k++] = array[j++]; // copia os elementos restantes da segunda metade

        System.arraycopy(temp, 0, array, start, temp.length); // copia os elementos mesclados de volta ao array original
    }

    public static void parallelSort(int[] array, int threads) { // método para iniciar o sort paralelo usando o ForkJoinPool
        ForkJoinPool pool = new ForkJoinPool(threads); // cria um pool de threads com o número de threads especificado
        pool.invoke(new InsertionSortParallel(array, 0, array.length - 1)); // inicia a tarefa de ordenação no pool
        pool.shutdown(); 
        pool.shutdown(); 
    }
}
