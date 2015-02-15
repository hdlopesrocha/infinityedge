package pt.hidrogine.infinityedge.scene;


import android.opengl.GLES20;

import javax.microedition.khronos.opengles.GL10;

import hidrogine.math.Matrix;
import hidrogine.math.Vector3;
import pt.hidrogine.infinityedge.activity.Renderer;
import pt.hidrogine.infinityedge.dto.Asteroid;
import pt.hidrogine.infinityedge.dto.Object3D;
import pt.hidrogine.infinityedge.util.ShaderProgram;

public class Background extends Scene {

    private float angle = 0;


    public Background(){
        new Object3D(new Vector3(0,0,0), Renderer.fighter).insert(space);
        new Asteroid(new Vector3(3,0,0), Renderer.asteroid1).insert(space);
        new Asteroid(new Vector3(-3,0,0), Renderer.asteroid2).insert(space);
        new Asteroid(new Vector3(0,0,3), Renderer.asteroid3).insert(space);
        new Asteroid(new Vector3(0,0,-3), Renderer.asteroid4).insert(space);
    }

    @Override
    public void update(float delta_t){
        angle += delta_t;
    }

    @Override
    public void draw(final ShaderProgram shader){
        shader.applyCamera(Renderer.camera,new Matrix().identity());
        Renderer.camera.lookAt(new Vector3( (float) (4 * Math.sin(angle)), 1, (float) (4 * Math.cos(angle))),new Vector3(), new Vector3(0, 1, 0));
        drawSky(shader);
        super.draw(shader);
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
