package client;

import client.gui.Gui;
import client.input.AnalogInput;
import client.input.InputManagerFactory;
import model.Player;
import com.jme3.app.SimpleApplication;
import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.Message;
import com.jme3.network.Network;
import com.jme3.network.serializing.Serializer;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FogFilter;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.JmeContext;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
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

    private TerrainQuad terrain;
    Material mat_terrain;
    private Client myClient;
    private final Player myPlayer = new Player();

    public static void main(String[] args) {
        ClientMain app = new ClientMain();
        app.start(JmeContext.Type.Display); // standard display type
    }
    private Geometry playerBox;

    @Override
    public void destroy() {
        myClient.close();
        super.destroy();
    }

    @Override
    public void simpleInitApp() {
        try {
            Serializer.registerClass(MovePlayerMessage.class);
            Serializer.registerClass(SendPosition.class);
            Serializer.registerClass(RequestPosition.class);
            myClient = Network.connectToServer("localhost", 6143);

            InputManager input = this.getInputManager();
            InputManagerFactory.handleInput(input);

            input.addListener(new AnalogInput(myClient), new String[]{"up", "down", "left", "right"});

            myClient.addMessageListener(new ClientListener(this), SendPosition.class);
            myClient.addClientStateListener(this);

            Gui.createGui(this.assetManager, this.guiNode, this.guiFont);
            this.pauseOnFocus = false;


            //draw player
            Box b = new Box(1, 1, 1); // create cube shape
            playerBox = new Geometry("Player", b);  // create cube geometry from the shape
            Material mat = new Material(assetManager,
                    "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
            mat.setColor("Color", ColorRGBA.Blue);   // set color of material to blue
            playerBox.setMaterial(mat);
            rootNode.attachChild(playerBox);


            Box redBox = new Box(2, 1, 1); // create cube shape
            Geometry redGeo = new Geometry("RedBox", redBox);  // create cube geometry from the shape
            // create a simple material
            Material matRed = new Material(assetManager,
                    "Common/MatDefs/Misc/Unshaded.j3md");
            matRed.setColor("Color", ColorRGBA.Red);   // set color of material to blue
            redGeo.setMaterial(matRed);
            rootNode.attachChild(redGeo);

            viewPort.setBackgroundColor(ColorRGBA.Cyan);


            /**
             * 1. Create terrain material and load four textures into it.
             */
            mat_terrain = new Material(assetManager,
                    "Common/MatDefs/Terrain/Terrain.j3md");

            /**
             * 1.1) Add ALPHA map (for red-blue-green coded splat textures)
             */
            mat_terrain.setTexture("Alpha", assetManager.loadTexture(
                    "Textures/Terrain/splat/alphamap.png"));

            /**
             * 1.2) Add GRASS texture into the red layer (Tex1).
             */
            Texture grass = assetManager.loadTexture(
                    "Textures/Terrain/splat/grass.jpg");
            grass.setWrap(WrapMode.Repeat);
            mat_terrain.setTexture("Tex1", grass);
            mat_terrain.setFloat("Tex1Scale", 64f);

            /**
             * 1.3) Add DIRT texture into the green layer (Tex2)
             */
            Texture dirt = assetManager.loadTexture(
                    "Textures/Terrain/splat/dirt.jpg");
            dirt.setWrap(WrapMode.Repeat);
            mat_terrain.setTexture("Tex2", dirt);
            mat_terrain.setFloat("Tex2Scale", 32f);

            /**
             * 1.4) Add ROAD texture into the blue layer (Tex3)
             */
            Texture rock = assetManager.loadTexture(
                    "Textures/Terrain/splat/road.jpg");
            rock.setWrap(WrapMode.Repeat);
            mat_terrain.setTexture("Tex3", rock);
            mat_terrain.setFloat("Tex3Scale", 128f);

            /**
             * 2. Create the height map
             */
            AbstractHeightMap heightmap = null;
            Texture heightMapImage = assetManager.loadTexture(
                    "Textures/Terrain/splat/mountains512.png");
            heightmap = new ImageBasedHeightMap(heightMapImage.getImage());
            heightmap.load();

            /**
             * 3. We have prepared material and heightmap. Now we create the
             * actual terrain: 3.1) Create a TerrainQuad and name it "my
             * terrain". 3.2) A good value for terrain tiles is 64x64 -- so we
             * supply 64+1=65. 3.3) We prepared a heightmap of size 512x512 --
             * so we supply 512+1=513. 3.4) As LOD step scale we supply
             * Vector3f(1,1,1). 3.5) We supply the prepared heightmap itself.
             */
            int patchSize = 65;
            terrain = new TerrainQuad("my terrain", patchSize, 513, heightmap.getHeightMap());

            /**
             * 4. We give the terrain its material, position & scale it, and
             * attach it.
             */
            terrain.setMaterial(mat_terrain);
            terrain.setLocalTranslation(0, -100, 0);
            terrain.setLocalScale(2f, 1f, 2f);
            rootNode.attachChild(terrain);

            /**
             * 5. The LOD (level of detail) depends on were the camera is:
             */
            TerrainLodControl control = new TerrainLodControl(terrain, getCamera());
            terrain.addControl(control);


            myClient.start();

            // Disable the default flyby cam
            flyCam.setEnabled(false);

            // Enable a chase cam for this target (typically the player)            
            ChaseCamera chaseCam = new ChaseCamera(cam, playerBox, inputManager);
            chaseCam.setSmoothMotion(false);
        } catch (IOException ex) {
            Logger.getLogger(ClientMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void simpleUpdate(float tpf) {
        playerBox.setLocalTranslation(myPlayer.getX(), myPlayer.getY(), tpf);
    }

    @Override
    public void simpleRender(RenderManager rm) {
    }

    public Player getMyPlayer() {
        return myPlayer;
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
