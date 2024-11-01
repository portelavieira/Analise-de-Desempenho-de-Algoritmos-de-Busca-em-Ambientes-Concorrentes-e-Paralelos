package QuickSort;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;

public class QuickSortParallel extends RecursiveAction {
    private int[] array;
    private int low;
    private int high;

    public QuickSortParallel(int[] array, int low, int high) {
        this.array = array;
        this.low = low;
        this.high = high;
    }

    @Override
    protected void compute() {
        if (low < high) {
            int pivotIndex = partition(array, low, high);
            QuickSortParallel leftTask = new QuickSortParallel(array, low, pivotIndex - 1);
            QuickSortParallel rightTask = new QuickSortParallel(array, pivotIndex + 1, high);
            invokeAll(leftTask, rightTask);
        }
    }

    private int partition(int[] array, int low, int high) {
        int pivot = array[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (array[j] <= pivot) {
                i++;
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
        int temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;
        return i + 1;
    }

    public static void sort(int[] array, int numThreads) {
        ForkJoinPool pool = new ForkJoinPool(numThreads);
        pool.invoke(new QuickSortParallel(array, 0, array.length - 1));
    }
}
