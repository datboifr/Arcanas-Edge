
package main;

import java.awt.event.*;

public class KeyHandler implements KeyListener {
    public boolean pActive, keyDown, upPressed, downPressed, leftPressed, rightPressed, upLeftPressed, upRightPressed,IPressed;

    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();
        keyDown = true;

        if (code == KeyEvent.VK_W)
            upPressed = true;
        if (code == KeyEvent.VK_S)
            downPressed = true;
        if (code == KeyEvent.VK_A)
            leftPressed = true;
        if (code == KeyEvent.VK_D)
            rightPressed = true;

        // SWITCH
        if (code == KeyEvent.VK_P)
            pActive = pActive ? false : true;

        if (code == KeyEvent.VK_I)
            IPressed = true;

        
    }

    @Override
    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W)
            upPressed = false;
        if (code == KeyEvent.VK_S)
            downPressed = false;
        if (code == KeyEvent.VK_A)
            leftPressed = false;
        if (code == KeyEvent.VK_D)
            rightPressed = false;
        if (code == KeyEvent.VK_I)
        IPressed = false;


    }
}
