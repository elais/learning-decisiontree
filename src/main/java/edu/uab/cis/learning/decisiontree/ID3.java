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
        if (features.isEmpty() || allExamplesPositive(labels)){
            root.setLabel(labels.get(0));
            //System.out.println(root.getLabel());
        }
        else{
            FEATURE_NAME bestFeature = findBestFeature(examples, labels, features);
            root.setFeature(bestFeature);
            //System.out.println(bestFeature);
            Iterator<FEATURE_VALUE> it = features.get(bestFeature).elementSet().iterator();
            while(it.hasNext()){
                FEATURE_VALUE value = it.next();
                Table<Integer, FEATURE_NAME, FEATURE_VALUE> trimmedExamples = HashBasedTable.create();
                List<LABEL> newLabels = new ArrayList<>();
                int count = 0;
                Set<FEATURE_NAME> feats = features.keySet();
                for(int i = 0; i < examples.rowKeySet().size(); i++){
                    if(examples.row(i).containsValue(value)){
                        for(FEATURE_NAME feat : feats){
                            trimmedExamples.put(count, feat, examples.get(i, feat));
                        }
                        newLabels.add(labels.get(i));
                        count++;
                    }
                }
                if(trimmedExamples.rowKeySet().isEmpty()){
                    Node leafNode = new Node(_nodeCounter);
                    _nodeCounter++;
                    Multiset<LABEL> mcv = HashMultiset.create(newLabels);
                    System.out.println(mcv);
                    mcv = Multisets.copyHighestCountFirst(mcv);
                    LABEL bestLabel = mcv.entrySet().iterator().next().getElement();
                    //System.out.println(bestLabel);
                    leafNode.setLabel(bestLabel);
                    root.addBranch(value, leafNode);
                } else {
                    trimmedExamples.column(bestFeature).clear();
                    Map<FEATURE_NAME, Multiset<FEATURE_VALUE>> secondFeatures = features;
                    secondFeatures.remove(bestFeature);
                    root.addBranch(value, runID3(trimmedExamples, newLabels, secondFeatures));
                }
            }
        }
        return root;
    }

    private Boolean allExamplesPositive(List<LABEL> labels){
        Multiset<LABEL> labelOccurrences = LinkedHashMultiset.create(labels);
        if(labelOccurrences.elementSet().size() == 1){
            return true;
        } else
            return false;
    }

    private FEATURE_NAME findBestFeature(Table<Integer, FEATURE_NAME, FEATURE_VALUE> examples, List<LABEL> labels, Map<FEATURE_NAME, Multiset<FEATURE_VALUE>> features) {
        double bestGain = Double.NEGATIVE_INFINITY;
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
                Multiset<LABEL> tempSet = LinkedHashMultiset.create();
                tempSet.add(labels.get(i));
                value_summation.put(colPos.get(i), tempSet);
            }else {
                Multiset<LABEL> tempSet = value_summation.get(colPos.get(i));
                tempSet.add(labels.get(i));
                value_summation.put(colPos.get(i), tempSet);
            }
        }
        double value = 0;
        for(FEATURE_VALUE val : value_summation.keySet()){
            Iterator it = value_summation.get(val).iterator();
            Multiset<LABEL> valueLabels = value_summation.get(val);
            double valueOccurrences = valueLabels.size();
            //System.out.println(valueOccurrences);
            value -= -(valueOccurrences / totalLabelOccurrence) * calculateEntropy(valueLabels);
        }
        double gain = calculateLabelEntropy(labelOccurrences);
        gain -= value;
        return gain;
    }

    private double calculateLabelEntropy(Multiset<LABEL> labels) {
        double totalLabels = labels.size();

        if(totalLabels == 0){
            return 0;
        }
        if(labels.elementSet().size() == 1)
            return 0;

        double entropy = 0;
        for(LABEL label : labels.elementSet()) {
            double truths = labels.count(label);
            if(truths != 0)
                entropy -= (truths / totalLabels) * log2((truths / totalLabels));

        }
        //System.out.println(entropy);
        return entropy;
    }

    private double calculateEntropy(Multiset<LABEL> valueLabels) {
        double featureLabels = valueLabels.size();
        Multiset<LABEL> labels = valueLabels;


        if(featureLabels == 0){
            return 0;
        }
        if(labels.elementSet().size() == 1) {
            return 0;
        }


        double entropy = 0;
        for(LABEL label : labels.elementSet()) {
            double v = labels.count(label);
            //System.out.println(featureLabels);
            if(v != 0)
                entropy -= (v/ featureLabels) * log2((v/ featureLabels));
        }
        return entropy;
    }

    public int get_nodeCounter(){
        return _nodeCounter;
    }



}

