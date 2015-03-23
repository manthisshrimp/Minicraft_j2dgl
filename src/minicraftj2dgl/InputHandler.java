package minicraftj2dgl;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class InputHandler implements KeyListener {

    public List<Key> keys = new ArrayList<>();

    public Key up = new Key();
    public Key down = new Key();
    public Key left = new Key();
    public Key right = new Key();
    public Key attack = new Key();
    public Key menu = new Key();
    public Key mainMenu = new Key();
    public Key quickSave = new Key();
    public Key quickLoad = new Key();

    public void releaseAll() {
        keys.stream().forEach((key) -> {
            key.down = false;
        });
    }

    public void tick() {
        keys.stream().forEach((key) -> {
            key.tick();
        });
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        toggle(ke, true);
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        toggle(ke, false);
    }

    private void toggle(KeyEvent ke, boolean pressed) {
        if (ke.getKeyCode() == KeyEvent.VK_NUMPAD8) {
            up.toggle(pressed);
        } else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD2) {
            down.toggle(pressed);
        } else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD4) {
            left.toggle(pressed);
        } else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD6) {
            right.toggle(pressed);
        } else if (ke.getKeyCode() == KeyEvent.VK_W) {
            up.toggle(pressed);
        } else if (ke.getKeyCode() == KeyEvent.VK_S) {
            down.toggle(pressed);
        } else if (ke.getKeyCode() == KeyEvent.VK_A) {
            left.toggle(pressed);
        } else if (ke.getKeyCode() == KeyEvent.VK_D) {
            right.toggle(pressed);
        } else if (ke.getKeyCode() == KeyEvent.VK_UP) {
            up.toggle(pressed);
        } else if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
            down.toggle(pressed);
        } else if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
            left.toggle(pressed);
        } else if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
            right.toggle(pressed);
        } else if (ke.getKeyCode() == KeyEvent.VK_TAB) {
            menu.toggle(pressed);
        } else if (ke.getKeyCode() == KeyEvent.VK_ALT) {
            menu.toggle(pressed);
        } else if (ke.getKeyCode() == KeyEvent.VK_ALT_GRAPH) {
            menu.toggle(pressed);
        } else if (ke.getKeyCode() == KeyEvent.VK_SPACE) {
            attack.toggle(pressed);
        } else if (ke.getKeyCode() == KeyEvent.VK_CONTROL) {
            attack.toggle(pressed);
        } else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD0) {
            attack.toggle(pressed);
        } else if (ke.getKeyCode() == KeyEvent.VK_INSERT) {
            attack.toggle(pressed);
        } else if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
            menu.toggle(pressed);
        } else if (ke.getKeyCode() == KeyEvent.VK_X) {
            menu.toggle(pressed);
        } else if (ke.getKeyCode() == KeyEvent.VK_C) {
            attack.toggle(pressed);
        } else if (ke.getKeyCode() == KeyEvent.VK_F5) {
            quickSave.toggle(pressed);
        } else if (ke.getKeyCode() == KeyEvent.VK_F9) {
            quickLoad.toggle(pressed);
        } else if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
            mainMenu.toggle(pressed);
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    public class Key {

        public int presses, absorbs;
        public boolean down, clicked;

        public Key() {
            keys.add(this);
        }

        public void toggle(boolean pressed) {
            if (pressed != down) {
                down = pressed;
            }
            if (pressed) {
                presses++;
            }
        }

        public void tick() {
            if (absorbs < presses) {
                absorbs++;
                clicked = true;
            } else {
                clicked = false;
            }
        }
    }
}