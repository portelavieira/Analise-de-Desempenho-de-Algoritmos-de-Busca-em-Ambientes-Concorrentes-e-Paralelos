package MergeSort;
public class ParallelMergeSort {
    private int[] arr;
    private int[] temp;
    private int numThreads;

    public ParallelMergeSort(int[] arr, int numThreads) {
        this.arr = arr;
        this.temp = new int[arr.length];
        this.numThreads = numThreads;
    }

    public void mergeSort() {
        parallelMergeSort(0, arr.length - 1, numThreads);
    }

    private void parallelMergeSort(int left, int right, int availableThreads) {
        if (left >= right) return;

        if (availableThreads <= 1) {
            // Reverte para a versão sequencial se não houver threads disponíveis
            mergeSortSequential(arr, temp, left, right);
        } else {
            int mid = (left + right) / 2;
            Thread leftThread = new Thread(() -> parallelMergeSort(left, mid, availableThreads / 2));
            Thread rightThread = new Thread(() -> parallelMergeSort(mid + 1, right, availableThreads / 2));

            leftThread.start();
            rightThread.start();

            try {
                leftThread.join();
                rightThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            merge(arr, temp, left, mid, right);
        }
    }

    private void mergeSortSequential(int[] arr, int[] temp, int left, int right) {
        if (left >= right) return;

        int mid = (left + right) / 2;
        mergeSortSequential(arr, temp, left, mid);
        mergeSortSequential(arr, temp, mid + 1, right);
        merge(arr, temp, left, mid, right);
    }

    private void merge(int[] arr, int[] temp, int left, int mid, int right) {
        System.arraycopy(arr, left, temp, left, right - left + 1);

        int i = left, j = mid + 1, k = left;
        while (i <= mid && j <= right) {
            if (temp[i] <= temp[j]) {
                arr[k++] = temp[i++];
            } else {
                arr[k++] = temp[j++];
            }
        }

        while (i <= mid) arr[k++] = temp[i++];
    }
}
