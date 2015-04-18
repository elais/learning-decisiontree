package edu.uab.cis.learning.decisiontree;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Node<LABEL, FEATURE_NAME, FEATURE_VALUE>{

    private Collection<LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE>> data;
    private boolean isUsed;
    private Map<FEATURE_VALUE, Node> branches = new LinkedHashMap<>();
    private LABEL label = null;
    private FEATURE_NAME feature = null;
    private int nodeID;


    public Node(int nodeCounter){
        nodeID = nodeCounter;

    }

    public Node(Node that){
        this(-1);
    }




    public int getNodeID() {
        return nodeID;
    }
    public LABEL getLabel() {
        return label;
    }
    public void setLabel(LABEL l) {
        label = l;
    }
    public void setFeature(FEATURE_NAME bestFeature) {
        feature = bestFeature;
    }
    public void addBranch(FEATURE_VALUE value, Node node) {
        branches.put(value, node);
    }
    public Map<FEATURE_VALUE, Node> getBranches() {
        return branches;
    }
    public FEATURE_NAME getFeature(){
        return this.feature;
    }

    public LABEL makeDecision(Features<FEATURE_NAME, FEATURE_VALUE> features, FEATURE_NAME name){
        LABEL decision;
        decision = null;
        if(branches.isEmpty()){
            return getLabel();
        } else {
                FEATURE_VALUE value = features.getFeatureValue(name);
                if(branches.containsKey(value)){
                    System.out.println(value);
                    System.out.println(branches.get(value));
                    Node childNode = branches.get(value);
                    decision = (LABEL)childNode.makeDecision(features, childNode.getFeature());
                    //if(decision == null){
                    //    return getLabel();
                    //}
                }
            }
        return decision;
    }



}
