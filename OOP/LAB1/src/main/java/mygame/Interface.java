package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.font.BitmapText;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Vector3f;


public class Interface extends SimpleApplication {
    private BulletAppState bulletAppState;
    private BallCreator creator;
    private final static String FONT = "Interface/Fonts/Default.fnt";


    @Override
    public void simpleInitApp() {
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        cam.setLocation(new Vector3f(0, 10f, 20f));
        cam.lookAt(new Vector3f(0, 0, -10), Vector3f.UNIT_Y);

        Floor floor = new Floor(assetManager, rootNode, bulletAppState);
        creator = new BallCreator(assetManager, rootNode, bulletAppState);

        inputManager.addMapping("create", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(actionListener, "create");
        inputManager.addMapping("move", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addListener(actionListener, "move");

        initCrossHairs();
    }


    private final ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("create") && !keyPressed) {
                creator.createBall(cam);
            }
            if (name.equals("move") && !keyPressed) {
                creator.move(cam);
            }
        }
    };


    private void initCrossHairs() {
        guiNode.detachAllChildren();
        guiFont = assetManager.loadFont(FONT);
        BitmapText ch = new BitmapText(guiFont);
        ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        ch.setText("+");
        ch.setLocalTranslation((settings.getWidth() - ch.getLineWidth()) / 2, (settings.getHeight() + ch.getLineHeight()) / 2, 0);
        guiNode.attachChild(ch);
    }

}
