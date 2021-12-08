package mygame;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PhysicsTest {

    @Mock
    private Camera camera;

    @Mock
    private RigidBodyControl ball_phys;

    private static final Vector3f cameraLocation = new Vector3f(0, 15f, 15f);

    private static final Vector3f cameraDirection = new Vector3f(-1, -7, -1);

    @Test
    public void testCalcPosition() {
        Vector3f expected = new Vector3f((cameraDirection.x * (0.5f - cameraLocation.y) / cameraDirection.y) + cameraLocation.x,
                0.5f,
                (cameraDirection.z * (0.5f - cameraLocation.y) / cameraDirection.y) + cameraLocation.z);

        when(camera.getLocation()).thenReturn(cameraLocation);
        when(camera.getDirection()).thenReturn(cameraDirection);

        Physics physics = new Physics();
        Vector3f actual = physics.calcPosition(camera);

        assertEquals(expected, actual);
    }


    @Test
    public void testCalcDirection() {
        when(camera.getLocation()).thenReturn(cameraLocation);
        when(camera.getDirection()).thenReturn(cameraDirection);

        when(ball_phys.getPhysicsLocation()).thenReturn(new Vector3f());

        Vector3f expected = new Vector3f((cameraDirection.x * (0.5f - cameraLocation.y) / cameraDirection.y) + cameraLocation.x - ball_phys.getPhysicsLocation().x,
                0.5f,
                (cameraDirection.z * (0.5f - cameraLocation.y) / cameraDirection.y) + cameraLocation.z - ball_phys.getPhysicsLocation().z).normalizeLocal();

        Physics physics = new Physics();
        Vector3f actual = physics.calcDirection(camera, ball_phys);

        assertEquals(expected, actual);
    }
}