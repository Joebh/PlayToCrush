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

    private static Server myServer;

    public static void main(String[] args) {
        ServerMain app = new ServerMain();
        app.start(JmeContext.Type.Headless); // headless type for servers!
        
        ClientMain client = new ClientMain();
        client.start();
    }

    @Override
    public void destroy() {
        myServer.close();


        super.destroy();
    }

    @Override
    public void simpleInitApp() {
        try {            
            myServer = Network.createServer(6143);
            
            Serializer.registerClass(MovePlayerMessage.class);
            Serializer.registerClass(SendPosition.class);
            Serializer.registerClass(RequestPosition.class);
            myServer.addMessageListener(new ServerListener(), MovePlayerMessage.class);
            myServer.addMessageListener(new ServerListener(), RequestPosition.class);
            myServer.addConnectionListener(new PlayerConnectedListener());
            
            myServer.start();
        } catch (IOException ex) {
            Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void simpleUpdate(float tpf) {
        Collection<HostedConnection> connections = myServer.getConnections();

        for (HostedConnection connection : connections) {
            
        }
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
