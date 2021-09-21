package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int countLines = 100;
        int countColumns = 1000;
        long startTime = System.currentTimeMillis();
        int [][] array = generateArray(countLines,countColumns);
        float average = findAverage(array,countLines,countColumns);
        System.out.println("Среднее арифмитическое элементов матрицы: "+average);
        oneThread(array, countLines, countColumns);
        threadPoolExecutor(array, countLines, countColumns);
        forkJoinPool(array, countLines, countColumns);
    }

    public static int[][] generateArray(int countLines, int countColumns){
        int[][] array = new int[countLines][countColumns];
//System.out.println("Исходный массив:");
        for (int i=0; i<array.length; i++){
            for(int j=0; j<array[i].length;j++){
                array[i][j] = (int)(Math.random() * 50);
// System.out.printf("%4d",array[i][j]);
            }
            System.out.println();
        }
        return array;
    }

    public static float findAverage(int[][] array, float countLines, float countColumns){
        float sum = 0;
        float average;
        for (int i=0; i<array.length; i++){
            for(int j=0; j<array[i].length;j++){
                sum+=array[i][j];
            }
        }
        average = sum/(countLines*countColumns);
        return average;
    }

    public static void oneThread (int [][] array, float countLines,float countColumns) throws InterruptedException{
        long start = System.currentTimeMillis();
        Thread thread = new Thread(String.valueOf(findAverage(array, countLines, countColumns)));
        thread.start();
        thread.join();
        long stop = System.currentTimeMillis();
        System.out.println("One thread result time: "+(stop-start));
    }

    public static float findSumSub(int [][] array, int line){
        float subsum =0;
        for (int j=0; j<array[line].length; j++){
            subsum+=array[line][j];
        }
        return subsum;
    }

    public static int findSumString(int [][] array, int line){
        int subsum =0;
        for (int j=0; j<array[line].length; j++){
            subsum+=array[line][j];
        }
        return subsum;
    }


    public static void threadPoolExecutor (int [][] array, float countLines,float countColumns) throws ExecutionException, InterruptedException {
        float sum=0;
        float value = 0;
        ExecutorService executorService = Executors.newCachedThreadPool();

        long start = System.currentTimeMillis();
        List<Future<Float>> futures = new ArrayList<>();

        for (int i = 0; i < countLines; i++) {
            final int line = i;
            futures.add(CompletableFuture.supplyAsync(() -> findSumSub(array,line), executorService));
        }
        for (Future<Float> future : futures) {
            sum += future.get();
        }

        value = sum/(countLines*countColumns);

        long stop = System.currentTimeMillis();
        System.out.println("ThreadPoolExecutor result time: "+ (stop-start));
        executorService.shutdown();
    }

    public static void forkJoinPool(int [][] array, float countLines,float countColumns) throws InterruptedException, ExecutionException {
        float sum=0;
        float value = 0;
        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        long start = System.currentTimeMillis();
        List<Future<Float>> futures = new ArrayList<>();

        for (int i = 0; i < countLines; i++) {
            final int line = i;
            futures.add(CompletableFuture.supplyAsync(() -> findSumSub(array,line), commonPool));
        }
        for (Future<Float> future : futures) {
            sum += future.get();
        }

        value = sum/(countLines*countColumns);

        commonPool.shutdown();
        commonPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MICROSECONDS);
        long stop = System.currentTimeMillis();
        System.out.println("ForkJoinPool result time: "+ (stop-start));
    }
}
