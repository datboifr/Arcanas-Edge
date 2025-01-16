
package main;

import java.awt.event.*;

public class KeyHandler implements KeyListener {
    public boolean keyDown, upActive, downActive, leftActive, rightActive, upLeftPressed, upRightPressed;
    public boolean aActive, bActive, cActive, xActive, yActive, zActive; // input

    public boolean cheatActive;

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
        if (code == KeyEvent.VK_U)
            aActive = true;
        if (code == KeyEvent.VK_J)
            bActive = true;
        if (code == KeyEvent.VK_I)
            cActive = true;
        if (code == KeyEvent.VK_K)
            xActive = true;
        if (code == KeyEvent.VK_O)
            yActive = true;
        if (code == KeyEvent.VK_L)
            zActive = true;

        if (code == KeyEvent.VK_C)
            if (cheatActive)
                cheatActive = false;
            else
                cheatActive = true;
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
        if (code == KeyEvent.VK_U)
            aActive = false;
        if (code == KeyEvent.VK_J)
            bActive = false;
        if (code == KeyEvent.VK_I)
            cActive = false;
        if (code == KeyEvent.VK_K)
            xActive = false;
        if (code == KeyEvent.VK_O)
            yActive = false;
        if (code == KeyEvent.VK_L)
            zActive = false;
    }
}
