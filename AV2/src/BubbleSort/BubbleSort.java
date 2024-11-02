package BubbleSort;
public class BubbleSort {
    public static void bubbleSort(int[] array) {
        int n = array.length; //armazena em n o tamanho da array (parâmetro)
        boolean swapped; // declara uma variável booleana para rastrear se houve troca de elementos
        
        do {
            swapped = false;
            for (int i = 0; i < n - 1; i++) { //percorre o array
                if (array[i] > array[i + 1]) { // verifica se o elemento atual é maior que o próximo
                    //é feita a troca, cado verdadeiro
                    int temp = array[i];
                    array[i] = array[i + 1];
                    array[i + 1] = temp;
                    swapped = true; //sinaliza que houve troca
                }
            } 
            n--; // decrementa n, pois o último elemento já está ordenado
        } while (swapped); // repete o loop enquanto tiver acontecido trocas na passagem anterior
    }
}
