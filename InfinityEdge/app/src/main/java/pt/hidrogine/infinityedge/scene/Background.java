package pt.hidrogine.infinityedge.scene;


import android.opengl.GLES20;

import javax.microedition.khronos.opengles.GL10;

import hidrogine.math.Matrix;
import hidrogine.math.Space;
import hidrogine.math.Vector3;
import pt.hidrogine.infinityedge.activity.Renderer;
import pt.hidrogine.infinityedge.object.Asteroid;
import pt.hidrogine.infinityedge.object.Object3D;
import pt.hidrogine.infinityedge.util.ShaderProgram;

public class Background extends Scene {


    public static Object3D selectedObject;
    private static Space space2;
    public Background(){
        space2 = space;
        selectedObject =  new Object3D(new Vector3(0,0,0), Renderer.fighter1);
        selectedObject.insert(space);
        new Asteroid(new Vector3(20,0,0), Renderer.asteroid1).insert(space);
        new Asteroid(new Vector3(-20,0,0), Renderer.asteroid2).insert(space);
        new Asteroid(new Vector3(0,0,20), Renderer.asteroid3).insert(space);
        new Asteroid(new Vector3(0,0,-20), Renderer.asteroid4).insert(space);
    }

    public static Space getSpace(){
        return space2;
    }


    @Override
    public void update(float delta_t){
        super.update(delta_t);
    //    System.out.println("MEM: " + getUsedMemorySize()/(1024*1024f));

    }

    @Override
    public void draw(final ShaderProgram shader){
        shader.applyCamera(Renderer.camera,new Matrix().identity());
        Renderer.camera.lookAt(new Vector3( (float) (4 * Math.sin(getTime())), 1, (float) (4 * Math.cos(getTime()))),new Vector3(), new Vector3(0, 1, 0));
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
        Renderer.sky.draw(shader, Renderer.camera,mat);
        shader.enableLight();
        GLES20.glEnable(GL10.GL_CULL_FACE);
    }


}
