import AI.Network;

public class Main {
    public static void main(String[] args) {
        Network net = new Network();
        net.init();
        System.out.println(net.getGenome());
    }
}
