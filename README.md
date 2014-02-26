# Assignment: Learning: DecisionTree

The goal of this assignment is to give you some experience implementing a supervised classifier, specifically, a decision tree. It assumes you have already familiarized yourself with Git and Maven in a previous assignment.

## Get a copy of the assignment template

1. Fork the repository https://git.cis.uab.edu/cs-460/learning-decisiontree.

2. Give your instructor access to your fork. **I cannot grade your assignment unless you complete this step.**

## Compile and test the code

1.  Compile the code. Run the following command:

        mvn clean compile

    Everything should compile and you should see a message like:

        [INFO] ------------------------------------------------------------------------
        [INFO] BUILD SUCCESS
        [INFO] ------------------------------------------------------------------------

2.  Test the code. Run the following command:

        mvn clean test

    The tests should fail, and you should see a message like:

        Failed tests:
          testMostFrequentClass(edu.uab.cis.learning.decisiontree.DecisionTreeTest): expected:<1> but was:<null>
          testFullyPredictiveFeature(edu.uab.cis.learning.decisiontree.DecisionTreeTest): expected:<B> but was:<null>

        Tests run: 2, Failures: 2, Errors: 0, Skipped: 0

        [INFO] ------------------------------------------------------------------------
        [INFO] BUILD FAILURE
        [INFO] ------------------------------------------------------------------------

    Note the `clean`, which ensures that Maven alone (not your development environment) is compiling your code.

## Implement your part of the code

Your task is to implement the constructor and `classify` method in `DecisionTree.java`. All your code should go into `DecisionTree.java`, and you should not modify any other files.

In the constructor, a decision tree should be created as follows:

1. If all examples have the same label, a leaf node is created.
2. If no features are remaining, a leaf node is created.
3. Otherwise, the feature F with the highest information gain is identified. A branch node is created where for each possible value V of feature F:  
   1. The subset of examples where F=V is selected.
   2. A decision (sub)tree is recursively created for the selected examples. None of these subtrees nor their descendants are allowed to branch again on feature F.

In `classify`, a prediction for a new example E should be made as follows:

1. For a leaf node where all examples have the same label, that label is returned.
2. For a leaf node where the examples have more than one label, the most frequent label is returned.
3. For a branch node based on a feature F, E is inspected to determine the value V that it has for feature F.
   1. If the branch node has a subtree for V, then example E is recursively classified using the subtree.
   2. If the branch node does not have a subtree for V, then the most frequent label for the examples at the branch node is returned.

## Test your code

1.  Re-run the tests:

        mvn test

    You should now see a message like:

        [INFO] ------------------------------------------------------------------------
        [INFO] BUILD SUCCESS
        [INFO] ------------------------------------------------------------------------

    Your code is now passing the tests that were given to you. This is a good sign, but note that a successful `mvn test` does not guarantee you full credit on an assignment. I will run extra tests on your code when grading it.

## Submit your assignment

1.  To submit your assignment, make sure that you have pushed all of your changes to your repository at `git.cis.uab.edu`.

2.  I will inspect the date of your last push to your `git.cis.uab.edu` repository. If it is after the deadline, your submission will be marked as late. So please **do not push changes to `git.cis.uab.edu` after the assignment deadline** unless you intend to submit a late assignment.
