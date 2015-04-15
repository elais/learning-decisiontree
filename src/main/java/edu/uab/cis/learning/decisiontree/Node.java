package edu.uab.cis.learning.decisiontree;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Node<LABEL, FEATURE_NAME, FEATURE_VALUE>{

    private Collection<LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE>> data;
    private boolean isUsed;
    private Map<Double, Node> branches;
    private LABEL label;
    private FEATURE_NAME feature;
    private int nodeID;


    public Node(int nodeCounter){
        label = null;
        branches = new LinkedHashMap<>();
        feature = null;
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
    public void addBranch(double value, Node node) {
        branches.put(value, node);
    }
    public Map<Double, Node> getBranches() {
        return branches;
    }
    public FEATURE_NAME getFeature() {
        return feature;
    }


}
