package rvib.lab4.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.lang.IgniteCallable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.OptionalInt;
import java.util.Random;

@SpringBootApplication
public class IgniteAlgorithm implements CommandLineRunner {

    @Autowired
    private Ignite ignite;
    long time;

    public static void main(String[] args) {
        SpringApplication.run(IgniteAlgorithm.class, args);
    }

    IgniteCallable<Integer> average(int[][] mass, int startIndex, int stopIndex) {
        return new IgniteCallable<Integer>() {
            @Override
            public Integer call() throws Exception {
                int sum = 0;
                for (int i = startIndex; i < stopIndex; i++) {
                    for (int j = startIndex; j < mass.length; j++) {
                        if (j > i) {
                            sum += mass[i][j];
                        }
                    }
                }
                return sum;
            }
        };
    }


    @Override
    public void run(String... args) {
        long start = System.currentTimeMillis();
        try {
            Collection<IgniteCallable<Integer>> calls = new ArrayList<>();

            int n = 5;
            int[][] mass = new int[n][n];
            Random rnd = new Random();
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    mass[i][j] = rnd.nextInt(99) + 1;
                    System.out.print(mass[i][j] + " ");
                }
                System.out.println();
            }

            int len = mass.length / 5;
            for (int i = 0; i < mass.length; i += len) {
                calls.add(average(mass, i, i + len));
            }



            Collection<Integer> result = ignite.compute().call(calls);
            int total = result.stream().mapToInt(Integer::intValue).sum();
            float res = total / ((mass.length * mass.length - mass.length) / 2);
            long stop = System.currentTimeMillis();
            time=stop-start;
            System.out.println("Result = " + res + " Time = " + time);
            System.in.read();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}

