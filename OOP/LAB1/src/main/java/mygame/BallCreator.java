package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;

import java.util.ArrayList;
import java.util.Random;


public class BallCreator {
    private final Node rootNode;
    private final BulletAppState bulletAppState;
    private Material material;
    private final ArrayList<RigidBodyControl> movableRigidBodyes = new ArrayList<>();
    private static final Sphere SPHERE;

    static {
        SPHERE = new Sphere(32, 32, 0.6f, true, false);
        SPHERE.setTextureMode(Sphere.TextureMode.Projected);
    }

    BallCreator(AssetManager assetManager, Node rootNode, BulletAppState bulletAppState) {
        this.rootNode = rootNode;
        this.bulletAppState = bulletAppState;


        initMaterials(assetManager);
        initBall(new Vector3f(-6, 0.4f, 1));
        initBall(new Vector3f(-7, 0.4f, 2));
        initBall(new Vector3f(-7, 0.4f, 0));
    }


    private void initMaterials(AssetManager assetManager) {
        material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey key = new TextureKey("Textures/ball.jpg");
        key.setGenerateMips(true);
        Texture texture = assetManager.loadTexture(key);
        material.setTexture("ColorMap", texture);
    }

    private RigidBodyControl initBall(Vector3f position) {
        Random rand = new Random();

        Geometry ballGeometry = new Geometry();
        ballGeometry.setMesh(SPHERE);
        ballGeometry.setMaterial(material);
        RigidBodyControl ballRigidBody = new RigidBodyControl(5 + rand.nextInt(4));
        ballGeometry.addControl(ballRigidBody);
        ballRigidBody.setPhysicsLocation(position);

        bulletAppState.getPhysicsSpace().add(ballRigidBody);
        rootNode.attachChild(ballGeometry);
        return ballRigidBody;
    }

    protected void createBall(Camera cam) {
        RigidBodyControl ballRigidBody = initBall(Physics.calcPosition(cam));
        movableRigidBodyes.add(ballRigidBody);
    }

    protected void move(Camera cam) {
        Random rand = new Random();
        for (RigidBodyControl rigidBody : movableRigidBodyes) {
            rigidBody.setLinearVelocity(Physics.calcDirection(cam, rigidBody).mult(6 + rand.nextInt(20)));
        }
    }
}


