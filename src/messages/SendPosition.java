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
public class SendPosition extends AbstractMessage {

    private float x, y;

    public SendPosition() {
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public SendPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
