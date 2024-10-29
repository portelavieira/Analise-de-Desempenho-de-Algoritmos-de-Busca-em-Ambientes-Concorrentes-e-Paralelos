package CountingSort;
public class CountingSort {
    public static void countingSort(int[] arr, int range) {
        int[] count = new int[range];
        int[] output = new int[arr.length];

        // Conta cada elemento no array de entrada
        for (int num : arr) {
            count[num]++;
        }

        // Modifica o array de contagem para armazenar as posições finais dos elementos
        for (int i = 1; i < range; i++) {
            count[i] += count[i - 1];
        }

        // Constrói o array de saída
        for (int i = arr.length - 1; i >= 0; i--) {
            output[count[arr[i]] - 1] = arr[i];
            count[arr[i]]--;
        }

        // Copia o array de saída para o array de entrada
        System.arraycopy(output, 0, arr, 0, arr.length);
    }
}