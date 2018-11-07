package AI;

import java.util.Arrays;

public class Neuron {
    private double bias;
    private double[] weights;
    public double[] inputs;
    private Neuron[] inputNeurons;
    public int index = -1;
    private double outputValue;

    public Neuron(double[] weights, double bias, int index) throws NeuronException {
        for(double d: weights)
            if(d>1||d<0)
                throw new NeuronException(d>1?"Weight is too large":"Weight is too small"+": "+d);
        if(bias>1||bias<0)
            throw new NeuronException(bias>1?"Bias is too large":"Bias is too small");
        this.weights = weights;
        this.bias = bias;
        inputNeurons = null;
        this.index = index;
    }

    public void initOutput(){
        double total = 0;
        for(int i =0;i<inputs.length;i++)
            total+=inputs[i]*weights[i];
        total+=bias;
        System.out.println(total);
        outputValue = sigmoid(total);
    }

    public void getOutput(){

    }

    public double[] getWeights(){
        return weights;
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }

    public void setWeights(double[] weights) {
        this.weights = weights;
    }

    public static double sigmoid(double x){
        return 1./(1+Math.exp(-x));
    }

    @Override
    public String toString() {
        return bias + Arrays.toString(weights) + Arrays.toString(inputNeurons);
    }
}
