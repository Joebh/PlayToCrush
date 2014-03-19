package listener;

import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Server;
import model.Player;

/**
 *
 * @author Joebh
 */
public class PlayerConnectedListener implements ConnectionListener{

    public void connectionAdded(Server server, HostedConnection conn) {
        System.out.println("Connection loaded " + conn.getId());
        
        conn.setAttribute("player", new Player(2.0f, 2.0f));
    }

    public void connectionRemoved(Server server, HostedConnection conn) {
        
    }
    
}
