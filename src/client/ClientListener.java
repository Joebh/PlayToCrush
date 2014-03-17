/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import messages.SendPosition;
import model.Player;

public class ClientListener implements MessageListener<Client> {

    private ClientMain main;
    private Player player;
    
    public ClientListener(ClientMain main) {
        System.out.println("created listener");
        this.main = main;
    }

    public void messageReceived(Client source, Message message) {
         if (message instanceof SendPosition) {
            // do something with the message
            SendPosition sendPosition = (SendPosition) message;
            System.out.println("Client received '" + sendPosition.getX() + " " + sendPosition.getY() + "' from client #" + source.getId());
            player = main.getMyPlayer();
            player.setX(sendPosition.getX());
            player.setY(sendPosition.getY());
        }
    }

}