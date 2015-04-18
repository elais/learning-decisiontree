package edu.uab.cis.learning.decisiontree;

import com.google.common.collect.*;

import java.util.*;

import static com.google.common.math.DoubleMath.log2;

/**
 * A decision tree classifier.
 *
 * @param <LABEL>
 *          The type of label that the classifier predicts.
 * @param <FEATURE_NAME>
 *          The type used for feature names.
 * @param <FEATURE_VALUE>
 *          The type used for feature values.
 */
public class DecisionTree<LABEL, FEATURE_NAME, FEATURE_VALUE> {

  /**
   * Trains a decision tree classifier on the given training examples.
   *
   * <ol>
   * <li>If all examples have the same label, a leaf node is created.</li>
   * <li>If no features are remaining, a leaf node is created.</li>
   * <li>Otherwise, the feature F with the highest information gain is
   * identified. A branch node is created where for each possible value V of
   * feature F:
   * <ol>
   * <li>The subset of examples where F=V is selected.</li>
   * <li>A decision (sub)tree is recursively created for the selected examples.
   * None of these subtrees nor their descendants are allowed to branch again on
   * feature F.</li>
   * </ol>
   * </li>
   * </ol>
   *
   * @param trainingData
   *          The training examples, where each example is a set of features and
   *          the label that should be predicted for those features.
   */
  private int isLeaf;
  private FEATURE_NAME bestFeature;
  private Collection<LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE>> trainingData;
  private Map<FEATURE_VALUE, DecisionTree<LABEL, FEATURE_NAME, FEATURE_VALUE>> tree;
  LABEL return_label;

  public DecisionTree(Collection<LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE>> trainingData) {
    /*
    //Multimap<FEATURE_NAME,FEATURE_VALUE> examples = HashMultimap.create();
    myFeatures = new LinkedHashMap<>();
    Table<Integer, FEATURE_NAME, FEATURE_VALUE> examples = HashBasedTable.create();
    List<LABEL> labels = new ArrayList();


    ID3 id3 = new ID3();
    _root = id3.buildTree(trainingData, myFeatures);
  */
    this(trainingData, new LinkedHashSet<FEATURE_NAME>());

  }

  private DecisionTree(Collection<LabeledFeatures<LABEL,FEATURE_NAME,FEATURE_VALUE>> trainingData, Set<FEATURE_NAME> used_features) {
    tree = new LinkedHashMap<>();

    List<FEATURE_NAME> myFeatures = new ArrayList<>();
    for(LabeledFeatures<LABEL,FEATURE_NAME,FEATURE_VALUE> labeled_feature : trainingData)
    {
      for(FEATURE_NAME fn : labeled_feature.getFeatureNames())
      {
        myFeatures.add(fn);
      }
    }
    myFeatures.removeAll(used_features);

    Multiset<LABEL> labels = HashMultiset.create();
    for(LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE> lf : trainingData)
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
    return_label = max_label;

    isLeaf = 0;
    if ( myFeatures.isEmpty()|| labels.size() < 2){
      isLeaf=1; //true
      return;
    }
    else { isLeaf = 0; }


    bestFeature = findBestFeature(trainingData, myFeatures);
    Map<FEATURE_VALUE, List<LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE>>> branchmap = new LinkedHashMap<>();
      for(LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE> e : trainingData)
      {
        if(!branchmap.containsKey(e.getFeatureValue(bestFeature)))
        {
          branchmap.put(e.getFeatureValue(bestFeature), new ArrayList<LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE>>());
        }
        branchmap.get(e.getFeatureValue(bestFeature)).add(e);
      }
      used_features.add(bestFeature);
      for(FEATURE_VALUE value : branchmap.keySet())
      {
        tree.put(value, new DecisionTree<>( branchmap.get(value), new LinkedHashSet<FEATURE_NAME>(used_features)));
      }
  }

  /**
   * Predicts a label given a set of features.
   *
   * <ol>
   * <li>For a leaf node where all examples have the same label, that label is
   * returned.</li>
   * <li>For a leaf node where the examples have more than one label, the most
   * frequent label is returned.</li>
   * <li>For a branch node based on a feature F, E is inspected to determine the
   * value V that it has for feature F.
   * <ol>
   * <li>If the branch node has a subtree for V, then example E is recursively
   * classified using the subtree.</li>
   * <li>If the branch node does not have a subtree for V, then the most
   * frequent label for the examples at the branch node is returned.</li>
   * </ol>
   * <li>
   * </ol>
   *
   * @param features
   *          The features for which a label is to be predicted.
   * @return The predicted label.
   */
  public LABEL classify(Features<FEATURE_NAME, FEATURE_VALUE> features)
  {
    if(isLeaf == 1) { return return_label; }
    if (features.getFeatureNames().contains(bestFeature) && tree.containsKey(features.getFeatureValue(bestFeature))) {
      return tree.get(features.getFeatureValue(bestFeature)).classify(features);
    }
    else { return return_label; }

  }

  private FEATURE_NAME findBestFeature(Collection<LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE>> examples, List<FEATURE_NAME> features) {
    double bestGain = Double.NEGATIVE_INFINITY;
    FEATURE_NAME bestFeature = null;

    Multiset<LABEL> totalLabelCount = LinkedHashMultiset.create();
    for(LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE> e : examples)
    {
      totalLabelCount.add(e.getLabel());
    }

    for (FEATURE_NAME name : features)
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
    for(FEATURE_NAME name : features){
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

}

