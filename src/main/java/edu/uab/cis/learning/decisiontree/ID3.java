package edu.uab.cis.learning.decisiontree;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Table;

import java.util.*;

/**
 * Created by elais on 4/14/15.
 */
public class ID3 <LABEL, FEATURE_NAME, FEATURE_VALUE>{
    private int nodeCounter;

    public ID3() {

        nodeCounter = 0;
    }

    public Node buildTree(Table<Integer, FEATURE_NAME, FEATURE_VALUE> examples, Set<LABEL> labelSet, List<LABEL> labels, Set<FEATURE_NAME> features){

        Node root = runID3(examples, labelSet, labels, features);

        return root;
    }

    public Node runID3(Table<Integer, FEATURE_NAME, FEATURE_VALUE> examples, Set<LABEL> labelSet, List<LABEL> labels, Set<FEATURE_NAME> features){
        Node root = new Node(nodeCounter);
        nodeCounter++;


        return root;
    }

    public FEATURE_NAME findBestFeature(Table<Integer, FEATURE_NAME, FEATURE_VALUE> examples, Set<LABEL> labelSet,
                           List<LABEL> labels, Set<FEATURE_NAME> f){
        double bestGain = -10;
        FEATURE_NAME bestFeature = null;
        Iterator<FEATURE_NAME> features = f.iterator();
        while(features.hasNext()){
            FEATURE_NAME name = features.next();
            Map<Integer, FEATURE_VALUE> column = examples.column(name);
            double gain = calculateGain(column, examples, labels, f);
        }

        return null;
    }

    private double calculateGain(Map<Integer, FEATURE_VALUE> column, Table<Integer, FEATURE_NAME, FEATURE_VALUE> examples, List<LABEL> labels, Set<FEATURE_NAME> f) {

        Multiset<FEATURE_VALUE> totalOccurances = HashMultiset.create();

        return 0;
    }

    public Double calculateEntropy(Table<Integer, FEATURE_NAME, FEATURE_VALUE> examples, List<LABEL> labels, Map<FEATURE_NAME, Set<FEATURE_VALUE>> feature_map){
        double entropy = 0;


        return null;
    }



}

