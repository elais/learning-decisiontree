package edu.uab.cis.learning.decisiontree;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Entropy <LABEL, FEATURE_NAME, FEATURE_VALUE>{
    public Double calculateEntropy(Collection<LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE>> data){
        double entropy = 0;

        if(data.size() == 0){
            //do nothing
            return 0.0;
        }

        double dataSize = data.size();
        Iterator it = data.iterator();
        while(it.hasNext()){
            LabeledFeatures next = it.next();
        }

        return null;

    }

}
