package lab2.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class LoadText {

    public String loadFromFile(File file, String reduceString) {
        try {
            return new BufferedReader(new FileReader(file))
                    .lines()
                    .reduce("", (s1, s2) -> s1 + reduceString + s2)
                    .trim();

        } catch (FileNotFoundException e) {
            return e.getMessage();
        }
    }

    public String loadFromFile(File file) {
        return loadFromFile(file, "\n");
    }
}
