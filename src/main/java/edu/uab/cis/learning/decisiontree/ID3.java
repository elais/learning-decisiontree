package edu.uab.cis.learning.decisiontree;
import com.google.common.collect.*;
import com.google.common.math.DoubleMath;
import java.util.*;
import static com.google.common.math.DoubleMath.log2;
import static java.lang.Math.*;

/**
 * Created by elais on 4/14/15.
 */
public class ID3 <LABEL, FEATURE_NAME, FEATURE_VALUE>{
    private int _nodeCounter;

    public ID3() {

        _nodeCounter = 0;
    }

    public Node buildTree(Table<Integer, FEATURE_NAME, FEATURE_VALUE> examples, List<LABEL> labels,Map<FEATURE_NAME, Multiset<FEATURE_VALUE>> features) {
        Node root = runID3(examples, labels, features);
        return root;
    }

    private Node runID3(Table<Integer, FEATURE_NAME, FEATURE_VALUE> examples, List<LABEL> labels ,Map<FEATURE_NAME, Multiset<FEATURE_VALUE>> features) {
        Node root = new Node(_nodeCounter);
        _nodeCounter++;
        if(features.isEmpty() || allExamplesPositive(labels))
            root.setLabel(labels.iterator().next());
        else{
            FEATURE_NAME bestFeature = findBestFeature(examples, labels, features);
            Multiset<FEATURE_VALUE> forLater = features.get(bestFeature);
            root.setFeature(bestFeature);
            System.out.println(bestFeature);
            Iterator<FEATURE_VALUE> it = features.get(bestFeature).elementSet().iterator();
            while(it.hasNext()){
                FEATURE_VALUE value = it.next();
                Table<Integer, FEATURE_NAME, FEATURE_VALUE> trimmedExamples = HashBasedTable.create();
                List<LABEL> newLabels = new ArrayList<>();
                for(int i = 0; i < examples.rowKeySet().size(); i++){
                    if(examples.row(i).containsValue(value)) {
                        Iterator<FEATURE_NAME> it2 = features.keySet().iterator();
                        while (it2.hasNext()) {
                            FEATURE_NAME name = it2.next();
                            if (name != bestFeature) {
                                trimmedExamples.put(i, name, examples.row(i).get(name));
                                newLabels.add(labels.get(i));
                            }
                        }
                    }
                }
                System.out.println(trimmedExamples);
                if(trimmedExamples.rowKeySet().size() < 1){
                    Node leafNode = new Node(_nodeCounter);
                    _nodeCounter++;
                    Multiset<LABEL> labelOccurrences = LinkedHashMultiset.create(newLabels);
                    double mcv = 0;
                    LABEL bestLabel = null;
                    Iterator<LABEL> another = labelOccurrences.elementSet().iterator();
                    while(it.hasNext()){
                        LABEL nextLabel = another.next();
                        if(labelOccurrences.count(nextLabel) > mcv) {
                            mcv = labelOccurrences.count(nextLabel);
                            bestLabel = nextLabel;
                        }
                    }
                    leafNode.setLabel(bestLabel);
                    root.addBranch(value, leafNode);
                } else {
                    features.remove(bestFeature);
                    root.addBranch(value, runID3(trimmedExamples, newLabels, features));
                    //features.put(bestFeature, forLater);
                }
            }
        }
        return null;
    }

    private Boolean allExamplesPositive(List<LABEL> labels){
        Multiset<LABEL> labelOccurrences = LinkedHashMultiset.create(labels);
        if(labelOccurrences.elementSet().size() < 2){
            return true;
        } else
            return false;
    }

    private FEATURE_NAME findBestFeature(Table<Integer, FEATURE_NAME, FEATURE_VALUE> examples, List<LABEL> labels, Map<FEATURE_NAME, Multiset<FEATURE_VALUE>> features) {
        double bestGain = -10;
        FEATURE_NAME bestFeature = null;

        for (FEATURE_NAME name : features.keySet()) {
            Map<Integer, FEATURE_VALUE> colPos = examples.column(name);
            double gain = calculateGain(labels, colPos, features.get(name));
            if (gain >= bestGain) {
                bestGain = gain;
                bestFeature = name;
            }
        }
        for(FEATURE_NAME name : features.keySet()){
            if(name == bestFeature)
                return bestFeature;
        }
        return null;
    }

    private double calculateGain(List<LABEL> labels, Map<Integer, FEATURE_VALUE> colPos, Multiset<FEATURE_VALUE> feature_values) {

        Map<FEATURE_VALUE, Multiset<LABEL>> value_summation = new LinkedHashMap<>();
        Multiset<LABEL> labelOccurrences = LinkedHashMultiset.create(labels);
        double totalLabelOccurrence = labelOccurrences.size();

        //compares labels to feature values, puts results in value_summation
        for(int i = 0; i < labels.size(); i++){
            if(!value_summation.containsKey(colPos.get(i))){
                Multiset<LABEL> l = HashMultiset.create();
                l.add(labels.get(i));
                value_summation.put(colPos.get(i), l);
            } else{
                Multiset<LABEL> l = value_summation.get(colPos.get(i));
                l.add(labels.get(i));
                value_summation.put(colPos.get(i), l);
            }
        }

        double value = 0;
        for (FEATURE_VALUE val : value_summation.keySet()) {
            Iterator<LABEL> l = value_summation.get(val).elementSet().iterator();
            while (l.hasNext()) {
                LABEL lab = l.next();
                double valSum = value_summation.get(val).count(lab);
                double lO = labelOccurrences.size();
                value += (-1) * (valSum / lO) *
                        calculateEntropy(feature_values);
            }
        }
        double gain = calculateLabelEntropy(labelOccurrences);
        gain += value;
        System.out.println(gain);
        return gain;
    }

    private double calculateLabelEntropy(Multiset<LABEL> labelOccurences) {
        if(labelOccurences.size() == 0){
            return 0;
        }
        if(labelOccurences.elementSet().size() < 2)
            return 0;

        double entropy = 0;
        for (LABEL feature_value : labelOccurences.elementSet()) {
            double count = labelOccurences.count(feature_value);
            double totalOccurrence = labelOccurences.size();
            entropy +=  (-1) * (count/ totalOccurrence)
                    * (log10(count/ totalOccurrence) / log10(2));
        }
        return entropy;
    }

    private double calculateEntropy(Multiset<FEATURE_VALUE> feature_values) {
        if(feature_values.size() == 0){
            return 0;
        }
        if(feature_values.elementSet().size() < 2)
            return 0;

        double entropy = 0;
        for (FEATURE_VALUE feature_value : feature_values.elementSet()) {
            double count = feature_values.count(feature_value);
            double totalOccurrence = feature_values.size();

            entropy += (-1) * (count / totalOccurrence) * (log10((count / totalOccurrence)) / log10(2));
            //System.out.println(entropy);
        }
        return entropy;
    }

    public int get_nodeCounter(){
        return _nodeCounter;
    }



}

