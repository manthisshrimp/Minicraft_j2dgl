package minicraftj2dgl.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import minicraftj2dgl.Game;

public class SaveManager {

    private final Game game;

    public SaveManager(Game game) {
        this.game = game;
    }

    public String[] getSaveGameNames() {
        ArrayList<String> returnValues = new ArrayList<>();
        File folder = new File("saves");
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isFile()) {
                String fileName = fileEntry.getName();
                if (fileName.endsWith(".sav")) {
                    returnValues.add(fileName);
                }
            }
        }
        return returnValues.toArray(new String[returnValues.size()]);
    }

    public GameState loadSavedGame(String saveFileName) {
        try (ObjectInputStream objectStream = new ObjectInputStream(new FileInputStream("saves/" + saveFileName))) {
            GameState gameState = (GameState) objectStream.readObject();
            return gameState;
        } catch (IOException | ClassNotFoundException ex) {
            return null;
        }
    }

    public boolean saveGame(String saveFileName) {
        GameState gameState = game.getGameState();
//        new Thread(() -> {
        try {
            try (ObjectOutputStream objectStream = new ObjectOutputStream(
                    new FileOutputStream("saves/" + saveFileName + ".sav"))) {
                objectStream.writeObject(gameState);
                objectStream.flush();
                game.config.putConfig("lastSave", saveFileName + ".sav");
            }
            return true;
        } catch (IOException ex) {
            return false;
        }
//        }).start();
    }
}
