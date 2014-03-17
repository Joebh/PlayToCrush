/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author Joebh
 */
@Serializable
public class RequestPosition  extends AbstractMessage{

    public RequestPosition() {
    }
    
}
