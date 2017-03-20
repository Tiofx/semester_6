package lab3.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        String filename = "test_lab_3.txt";

        int i = 0;

        System.out.println(i++);
        System.out.println(i);

        Resolver resolver = new Resolver();

        List<Integer> codes = new BufferedReader(new FileReader(new File(filename)))
                .lines()
                .flatMap(s -> Arrays.stream(s.split("[ \n\t]")))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        resolver.setCodes(codes);
        resolver.check();
    }
}
