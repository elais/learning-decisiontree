package edu.uab.cis.learning.decisiontree;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;

public class Features<FEATURE_NAME, FEATURE_VALUE> {

  private Map<FEATURE_NAME, FEATURE_VALUE> features;

  public Features(Map<FEATURE_NAME, FEATURE_VALUE> features) {
    this.features = features;
  }

  @SafeVarargs
  public static <FEATURE_VALUE_TYPE> Features<Integer, FEATURE_VALUE_TYPE> of(
      FEATURE_VALUE_TYPE... featureValues) {
    Map<Integer, FEATURE_VALUE_TYPE> features = Maps.newHashMap();
    for (int i = 0; i < featureValues.length; ++i) {
      features.put(i, featureValues[i]);
    }
    return new Features<Integer, FEATURE_VALUE_TYPE>(features);
  }

  public Set<FEATURE_NAME> getFeatureNames() {
    return Collections.unmodifiableSet(this.features.keySet());
  }

  public FEATURE_VALUE getFeatureValue(FEATURE_NAME featureName) {
    return this.features.get(featureName);
  }
}
