package Genetics;

import Game.Tetris;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.util.*;

public class Evolution implements ActionListener {

    private double mutationRate, mutationSeverity, crossoverRate;
    private int populationSize, championCount, generation;
    Individual[] population;
    private PrintWriter output;
    private char seed;
    private long universalSeed=-1;

    @Override
    public void actionPerformed(ActionEvent e) {
        if(allDone())
            endEpoch();
    }

    private boolean allDone(){
        for(Individual i:population)
            if(!i.isDone())
                return false;
        return true;
    }

    public void endEpoch() {
        Arrays.sort(population);
        System.out.println("Results of generation " + generation + ":\n" + Arrays.toString(population));
        if(output!=null) {
            Date d = new Date();
            output.printf("\n\n%d/%d/%d\t%d:%d:%d\n", d.getMonth(), d.getDate(), d.getYear(), d.getHours(), d.getMinutes(), d.getSeconds());
            output.printf("Generation %d, highest score: %d, genome: %s\nOther individuals:\n", generation, population[0].score, Arrays.toString(population[0].genome));
            for (int i = 1; i < population.length; i++)
                output.println(population[i]);
        }
        System.out.println("Best score: " + population[0].score + " with genome: " + Arrays.toString(population[0].genome) + "\nProceed? [y/n]: ");
        Scanner in = new Scanner(System.in);
        if (in.next().trim().toLowerCase().charAt(0) == 'n') {
            System.out.println("Goodbye");
            if(output!=null)
                output.close();
            System.exit(0);
        }
        Individual[] champions = new Individual[championCount];
        for (int i = 0; i < championCount; i++)
            champions[i] = population[i];

        double[][] genomes = new double[populationSize][4];
        int genesPerChamp = genomes.length / (championCount - 1);
        for (int champ = 0; champ < championCount - 1; champ++)
            for (int i = 0; i < genesPerChamp; i++)
                genomes[i + (champ * genesPerChamp)] = crossover(population[champ], population[champ + 1]);
        for (int i = 0; i < populationSize * mutationRate; i++){
            //mutates random genes
            int genome = (int) (Math.random() * genomes.length);
            genomes[genome] = mutate(genomes[genome]);
        }

        genomes[genomes.length-1]=population[0].genome;//to allow the top champion to compete again w/o modification

//        for(double[] gene: genomes){
//            for(double d:gene)
//                System.out.print(d+", ");
//            System.out.println();
//        }
        generation++;
        long seedValue= new Random().nextLong();
        for(int i=0;i<population.length;i++)
            if(seed=='y') {
                population[i].restart(genomes[i],seedValue);
            } else if(seed=='a') {
                if (universalSeed < 0)
                    universalSeed = new Random().nextLong();
                population[i].restart(genomes[i], universalSeed);
            } else
                population[i].restart(genomes[i],new Random().nextLong());
    }

    private class Individual implements Comparable<Individual>, ActionListener{
        public double[] genome;
        public int score;
        public Tetris game;
        ActionListener parent;

        public Individual(double[] genome, int score, int delay, int x,int y,ActionListener parent, long seed) {
            this.genome = genome;
            this.score = score;
            game = new Tetris(true,delay,genome,x,y,this, seed);
            this.parent=parent;
        }

        public void restart(double[] genome, long seed){
            game.restart(genome, seed);
        }

        public boolean isDone(){
            return game.isGameDone();
        }

        @Override
        public int compareTo(Individual o) {
            return o.score-score;
        }

        @Override
        public String toString(){
            return "Score: "+score+"\tGenome:"+Arrays.toString(genome);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            score=game.getScore();
            parent.actionPerformed(e);
        }
    }


