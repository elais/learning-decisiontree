package edu.uab.cis.learning.decisiontree;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class DecisionTreeTest {

  @Test
  public void testMostFrequentClass() {
    // assemble training data
    List<LabeledFeatures<Integer, Integer, String>> trainingData = new ArrayList<>();
    trainingData.add(LabeledFeatures.ofStrings(0));
    trainingData.add(LabeledFeatures.ofStrings(1));
    trainingData.add(LabeledFeatures.ofStrings(1));
    // train the classifier
    DecisionTree<Integer, Integer, String> classifier = new DecisionTree<>(trainingData);
    // test the classifier
    Features<Integer, String> testDatum = Features.of();
    Assert.assertEquals(Integer.valueOf(1), classifier.classify(testDatum));
  }

  @Test
  public void testSingleSplit() {
    // assemble training data
    List<LabeledFeatures<String, Integer, Integer>> trainingData = new ArrayList<>();
    trainingData.add(LabeledFeatures.ofIntegers("A", 0, 0));
    trainingData.add(LabeledFeatures.ofIntegers("A", 1, 0));
    trainingData.add(LabeledFeatures.ofIntegers("B", 0, 1));
    // train the classifier
    DecisionTree<String, Integer, Integer> classifier = new DecisionTree<>(trainingData);
    // test on the training data
    Assert.assertEquals("A", classifier.classify(Features.of(0, 0)));
    Assert.assertEquals("A", classifier.classify(Features.of(1, 0)));
    Assert.assertEquals("B", classifier.classify(Features.of(0, 1)));
    // test that the classifier split on the second feature
    Assert.assertEquals("B", classifier.classify(Features.of(1, 1)));
  }
}
