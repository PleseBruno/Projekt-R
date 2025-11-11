package hr.fer.projekt.genetskiAlgoritam;

import hr.fer.projekt.neuronskaMreza.Single;
import java.util.*;

public class GeneticAlgorithm {

    Single single = new Single();

    Map<List<Integer>, List<Integer>> dataSetMap = new HashMap<>();

    private final int VELICINA_POP = 50;

    List<Single> oldGeneration;

    Map<Single, Integer> oldGenerationFitnessMap;

    public GeneticAlgorithm(List<Single> oldGeneration) {
        this.oldGeneration = oldGeneration;
        oldGeneration.stream().forEach(single -> oldGenerationFitnessMap.put(single, checkFitness(single)));
    }

    public List<Single> makeBabies(Map<Single, Integer> oldGenerationFitnessMap) {
        List<Single> newGeneration = new ArrayList<>();


        // dodaje najboljeg stare generacije u novu i mice ga iz stare
        newGeneration.add(oldGenerationFitnessMap.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey());
        oldGenerationFitnessMap.remove(newGeneration.getFirst());

        //racuna ukupni fitness
        double sum = oldGenerationFitnessMap.values().stream().mapToDouble(Integer::doubleValue).sum();

        //tezinski bira drugog novog roditelj od preostalih starih
        Random rand = new Random();

        double clan = rand.nextDouble(0, sum);

        double floor = 0;
        Iterator<Map.Entry<Single, Integer>> iterator = oldGenerationFitnessMap.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Single, Integer> entry = iterator.next();

            if (clan < (entry.getValue() + floor) && clan >= floor) {
                newGeneration.add(entry.getKey());
                iterator.remove();
                break;
            } else {
                floor += entry.getValue();
            }
        }

        // dva roditelja u novoj generaciji stvaraju potomke
        for (int i = 2; i < VELICINA_POP; i++) {
            //TODO: kreiranje beba za svaka dva weighted random roditelja
            newGeneration.add(single.makeBaby(newGeneration.get(1), newGeneration.getFirst()));
        }
        return newGeneration;
    }

    /**
     * ova funkcija vraca fitnes neke neuronske mreze. Racuna ga tako da ako je
     * velicina vracenog inputa i zeljenog jednaka onda povecava score za 1000,
     * ako su vraceni i zeljeni output oboje prazni ili jednaki povecava score za 500,
     * a inace povecava score za svaki digit koji se pojavljuje u oba.
     *
     * @param single
     * @return score u intervalu [0, 1500]
     */

    public int checkFitness(Single single) {
        int score = 0;

        List<Integer> input = single.getInput();
        double[] result = single.getResult();

        List<Integer> resultList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            if (result[i] > 0.5) {
                resultList.add(i + 1);
            }
        }

        if (input.size() == resultList.size()) {
            score += 1000;
        }

        if (dataSetMap.get(input).isEmpty() && resultList.isEmpty() || dataSetMap.get(input).equals(resultList)) {
            score += 500;
        } else {
            for (Integer realResultInteger : dataSetMap.get(input)) {
                if (resultList.contains(realResultInteger)) {
                    score += 100;
                }
            }
        }

        return score;
    }
}

//    Generiraj slucajnu populaciju mozgova od VEL_POP jedinki; evaluiraj svaki. ˇ
//          • Ponavljaj dok nije kraj:
//            – Inicijaliziraj pomocnu populaciju na praznu. ´
//            – Ponavljaj dok velicina pomo ˇ cne populacije ne postane jednaka veli ´ cini populacije ˇ
//            roditelja
//                  * Odaberi dva roditelja iz populacije roditelja
//                  * Dijete = Križaj roditelje + Mutacija
//                  * Vrednuj dijete
//                  * Ubaci ga u pomocnu populaciju ´
//            – Obriši populaciju roditelja
//            – Promoviraj pomocnu populaciju u populaciju roditelja
//}