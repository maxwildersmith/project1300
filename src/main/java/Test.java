import AI.Neuron;
import AI.NeuronException;

public class Test {
    public static void main(String[] args) throws NeuronException {
        Neuron n = new Neuron(new double[] {0},0);
        System.out.println(n.getBias());
        test(n);
        System.out.println(n);
    }

    public static void test(Neuron n){
        n.setBias(.5);
    }
}
