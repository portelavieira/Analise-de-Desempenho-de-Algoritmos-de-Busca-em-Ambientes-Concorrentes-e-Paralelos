package InsertionSort;
public class InsertionSort {
    public static void sort(int[] array) {
        int n = array.length; //armazena em n o tamanho da array (parâmetro)
        for (int i = 1; i < n; i++) { //percorre o array
            int key = array[i]; //armazena o valor do elemento atual
            int j = i - 1; //armazena a posição do elemento anterior
            while (j >= 0 && array[j] > key) { //percorre os elementos à esquerda da variável key
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = key; // insere o valor de key na posição correta, depois dos elementos maiores que ele
        }
    }
}
