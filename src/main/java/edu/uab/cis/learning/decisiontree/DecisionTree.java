package edu.uab.cis.learning.decisiontree;

import java.util.*;

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
  private Collection<LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE>> trainingData;
  private Map<FEATURE_NAME, Boolean> feature_names;
  private Set<LABEL> labels;
  public DecisionTree(Collection<LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE>> trainingData) {
    this.trainingData = trainingData;
    Iterator<LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE>> it = trainingData.iterator();
    while(it.hasNext()){
      LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE> next = it.next();
      feature_names.addAll(next.getFeatureNames());
      labels.add(next.getLabel());
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
  public LABEL classify(Features<FEATURE_NAME, FEATURE_VALUE> features) {
    // TODO
    return null;
  }

  public Double calculateEntropy(Collection<LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE>> data){
    double entropy = 0;

    if(data.size() == 0){
      //do nothing
      return 0.0;
    }




    return null;

  }
}

