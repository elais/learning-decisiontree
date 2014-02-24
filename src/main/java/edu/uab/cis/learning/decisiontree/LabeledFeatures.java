package edu.uab.cis.learning.decisiontree;

import java.util.Map;

import com.google.common.collect.Maps;

public class LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE>
    extends
    Features<FEATURE_NAME, FEATURE_VALUE> {

  private LABEL label;

  public LabeledFeatures(LABEL label, Map<FEATURE_NAME, FEATURE_VALUE> features) {
    super(features);
    this.label = label;
  }

  @SafeVarargs
  public static
      <LABEL_TYPE, FEATURE_VALUE_TYPE>
      LabeledFeatures<LABEL_TYPE, Integer, FEATURE_VALUE_TYPE>
      of(LABEL_TYPE label, FEATURE_VALUE_TYPE... featureValues) {
    Map<Integer, FEATURE_VALUE_TYPE> features = Maps.newHashMap();
    for (int i = 0; i < featureValues.length; ++i) {
      features.put(i, featureValues[i]);
    }
    return new LabeledFeatures<LABEL_TYPE, Integer, FEATURE_VALUE_TYPE>(label, features);
  }

  public static <LABEL_TYPE> LabeledFeatures<LABEL_TYPE, Integer, String> ofStrings(
      LABEL_TYPE label,
      String... featureValues) {
    return LabeledFeatures.of(label, featureValues);
  }

  public static <LABEL_TYPE> LabeledFeatures<LABEL_TYPE, Integer, Integer> ofIntegers(
      LABEL_TYPE label,
      Integer... featureValues) {
    return LabeledFeatures.of(label, featureValues);
  }

  public LABEL getLabel() {
    return this.label;
  }
}
