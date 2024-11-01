package MergeSort;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;

public class MergeSortParallel extends RecursiveAction {
    private int[] array;
    private int left;
    private int right;

    public MergeSortParallel(int[] array, int left, int right) {
        this.array = array;
        this.left = left;
        this.right = right;
    }

    @Override
    protected void compute() {
        if (left < right) {
            int mid = (left + right) / 2;
            MergeSortParallel leftTask = new MergeSortParallel(array, left, mid);
            MergeSortParallel rightTask = new MergeSortParallel(array, mid + 1, right);
            invokeAll(leftTask, rightTask);
            merge(array, left, mid, right);
        }
    }

    private void merge(int[] array, int left, int mid, int right) {
        int[] temp = new int[right - left + 1];
        int i = left, j = mid + 1, k = 0;

        while (i <= mid && j <= right) {
            temp[k++] = (array[i] <= array[j]) ? array[i++] : array[j++];
        }
        while (i <= mid) temp[k++] = array[i++];
        while (j <= right) temp[k++] = array[j++];

        System.arraycopy(temp, 0, array, left, temp.length);
    }

    public static void sort(int[] array, int numThreads) {
        ForkJoinPool pool = new ForkJoinPool(numThreads);
        pool.invoke(new MergeSortParallel(array, 0, array.length - 1));
    }
}
