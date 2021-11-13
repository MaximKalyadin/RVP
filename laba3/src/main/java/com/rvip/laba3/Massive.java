package com.rvip.laba3;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import java.util.Random;
import java.util.concurrent.Future;

@Service
public class Massive {

    @Async
    public Future<int[][]> getMassive(int size) {
        Random rnd = new Random();
        int [][] mass = new int[size][size];
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
                mass[i][j] = rnd.nextInt(99) + 1;
        }
        return new AsyncResult<int[][]>(mass);
    }
}
