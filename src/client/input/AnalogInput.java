/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.input;

import com.jme3.input.controls.AnalogListener;
import com.jme3.network.Client;
import messages.MovePlayerMessage;

/**
 *
 * @author Joebh
 */
public class AnalogInput implements AnalogListener {

    private Client client;

    public AnalogInput(Client client) {
        this.client = client;
    }
    
    
    
    public void onAnalog(String name, float value, float tpf) {
        if ("up".equals(name)) {
            client.send(new MovePlayerMessage("up"));
        } else if ("down".equals(name)) {
            client.send(new MovePlayerMessage("down"));
        } else if ("left".equals(name)) {
            client.send(new MovePlayerMessage("left"));
        } else if ("right".equals(name)) {
            client.send(new MovePlayerMessage("right"));
        }

    }
}
