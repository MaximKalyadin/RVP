package com.company;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    static int countColumn = 1000;
    static int[][] array = new int[countColumn][countColumn];

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        getArray();
        System.out.println("Not Thread");
        getAverageAboveArrayMainDiagonal();
        one_Tread();
        threadPoolExecutor();
        forkJoinPool();
    }

    public static int[][] getArray() {
        for(int i = 0; i < array.length; ++i) {
            for(int j = 0; j < array[i].length; ++j) {
                array[i][j] = (int)(Math.random() * 50);
               // System.out.print(array[i][j] + " ");
            }
           // System.out.println();
        };
       // System.out.println();
        return array;
    }

    public static float getAverageAboveArrayMainDiagonal() {
        float sum = 0;
        for (int i = 0; i < countColumn; i++) {
            for (int j = 0; j < countColumn; j++) {
                if (j > i) {
                    sum += array[i][j];
                }
            }
        }
        System.out.println( "average - " + sum / (countColumn * countColumn));
        return sum / (countColumn * countColumn);
    }

    public static float sumForLine(int array_row) {
        float sum = 0;

        for(int j = 0; j < countColumn; j++) {
            if (j > array_row) {
                sum += array[array_row][j];
            }
        }

        return sum;
    }

    public static void one_Tread() throws InterruptedException {
        System.out.println();
        System.out.println("one_Tread");
        long start_time = System.currentTimeMillis();

        Thread thread = new Thread(String.valueOf(getAverageAboveArrayMainDiagonal()));
        thread.start();
        thread.join();

        long end_time = System.currentTimeMillis();
        System.out.println("time - " + (end_time - start_time));
    }

    public static void threadPoolExecutor() {
        System.out.println();
        float sum = 0;

        ExecutorService executorService = Executors.newCachedThreadPool();
        long start_time = System.currentTimeMillis();
        List<Future<Float>> results = new ArrayList();

        for (int i = 0; i < countColumn; i++) {
            int finalI = i;
            results.add(CompletableFuture.supplyAsync(() -> { return sumForLine(finalI); }, executorService));
        }

        for (Future<Float> task: results) {
            try {
                sum += task.get();
            } catch (InterruptedException | ExecutionException ignored) { }
        }
        float average = sum / (countColumn * countColumn);
        long end_time = System.currentTimeMillis();

        System.out.println("threadPoolExecutor");
        System.out.println("average - " + average);
        System.out.println("time - " + (end_time - start_time));

        executorService.shutdown();

    }

    public static void forkJoinPool() throws InterruptedException, ExecutionException {
        System.out.println();
        float sum = 0;

        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        long start_time = System.currentTimeMillis();

        RecursiveTask<Integer> arrayTask = new RecursiveArray(array, countColumn, countColumn, 0);
        commonPool.execute(arrayTask);
        try {
            sum += arrayTask.get();
        } catch (InterruptedException | ExecutionException ignored) { }

        float average = sum / (countColumn * countColumn);
        long end_time = System.currentTimeMillis();

        System.out.println("forkJoinPool");
        System.out.println("average - " + average);
        System.out.println("time - " + (end_time - start_time));

        commonPool.shutdown();
        commonPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

    }

}

class RecursiveArray extends RecursiveTask<Integer> {
    private int[][] array;
    private int countColumn;
    private int countRow;
    private int index;

    public RecursiveArray(int[][] array, int countRow, int countColumn, int index) {
        this.array = array;
        this.countColumn = countColumn;
        this.countRow = countRow;
        this.index = index;
    }

    @Override
    protected Integer compute() {
        if (array.length > 1){
            return ForkJoinTask.invokeAll(createSubTask()).stream().mapToInt(ForkJoinTask::join).sum();
        } else {
            return processing();
        }
    }

    private Collection<RecursiveArray> createSubTask() {
        List<RecursiveArray> recArray = new ArrayList<>();
        recArray.add(new RecursiveArray(Arrays.copyOfRange(array, 0, array.length / 2), countRow / 2, countColumn, index));
        recArray.add(new RecursiveArray(Arrays.copyOfRange(array, array.length / 2, array.length), countRow - countRow / 2, countColumn, index + countRow / 2));
        return recArray;
    }

    private Integer processing() {
        int result = 0;
        for(int j = 0; j < countColumn; j++) {
            if (j > index) {
                result += array[0][j];
            }
        }
        return result;
    }
}