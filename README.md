

  <h1 align="center">Lost in Translation</h1>

  <p align="center">
    A project for the <strong>Advanced Algorithms and Complexity </strong> done by Walid, AbdulRahman, Saria, and Noor
    <br />
  </p>
</p>

## Running the project

To run the project you can simply add the folder to your IDE, navigate to the /src folder, and run the Main.java file.

To run the project from the terminal however you can navigate to the /src folder and run 

  ```sh
  javac Main.java
  java Main.class
  ```
  
## Changing the test case 

In order to change the text cases between the 3 files we were provided simply navigate to line number 18

``` java
File sequencesFile = new File(System.getProperty("user.dir") + "/input2.txt");
```

and swap out the input2.txt for input1.txt (shortest sequence), or input2.txt (longest sequence)

## Test cases results

For each of the test cases we have an output the minimum penalty and the elapsed time it took to compute that penalty.

input1.txt test case: 
```
AMD Penalty: 6
Elapsed Time: 0 ms
```

input2.txt test case: 
```
AMD Penalty: 758
Elapsed Time: 67 ms
```

input3.txt test case: 
```
AMD Penalty: 160
Elapsed Time: 1060 ms
```

## Initial Approaches

<Strong>Greedy: </Strong>We tried to find a greedy solution, however for all the heuristics we came up with, some local optimums are not necessarily a part of the optimal solution, therefore this approach will not work.

<Strong>Brute Force: </Strong>We simply wrote a recursive function that will find all the possible alignments of the messages and pick the alignment with the lowest penalty out of all of them.

``` py
AMD(message1, message2):
    Min(noGapCost + AMD(message1.substring(1), message2.subString(1)),
        gapCost + AMD(message1.subString(1), message2),
        gapCost + AMD(message1, message2.subString(1)))
```
This solution is correct, however it has exponential time complexity and took over 20 minutes to run for the medium input (input2.txt).

## Improvements

We found that these subproblems will be duplicated many times, and we can use memoization to improve the runtime complexity. Particularly, cache the solutions of unique string inputs.

``` py
AMD(message1, message2):
    if(cached[message1, message2]):
        return cached[message1, message2]

    Min(noGapCost + AMD(message1.substring(1), message2.subString(1)),
        gapCost + AMD(message1.subString(1), message2),
        gapCost + AMD(message1, message2.subString(1)))

    cached[message1, message2] = amd
    return amd
```

This way the time complexity will be 

1. Number of unique subproblems: all combinations of substrings from the ith character to the end of the 2 inputs = O(N^2)
2. Runtime per unique subproblem: O(N) Since taking the substring is O(N) in java.

Additionally, using the strings as cache keys results in heap overflow for large inputs.

## Final Solution

1. Treat strings as character arrays, use pointers to mimic substring. Moving the pointer once to the right is equivalent to taking a substring starting from the second letter. This way we can eliminate the runtime per unique sub-problem to O(1) making our solution O(N^2).

2. Use the lengths of the strings as cache keys instead of the strings themselves because input lengths are unique. This is because we always take the substring from a certain character to the end of the string, so given a length of a string we can uniquely identify the sequence inside it from the full sequence.

``` py
mChars1 = Message1.characters
mChars2 = Message2.characters

AMD(pointer1, pointer2):
    length1 = mChars1.length - pointer1
    length2 = mChars2.length - pointer2

    if(cached[length1, length2]):
        return cached[length1, length2]

    Min(noGapCost + AMD(pointer1 + 1, pointer2 + 1),
        gapCost + AMD(pointer1 + 1, pointer2),
        gapCost + AMD(pointer1, pointer2 + 1))

    cached[length1, length2] = amd
    return amd
```

