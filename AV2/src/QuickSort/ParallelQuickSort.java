package QuickSort;
class ParallelQuickSort {
    private int[] arr;
    private int low, high, maxThreads;

    public ParallelQuickSort(int[] arr, int low, int high, int maxThreads) {
        this.arr = arr;
        this.low = low;
        this.high = high;
        this.maxThreads = maxThreads;
    }

    public void quickSort() {
        if (maxThreads > 1) {
            quickSortParallel(arr, low, high, maxThreads);
        } else {
            QuickSort.quickSort(arr, low, high); // Usa a versão sequencial
        }
    }

    private void quickSortParallel(int[] arr, int low, int high, int threads) {
        if (low < high) {
            int pivotIndex = partition(arr, low, high);

            if (threads > 1) {
                Thread leftThread = new Thread(() -> quickSortParallel(arr, low, pivotIndex - 1, threads / 2));
                Thread rightThread = new Thread(() -> quickSortParallel(arr, pivotIndex + 1, high, threads / 2));

                leftThread.start();
                rightThread.start();

                try {
                    leftThread.join();
                    rightThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                QuickSort.quickSort(arr, low, pivotIndex - 1);
                QuickSort.quickSort(arr, pivotIndex + 1, high);
            }
        }
    }

    private int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }
}
