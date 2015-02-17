package pt.hidrogine.infinityedge.scene;

import android.opengl.GLES20;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Stack;

import javax.microedition.khronos.opengles.GL10;

import hidrogine.math.IBoundingSphere;
import hidrogine.math.Space;
import hidrogine.math.VisibleObjectHandler;
import pt.hidrogine.infinityedge.activity.Renderer;
import pt.hidrogine.infinityedge.dto.Asteroid;
import pt.hidrogine.infinityedge.dto.Bullet;
import pt.hidrogine.infinityedge.dto.BillBoard;
import pt.hidrogine.infinityedge.dto.Object3D;
import pt.hidrogine.infinityedge.model.Model3D;
import pt.hidrogine.infinityedge.util.ShaderProgram;

/**
 * Created by Henrique on 12/02/2015.
 */
public abstract class Scene {
    protected Space space = new Space();


    public abstract void update(float delta_t);
    public void draw(final ShaderProgram shader){
        final LinkedList<Object3D> alphaObjects = new LinkedList<Object3D>();
        final LinkedList<Object3D> simpleObjects = new LinkedList<Object3D>();



        /* DRAW OBJECTS */
        space.handleVisibleObjects(Renderer.camera.getBoundingFrustum(), new VisibleObjectHandler() {
            @Override
            public void onObjectVisible(Object o) {
                Object3D obj = (Object3D) o;

                if(obj instanceof BillBoard){
                    alphaObjects.push(obj);
                }
                else {
                    simpleObjects.push(obj);

                }
            }
        });

        Comparator<Object3D> com = new Comparator<Object3D>() {
            @Override
            public int compare(Object3D lhs, Object3D rhs) {
                float ld = (float)lhs.getPosition().distanceSquared(Renderer.camera.getPosition());
                float rd = (float)rhs.getPosition().distanceSquared(Renderer.camera.getPosition());

                return ld > rd ? -1 : ld < rd ? 1 : 0;
            }
        };

        Collections.sort(alphaObjects,com);
        shader.disableLight();
        GLES20.glDisable(GL10.GL_DEPTH_TEST);
        GLES20.glDisable(GL10.GL_CULL_FACE);
        for (Object3D obj : alphaObjects) {
            Model3D mod = (Model3D) obj.getModel();
            if(obj instanceof BillBoard){
                obj.getRotation().set(Renderer.camera.getRotation()).conjugate();
            }

            shader.applyCamera(Renderer.camera, obj.getModelMatrix());
            mod.draw(shader, Renderer.camera);
        }
        shader.enableLight();
        GLES20.glEnable(GL10.GL_CULL_FACE);
        GLES20.glEnable(GL10.GL_DEPTH_TEST);







        for (Object3D obj : simpleObjects) {
            if(obj instanceof Bullet) {
                shader.disableLight();
            }

            Model3D mod = (Model3D) obj.getModel();
            shader.applyCamera(Renderer.camera, obj.getModelMatrix());
            mod.draw(shader, Renderer.camera);
            if(obj instanceof Bullet) {
                shader.enableLight();
            }
        }



    }
    public abstract void end();
}
