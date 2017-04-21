package main;

import mpi.Comm;
import mpi.MPI;

import java.util.Random;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        MPI.Init(args);
        int size = MPI.COMM_WORLD.Size();
        Comm comm = MPI.COMM_WORLD;

        long startTime = System.currentTimeMillis();

        sort((int) 1e5);

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;

        System.out.println("ранг: " + comm.Rank() +
                " затраченное время: " + elapsedTime + " мс\n\n");

        MPI.Finalize();
    }

    private static void sort(int size) {
        final int maxValue = size / 10;
        int array[] = generateArray(size, maxValue);
        Quicksort quicksort = new Quicksort(array);

        quicksort.sort();
    }

    public static int[] generateArray(int size, int maxValue) {
        int[] numbers = new int[size];
        Random generator = new Random();

        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = generator.nextInt(maxValue);
        }

        return numbers;
    }
}
