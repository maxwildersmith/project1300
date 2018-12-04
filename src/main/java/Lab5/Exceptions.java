package Lab5;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Exceptions {
    public static void main(String[] args) {
        try{
            Scanner inputFile = new Scanner(new File("numbers.txt"));
            /**
             * in the format of "1, 3, 4, ..."
             */
            String data = "";
            while(inputFile.hasNextLine())
                data+=inputFile.nextLine();
            ArrayList<Integer> values = new ArrayList<>();
            for(String s: data.trim().split(", "))
                values.add(Integer.parseInt(s));
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (InputMismatchException e){
            System.out.println("Not all values are ints!");
        }
    }
}