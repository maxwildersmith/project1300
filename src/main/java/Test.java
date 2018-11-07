import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Consumer;

public class Test{


    public static void main(String[] args) {
        int[] thing = randomArray(5);
        long[][] data = new long[3][15];
        System.out.println(Arrays.toString(thing));

        for(int i=0;i<data[0].length;i++){

        }
        timeMethod(thing, Test::bubbleSort, "Bubblesort");
        timeMethod(thing, Arrays::sort, "Arrays.sort");
        timeMethod(thing, Arrays::parallelSort, "Arrays.parallelSort");
    }

    public static void bubbleSort(int[] t){
        boolean swapped = true;
        while(swapped){
            swapped = false;
            for (int i = 0; i < t.length - 1; i++)
                if (t[i] > t[i + 1]) {
                    int temp = t[i + 1];
                    t[i + 1] = t[i];
                    t[i] = temp;
                    swapped = true;
                }
        }
    }

    public static int[] randomArray(int size){
        int[] a = new int[size];
        for(int i=0;i<a.length;i++)
            a[i]=i+1;
        mixArray(a);
        return a;
    }

    public static void mixArray(int[] a){
        ArrayList<Integer> l = new ArrayList<>();
        for(int x: a)
            l.add(x);
        Collections.shuffle(l);
        for(int i=0;i<a.length;i++)
            a[i] = l.get(i);
    }

    public static long timeMethod(int[] array, Consumer<int[]> function, String name){
        long time = System.nanoTime();
        function.accept(array);
        time = System.nanoTime()-time;
        System.out.printf("%s returned %s in %d ns%n", name, Arrays.toString(array), time);
        return time;
    }
}
