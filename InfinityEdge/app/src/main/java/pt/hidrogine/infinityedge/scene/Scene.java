package pt.hidrogine.infinityedge.scene;

import android.opengl.GLES20;

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
        /* DRAW OBJECTS */
        space.handleVisibleObjects(Renderer.camera.getBoundingFrustum(), new VisibleObjectHandler() {
            @Override
            public void onObjectVisible(Object iBoundingSphere) {
                Object3D obj = (Object3D) iBoundingSphere;

                if(obj instanceof Asteroid)
                {
                    obj.getRotation().multiply(((Asteroid) obj).rotation);
                }

                Model3D mod = (Model3D) obj.getModel();
                shader.applyCamera(Renderer.camera, obj.getModelMatrix());

                if(obj instanceof Bullet){
                    shader.disableLight();
                }
                if(obj instanceof BillBoard){
                    shader.disableLight();
                    obj.getRotation().set(Renderer.camera.getRotation()).conjugate();
                    GLES20.glDisable(GL10.GL_CULL_FACE);

                }


                mod.draw(shader, Renderer.camera);

                if(obj instanceof BillBoard) {
                    shader.enableLight();
                    GLES20.glEnable(GL10.GL_CULL_FACE);
                }

                if(obj instanceof Bullet){
                    shader.enableLight();
                }

            }
        });
    }
    public abstract void end();
}
