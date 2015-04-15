package edu.uab.cis.learning.decisiontree;

import java.util.*;

/**
 * Created by elais on 4/14/15.
 */
public class ID3 <LABEL, FEATURE_NAME, FEATURE_VALUE>{
    private int nodeCounter;

    public ID3() {

        nodeCounter = 0;
    }

    public Node buildTree(Map<FEATURE_NAME, List<FEATURE_VALUE>> examples, Set<LABEL> labelSet, List<LABEL> labels, Set<FEATURE_NAME> featureSet){

        Node root = runID3(examples,labelSet, labels, featureSet);

        return root;
    }

    public Node runID3(Map<FEATURE_NAME, List<FEATURE_VALUE>> examples, Set<LABEL> labelSet, List<LABEL> labels, Set<FEATURE_NAME> featureSet){
        Node root = new Node(nodeCounter);
        nodeCounter++;


        return root;
    }

}

