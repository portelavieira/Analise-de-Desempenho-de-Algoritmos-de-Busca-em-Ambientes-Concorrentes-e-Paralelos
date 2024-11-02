package QuickSort;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;

public class QuickSortParallel extends RecursiveAction {
    private int[] array; // array a ser ordenado
    private int low; // índice mais baixo da porção do array a ser ordenada
    private int high; // índice mais alto da porção do array a ser ordenada

    public QuickSortParallel(int[] array, int low, int high) {
        this.array = array;
        this.low = low;
        this.high = high;
    }

    @Override
    protected void compute() {
        if (low < high) { // verifica se a porção do array tem mais de um elemento para dividir
            int pivotIndex = partition(array, low, high); // particiona o array e obtém o índice do pivô
            // cria tarefas para a metade esquerda e direita, excluindo o pivô
            QuickSortParallel leftTask = new QuickSortParallel(array, low, pivotIndex - 1);
            QuickSortParallel rightTask = new QuickSortParallel(array, pivotIndex + 1, high);
            invokeAll(leftTask, rightTask); // executa ambas as tarefas em paralelo
        }
    }

    private int partition(int[] array, int low, int high) {
        int pivot = array[high]; // define o último elemento como pivô
        int i = low - 1; // inicializa o índice do menor elemento
        for (int j = low; j < high; j++) { // percorre os elementos do array
            if (array[j] <= pivot) { // se o elemento atual for menor ou igual ao pivô
                i++; // incrementa o índice do menor elemento
                // troca o elemento na posição i com o elemento atual
                int temp = array[i]; 
                array[i] = array[j];
                array[j] = temp;
            }
        }
        // troca o pivô para sua posição correta
        int temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;
        return i + 1; // retorna o índice do pivô
    }

    public static void sort(int[] array, int numThreads) { // método para iniciar a ordenação paralela com Quick Sort
        ForkJoinPool pool = new ForkJoinPool(numThreads); // cria um pool de threads com o número especificado
        pool.invoke(new QuickSortParallel(array, 0, array.length - 1)); // inicia a tarefa de ordenação no pool
        pool.shutdown();
    }
}
