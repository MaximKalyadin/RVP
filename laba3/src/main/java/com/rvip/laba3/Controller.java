package com.rvip.laba3;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Autowired
    Massive massive;

    @Autowired
    Average average;

    int size = 5;

    @GetMapping("algorithm")
    public String algorithm() {
        try {
            int[][] mass = massive.getMassive(size).get();
            long start_time = System.currentTimeMillis();
            float result = average.average(mass).get();
            long stop_timer = System.currentTimeMillis();
            long time = stop_timer - start_time;

            for (int i = 0; i < mass.length; i++) {
                for (int j = 0; j < mass.length; j++) {
                    System.out.print(mass[i][j] + " ");
                }
                System.out.println();
            }

            String string_result = "Time = " + time + ", Result = " + result;
            return string_result;

        } catch (Exception ex) {
            return ex.getMessage();
        }
    }
}
