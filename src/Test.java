public class Test {
    public static void main(String[] args) {
        String w = "word";
        System.out.println(getStringFromCharacters(w.toCharArray(), 4));
    }

    static String getStringFromCharacters(char[] characters, int pointer) {
        StringBuilder builder = new StringBuilder();
        for (int i = pointer; i < characters.length; i++) {
            builder.append(characters[i]);
        }
        return builder.toString();
    }
}
