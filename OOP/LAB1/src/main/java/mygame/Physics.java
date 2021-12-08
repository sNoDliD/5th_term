package mygame;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;


public class Physics {
    public Vector3f calcPosition(Camera cam) {
        Vector3f v = cam.getDirection();
        Vector3f p = cam.getLocation();

        return new Vector3f((v.x * (0.5f - p.y) / v.y) + p.x, 0.5f, (v.z * (0.5f - p.y) / v.y) + p.z);
    }

    public Vector3f calcDirection(Camera cam, RigidBodyControl rigidBody) {
        return calcPosition(cam).subtractLocal(rigidBody.getPhysicsLocation()).normalizeLocal();
    }
}
