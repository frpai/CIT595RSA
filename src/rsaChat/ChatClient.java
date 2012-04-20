package rsaChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ChatClient {
    public static void main(String[] args){
        try {
            Socket client = new Socket(args[0], Integer.valueOf(args[1]));
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            ArrayList<Integer> selfKeys = MiniRSA.makeKey(true);
            String ourKeyMessage = String.valueOf(selfKeys.get(0)) + " " + String.valueOf(selfKeys.get(2));
            String theirKey = in.readLine();
            out.println(ourKeyMessage);
            int theirE = Integer.valueOf(theirKey.split(" ")[0]);
            int theirC = Integer.valueOf(theirKey.split(" ")[1]);
            Thread listener = new Thread(new Listener(in, selfKeys));
            listener.start();
            BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
            String userInput;
            while((userInput=stdin.readLine())!=null){
                if (".shutdown".equals(userInput)){
                    break;
                }
                String encrypted = MiniRSA.encryptMessage(userInput, theirE, theirC);
                System.out.println("sending encrypted... " + encrypted);
                out.println(encrypted);
            }
            client.close();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

}
