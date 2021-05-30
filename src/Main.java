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
        String s1 = "ZBDBFZZBDD";
        String s2 = "ZBBFFZDB";
        amdCache = new int[s1.length()][s2.length()];
        message1Cache = new String[s1.length()][s2.length()];
        message2Cache = new String[s1.length()][s2.length()];
        for (int i = 0; i < amdCache.length; i++) {
            Arrays.fill(amdCache[i], -1);
            Arrays.fill(message1Cache[i], null);
            Arrays.fill(message1Cache[i], null);
        }
        Triplet amdResult = findAMD(s1, s2);

        String result = String.format("Minimum penalty: %d\nMessage 1:\n\t%s\nMessage 2:\n\t%s\n",
                amdResult.getAmd(),
                amdResult.getMessage1(),
                amdResult.getMessage2());

        System.out.println(result);
    }

    private static Triplet findAMD(String message1, String message2) {
        characters1 = message1.toCharArray();
        characters2 = message2.toCharArray();
        return findAMDAux(0, 0);
    }

    static Triplet findAMDAux(int pointer1, int pointer2) {
        if (pointer1 == characters1.length) // first message finished
            return new Triplet(getGapsString(characters2.length - pointer2), getStringFromCharacters(characters2, pointer2), (characters2.length - pointer2) * GAP_PENALTY); // return the rest of message2 length as gap penalties

        if (pointer2 == characters2.length) // second message finished
            return new Triplet(getStringFromCharacters(characters1, pointer1), getGapsString(characters1.length - pointer1), (characters1.length - pointer1) * GAP_PENALTY); // return the rest of message2 length as gap penalties

        Triplet cached = getFromCache(characters1.length - pointer1, characters2.length - pointer2);
        if (cached != null)
            return cached;

        int noGapCost = characters1[pointer1] == characters2[pointer2] ? 0 : MISMATCH_PENALTY;

        Triplet noGap = findAMDAux(pointer1 + 1, pointer2 + 1);
        Triplet gapInFirst = findAMDAux(pointer1, pointer2 + 1);
        Triplet gapInSecond = findAMDAux(pointer1 + 1, pointer2);

        int amdCost = Math.min(noGapCost + noGap.getAmd(), // no gaps
                Math.min(GAP_PENALTY + gapInSecond.getAmd(), // gap in message 2
                        GAP_PENALTY + gapInFirst.getAmd())); // gap in message 1

        String amdMessage1 = null;
        String amdMessage2 = null;

        if (amdCost == (noGap.getAmd() + noGapCost)) {
            amdMessage1 = characters1[pointer1] + noGap.getMessage1();
            amdMessage2 = characters2[pointer2] + noGap.getMessage2();
        } else if (amdCost == (gapInFirst.getAmd() + GAP_PENALTY)) {
            amdMessage1 = "_" + gapInFirst.getMessage1();
            amdMessage2 = characters2[pointer2] + gapInFirst.getMessage2();
        } else if (amdCost == (gapInSecond.getAmd() + GAP_PENALTY)) {
            amdMessage1 = characters1[pointer1] + gapInSecond.getMessage1();
            amdMessage2 = "_" + gapInSecond.getMessage2();
        }


        amdCache[characters1.length - pointer1 - 1][characters2.length - pointer2 - 1] = amdCost; // cache the result
        message1Cache[characters1.length - pointer1 - 1][characters2.length - pointer2 - 1] = amdMessage1; // cache the result
        message2Cache[characters1.length - pointer1 - 1][characters2.length - pointer2 - 1] = amdMessage2; // cache the result
        return new Triplet(amdMessage1, amdMessage2, amdCost);
    }

    private static Triplet getFromCache(int s1, int s2) {
        int result = amdCache[s1 - 1][s2 - 1];
        if (result != -1)
            return new Triplet(message1Cache[s1 - 1][s2 - 1], message2Cache[s1 - 1][s2 - 1], result);
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