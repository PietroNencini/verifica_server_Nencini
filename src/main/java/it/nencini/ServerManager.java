package it.nencini;
import java.util.ArrayList;
public class ServerManager {

    private static ArrayList<String> users = new ArrayList<String>();

    private static ArrayList<ServerThread> threads = new ArrayList<ServerThread>();

    public static boolean checkUser(String username) {
        if(users.contains(username)) {
            return true;
        } else {
            users.add(username);
            return false;
        }   
    }

    public static synchronized void sendGlobalNote(String new_note) {
        for(ServerThread t : threads) {
            t.addNote(new_note);
        }
    }

}
