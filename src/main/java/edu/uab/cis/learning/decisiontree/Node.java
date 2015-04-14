package edu.uab.cis.learning.decisiontree;

import java.util.Collection;
import java.util.Set;

public class Node<LABEL, FEATURE_NAME, FEATURE_VALUE>{

    public double entropy;
    private Collection<LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE>> data;
    private boolean isUsed;
    public int discreteValue;
    public Set<Node> children;
    public Node parent;
    public Node(Node p, Collection<LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE>> input){
        data = input;
        parent = p;
        setEntropy(0.0);
        setParent(null);
        setChildren(null);
        setUsed(false);
    }
    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getParent() {
        return parent;
    }

    public void setData(Collection<LabeledFeatures> data) {
        this.data = data;
    }

    public Collection<LabeledFeatures> getData() {
        return data;
    }

    public void setEntropy(double entropy) {
        this.entropy = entropy;
    }

    public double getEntropy() {
        return entropy;
    }

    public void setChildren(Set<Node> children) {
        this.children = children;
    }
    public Set<Node> getChildren() {
        return children;
    }

    public void setUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }

    public boolean isUsed() {
        return isUsed;
    }

}
