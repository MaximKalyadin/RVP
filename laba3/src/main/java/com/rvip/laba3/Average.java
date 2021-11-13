package com.rvip.laba3;


import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
public class Average {

    @Async
    public Future<Float> average(int[][] mass) {
        float sum = 0;
        for (int i = 0; i < mass.length; i++) {
            for (int j = 0; j < mass.length; j++) {
                if (j > i) {
                    sum += mass[i][j];
                }
            }
        }
        float result = sum / ((mass.length * mass.length - mass.length) / 2);
        return new AsyncResult<Float>(result);
    }
}
