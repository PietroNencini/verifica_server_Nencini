package it.nencini;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;

public class ServerThread extends Thread {

    private static int id_counter = 0;
    private int thread_id;
    private Socket socket;
    ArrayList<String> clientNotes = new ArrayList<String>();


    public ServerThread(Socket socket) {
        this.socket = socket;
        this.thread_id = id_counter;
        id_counter++;
        System.out.println("Thread creato: " + thread_id);
    }

    private String getNotes() {
        String output = "";
        if(this.clientNotes.isEmpty())
            return "LISTA VUOTA";
        for(String note : this.clientNotes) {
            output += note + "\n";
        }
        output += "@\n";
        return output;
    }

    private void searchAndDelete(String target) {
        target.replaceFirst("*", "");
        for(String note : this.clientNotes) {
            if(note.contains(target))
                this.clientNotes.remove(note);
        }
    }

    public void addNote(String note) {
        this.clientNotes.add(note);
    }

    public void run() {
        
        // prefisso * --> nota da eliminare
        // prefisso ^ --> nota da inviare a tutti i client
        // stringa: ! chiudi connessione
        // stringa: ? chiedi tutte le note


        try {
            
            BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            DataOutputStream out = new DataOutputStream(this.socket.getOutputStream());
            do {
                String received_data = in.readLine();       // Non so perché ma la prima nota inviata è giusta, la seconda volta è sempre null
                System.out.println("Stringa ricevuta: " + received_data);
                if(received_data.equals("!")) {
                    break;
                } else if(received_data.equals("?")) {
                    String notes = getNotes();
                    out.writeBytes(notes + "\n");
                    break;
                } else if(received_data.startsWith("*")) {
                    searchAndDelete(received_data);
                    out.writeBytes("La stringa " + received_data + ", se esisteva tra le note, è stata eliminata \n");
                } else if(received_data.startsWith("^")) {
                    String sending = received_data.replace("^", "");
                    ServerManager.sendGlobalNote(sending);
                    out.writeBytes("OK \n");
                } else {
                    out.writeBytes("OK \n");
                    addNote(received_data);
                }

            } while(true);

            System.out.println("TERMINE COMUNCAZIONE");

        } catch (IOException e) {
            System.out.println("ERRORE NELLA COMUNICAZIONE");
        }
    
    }

}
