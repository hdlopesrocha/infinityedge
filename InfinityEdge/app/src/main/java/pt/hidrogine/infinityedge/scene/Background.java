package pt.hidrogine.infinityedge.scene;


import android.opengl.GLES20;

import javax.microedition.khronos.opengles.GL10;

import hidrogine.math.BoundingFrustum;
import hidrogine.math.Camera;
import hidrogine.math.IBoundingSphere;
import hidrogine.math.Matrix;
import hidrogine.math.Space;
import hidrogine.math.Vector3;
import hidrogine.math.VisibleObjectHandler;
import pt.hidrogine.infinityedge.activity.Renderer;
import pt.hidrogine.infinityedge.dto.Object3D;
import pt.hidrogine.infinityedge.model.Model3D;
import pt.hidrogine.infinityedge.util.ShaderProgram;

public class Background implements Scene {

    private Space space;
    private float angle = 0;


    public Background(){
        space = new Space();
        new Object3D(new Vector3(0,0,0), Renderer.fighter).insert(space);
        new Object3D(new Vector3(3,0,0), Renderer.asteroid1).insert(space);
        new Object3D(new Vector3(-3,0,0), Renderer.asteroid2).insert(space);
        new Object3D(new Vector3(0,0,3), Renderer.asteroid3).insert(space);
        new Object3D(new Vector3(0,0,-3), Renderer.asteroid4).insert(space);
        Renderer.camera.lookAt(4, 1, 4, 0, 0, 0);

    }

    @Override
    public void update(float delta_t){
        angle += delta_t;
    }

    @Override
    public void draw(final ShaderProgram shader){
        shader.applyCamera(Renderer.camera,new Matrix().identity());
        Renderer.camera.lookAt((float) (4 * Math.sin(angle)), 1, (float) (4 * Math.cos(angle)),0, 0, 0);

        drawSky(shader);

        /* DRAW OBJECTS */
        space.handleVisibleObjects(Renderer.camera.getBoundingFrustum(), new VisibleObjectHandler() {
            @Override
            public void onObjectVisible(IBoundingSphere iBoundingSphere) {
                Object3D obj = (Object3D) iBoundingSphere;
                Model3D mod = (Model3D) obj.getModel();
                Matrix mat = new Matrix().createTranslation(obj.getPosition());
                shader.applyCamera(Renderer.camera, mat);
                mod.draw(shader, Renderer.camera);
            }
        });
    }

    @Override
    public void end() {

    }


    public void drawSky(ShaderProgram shader) {
        GLES20.glDisable(GL10.GL_CULL_FACE);
        shader.disableLight();
        Matrix mat = new Matrix().createTranslation(Renderer.camera.getPosition());
        shader.applyCamera(Renderer.camera, mat);
        Renderer.sky.draw(shader, Renderer.camera);
        shader.enableLight();
        GLES20.glEnable(GL10.GL_CULL_FACE);
    }


}
