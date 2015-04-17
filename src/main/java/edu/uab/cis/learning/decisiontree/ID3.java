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
        Set<LABEL> s= new HashSet<>(labels);
        if ( features.isEmpty()|| s.size() == 1){
            root.setLabel(s.iterator().next());
        }
        else{
            FEATURE_NAME bestFeature = findBestFeature(examples, labels, features);
            root.setFeature(bestFeature);
            for(FEATURE_VALUE value : features.get(bestFeature)){
                Table<Integer, FEATURE_NAME, FEATURE_VALUE> childTable = HashBasedTable.create();
                List<LABEL> newLabels = new ArrayList<>();
                int count = 0;
                for(int i = 0; i < examples.rowKeySet().size(); i++ ){
                    if(examples.get(i, bestFeature) == value){
                        for(FEATURE_NAME column : examples.columnKeySet()){
                            childTable.put(count, column, examples.get(count, column) );
                        }
                        newLabels.add(labels.get(i));
                        count++;
                    }
                }
                System.out.println(examples);
                System.out.println(labels);
                System.out.println(childTable);
                System.out.println(newLabels);
                childTable.column(bestFeature).clear();
                if(childTable.isEmpty()){
                    Node leafNode = new Node(_nodeCounter);
                    _nodeCounter++;
                    Multiset<LABEL> ladels = HashMultiset.create(labels);
                    LABEL mvp = Multisets.copyHighestCountFirst(ladels).iterator().next();
                    leafNode.setLabel(mvp);
                    root.addBranch(value, leafNode);
                } else {
                    Multiset<FEATURE_VALUE> save = features.remove(bestFeature);
                    root.addBranch(value, runID3(childTable, newLabels, features));
                    features.put(bestFeature, save);
                }
            }
        }
        return root;

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
            value -=  (valueOccurrences / totalLabelOccurrence)
                    * calculateEntropy(valueLabels);
        }
        double gain = calculateLabelEntropy(labelOccurrences);
        gain -= value;
        return gain;
    }

    private double calculateLabelEntropy(Multiset<LABEL> labels) {
        double totalLabels = labels.size();


        double entropy = 0;
        for(LABEL label : labels.elementSet()) {
            double truths = labels.count(label);
            if(truths != 0)
                entropy -=  (truths / totalLabels) * log2((truths / totalLabels));

        }
        return entropy;
    }

    private double calculateEntropy(Multiset<LABEL> valueLabels) {
        double featureLabels = valueLabels.size();
        Multiset<LABEL> labels = valueLabels;




        double entropy = 0;
        for(LABEL label : labels.elementSet()) {
            double v = labels.count(label);
            if(v != 0)
                entropy -=  (v/ featureLabels) * log2((v/ featureLabels));
        }
        return entropy;
    }

    public int get_nodeCounter(){
        return _nodeCounter;
    }



}

