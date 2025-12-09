package hr.fer.projekt.genetskiAlgoritam;

import hr.fer.projekt.matematika.Matrix;
import hr.fer.projekt.neuronskaMreza.NeuralNetwork;

import java.util.*;

public class GeneticAlgorithms {

    private static final Random rand = new Random();

    public static Map<NeuralNetwork, Double> makeNewGen(Map<NeuralNetwork, Double> oldGeneration, GeneticType geneticType, int gen) {
        Map<NeuralNetwork, Double> newGeneration = new HashMap<NeuralNetwork, Double>();

        switch (geneticType) {
            case DEFAULT:
                newGeneration = makeBabies(oldGeneration, gen);
                break;
            default:
                throw new IllegalArgumentException("Genetic type not supported");
        }

        return newGeneration;
    }

    private static double BLX_ALPHA(double w1, double w2, double alpha) {
        double length = Math.abs(w2 - w1);
        if (length == 0) {
            length = 0.01;
        }
        if (w1 >= w2) {
            return new Random().nextDouble(w2 - alpha * length < -1 ? -1 : w2 - alpha * length, w1 + alpha * length > 1 ? 1 : w1 + alpha * length);
        } else {
            return new Random().nextDouble(w1 - alpha * length < -1 ? -1 : w1 - alpha * length, w2 + alpha * length > 1 ? 1 : w2 + alpha * length);
        }
    }

    private static double mutate(double val, double mutationChance) {

        return rand.nextDouble(0, 1) < mutationChance ? rand.nextDouble(-1, 1) : val;
    }

    private static Map<NeuralNetwork, Double> makeBabies(Map<NeuralNetwork, Double> oldGeneration, int gen) {
        Map<NeuralNetwork, Double> newGeneration = new HashMap<NeuralNetwork, Double>();

        // dodaje najboljeg stare generacije u novu i mice ga iz stare
        NeuralNetwork elite = oldGeneration.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
        newGeneration.put(elite, null);

        System.out.println(elite + "\n With Fitness:" + oldGeneration.get(elite));

        //racuna ukupni fitness
        double sum = oldGeneration.values().stream().mapToDouble(Double::doubleValue).sum();

        for (int i = 0; i < oldGeneration.size() - 1; i++) {
            double idxPrviClan = new Random().nextDouble(0, sum);
            double idxDrugiClan = new Random().nextDouble(0, sum);

            NeuralNetwork prviClan;
            NeuralNetwork drugiClan;

            double floor = 0;
            prviClan = findRandomClan(oldGeneration, idxPrviClan, floor);
            floor = 0;
            drugiClan = findRandomClan(oldGeneration, idxDrugiClan, floor);

            newGeneration.put(createChild(prviClan, drugiClan, gen, i + 1), null);
        }

        return newGeneration;
    }

    private static NeuralNetwork findRandomClan(Map<NeuralNetwork, Double> oldGeneration, double idxPrviClan, double floor) {
        for (Map.Entry<NeuralNetwork, Double> entry : oldGeneration.entrySet()) {
            double value = entry.getValue();

            if (idxPrviClan < value + floor && idxPrviClan >= floor) {
                return entry.getKey();
            } else {
                floor += value;
            }
        }
        throw new RuntimeException("Clan not found - Linija:78");
    }

    private static NeuralNetwork createChild(NeuralNetwork prviClan, NeuralNetwork drugiClan, int gen, int id) {
        NeuralNetwork child = prviClan.copy();
        child.setID("NN-" + gen + "." + id);

        List<Matrix> childWeight = prviClan.getWeights();
        List<Matrix> drugiClanWeights = drugiClan.getWeights();

        List<Matrix> childBias = prviClan.getBiases();
        List<Matrix> drugiClanBiases = drugiClan.getBiases();

        Iterator<Matrix> iteratorPrvogClanaWeights = childWeight.iterator();
        Iterator<Matrix> iteratorPrvogClanaBiases = childBias.iterator();

        Iterator<Matrix> iteratorDrugogClanaWeights = drugiClanWeights.iterator();
        Iterator<Matrix> iteratorDrugogClanaBiases = drugiClanBiases.iterator();

        generateNewValues(childWeight, iteratorPrvogClanaWeights, iteratorDrugogClanaWeights);
        generateNewValues(childBias, iteratorPrvogClanaBiases, iteratorDrugogClanaBiases);

        return child;
    }

    private static void generateNewValues(List<Matrix> childBias, Iterator<Matrix> iteratorPrvogClanaBiases, Iterator<Matrix> iteratorDrugogClanaBiases) {
        for (Matrix w1 : childBias) {
            Matrix w2 = iteratorPrvogClanaBiases.next();
            Matrix w3 = iteratorDrugogClanaBiases.next();

            double[] arrPrvi = w2.toArray();
            double[] arrDrugi = w3.toArray();
            for (int i = 0; i < w1.rows; i++) {
                for (int j = 0; j < w1.cols; j++) {
                    w1.data[i][j] = BLX_ALPHA(arrPrvi[i * w1.cols + j], arrDrugi[i * w1.cols + j], 0.2);
                }
            }

            for (int i = 0; i < w1.rows; i++) {
                for (int j = 0; j < w1.cols; j++) {
                    w1.data[i][j] = mutate(w1.data[i][j], 0.05);
                }
            }
        }
    }
}
