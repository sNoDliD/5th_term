package mygame;

import com.jme3.system.AppSettings;

public class Main {
    public static void main(String[] args) {
        Interface app = new Interface();
        app.setShowSettings(false);

        AppSettings settings = new AppSettings(true);
        settings.put("Width", 1280);
        settings.put("Height", 720);
        settings.put("Title", "Lab1");
        app.setSettings(settings);

        app.start();
    }
}


