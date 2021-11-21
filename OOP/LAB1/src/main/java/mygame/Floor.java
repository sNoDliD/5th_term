package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;


public final class Floor {
    private Material material;
    private static final Box FLOOR;

    static {
        FLOOR = new Box(15f, 0.1f, 10f);
        FLOOR.scaleTextureCoordinates(new Vector2f(3, 6));
    }

    Floor(AssetManager assetManager, Node rootNode, BulletAppState bulletAppState) {
        initMaterials(assetManager);
        initPhysics(rootNode, bulletAppState);
    }

    private void initMaterials(AssetManager assetManager) {
        material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey key = new TextureKey("Textures/floor.jpg");
        key.setGenerateMips(true);
        Texture texture = assetManager.loadTexture(key);
        texture.setWrap(Texture.WrapMode.Repeat);
        material.setTexture("ColorMap", texture);
    }

    private void initPhysics(Node rootNode, BulletAppState bulletAppState) {
        Geometry floorGeometry = new Geometry("Floor", FLOOR);
        floorGeometry.setMaterial(material);
        floorGeometry.setLocalTranslation(0, -0.1f, 0);
        rootNode.attachChild(floorGeometry);

        RigidBodyControl floorRigidBodyControl = new RigidBodyControl(0.0f);
        floorGeometry.addControl(floorRigidBodyControl);
        bulletAppState.getPhysicsSpace().add(floorRigidBodyControl);
    }

}
