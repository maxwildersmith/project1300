import AI.Network;
import AI.Neuron;
import AI.NeuronException;

public class Main {
    public static void main(String[] args) throws NeuronException {
        Neuron n1 = new Neuron(5,1);
        Neuron n2 = new Neuron(2,2);
        Neuron o = new Neuron(new Neuron[]{n1,n2}, new double[]{.0,.00005},.0005,3);
        System.out.println(o.getOutput());
    }
}