    public Evolution(double mutationRate, double mutationSeverity, int populationSize, int championCount, int delay, boolean save, char seed) {
        this.mutationRate = mutationRate;
        this.mutationSeverity = mutationSeverity;
        this.populationSize = populationSize;
        this.championCount = championCount;
        this.seed = seed;

        if(save){
            try {
                output = new PrintWriter(new FileWriter("record.txt"));
                Date d = new Date();
                output.printf("Tetris AI: %d/%d/%d\t%d:%d:%d\n\n",d.getMonth(),d.getDate(),d.getYear(),d.getHours(),d.getMinutes(),d.getSeconds());
                output.printf("Conditions:\nMutation rate: %f\nMutation severity: %f\nPopulation size: %d\nChampion count: %d\n\n\n",mutationRate,mutationSeverity,populationSize,championCount);
            } catch (IOException e) {
                System.err.println("Can't print to file, writing to console instead");
                output = new PrintWriter(System.out);
            }

        }

        population = new Individual[populationSize];

        int maxAcross = Toolkit.getDefaultToolkit().getScreenSize().width/200;
        long seedValue= new Random().nextLong();
        for(int i=0;i<populationSize;i++){
            if(seed=='y') {
                population[i] = new Individual(randomGenome(), 0, delay, (i % maxAcross) * 200, (i / maxAcross) * 400, this,seedValue);
            } else if(seed=='a') {
                if (universalSeed < 0)
                    universalSeed = new Random().nextLong();
                population[i] = new Individual(randomGenome(), 0, delay, (i % maxAcross) * 200, (i / maxAcross) * 400, this, universalSeed);
            } else
                population[i] = new Individual(randomGenome(), 0, delay, (i % maxAcross) * 200, (i / maxAcross) * 400, this,new Random().nextLong());


        }

    }

    private double[] mutate(double[] gene){
        if(Math.random()*populationSize<populationSize*mutationRate)
            return randomGenome();
        for(int i=0;i<1+(int)(Math.random()*10*mutationRate);i++) {
            double change = (2 * Math.random() + 1) * mutationSeverity;
            gene[(int) (Math.random() * 4)] *= change;
        }
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

    /**
     * makes a gene's values out of 1 at most, so that all values are relative, does nothing if all values less than 1
     * @param arr
     */
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

    /**
     * Will perform a random amount of times, an operation to find a middle ground between champion genes
     * @param parent1 greater fitness score
     * @param parent2 lesser fitness score
     * @return genome with a random gene merged from the parents with a favoring towards the better parents
     */
    private double[] crossover(Individual parent1, Individual parent2){
        double[] genes = parent1.genome;
        for(int i=0;i<1+(int)(Math.random()*3);i++) {
            int gene = (int) (Math.random() * 4);
            genes[gene]=((parent1.score*parent1.genome[gene])+(parent2.score*parent2.genome[gene])+1)/(1+parent1.score+parent2.score);
        }
        System.out.println("\n");
        normalize(genes);
        return genes;
    }

    public static void main(String[] args) {
        double mutationRate, mutationSeverity;
        int populationSize, championCount, delay;
        Scanner in = new Scanner(System.in);
        System.out.println("Record output? [y/n]: ");
        boolean record = (in.next().trim().toLowerCase().charAt(0) == 'y');
        int maxDisplay = (Toolkit.getDefaultToolkit().getScreenSize().width/200)*(Toolkit.getDefaultToolkit().getScreenSize().height/400);
        System.out.println("Enter population size(can show "+maxDisplay+" at most): ");
        populationSize = in.nextInt();
        System.out.println("Enter mutation rate(chance of mutation, 0-1): ");
        mutationRate = in.nextDouble();
        if(mutationRate>1||mutationRate<0){
            System.out.println("Enter mutation rate between 0 and 1(chance of mutation, 0-1): ");
            mutationRate = in.nextDouble();
        }
        System.out.println("Enter mutation severity(% change of mutation, 0-1): ");
        mutationSeverity = in.nextDouble();
        System.out.println("Enter amount of champions to choose: ");
        championCount = in.nextInt();
        System.out.println("Enter ms delay between movement(200 minimum): ");
        delay = in.nextInt();
        System.out.println("Same seed?(Should all ind. play the same game[y], should all generations play the same game?[a]: ");
        char seed = in.next().trim().toLowerCase().charAt(0);

        new Evolution(mutationRate,mutationSeverity,populationSize,championCount,delay,record,seed);

    }
}
