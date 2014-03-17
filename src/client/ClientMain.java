/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import client.input.AnalogInput;
import client.input.HandleInput;
import model.Player;
import com.jme3.app.SimpleApplication;
import com.jme3.input.InputManager;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.Message;
import com.jme3.network.Network;
import com.jme3.network.serializing.Serializer;
import com.jme3.renderer.RenderManager;
import com.jme3.system.JmeContext;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import messages.MovePlayerMessage;
import messages.RequestPosition;
import messages.SendPosition;
import server.ServerListener;

/**
 *
 * @author Joebh
 */
public class ClientMain extends SimpleApplication implements ClientStateListener {

    private Client myClient;
    private Player myPlayer;

//    public static void main(String[] args) {
//        ClientMain app = new ClientMain();
//        app.start(JmeContext.Type.Display); // standard display type
//    }
    @Override
    public void destroy() {
        myClient.close();
        super.destroy();
    }

    @Override
    public void simpleInitApp() {
        try {
            myPlayer = new Player();
            
            
            Serializer.registerClass(MovePlayerMessage.class);
            Serializer.registerClass(SendPosition.class);
            Serializer.registerClass(RequestPosition.class);
            myClient = Network.connectToServer("localhost", 6143);
            
            InputManager input = this.getInputManager();
            new HandleInput(input);
            input.addListener(new AnalogInput(myClient), new String[]{"up", "down", "left", "right"});

            myClient.addMessageListener(new ClientListener(this), SendPosition.class);
            myClient.addClientStateListener(this);


            myClient.start();

            Message m = new MovePlayerMessage("up");
            myClient.send(m);
        } catch (IOException ex) {
            Logger.getLogger(ClientMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void simpleUpdate(float tpf) {
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    public Player getMyPlayer() {
        return myPlayer;
    }

    public void setMyPlayer(Player myPlayer) {
        this.myPlayer = myPlayer;
    }

    public void clientConnected(Client c) {
        System.out.println("Client #" + c.getId() + " is ready.");

        
        //send request location from server
        c.send(new RequestPosition());
        
        
    }

    public void clientDisconnected(Client c, DisconnectInfo info) {
        System.out.println("Client #" + c.getId() + " has left.");
    }
}
