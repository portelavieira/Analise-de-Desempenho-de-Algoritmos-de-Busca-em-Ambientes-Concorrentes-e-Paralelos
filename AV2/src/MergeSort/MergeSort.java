package MergeSort;
public class MergeSort {
    public static void sort(int[] array) {
        mergeSort(array, 0, array.length - 1);
    }

    private static void mergeSort(int[] array, int left, int right) {
        if (left < right) { //para verificar se é possível continuar a divisão
            int mid = (left + right) / 2; // calcula o índice do meio para dividir o array
            mergeSort(array, left, mid);// chama mergeSort para a metade esquerda do array
            mergeSort(array, mid + 1, right); // chama mergeSort para a metade direita do array
            merge(array, left, mid, right); // chama o método merge para combinar as duas metades
        }
    }

    private static void merge(int[] array, int left, int mid, int right) {
        int[] temp = new int[right - left + 1]; // cria um array temporário para armazenar os elementos mesclados
        int i = left, j = mid + 1, k = 0; // inicializa índices para percorrer as duas metades e o array temporário

        while (i <= mid && j <= right) { // percorre as duas metades enquanto houver elementos em ambas
            temp[k++] = (array[i] <= array[j]) ? array[i++] : array[j++]; // copia o menor elemento para temp
        }
        while (i <= mid) temp[k++] = array[i++]; // copia os elementos restantes da metade esquerda para temp
        while (j <= right) temp[k++] = array[j++]; // copia os elementos restantes da metade direita para temp

        System.arraycopy(temp, 0, array, left, temp.length); // copia os elementos mesclados de temp para o array original
    }
}
