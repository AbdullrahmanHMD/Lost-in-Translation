public class Triplet {
    private String message1, message2;
    private int amd;

    public Triplet(String message1, String message2, int amd) {
        this.message1 = message1;
        this.message2 = message2;
        this.amd = amd;
    }

    public String getMessage1() {
        return message1;
    }

    public String getMessage2() {
        return message2;
    }

    public int getAmd() {
        return amd;
    }
}
