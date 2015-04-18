package edu.uab.cis.learning.decisiontree;

import com.google.common.collect.*;
import com.google.common.math.DoubleMath;
import edu.uab.cis.learning.decisiontree.LabeledFeatures;
import edu.uab.cis.learning.decisiontree.Node;

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

    public Node buildTree(Collection<LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE>> examples,Map<FEATURE_NAME, Multiset<FEATURE_VALUE>> features) {
        Node root = runID3(examples, features);
        return root;
    }

    private Node runID3(Collection<LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE>> examples, Map<FEATURE_NAME, Multiset<FEATURE_VALUE>> features) {
        Node root = new Node(_nodeCounter);
        _nodeCounter++;
        Multiset<LABEL> labels = HashMultiset.create();
        for(LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE> lf : examples)
        {
            labels.add(lf.getLabel());
        }
        double max = Double.NEGATIVE_INFINITY;
        LABEL max_label = null;
        for (LABEL label : labels.elementSet())
        {
            if (labels.count(label) > max)
            {
                max = labels.count(label);
                max_label = label;
            }
        }
        max_label = max_label;
        if ( features.isEmpty()|| labels.size() < 2){
            root.setLabel(max_label);
            return root;
        }
        else{
            System.out.println(features);
            FEATURE_NAME bestFeature = findBestFeature(examples, features);
            root.setFeature(bestFeature);
            Map<FEATURE_VALUE, List<LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE>>> branchmap = new LinkedHashMap<>();
            for(LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE> e : examples)
            {
                if(!branchmap.containsKey(e.getFeatureValue(bestFeature)))
                {
                    branchmap.put(e.getFeatureValue(bestFeature), new ArrayList<LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE>>());
                }
                branchmap.get(e.getFeatureValue(bestFeature)).add(e);
            }
            for(FEATURE_VALUE value : branchmap.keySet())
            {
                _nodeCounter++;
                Multiset<FEATURE_VALUE> save = features.remove(bestFeature);
                root.addBranch(value, runID3(branchmap.get(value), features));
            }
        }
        return root;

    }

    private FEATURE_NAME findBestFeature(Collection<LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE>> examples, Map<FEATURE_NAME, Multiset<FEATURE_VALUE>> features) {
        double bestGain = Double.NEGATIVE_INFINITY;
        FEATURE_NAME bestFeature = null;

        Multiset<LABEL> totalLabelCount = LinkedHashMultiset.create();
        for(LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE> e : examples)
        {
            totalLabelCount.add(e.getLabel());
        }

        for (FEATURE_NAME name : features.keySet())
        {
            Map<FEATURE_VALUE, Multiset<LABEL>> feature_value_label_map = new LinkedHashMap<>();
            for(LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE> e : examples)
            {
                FEATURE_VALUE value = e.getFeatureValue(name);
                if(!feature_value_label_map.containsKey(value)){
                    Multiset<LABEL> label_value_tracker = LinkedHashMultiset.create();
                    feature_value_label_map.put(value, label_value_tracker);
                }
                feature_value_label_map.get(value).add(e.getLabel());
            }
            double gain = calculateGain(examples.size(), totalLabelCount, feature_value_label_map);
            if (gain >= bestGain) {
                bestGain = gain;
                bestFeature = name;
            }
        }
        for(FEATURE_NAME name : features.keySet()){
            if(name == bestFeature) {
                return bestFeature;
            }
        }
        return null;
    }

    private double calculateGain(double exampleSize, Multiset<LABEL> totalLabelCount, Map<FEATURE_VALUE, Multiset<LABEL>> value_map) {


        double value = 0;
        for(FEATURE_VALUE val : value_map.keySet()){
            double value_label_sum = 0.0;
            if(value_map.containsKey(val)){
                for(LABEL label : value_map.get(val).elementSet()){
                    value_label_sum += (double) value_map.get(val).count(label);
                }
            }
            value += (value_label_sum/exampleSize) * calculateEntropy(value_label_sum, value_map, val);

        }

        double gain = calculateLabelEntropy(totalLabelCount, exampleSize) - value;
        return gain;
    }

    private double calculateLabelEntropy(Multiset<LABEL> labels, double exampleSize)
    {
        double entropy = 0;
        for(LABEL label : labels.elementSet())
        {
            entropy -= ( labels.count(label)/exampleSize ) * log2(labels.count(label)/exampleSize);
        }
        return entropy;
    }

    private double calculateEntropy(double value_label_sum, Map<FEATURE_VALUE, Multiset<LABEL>> value_map, FEATURE_VALUE val) {

        double entropy = 0;
        for(LABEL label : value_map.get(val).elementSet()) {
            double v = value_map.get(val).count(label);
            if(v != 0)
                entropy -=  (v/ value_label_sum) * log2((v/ value_label_sum));
        }
        return entropy;
    }

    public int get_nodeCounter(){
        return _nodeCounter;
    }

}