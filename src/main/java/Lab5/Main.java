package Lab5;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String[] names = new String[32469];
        getNamesFromFile(names,"yob2017.txt");
        Scanner in = new Scanner(System.in);
        System.out.println("Enter name: ");
        displaySearchResults(in.next(), names);
    }

    public static void getNamesFromFile(String[] array, String filename){
        try {
            Scanner scanner = new Scanner(new File(filename));
            for(int i=0;i<array.length;i++)
                array[i]=scanner.nextLine().trim().split(",")[0];
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static int sequentialSearch(String[] array, String value){
        int index = -1;
        for(int i=0;i<array.length;i++)
            index = array[i].toLowerCase().equals(value.toLowerCase().trim())?i:index;
        return index;
    }

    public static void displaySearchResults(String input, String[] array){
        int line = sequentialSearch(array,input)+1;
        System.out.println(line!=0?input+" is located at line "+line:input+" is not in the file");
    }
}
