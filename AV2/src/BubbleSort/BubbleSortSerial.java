package BubbleSort;
public class BubbleSortSerial {
    public static void bubbleSort(int[] array) {
        int n = array.length;
        boolean swapped;
        
        do {
            swapped = false;
            for (int i = 0; i < n - 1; i++) {
                if (array[i] > array[i + 1]) {
                    int temp = array[i];
                    array[i] = array[i + 1];
                    array[i + 1] = temp;
                    swapped = true;
                }
            }
            n--; // Reduz o limite do Bubble Sort a cada iteração
        } while (swapped);
    }
}
