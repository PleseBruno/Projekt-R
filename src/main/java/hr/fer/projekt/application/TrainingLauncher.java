package hr.fer.projekt.application;

import hr.fer.projekt.genetskiAlgoritam.FitnessChecker;
import hr.fer.projekt.genetskiAlgoritam.GeneticAlgorithms;
import hr.fer.projekt.genetskiAlgoritam.GeneticType;
import hr.fer.projekt.neuronskaMreza.NeuralNetwork;
import hr.fer.projekt.temp.DataSet;

import java.util.*;

public class TrainingLauncher {
    private final static int NUM_TESTS = 30;
    private final static int NUM_NEURALNETWORKS = 50;
    private final static int NUM_GENS = 500;
    private static Random rand = new Random();

    public static void main(String[] args) {
        int inputNodes = 10;
        int[] hiddenLayers = {6, 8, 4};
        int outputNodes = 4;
        Map<Set<Integer>, Set<Integer>> dataSetMap = DataSet.getDataSetMap();

        List<Set<Integer>> inputsSet = dataSetMap.keySet().stream().toList();
        List<double[]> inputs = new ArrayList<double[]>();
        List<double[]> outputs = new ArrayList<double[]>();

        Map<NeuralNetwork, Double> Generacija = new HashMap<NeuralNetwork, Double>();

        //Zasad je manualno zadana velicina populacije na 5, ali cemo to mijenjati s visedretvenosti
        for (int i = 0; i < NUM_NEURALNETWORKS; i++) {
            Generacija.put(new NeuralNetwork("NN-1." + i+1, inputNodes, hiddenLayers, outputNodes), null);
        }

        for (int j = 0; j < NUM_GENS; j++) {
            System.out.println("Generation: " + j);

            for (NeuralNetwork n : Generacija.keySet()) {
                //pretvaranje inputa u pravi format (Set{1, 2, 7) == Array[0, 1, 1, 0, 0, 0, 0, 1, 0, 0])
//                for (int i = 0; i < NUM_TESTS; i++) {
//                    Set<Integer> set = inputsSet.get(rand.nextInt(0, inputsSet.size()));
//
//                    double[] arr = new double[10];
//
//                    for (int v : set) {
//                        arr[v] = 1.0;
//                    }
//
//                    inputs.add(arr);
//                }

                for (int i = 0; i < inputsSet.size(); i++) {
                    Set<Integer> set = inputsSet.get(i);

                    double[] arr = new double[10];

                    for (int v : set) {
                        arr[v] = 1.0;
                    }

                    inputs.add(arr);
                }
                

                //n.print();

                //System.out.println("Matrix: " + n.getID());
                double totalFitness = 0;

                for (double[] arr : inputs) {
                    double[] output = n.generateOutput(arr);
                    outputs.add(output);

                    //racunanje fitnesa pomocu fitnessCheckera
                    double fitness = FitnessChecker.rateDataSet(arr, output);
                    totalFitness += fitness;

                    //System.out.println("Output:");
                    //for (double val : output) {
                    //    System.out.printf("%.4f ", val);
                    //}

                    //System.out.println("\n For output Matrix has fitness: " + fitness);
                }
                inputs.clear();
                totalFitness /= NUM_TESTS;
                Generacija.merge(n, totalFitness, (ov, nv) -> nv);
                //System.out.println("Total fitness: " + totalFitness);
            }

            //System.out.println("Fitness Checker Results:");
            //for (NeuralNetwork n : Generacija.keySet()) {
            //    System.out.println(n.getID() + " has total fitness of:" + Generacija.get(n));
            //}

            Generacija = GeneticAlgorithms.makeNewGen(Generacija, GeneticType.DEFAULT, j);
        }
    }


    /*

    1. Korak - Generiranje Neuralnih mreza:
                    >> 50 nasumicnih neuralnih mreza
                    >> svaka prima 10 istih nasumicnih inputa iz dataSeta

    2. Korak - odigravanje neuralne mreze:
                    >> provrti se svaka da se dobije ispisni vektor

    3. Korak - Ocijenjivanje Fitnesa:
                    >> zasad FitnessChecker na temelju inputa i outputa ocjenjuje neural network
                            *kasnije na temelju igranja igrice

    ---- do ovdje je napravljeno ----

    4. Korak - pravljenje sljedece generacije na temelju stare:
                    >> genetski algoritam koji ce pomijesati matrice na neki nacin koji smo definirali

    5. Korak - vrati se na 2. korak

    6. Korak - paralelizirati cijeli postupak

     */
}
