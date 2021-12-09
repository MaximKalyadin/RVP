package rvib.lab4.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.lang.IgniteCallable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

@SpringBootApplication
public class IgniteHelloWorld implements CommandLineRunner {
    @Autowired
    private Ignite ignite;

    public static void main(String[] args) {
        SpringApplication.run(IgniteHelloWorld.class, args);
    }

    IgniteCallable<String> sort(int[][] matrix) {
        return new IgniteCallable<String>() {
            @Override
            public String call() throws Exception {
                for (int i = 0; i < matrix.length - 1; i++) {
                    for (int k = 0; k < matrix.length - 1; k++) {
                        if (matrix[k + 1][0] > matrix[k][0]) {
                            for (int j = 0; j < matrix[i].length; j++) {
                                int b = matrix[k][j];
                                matrix[k][j] = matrix[k + 1][j];
                                matrix[k + 1][j] = b;
                            }
                        }
                    }
                }
                String result = "";
                for (int i = 0; i < matrix.length; i++) {
                    for (int j = 0; j < matrix[i].length; j++) {
                        result += matrix[i][j] + " | ";
                    }
                    result += "\n";
                }
                return result;
            }
        };
    }

    @Override
    public void run(String... args) {
        try {
            Collection<IgniteCallable<String>> calls = new ArrayList<>();
            int n = 5;
            int[][] matrix = new int[n][n];
            Random r = new Random();
            System.out.println("Source array");
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    matrix[i][j] = r.nextInt(100);
                    System.out.print(matrix[i][j] + " | ");
                }
                System.out.println();
            }
            for (int i = 0; i < n; i++) {
                calls.add(sort(matrix));
            }

            Collection<String> res = ignite.compute().call(calls);
            System.out.println("Sorted array \n" + res.iterator().next());
            System.in.read();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
