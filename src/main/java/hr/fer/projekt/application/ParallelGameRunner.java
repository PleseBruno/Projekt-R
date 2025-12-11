package hr.fer.projekt.application;

import hr.fer.projekt.neuronskaMreza.NeuralNetwork;
import hr.fer.projekt.controllers.HeadlessGameInstance;
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
    
    /**
     * Run n instances of the game in parallel, each with a different neural network
     * @param neuralNetworks List of neural networks to evaluate
     * @return Map of neural network to fitness score
     */
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
    
    /**
     * Run a single game instance with given neural network
     */
    private GameResult runSingleGame(NeuralNetwork nn) {
        HeadlessGameInstance game = new HeadlessGameInstance(nn);
        double fitness = game.run(); // Blocks until game ends
        return new GameResult(nn, fitness);
    }
    
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
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