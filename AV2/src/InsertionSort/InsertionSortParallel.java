package InsertionSort;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;

public class InsertionSortParallel extends RecursiveAction {
    private static final int THRESHOLD = 1000; // Limiar para decidir quando usar o paralelismo
    private int[] array;
    private int start, end;

    public InsertionSortParallel(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected void compute() {
        if (end - start <= THRESHOLD) {
            insertionSort(array, start, end);
        } else {
            int mid = (start + end) / 2;
            InsertionSortParallel leftTask = new InsertionSortParallel(array, start, mid);
            InsertionSortParallel rightTask = new InsertionSortParallel(array, mid + 1, end);

            invokeAll(leftTask, rightTask);
            merge(array, start, mid, end);
        }
    }

    private void insertionSort(int[] array, int start, int end) {
        for (int i = start + 1; i <= end; i++) {
            int key = array[i];
            int j = i - 1;

            while (j >= start && array[j] > key) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = key;
        }
    }

    private void merge(int[] array, int start, int mid, int end) {
        int[] temp = new int[end - start + 1];
        int i = start, j = mid + 1, k = 0;

        while (i <= mid && j <= end) {
            if (array[i] <= array[j]) {
                temp[k++] = array[i++];
            } else {
                temp[k++] = array[j++];
            }
        }

        while (i <= mid) temp[k++] = array[i++];
        while (j <= end) temp[k++] = array[j++];

        System.arraycopy(temp, 0, array, start, temp.length);
    }

    public static void parallelSort(int[] array, int threads) {
        ForkJoinPool pool = new ForkJoinPool(threads);
        pool.invoke(new InsertionSortParallel(array, 0, array.length - 1));
        pool.shutdown();
    }
}
