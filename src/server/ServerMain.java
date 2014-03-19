package server;

import client.ClientMain;
import com.jme3.app.SimpleApplication;
import com.jme3.network.HostedConnection;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.network.serializing.Serializer;
import com.jme3.renderer.RenderManager;
import com.jme3.system.JmeContext;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import listener.PlayerConnectedListener;
import messages.MovePlayerMessage;
import messages.RequestPosition;
import messages.SendPosition;

/**
 * test
 *
 * @author normenhansen
 */
public class ServerMain extends SimpleApplication {

    private static Server server;

    public static void main(String[] args) {
        ServerMain app = new ServerMain();
        app.start(JmeContext.Type.Headless); // headless type for servers!
    }

    @Override
    public void destroy() {
        server.close();
        super.destroy();
    }

    @Override
    public void simpleInitApp() {
        try {            
            server = Network.createServer(6143);
            
            Serializer.registerClass(MovePlayerMessage.class);
            Serializer.registerClass(SendPosition.class);
            Serializer.registerClass(RequestPosition.class);
            server.addMessageListener(new ServerListener(), MovePlayerMessage.class);
            server.addMessageListener(new ServerListener(), RequestPosition.class);
            server.addConnectionListener(new PlayerConnectedListener());
            
            server.start();
        } catch (IOException ex) {
            Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void simpleUpdate(float tpf) {
        
    }

}
