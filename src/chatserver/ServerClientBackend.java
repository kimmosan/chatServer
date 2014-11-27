/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.ChatMessage;

/**
 *
 * @author Ohjelmistokehitys
 */
public class ServerClientBackend implements Runnable {

    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String username = "Anonymous";

    public String getUsername() {
        return username;
    }

    public ServerClientBackend(Socket socket) {
        this.socket = socket;
    }
    
    @Override
    public void run() {
        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            ChatServer.sendUserList();
            // Wait for data
            while (socket != null) {
                ChatMessage cm = (ChatMessage)input.readObject();
                if (cm.isUsernameChange()) {
                    username = cm.getUsername();
                    ChatServer.changeUsername(this, username);
                }
                else if (cm.isIsPrivate()) {
                    cm.setUsername(username);
                    ChatServer.sendPrivateMessage(cm.getPrivateName(), cm);
                    sendMessage(cm);
                }
                else {
                    cm.setUsername(username);
                    ChatServer.broadcastMessage(cm);
                }
            }

        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            System.out.println("ex");
            
        } finally {
            try {
                System.out.println("User " + username + " left the chat...");
                input.close();
                output.close();
                ChatServer.removeUser(this);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public void sendMessage(ChatMessage cm) {
        try {
            output.writeObject(cm);
            output.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
}
