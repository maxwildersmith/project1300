package AI;

import java.util.Arrays;

public class Neuron {
    private double bias;
    private double[] weights;
    private Neuron[] outputs;

    public Neuron(double[] weights, double bias) throws NeuronException {
        for(double d: weights)
            if(d>1||d<0)
                throw new NeuronException(d>1?"Weight is too large":"Weight is too small");
        if(bias>1||bias<0)
            throw new NeuronException(bias>1?"Bias is too large":"Bias is too small");
        this.weights = weights;
        this.bias = bias;
        outputs = null;
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

    public Neuron[] getOutputs() {
        return outputs;
    }

    public void setOutputs(Neuron[] outputs) {
        this.outputs = outputs;
    }

    @Override
    public String toString() {
        return bias + Arrays.toString(weights) + Arrays.toString(outputs);
    }
}
