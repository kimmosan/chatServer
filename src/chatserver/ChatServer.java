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
import java.util.logging.Level;
import java.util.logging.Logger;
import message.ChatMessage;

/**
 *
 * @author Ohjelmistokehitys
 */
public class ChatServer {

    static ArrayList<ServerClientBackend> clients = new ArrayList();
    static ArrayList<String> usernames = new ArrayList();
    
    public static void main(String[] args) {
        try {
            // Start the server to listen port 3010
            ServerSocket server = new ServerSocket(3030); 
            
            while(true) {
                Socket temp = server.accept();
                ServerClientBackend client = new ServerClientBackend(temp);
                clients.add(client);
                usernames.add("Anonymous");
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
    
    public static void sendPrivateMessage(String name, ChatMessage cm) {
        for (ServerClientBackend client: clients) {
            if (client.getUsername().equalsIgnoreCase(name)) {
                System.out.println("Private message receiver: " + client.getUsername());
                client.sendMessage(cm);
                break;
            }
        }
       
    }
    
    public static void removeUser(ServerClientBackend c) {
        int index = clients.indexOf(c);
        clients.remove(c);
        usernames.remove(index);
        System.out.println("User in " + index + " removed");
        sendUserList();
    }
    
    public static void changeUsername(ServerClientBackend c, String username) {
        int index = clients.indexOf(c);
        usernames.set(index, username);
        sendUserList();
    }
    
    public static void sendUserList() {
        ChatMessage cm = new ChatMessage();
        cm.setUserListUpdate(true);
        cm.setChatMessage("");
        for (String name : usernames) {
            cm.setChatMessage(cm.getChatMessage() + name + ";");
        }
        for (ServerClientBackend client: clients) {
            client.sendMessage(cm);
        }
    }
    
}
