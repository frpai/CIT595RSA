package rsaChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {
    static int port = 5050;
    
    public static void main(String[] args){
        if (args.length>0) port = Integer.valueOf(args[0]);
        new ChatServer();
    }
    
    public ChatServer(){
        try {
            startServer();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void startServer() throws IOException{
        ServerSocket ss = new ServerSocket(port);
        Socket s = ss.accept();
        ArrayList<Integer> selfKeys = MiniRSA.makeKey(true);
        PrintWriter out = new PrintWriter(s.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        String ourKeyMessage = String.valueOf(selfKeys.get(0)) + " " + String.valueOf(selfKeys.get(2));
        out.println(ourKeyMessage);
        String theirKey = in.readLine();
        int theirE = Integer.valueOf(theirKey.split(" ")[0]);
        int theirC = Integer.valueOf(theirKey.split(" ")[1]);
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        Thread listener = new Thread(new Listener(in, selfKeys));
        listener.start();
        String userInput;
        while((userInput=stdin.readLine())!=null){
            if (".shutdown".equals(userInput)){
                break;
            }
            String encrypted = MiniRSA.encryptMessage(userInput, theirE, theirC);
            System.out.println("sending encrypted... " + encrypted);
            out.println(encrypted);
        }
        s.close();
        ss.close();
    }
}