/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import messages.MovePlayerMessage;
import messages.RequestPosition;
import messages.SendPosition;
import model.Player;

public class ServerListener implements MessageListener<HostedConnection> {
 
    public void messageReceived(HostedConnection source, Message message) {
        if (message instanceof MovePlayerMessage) {
            // do something with the message
            MovePlayerMessage moveMessage = (MovePlayerMessage) message;
            System.out.println("Server received '" + moveMessage.getDirection() + "' from client #" + source.getId());
            
            
            Player p = (Player)source.getAttribute("player");
            if("up".equals(moveMessage.getDirection())){
                p.setY(p.getY()+.05);
                if(p.getY() > 360){
                    p.setY(p.getY()-360);
                }                
            }
            if("down".equals(moveMessage.getDirection())){
                p.setY(p.getY()-.05);
                if(p.getY() < 0){
                    p.setY(p.getY()+360);
                }
            }
            if("left".equals(moveMessage.getDirection())){
                p.setX(p.getX()-.05);
                if(p.getX() < 0){
                    p.setX(p.getX()+360);
                }
            }
            if("right".equals(moveMessage.getDirection())){
                p.setX(p.getX()+.05);
                 if(p.getX() > 360){
                    p.setX(p.getX()-360);
                }     
            }
            source.send(new SendPosition(p.getX(), p.getY()));
        }else if(message instanceof RequestPosition){
            Player p = (Player)source.getAttribute("player");
            source.send(new SendPosition(p.getX(), p.getY()));
        }
    }
}