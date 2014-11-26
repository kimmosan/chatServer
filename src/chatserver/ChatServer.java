/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import message.ChatMessage;

/**
 *
 * @author Ohjelmistokehitys
 */
public class ChatServer {

    static ArrayList<ServerClientBackend> clients = new ArrayList();
    
    public static void main(String[] args) {
        try {
            // Start the server to listen port 3010
            ServerSocket server = new ServerSocket(3013); 
            
            while(true) {
                Socket temp = server.accept();
                ServerClientBackend client = new ServerClientBackend(temp);
                clients.add(client);
                Thread t = new Thread(client);
                t.setDaemon(true);
                t.start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void broadcastMessage(ChatMessage cm) {
        for (ServerClientBackend client: clients) {
            client.sendMessage(cm);
        }
    }
    
}
