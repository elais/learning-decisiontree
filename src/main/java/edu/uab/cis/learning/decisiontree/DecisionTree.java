package edu.uab.cis.learning.decisiontree;

import com.google.common.collect.*;

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
  private int nodeCounter;
  private Node _root;
  private Collection<LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE>> trainingData;
  public DecisionTree(Collection<LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE>> trainingData) {
    //Multimap<FEATURE_NAME,FEATURE_VALUE> examples = HashMultimap.create();
    Map<FEATURE_NAME, Multiset<FEATURE_VALUE>> features = new LinkedHashMap<>();
    Table<Integer, FEATURE_NAME, FEATURE_VALUE> examples = HashBasedTable.create();
    List<LABEL> labels = new ArrayList();


    int count = 0;
    for(LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE> lf : trainingData){
      labels.add(lf.getLabel());
      Iterator<FEATURE_NAME> it = lf.getFeatureNames().iterator();
      while(it.hasNext()){
        FEATURE_NAME name = it.next();
        if(!features.containsKey(name)){
          Multiset<FEATURE_VALUE> v = LinkedHashMultiset.create();
          v.add(lf.getFeatureValue(name));
          features.put(name,v);
        } else {
          Multiset<FEATURE_VALUE> v = features.get(name);
          v.add(lf.getFeatureValue(name));
          features.put(name, v);
        }
        examples.put(count, name, lf.getFeatureValue(name));
      }
      count++;
    }
    ID3 id3 = new ID3();
    _root = id3.buildTree(examples, labels, features);
    System.out.println(id3.get_nodeCounter());


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

    System.out.println(_root.getFeature());
    return (LABEL) _root.makeDecision(features, _root.getFeature());
  }

}

