package hr.fer.projekt.neuronskaMreza;


import hr.fer.projekt.matematika.Matrix;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork {
    private final int inputNodes;
    private final int outputNodes;
    private final int[] hiddenLayers; // npr. [8, 6, 4]
    private List<Matrix> weights; // lista matrica tezina
    private List<Matrix> biases;  // lista w0 tezina

    // Konstruktor za kreiranje nove neuronske mreze sa zadanim brojem ulaznih, skrivenih i izlaznih cvorova i nasumicnim inicijaliziranjem tezina
    public NeuralNetwork(int inputNodes, int[] hiddenLayers, int outputNodes) {
        this.inputNodes = inputNodes;
        this.outputNodes = outputNodes;
        this.hiddenLayers = hiddenLayers.clone();

        weights = new ArrayList<>();
        biases = new ArrayList<>();

        // Broj slojeva = input + hidden + output
        int prevNodes = inputNodes;
        for (int hiddenNodes : hiddenLayers) {
            weights.add(new Matrix(hiddenNodes, prevNodes));
            biases.add(new Matrix(hiddenNodes, 1));
            prevNodes = hiddenNodes;
        }

        // Zadnji sloj (output)
        weights.add(new Matrix(outputNodes, prevNodes));
        biases.add(new Matrix(outputNodes, 1));

        // Inicijalizacija nasumično
        randomizeParameters();
    }

    // Konstruktor za harcodanje cijele neuronske mreze procitane u txt fileu
    public NeuralNetwork(int inputNodes, int[] hiddenLayers, int outputNodes, List<Matrix> weights, List<Matrix> biases) {
        this.inputNodes = inputNodes;
        this.outputNodes = outputNodes;
        this.hiddenLayers = hiddenLayers.clone();


        this.weights = new ArrayList<>();
        for (Matrix w : weights) {
            this.weights.add(Matrix.copy(w));
        }

        this.biases = new ArrayList<>();
        for (Matrix b : biases) {
            this.biases.add(Matrix.copy(b));
        }
    }


    public void randomizeParameters() {
        for (Matrix w : weights) w.randomize();
        for (Matrix b : biases) b.randomize();
    }

    // Izracun izlaza mreze za dani ulazni niz podataka
    public double[] generateOutput(double[] inputArray) {
        Matrix input = Matrix.fromArray(inputArray);
        Matrix current = input;

        // Prolazak kroz sve slojeve
        for (int i = 0; i < weights.size(); i++) {
            Matrix w = weights.get(i);
            Matrix b = biases.get(i);

            // izracun net koeficijenata sloja ( wx + b )
            Matrix layer = Matrix.multiply(w, current);
            layer.add(b);
            layer.map(x -> sigmoid(x));
            current = layer;
        }

        return current.toArray();
    }


    // Mogućnost kloniranja
    public NeuralNetwork copy() {
        NeuralNetwork clone = new NeuralNetwork(inputNodes, hiddenLayers, outputNodes);
        for (int i = 0; i < weights.size(); i++) {
            clone.weights.set(i, Matrix.copy(weights.get(i)));
            clone.biases.set(i, Matrix.copy(biases.get(i)));
        }
        return clone;
    }

    // sigmuoidna funkcija za racunanje net koeficijenata
    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }


    public void print() {
        System.out.println("Network structure:");
        System.out.print("Input: " + inputNodes + " -> ");
        for (int h : hiddenLayers) System.out.print(h + " -> ");
        System.out.println("Output: " + outputNodes);
        System.out.println("Total layers: " + (hiddenLayers.length + 2));
        System.out.println("Weight matrices: ");
        for (Matrix w : weights) {
            for (int i = 0; i < w.rows; i++) {
                for (int j = 0; j < w.cols; j++) {
                    System.out.printf("%.4f ", w.data[i][j]);
                }
                System.out.println();
            }
        }
        System.out.println("Bias matrices: ");
        for (Matrix b : biases) {
            for (int i = 0; i < b.rows; i++) {
                for (int j = 0; j < b.cols; j++) {
                    System.out.printf("%.4f ", b.data[i][j]);
                }
                System.out.println();
            }
        }
    }

    public List<Matrix> getWeights() {
        return weights;
    }

    public void setWeights(List<Matrix> weights) {
        this.weights = weights;
    }

    public List<Matrix> getBiases() {
        return biases;
    }

    public void setBiases(List<Matrix> biases) {
        this.biases = biases;
    }

    public int getInputNodes() {
        return inputNodes;
    }

    public int getOutputNodes() {
        return outputNodes;
    }

    public int[] getHiddenLayers() {
        return hiddenLayers;
    }

    public static void main(String[] args) {
        int inputNodes = 5;
        int[] hiddenLayers = {4, 3};
        int outputNodes = 2;

        NeuralNetwork nn = new NeuralNetwork(inputNodes, hiddenLayers, outputNodes);
        nn.print();

        double[] input = {0.5, 0.2, 0.8, 0.1, 0.9};
        double[] output = nn.generateOutput(input);

        System.out.println("Output:");
        for (double val : output) {
            System.out.printf("%.4f ", val);
        }
    }
}