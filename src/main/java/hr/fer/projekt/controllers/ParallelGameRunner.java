package hr.fer.projekt.controllers;

import hr.fer.projekt.neuronskaMreza.NeuralNetwork;

import java.util.*;
import java.util.concurrent.*;

public class ParallelGameRunner {
    
    private final int threadPoolSize;
    private final ExecutorService executorService;
    private final ConcurrentHashMap<NeuralNetwork, Double> results;
    
    public ParallelGameRunner(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
        this.results = new ConcurrentHashMap<>();
    }
    
    public Map<NeuralNetwork, Double> runGamesInParallel(List<NeuralNetwork> neuralNetworks)
            throws InterruptedException, ExecutionException {
        
        List<Future<GameResult>> futures = new ArrayList<>();
        
        // Submit all game instances to thread pool
        for (NeuralNetwork nn : neuralNetworks) {
            Future<GameResult> future = executorService.submit(() -> runSingleGame(nn));
            futures.add(future);
        }
        
        // Collect results
        for (Future<GameResult> future : futures) {
            GameResult result = future.get(); // Blocks until complete
            results.put(result.neuralNetwork, result.fitness);
        }
        
        return new HashMap<>(results);
    }
    

    private GameResult runSingleGame(NeuralNetwork nn) throws InterruptedException {
        HeadlessGameInstance game = new HeadlessGameInstance(nn);
        double fitness = game.run(); // Blocks until game ends
        return new GameResult(nn, fitness);
    }
    
    public void shutdown() {

        try {
            if (!executorService.awaitTermination(60, TimeUnit.MILLISECONDS)) {
                System.exit(0);
            }
        } catch (InterruptedException e) {
                System.exit(0);
        }
    }
    
    // Inner class to hold results
    private static class GameResult {
        NeuralNetwork neuralNetwork;
        Double fitness;
        
        GameResult(NeuralNetwork nn, Double fitness) {
            this.neuralNetwork = nn;
            this.fitness = fitness;
        }
    }
}