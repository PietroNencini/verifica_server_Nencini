package it.nencini;

import java.net.Socket;
import java.net.ServerSocket;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

public class Main {

    
    public static void main(String[] args) throws Exception{
        System.out.println("Server avviato e in attesa di client...");

        ServerSocket ss = new ServerSocket(3000);
        BufferedReader in;
        DataOutputStream out;
        do {

            
            Socket socket = ss.accept();

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());

            String username = in.readLine();

            if(ServerManager.checkUser(username)) {
                out.writeBytes("UTENTE GIA IN USO\n");
                continue;
            } else {
                out.writeBytes("UTENTE REGISTRATO \n");
                ServerThread t = new ServerThread(socket);
                t.start(); 
            }
        } while(true);

    }

}