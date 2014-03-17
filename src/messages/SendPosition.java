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
public class SendPosition extends AbstractMessage{

    private Double x, y;

    public SendPosition() {
    }
    
    
    public SendPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    
}
