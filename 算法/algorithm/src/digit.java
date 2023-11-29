import java.util.Random;

public class digit {
    static Random random=new Random();
    public static void main(String[] args) {
      /*  String randomString = usingRandom(100);

        System.out.println(randomString);
        int num= random.nextInt(50);
        System.out.println(num);
        for (int i = 0; i < num; i++) {
            int be= random.nextInt(randomString.length());
            int ed= random.nextInt(be,randomString.length());
            System.out.println(randomString.substring(be,ed));
        }*/
        int x= random.nextInt(1000);
        int l= random.nextInt((int) -Math.pow(10,6), (int) Math.pow(10,6));
        int r= random.nextInt(l, (int) Math.pow(10,9));
        System.out.println(x+" "+l+" "+r);
        for (int i = 0; i < x; i++) {

            int v=random.nextInt((int) -Math.pow(10,4),(int) Math.pow(10,4));
            System.out.print(v+" ");
        }
    }

/*    static String usingRandom(int length) {
        String alphabetsInUpperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String alphabetsInLowerCase = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String teshu="!@#$%^&*()_+=";
        // create a super set of all characters
        String allCharacters = alphabetsInLowerCase + alphabetsInUpperCase + numbers+ teshu;
        // initialize a string to hold result
        StringBuffer randomString = new StringBuffer();
        // loop for 10 times
        for (int i = 0; i < length; i++) {
            // generate a random number between 0 and length of all characters
            int randomIndex = random.nextInt(allCharacters.length());
            // retrieve character at index and add it to result
            randomString.append(allCharacters.charAt(randomIndex));
        }
        return randomString.toString();
    }*/
}