package messages;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author Joebh
 */
@Serializable
public class MovePlayerMessage extends AbstractMessage{

    private String direction;
    
    public MovePlayerMessage() {
    }

    public MovePlayerMessage(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
    
    
    
}
