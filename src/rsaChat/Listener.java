package rsaChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class Listener implements Runnable{
    BufferedReader reader;
    ArrayList<Integer> keys;
    
    public Listener(BufferedReader reader, ArrayList<Integer> keys){
        this.reader = reader;
        this.keys = keys;
    }
    
    @Override
    public void run() {
        String input;
        try {
            while ((input=reader.readLine())!=null){
                String decrypted = MiniRSA.decryptMessage(input.trim(), keys.get(1), keys.get(2));
                System.out.println("received: " + input);
                System.out.println("decrypted: " + decrypted);
            }
        } catch (IOException e) {
            //do nothing
        }
        System.out.println("Shutdowned");
    }
}
