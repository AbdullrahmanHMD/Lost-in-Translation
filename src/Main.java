
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    private static final int GAP_PENALTY = 2;
    private static final int MISMATCH_PENALTY = 1;

    static char[] characters1;
    static char[] characters2;

    static int[][] amdCache;
    static String[][] message1Cache;
    static String[][] message2Cache;

    public static void main(String[] args) {
        ArrayList<String> sequences = new ArrayList<>();
        File sequencesFile = new File(System.getProperty("user.dir") + "/input1.txt");
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
        amdCache = new int[s1.length()][s2.length()];
        message1Cache = new String[s1.length()][s2.length()];
        message2Cache = new String[s1.length()][s2.length()];
        for (int i = 0; i < amdCache.length; i++) {
            Arrays.fill(amdCache[i], -1);
            Arrays.fill(message1Cache[i], null);
            Arrays.fill(message1Cache[i], null);
        }
        long startTime = System.currentTimeMillis();
        Object[] amdResult = findAMD(s1, s2);
        long elapsedTime = System.currentTimeMillis() - startTime;
        String result = String.format("Minimum penalty: %d\nMessage 1:\n\t%s\nMessage 2:\n\t%s\n\nElapsed Time: %d ms\n",
                (int) amdResult[2],
                amdResult[0],
                amdResult[1],
                elapsedTime);

        System.out.println(result);
    }

    private static Object[] findAMD(String message1, String message2) {
        characters1 = message1.toCharArray();
        characters2 = message2.toCharArray();
        return findAMDAux(0, 0);
    }

    static Object[] findAMDAux(int pointer1, int pointer2) {
        if (pointer1 == characters1.length) // first message finished
            return new Object[]{getGapsString(characters2.length - pointer2), getStringFromCharacters(characters2, pointer2), (characters2.length - pointer2) * GAP_PENALTY}; // return the rest of message2 length as gap penalties

        if (pointer2 == characters2.length) // second message finished
            return new Object[]{getStringFromCharacters(characters1, pointer1), getGapsString(characters1.length - pointer1), (characters1.length - pointer1) * GAP_PENALTY}; // return the rest of message2 length as gap penalties

        Object[] cached = getFromCache(characters1.length - pointer1, characters2.length - pointer2);
        if (cached != null)
            return cached;

        int noGapCost = characters1[pointer1] == characters2[pointer2] ? 0 : MISMATCH_PENALTY;

        Object[] noGap = findAMDAux(pointer1 + 1, pointer2 + 1);
        Object[] gapInFirst = findAMDAux(pointer1, pointer2 + 1);
        Object[] gapInSecond = findAMDAux(pointer1 + 1, pointer2);

        int amdCost = Math.min(noGapCost + (int) noGap[2], // no gaps
                Math.min(GAP_PENALTY + (int) gapInSecond[2], // gap in message 2
                        GAP_PENALTY + (int) gapInFirst[2])); // gap in message 1

        String amdMessage1 = null;
        String amdMessage2 = null;

        if (amdCost == ((int) noGap[2] + noGapCost)) {
            amdMessage1 = characters1[pointer1] + (String) noGap[0];
            amdMessage2 = characters2[pointer2] + (String) noGap[1];
        } else if (amdCost == ((int) gapInFirst[2] + GAP_PENALTY)) {
            amdMessage1 = "_" + gapInFirst[0];
            amdMessage2 = characters2[pointer2] + (String) gapInFirst[1];
        } else if (amdCost == ((int) gapInSecond[2] + GAP_PENALTY)) {
            amdMessage1 = characters1[pointer1] + (String) gapInSecond[0];
            amdMessage2 = "_" + gapInSecond[1];
        }


        amdCache[characters1.length - pointer1 - 1][characters2.length - pointer2 - 1] = amdCost; // cache the result
        message1Cache[characters1.length - pointer1 - 1][characters2.length - pointer2 - 1] = amdMessage1; // cache the result
        message2Cache[characters1.length - pointer1 - 1][characters2.length - pointer2 - 1] = amdMessage2; // cache the result
        return new Object[]{amdMessage1, amdMessage2, amdCost};
    }

    private static Object[] getFromCache(int s1, int s2) {
        int result = amdCache[s1 - 1][s2 - 1];
        if (result != -1)
            return new Object[]{message1Cache[s1 - 1][s2 - 1], message2Cache[s1 - 1][s2 - 1], result};
        return null;
    }

    static String getGapsString(int numOfGaps) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < numOfGaps; i++)
            builder.append("_");
        return builder.toString();
    }

    static String getStringFromCharacters(char[] characters, int pointer) {
        StringBuilder builder = new StringBuilder();
        for (int i = pointer; i < characters.length; i++) {
            builder.append(characters[i]);
        }
        return builder.toString();
    }
}
