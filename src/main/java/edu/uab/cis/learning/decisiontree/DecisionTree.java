package edu.uab.cis.learning.decisiontree;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.TreeSet;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.SetMultimap;
import com.google.common.math.DoubleMath;

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

  /* 
   * Formulas:
   * Entropy:
   * H(X) = - Sum(x E X) P(x) log2 P(x)
   *
   * Specific Conditional Entropy
   * H(Y | X = x) = - Sum(y E Y) P(y|x) log2 P(y|x)
   *
   * Conditional Entroypy
   * H(Y|X) = Sum(x E X) P(X=x)H(Y|X=x)
   *
   * Informtion Gain
   * IG(Y|X) = H(Y) - H(Y|X)
   *
   */
  public boolean leaf;
  public FEATURE_NAME feature_pick;
  public LABEL label_pick;
  public Map<FEATURE_VALUE, DecisionTree<LABEL, FEATURE_NAME, FEATURE_VALUE>> child_nodes;

  public DecisionTree(Collection<LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE>> trainingData)
  {
    this(trainingData, new TreeSet<FEATURE_NAME>());
  }

  private DecisionTree(Collection<LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE>> trainingData, Set<FEATURE_NAME> ufeatures)
  {

    child_nodes = new LinkedHashMap<>();
    double trainSize = trainingData.size();

    // Get the features
    List<FEATURE_NAME> features = new ArrayList<>();
    for(LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE> lf : trainingData)
    {
      for(FEATURE_NAME name : lf.getFeatureNames())
      {
        features.add(name);
      }
    }
    // remove features already visted
    features.removeAll(ufeatures);

    // Map labels to their counts
    Multiset<LABEL> labelcount = HashMultiset.create();
    for(LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE> lf : trainingData)
    {
      labelcount.add(lf.getLabel());
    }

    //Return max label if we can't go any futher than the parent node
    double max = Double.NEGATIVE_INFINITY;
    LABEL max_label = null;
    for (LABEL label : labelcount.elementSet())
    {
      if (labelcount.count(label) > max)
      {
        max = labelcount.count(label);
        max_label = label;
      }
    }
    label_pick = max_label;

    // If all examples have the same label or no features remain, a leaf node is created.
    if(labelcount.size() == 1 || features.size() == 0)
    {
      leaf = true;
      return; // exit method
    }
    else
    {
      leaf = false;
    }

    double label_entropy = 0.0;   // H(L)
    for(LABEL l : labelcount.elementSet())
    {
      double p_x = labelcount.count(l) / trainSize;
      label_entropy -= p_x * DoubleMath.log2(p_x);
    }

    feature_pick = features.get(0);
    max = Double.NEGATIVE_INFINITY;
    for(FEATURE_NAME f : features)
    {
      double entropy = 0.0;
      // Map the feature to its value and label counts 
      Map<FEATURE_VALUE, Multiset<LABEL>> fvalue_map = new LinkedHashMap<>();
      for(LabeledFeatures<LABEL,FEATURE_NAME,FEATURE_VALUE> lf : trainingData)
      {
        FEATURE_VALUE v = lf.getFeatureValue(f);
        if(!fvalue_map.containsKey(v))
        {
          Multiset<LABEL> counts = HashMultiset.create();
          fvalue_map.put(lf.getFeatureValue(f), counts );
        }
        fvalue_map.get(v).add(lf.getLabel());
      }
      // Calulate specific conditonal entropy: H(Label|Feature=value) 
      // and conditonal entory: H(Label|Feature)
      for(FEATURE_VALUE val : fvalue_map.keySet() )
      {
        double entropy2 = 0.0;
        double sumL = 0.0;
        // Sum labels for each feature_val (used for the probability caluation)
        if (fvalue_map.containsKey(val))
        {
          for(LABEL l : fvalue_map.get(val).elementSet() )
          {
            sumL += (double) fvalue_map.get(val).count(l);
          }
        }
        for(LABEL l : fvalue_map.get(val).elementSet() )
        {
          double p_x = ( (double) fvalue_map.get(val).count(l) ) / sumL;
          entropy2 -= p_x * DoubleMath.log2(p_x);   // H(L|F=v)
        }

        entropy += ((sumL/trainSize) * entropy2);  // H(L|F)
      }
      // Info gain: H(L) - H(L|F)
      if( (label_entropy - entropy) > max)
      {
        feature_pick = f;
        max = label_entropy - entropy;
      }
    }

    // Split features on the feature we picked (highest gain)
    SetMultimap<FEATURE_VALUE, LabeledFeatures<LABEL,FEATURE_NAME,FEATURE_VALUE>> split_features2 = HashMultimap.create();
    for( LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE> lf : trainingData )
    {
      split_features2.put(lf.getFeatureValue(feature_pick), lf);
    }

    // call decision tree on its children
    ufeatures.add(feature_pick);
    for (FEATURE_VALUE v : split_features2.keySet())
    {
      child_nodes.put(v, new DecisionTree<>(split_features2.get(v), new HashSet<FEATURE_NAME>(ufeatures)));
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
    FEATURE_VALUE pick = features.getFeatureValue(feature_pick);
    if(leaf)
    {
      return label_pick;
    }
    if (features.getFeatureNames().contains(feature_pick) && child_nodes.containsKey(pick))
    {
      return child_nodes.get(pick).classify(features);
    }
    else
    {
      return label_pick;
    }
  }
}
