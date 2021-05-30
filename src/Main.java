
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    private static final int GAP_PENALTY = 2;
    private static final int MISMATCH_PENALTY = 1;

    static int[][] cache;

    public static void main(String[] args) {
        // Read the file lines and store them in the sequences array
        ArrayList<String> sequences = new ArrayList<>();
        File sequencesFile = new File(System.getProperty("user.dir") + "/sequences.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(sequencesFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                sequences.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Take out the 2 sequences from the array and initialize the cache
        String s1 = sequences.get(0);
        String s2 = sequences.get(1);
        cache = new int[s1.length()][s2.length()];
        for (int[] a : cache)
            Arrays.fill(a, -1);
        System.out.println(findAMD(s1, s2));
    }

    private static int findAMD(String message1, String message2) {
        return findAMDAux(message1.toCharArray(), message2.toCharArray(), 0, 0);
    }

    static int findAMDAux(char[] characters1, char[] characters2, int pointer1, int pointer2) {
        if (pointer1 == characters1.length) // first message finished
            return (characters2.length - pointer2) * GAP_PENALTY; // return the rest of message2 length as gap penalties

        if (pointer2 == characters2.length) // second message finished
            return (characters1.length - pointer1) * GAP_PENALTY; // return the rest of message2 length as gap penalties

        int cached = getFromCache(characters1.length - pointer1, characters2.length - pointer2);
        if (cached != -1)
            return cached;

        int letterCost = characters1[pointer1] == characters2[pointer2] ? 0 : MISMATCH_PENALTY;


        int amd = Math.min(letterCost + findAMDAux(characters1, characters2, pointer1 + 1, pointer2 + 1), // no gaps
                Math.min(GAP_PENALTY + findAMDAux(characters1, characters2, pointer1 + 1, pointer2), // gap in message 2
                        GAP_PENALTY + findAMDAux(characters1, characters2, pointer1, pointer2 + 1))); // gap in message 1

        cache[characters1.length - pointer1 - 1][characters2.length - pointer2 - 1] = amd; // cache the result
        return amd;
    }

    private static Integer getFromCache(int s1, int s2) {
        return cache[s1 - 1][s2 - 1];
    }
}