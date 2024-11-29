
package main;

import java.awt.event.*;

public class KeyHandler implements KeyListener {
    public boolean keyDown, upActive, downActive, leftActive, rightActive, upLeftPressed, upRightPressed;
    public boolean aActive, bActive, cActive, xActive, yActive, zActive; // input

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();
        keyDown = true;

        // movement
        if (code == KeyEvent.VK_W)
            upActive = true;
        if (code == KeyEvent.VK_S)
            downActive = true;
        if (code == KeyEvent.VK_A)
            leftActive = true;
        if (code == KeyEvent.VK_D)
            rightActive = true;

        // input
        if (code == KeyEvent.VK_J)
            aActive = true;
        if (code == KeyEvent.VK_I)
            bActive = true;
        if (code == KeyEvent.VK_K)
            cActive = true;
        if (code == KeyEvent.VK_O)
            xActive = true;
        if (code == KeyEvent.VK_L)
            yActive = true;
        if (code == KeyEvent.VK_P)
            zActive = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();

        // movement
        if (code == KeyEvent.VK_W)
            upActive = false;
        if (code == KeyEvent.VK_S)
            downActive = false;
        if (code == KeyEvent.VK_A)
            leftActive = false;
        if (code == KeyEvent.VK_D)
            rightActive = false;

        // input
        if (code == KeyEvent.VK_J)
            aActive = false;
        if (code == KeyEvent.VK_I)
            bActive = false;
        if (code == KeyEvent.VK_K)
            cActive = false;
        if (code == KeyEvent.VK_O)
            xActive = false;
        if (code == KeyEvent.VK_L)
            yActive = false;
        if (code == KeyEvent.VK_P)
            zActive = false;

    }
}
