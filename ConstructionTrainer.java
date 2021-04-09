package scripts;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.tribot.api.General;
import org.tribot.api2007.Login;
import org.tribot.api2007.Player;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Arguments;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.Starting;
import org.tribot.util.Util;
import scripts.construction.Construction;
import scripts.construction.house.BuyHouse;
import scripts.data.GameState;
import scripts.dax.api_lib.DaxWalker;
import scripts.framework.event.EventSet;
import scripts.gui.GUI;
import scripts.gui.GuiSettings;
import scripts.paint.PaintInfo;
import scripts.paint.SimplePaint;
import scripts.utilities.FileUtilities;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

@ScriptManifest(
        category = "Construction",
        authors = "Botuser420",
        name = "Construction by Breaker")
public class ConstructionTrainer extends Script implements PaintInfo, Painting, Starting, Arguments {

    public static EventSet events = new EventSet();

    private GUI gui;
    private URL fxml;
    private String profileName;


    private final SimplePaint PAINT = new SimplePaint(this, SimplePaint.PaintLocations.TOP_MID_PLAY_SCREEN,
            new Color[]{new Color(255, 251, 255)}, "Trebuchet MS", new Color[]{new Color(50, 50, 50, 128)},
            new Color[]{new Color(50, 50, 50)}, 1, false, 5, 3, 0);

    public String[] getPaintInfo() {
        return new String[]{
                "Runtime: " + PAINT.getRuntimeString(),
                events.getString(),
                events.getStatus(),
        };
    }

    @SneakyThrows
    @Override
    public void run() {
        if (profileName == null)
            openGUI();
        else
            loadProfile();
        login();
        events.execute();
    }

    @Override
    public void onPaint(Graphics graphics) {
        PAINT.paint(graphics);
    }

    @Override
    public void onStart() {
        Walk.setKey();
        DaxWalker.setGlobalWalkingCondition(Walk.getDefaultCondition());
    }

    private boolean login() {
        while (Player.getRSPlayer() == null) {
            General.println("Logging in before calculating needed planks");
            Login.login();
            General.sleep(1000);
        }
        return true;
    }

    private void openGUI() {
        try {
            fxml = new URL("https://raw.githubusercontent.com/botuser420/tribot-resources/main/construction/ConstructionGUI.fxml");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        gui = new GUI(fxml, null, false);
        gui.show();
        while (gui.isOpen())
            sleep(500);
    }

    @Override
    public void passArguments(HashMap<String, String> hashMap) {
        String scriptSelect = hashMap.get("custom_input");
        String clientStarter = hashMap.get("autostart");
        String input = clientStarter != null ? clientStarter : scriptSelect;
        String[] settings = input.split(",");
        if (settings.length > 0) {
            for (String s : settings) {
                if (s.contains("settings:")) {
                    profileName = s.split(":")[1] != null ? s.split(":")[1] : null;
                }
            }
        }
    }

    private void loadProfile() throws IOException {
        General.println("Loading profile " + profileName);
        try {
            GuiSettings settings = new Gson().fromJson(new String(FileUtilities.loadFile(new File(Util.getWorkingDirectory().getAbsolutePath() + "\\BreakerScripts\\Construction\\" + profileName + ".json"))), GuiSettings.class);
            events.addAll(new Construction(Integer.parseInt(settings.getStopLevel()), settings.getTableUsed()), new BuyHouse());
        } catch (NumberFormatException exception) {
            General.println(exception);
        }
    }
}
