package lab2.main;

import lab2.util.AutomationFactory;
import lab2.util.variantNative.CodesAnalyser;
import lab2.util.variantNative.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;


public class Main {

    private static void fromFileByLine(Automation automation, String filename) throws FileNotFoundException {
        new BufferedReader(new FileReader(new File(filename)))
                .lines()
                .forEach(automation::sendLine);
        //                .forEach(s -> s.chars()
//                        .mapToObj(i -> (char) i)
//                        .forEachOrdered(automation::sendCharacter)
//                );
    }

    public static void main(String[] args) throws FileNotFoundException {
        String filename = "test_lab2.txt";
        Automation automation = AutomationFactory.createAutomation(Constants.myVariant);
        CodesAnalyser analyser = new CodesAnalyser();

        fromFileByLine(automation, filename);

        String result =
                automation.getLog().stream()
//                        .filter(log -> log.automationPosition != Code.Sign.NEW_LINE && log.automationPosition != Code.Sign.SPACE)
                        .map(analyser::interpret)
                        .reduce("", (s1, s2) -> s1 + "\n" + s2);

        System.out.println(result);
    }
}
