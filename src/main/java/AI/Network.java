package AI;

public class Network {
    public static final int INPUTS = 220, OUTPUTS = 5, HIDDEN1 = 80, HIDDEN2 = 20;
    private Neuron[] inputs, hidden1, hidden2, output;
    protected enum Commands {left, right, rotateLeft, rotateRight, drop};

    public Network(){
        inputs = new Neuron[INPUTS];
        hidden1 = new Neuron[HIDDEN1];
        hidden2 = new Neuron[HIDDEN2];
        output = new Neuron[OUTPUTS];
    }

    public Network(String genome){
        inputs = new Neuron[INPUTS];
        hidden1 = new Neuron[HIDDEN1];
        hidden2 = new Neuron[HIDDEN2];
        output = new Neuron[OUTPUTS];
    }

    public Network(int INPUTS, int HIDDEN1, int HIDDEN2, int OUTPUTS){
        inputs = new Neuron[INPUTS];
        hidden1 = new Neuron[HIDDEN1];
        hidden2 = new Neuron[HIDDEN2];
        output = new Neuron[OUTPUTS];
    }

    public void initLayer(Neuron[] layer) throws NeuronException {

        for(int i = 0; i < layer.length; i++)
            layer[i] = new Neuron(new double[]{Math.random()},Math.random(),i);
    }
    public void initLayer(Neuron[] layer, Neuron[] previousLayer) throws NeuronException {
        double[] weights = new double[previousLayer.length];
        for(int i=0;i<weights.length;i++)
            weights[i] = Math.random();

        for(int i = 0; i < layer.length; i++)
            layer[i] = new Neuron(weights,Math.random(),i);
    }

    public void init() {
        try{
            initLayer(inputs);
        } catch (NeuronException e){
            System.err.println(e.getMessage());
        }
    }

    public String getGenome(){
        String geneome = "";
        for(Neuron n:inputs)
            geneome+=n.getWeights()+";"+n.getBias()+"";
        return getGenome();
    }
}
