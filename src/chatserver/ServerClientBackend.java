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
import message.ChatMessage;

/**
 *
 * @author Ohjelmistokehitys
 */
public class ServerClientBackend implements Runnable {

    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public ServerClientBackend(Socket socket) {
        this.socket = socket;
    }
    
    @Override
    public void run() {
        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            
            // Wait for data
            while (true) {
                ChatMessage cm = (ChatMessage)input.readObject();
                ChatServer.broadcastMessage(cm);
                //System.out.println(m.getChatMessage());
            }

        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
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
