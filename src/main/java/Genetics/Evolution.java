package Genetics;

import Game.Tetris;

import java.util.Scanner;

public class Evolution {

    private double mutationRate, mutationSeverity;
    private int populationSize, championCount;

    private class Individual implements Comparable<Individual>{
        public double[] genome;
        public int score;
        public Tetris game;

        public Individual(double[] genome, int score, int delay, int x,int y) {
            this.genome = genome;
            this.score = score;
            game = new Tetris(true,delay,genome,x,y);
        }

        public boolean isDone(){
            return game.isGameDone();
        }

        @Override
        public int compareTo(Individual o) {
            return score-o.score;
        }
    }


    public Evolution(double mutationRate, double mutationSeverity, int populationSize, int championCount, int delay) {
        this.mutationRate = mutationRate;
        this.mutationSeverity = mutationSeverity;
        this.populationSize = populationSize;
        this.championCount = championCount;

        Individual[] population = new Individual[populationSize];
        for(int i=0;i<populationSize;i++){
            population[i]=new Individual(randomGenome(),0,delay,(i%10)*205,(i/10)*405);
        }
    }

    private double[] mutate(double[] gene){
        double change = (2*Math.random()+1)*mutationSeverity;
        gene[(int)(Math.random()*4)]*=change;
        normalize(gene);
        return gene;
    }

    public static double[] randomGenome(){
        int size=4;
        double[] gene = new double[size];
        for(int i=0;i<size;i++)
            gene[i]=-(Math.random());
        gene[1]=-gene[1];
        return gene;
    }

    public static void normalize(double[] arr){
        double max=0;
        boolean lessThanOne = true;
        for(double d:arr) {
            if(lessThanOne&&d>1)
                lessThanOne=false;
            if (Math.abs(d) > max)
                max = Math.abs(d);
        }
        if(lessThanOne)
            return;
            for(int i=0;i<arr.length;i++)
                arr[i]/=max;
    }

    public static void main(String[] args) {
        double mutationRate, mutationSeverity;
        int populationSize, championCount, delay;
        Scanner in = new Scanner(System.in);
        System.out.println("Enter population size: ");
        populationSize = in.nextInt();
        System.out.println("Enter mutation rate(chance of mutation, 0-1): ");
        mutationRate = in.nextDouble();
        System.out.println("Enter mutation severity(% change of mutation, 0-1): ");
        mutationSeverity = in.nextDouble();
        System.out.println("Enter amount of champions to choose: ");
        championCount = in.nextInt();
        System.out.println("Enter ms delay between movement (10 min): ");
        delay = in.nextInt();

        new Evolution(mutationRate,mutationSeverity,populationSize,championCount,delay);

    }
}
