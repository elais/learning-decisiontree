package edu.uab.cis.learning.decisiontree;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class DecisionTreeTest {

  @Test(timeout = 10000)
  public void testNoLabel()
  {
// assemble training data
    List<LabeledFeatures<String, Integer, Integer>> trainingData = new ArrayList<>();
    trainingData.add(LabeledFeatures.ofIntegers("A", 0));
    trainingData.add(LabeledFeatures.ofIntegers("A", 0));
    trainingData.add(LabeledFeatures.ofIntegers("B", 1));
// train the classifier
    DecisionTree<String, Integer, Integer> classifier = new DecisionTree<>(trainingData);
// test that the classifier split on the second feature
    Assert.assertEquals("A", classifier.classify(Features.of(2)));
  }

  @Test(timeout = 10000)
  public void testCorrectFeatureSplit()
  {
// assemble training data
    List<LabeledFeatures<String, Integer, Integer>> trainingData = new ArrayList<>();
    trainingData.add(LabeledFeatures.ofIntegers("A", 1,-1));
    trainingData.add(LabeledFeatures.ofIntegers("A", 1,-1));
    trainingData.add(LabeledFeatures.ofIntegers("A", 0,-1));
    trainingData.add(LabeledFeatures.ofIntegers("B", 0,-1));
    trainingData.add(LabeledFeatures.ofIntegers("B", 0,-1));
    trainingData.add(LabeledFeatures.ofIntegers("B", 1,-1));
    trainingData.add(LabeledFeatures.ofIntegers("C", 1,-2));
    trainingData.add(LabeledFeatures.ofIntegers("C", 1,-2));
// train the classifier
    DecisionTree<String, Integer, Integer> classifier = new DecisionTree<>(trainingData);
// test that the classifier split on the second feature
    Assert.assertEquals("C", classifier.classify(Features.of(0,-2)));
    Assert.assertEquals("A", classifier.classify(Features.of(1,-1)));
    Assert.assertEquals("A", classifier.classify(Features.of(0,-3)));
    Assert.assertEquals("A", classifier.classify(Features.of(-1,2)));
  }

  @Test(timeout = 10000)
  public void testExampleFromSlides()
  {
// assemble training data
    List<LabeledFeatures<String, Integer, Integer>> trainingData = new ArrayList<>();
    trainingData.add(LabeledFeatures.ofIntegers("true",  0, 0));
    trainingData.add(LabeledFeatures.ofIntegers("false", 0,-1));
    trainingData.add(LabeledFeatures.ofIntegers("true",  1,-2));
    trainingData.add(LabeledFeatures.ofIntegers("false", 0,-2));
    trainingData.add(LabeledFeatures.ofIntegers("false", 1,-1));
    trainingData.add(LabeledFeatures.ofIntegers("true",  1, 0));
// train the classifier
    DecisionTree<String, Integer, Integer> classifier = new DecisionTree<>(trainingData);
// test that the classifier split on the second feature
    Assert.assertEquals("true", classifier.classify(Features.of(0,0)));
    Assert.assertEquals("false", classifier.classify(Features.of(0,-2)));
  }

  @Test(timeout = 10000)
  public void testDifferentTypes()
  {
// assemble training data
    List<LabeledFeatures<String, Integer, Object>> trainingData = new ArrayList<>();
    trainingData.add( LabeledFeatures.<String,Object>of("true",  0, "a") );
    trainingData.add( LabeledFeatures.<String,Object>of("false", 0, "b") );
    trainingData.add( LabeledFeatures.<String,Object>of("true",  1, "c") );
    trainingData.add( LabeledFeatures.<String,Object>of("false", 0, "c") );
    trainingData.add( LabeledFeatures.<String,Object>of("false", 1, "b") );
    trainingData.add( LabeledFeatures.<String,Object>of("true",  1, "a") );
// train the classifier
    DecisionTree<String, Integer, Object> classifier = new DecisionTree<>(trainingData);
// test that the classifier split on the second feature
    Assert.assertEquals("true", classifier.classify(Features.<Object>of(0,"a")));
    Assert.assertEquals("false", classifier.classify(Features.<Object>of(0,"c")));
  }

  @Test(timeout = 10000)
  public void testDifferentTypes2()
  {
// assemble training data
    List<LabeledFeatures<String, Integer, Object>> trainingData = new ArrayList<>();
    trainingData.add( LabeledFeatures.<String,Object>of("true",  0.5, "a") );
    trainingData.add( LabeledFeatures.<String,Object>of("false", 0.5, "b") );
    trainingData.add( LabeledFeatures.<String,Object>of("true",  1.4, "c") );
    trainingData.add( LabeledFeatures.<String,Object>of("false", 0.5, "c") );
    trainingData.add( LabeledFeatures.<String,Object>of("false", 1.4, "b") );
    trainingData.add( LabeledFeatures.<String,Object>of("true",  1.4, "a") );
// train the classifier
    DecisionTree<String, Integer, Object> classifier = new DecisionTree<>(trainingData);
// test that the classifier split on the second feature
    Assert.assertEquals("true", classifier.classify(Features.<Object>of(0.5,"a")));
    Assert.assertEquals("false", classifier.classify(Features.<Object>of(0.5,"c")));
  }

  @Test(timeout = 10000)
  public void testSunscreen(){
    // assemble training data
    List<LabeledFeatures<String, Integer, String>> trainingData = new ArrayList<>();
    //Name
    trainingData.add(LabeledFeatures.ofStrings("sunburned", "sarah","blonde", "average", "light", "no"));
    trainingData.add(LabeledFeatures.ofStrings("none", "dana","blonde", "tall", "average", "yes"));
    trainingData.add(LabeledFeatures.ofStrings("none", "alex","brown", "short", "average", "yes"));
    trainingData.add(LabeledFeatures.ofStrings("sunburned", "annie","blonde", "short", "average", "no"));
    trainingData.add(LabeledFeatures.ofStrings("sunburned", "emily","red", "average", "heavy", "no"));
    trainingData.add(LabeledFeatures.ofStrings("none", "pete","brown", "tall", "heavy", "no"));
    trainingData.add(LabeledFeatures.ofStrings("none", "john","brown", "average", "heavy", "no"));
    trainingData.add(LabeledFeatures.ofStrings("none", "katie","blonde", "short", "light", "yes"));
    // train the classifier
    DecisionTree<String, Integer, String> classifier = new DecisionTree<>(trainingData);
    // test that the classifier split on something
    Assert.assertEquals("none", classifier.classify(Features.of("john", "brown", "average", "heavy", "no")));
  }
}
