package QuickSort;
public class QuickSort {
    
    public static void sort(int[] array) { 
        quickSort(array, 0, array.length - 1); 
    }

    private static void quickSort(int[] array, int low, int high) {
        if (low < high) { // verifica se tem mais de um elemento
            int pivotIndex = partition(array, low, high); // particiona o array e obtém o índice do pivô
            quickSort(array, low, pivotIndex - 1); // chama quickSort para a sublista à esquerda do pivô
            quickSort(array, pivotIndex + 1, high); // chama quickSort para a sublista à direita do pivô
        }
    }

    private static int partition(int[] array, int low, int high) { 
        int pivot = array[high]; // define o último elemento do array como pivô
        int i = low - 1; // inicializa i uma posição antes do limite inferior

        for (int j = low; j < high; j++) { // percorre os elementos do array até o elemento anterior ao pivô
            if (array[j] <= pivot) { // se o elemento atual for menor ou igual ao pivô,
                i++; // incrementa i para definir uma nova posição de troca
                //faz a troca entre os elementos
                int temp = array[i]; 
                array[i] = array[j]; 
                array[j] = temp; 
            }
        }
        int temp = array[i + 1]; // armazena temporariamente o valor após a última posição trocada
        array[i + 1] = array[high]; // move o pivô para sua posição correta (no meio dos menores e maiores)
        array[high] = temp; // coloca o valor temporário na posição do pivô original

        return i + 1; // retorna o índice do pivô
    }
}
