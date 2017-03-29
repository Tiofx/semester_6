package test;

import mpi.Comm;
import mpi.MPI;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        MPI.Init(args);

        int size = MPI.COMM_WORLD.Size();
        Comm comm = MPI.COMM_WORLD;

        System.out.println("arraySize: " + size + " rank: " + comm.Rank());

        MPI.Finalize();
    }
}
