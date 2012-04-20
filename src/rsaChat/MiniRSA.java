package rsaChat;

import java.util.ArrayList;
import java.util.Random;

public class MiniRSA {
    public static final int maxSize = 100;
    
    public static int coprime(int x) {
        Random rand = new Random();
        int randNum = rand.nextInt(maxSize);
        while (GCD(randNum, x)!=1){
            randNum = rand.nextInt(maxSize);
        }
        return randNum;
    }

    public static int endecrypt(int msgOrCipher, int key, int c) {
        return modulo(msgOrCipher, key, c);
    }

    public static long GCD(long a, long b) {
        if (a==b) return a;
        else if (a<=1 || b<=1) return 1;
        if (a>b){
            return GCD(a-b*((a%b==0)?(a/b-1):(a/b)), b);
        } else {
            return GCD(a, b-a*((b%a==0)?(b/a-1):(b/a)));
        }
    }
    
    public static int mod_inverse(int a, int m){
        ArrayList<Integer> values = extendedEuclid(a, m);
        if (values.get(0)!=1) return 0;
        if (values.get(1)<0) return values.get(1)+m;
        return values.get(1);
    }

    public static ArrayList<Integer> extendedEuclid(int a, int b){
        ArrayList<Integer> quotient = new ArrayList<Integer>();
        ArrayList<Integer> remainder = new ArrayList<Integer>();
        ArrayList<Integer> x = new ArrayList<Integer>();
        ArrayList<Integer> y = new ArrayList<Integer>();
        quotient.add(0); quotient.add(0);
        remainder.add(Math.max(a, b)); remainder.add(Math.min(a, b));
        x.add(1); x.add(0);
        y.add(0); y.add(1);
        int step = 2;
        while (remainder.get(remainder.size()-1)!=0){
            quotient.add(remainder.get(step-2)/remainder.get(step-1));
            remainder.add(remainder.get(step-2)%remainder.get(step-1));
            x.add(x.get(step-2)-quotient.get(step)*x.get(step-1));
            y.add(y.get(step-2)-quotient.get(step)*y.get(step-1));
            step++;
        }
        ArrayList<Integer> returnList = new ArrayList<Integer>();
        returnList.add(remainder.get(step-2));
        if ((x.get(step-2)*a+y.get(step-2)*b) == remainder.get(step-2)){
            returnList.add(x.get(step-2));
            returnList.add(y.get(step-2));
        } else {
            returnList.add(y.get(step-2));
            returnList.add(x.get(step-2));
        }
        return returnList;
    }
    
    public static long mod_inverse(long base, long m) {
        for (int i=0; i<maxSize; i++){
            if (((base*i)%m)==1) return i;
        }
        return -1;
    }

    public static long totient(long n) {
        return 0;
    }
    
    public static boolean isPrime(int possible){
        int sqrt = (int) Math.ceil(Math.pow((double) possible, 0.5));
        for (int i = 2; i<=sqrt; i++){
            if (possible%i==0) return false;
        }
        return true;
    }
    
    public static int findAPrime(){
        return findAPrime(20, maxSize);
    }
    
    public static int findAPrime(int minSize, int maxSize){
        Random rand = new Random();
        int prime = rand.nextInt(maxSize-minSize)+minSize;
        while (!isPrime(prime)){
            prime = rand.nextInt(maxSize-minSize)+minSize;
        }
        return prime;
    }
    
    public static ArrayList<Boolean> intToBase2(int x){
        ArrayList<Boolean> base2 = new ArrayList<Boolean>();
        while (x>0){
            base2.add((x&1)==1);
            x = x>>1;
        }
        return base2;
    }
    
    public static long pow(int num, int base){
        long pow = 1;
        for (int i=0; i<base; i++){
            pow*=num;
        }
        return pow;
    }
    
    public static int modulo(int a, int d, int n){
        ArrayList<Integer> modValues = new ArrayList<Integer>();
        ArrayList<Boolean> base2 = intToBase2(d);
        modValues.add((int)(pow(a, 1)%n));
        for (int i=1; i<base2.size(); i++){
            modValues.add((int)(pow(modValues.get(i-1), 2)%n));
        }
        long returnValue=1;
        for (int i=0; i<base2.size(); i++){
            if (base2.get(i)) returnValue*=modValues.get(i);
        }
        return (int) (returnValue%n);
    }
    
    public static ArrayList<Integer> makeKey(boolean display){
        int a = findAPrime();
        int b = findAPrime();
        while (b==a) b = findAPrime();
        int c = a*b;
        int m = (a-1)*(b-1);
        int e = findAPrime(2, 50);
        while ((m/e)*e==m) e = findAPrime(2,50);
        int d = mod_inverse(e, m);
        if (d==0) return makeKey(display);
        ArrayList<Integer> returnList = new ArrayList<Integer>();
        returnList.add(e); returnList.add(d); returnList.add(c);
        if (!isValidKey(returnList)) return makeKey(display);
        if (display){
            System.out.println("a=" + a + " b=" + b + " c=" + c + " m=" + m + " e=" + e + " d=" + d);
            System.out.println("public key:" + "(" + e + "," + c + ")" + " private key:" + "(" + d + "," + c + ")");
        }
        return returnList;
    }
    
    public static boolean isValidKey(ArrayList<Integer> key){
        String plainText = "Hello World! How are you?";
        String encrypted = MiniRSA.encryptMessage(plainText, key.get(0), key.get(2));
        String decrypted = MiniRSA.decryptMessage(encrypted, key.get(1), key.get(2));
        return plainText.equals(decrypted);
    }
    
    public static String encryptMessage(String message, int key, int c){
        StringBuffer encryptedMessage = new StringBuffer();
        for (int i=0; i<message.length(); i++){
            int currentLetter = endecrypt(message.charAt(i), key, c);
            encryptedMessage.append(String.valueOf(currentLetter));
            encryptedMessage.append(" ");
        }
        return encryptedMessage.toString().trim();
    }
    
    public static String decryptMessage(String cipher, int key, int c){
        StringBuffer plainText = new StringBuffer();
        String[] split = cipher.split(" ");
        for (int i=0; i<split.length; i++){
            int currentLetter = endecrypt(Integer.valueOf(split[i]), key, c);
            plainText.append(Character.toString((char)currentLetter));
        }
        return plainText.toString();
    }
}
