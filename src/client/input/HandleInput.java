/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.input;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;

/**
 *
 * @author Joebh
 */
public class HandleInput {

    public HandleInput(InputManager inputManager) {
        inputManager.addMapping("left", new KeyTrigger(KeyInput.KEY_A),
                new KeyTrigger(KeyInput.KEY_LEFT)); // A and left arrow
        inputManager.addMapping("right", new KeyTrigger(KeyInput.KEY_D),
                new KeyTrigger(KeyInput.KEY_RIGHT)); // D and right arrow
        inputManager.addMapping("up", new KeyTrigger(KeyInput.KEY_W),
                new KeyTrigger(KeyInput.KEY_UP)); // A and left arrow
        inputManager.addMapping("down", new KeyTrigger(KeyInput.KEY_S),
                new KeyTrigger(KeyInput.KEY_DOWN)); // D and right arrow
        
    }
}
