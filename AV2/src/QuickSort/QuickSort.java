package QuickSort;
public class QuickSort {
    // Método principal que implementa o Quick Sort
    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(arr, low, high); // Encontra o índice do pivô
            quickSort(arr, low, pivotIndex - 1); // Ordena os elementos antes do pivô
            quickSort(arr, pivotIndex + 1, high); // Ordena os elementos depois do pivô
        }
    }

    // Função para dividir o array e encontrar o pivô
    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                // Troca arr[i] e arr[j]
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        // Troca arr[i+1] e arr[high] (ou pivô)
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }

    // Método para imprimir o array
    public static void printArray(int[] arr) {
        for (int num : arr) {
            System.out.print(num + " ");
        }
        System.out.println();
    }

    // Método principal
    public static void main(String[] args) {
        int[] arr = {10, 7, 8, 9, 1, 5}; // Array de teste
        System.out.println("Array original:");
        printArray(arr);

        quickSort(arr, 0, arr.length - 1); // Chama o Quick Sort

        System.out.println("Array ordenado:");
        printArray(arr);
    }
}
