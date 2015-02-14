package pt.hidrogine.infinityedge.activity;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import hidrogine.math.Camera;
import hidrogine.math.Matrix;
import pt.hidrogine.infinityedge.R;
import pt.hidrogine.infinityedge.model.Model3D;
import pt.hidrogine.infinityedge.scene.Background;
import pt.hidrogine.infinityedge.scene.Demo;
import pt.hidrogine.infinityedge.scene.Scene;
import pt.hidrogine.infinityedge.util.ShaderProgram;


public class Renderer implements GLSurfaceView.Renderer {
    private ShaderProgram shader;
    private Game game;
    public static Model3D fighter;
    public static Model3D asteroid1;
    public static Model3D asteroid2;
    public static Model3D asteroid3;
    public static Model3D asteroid4;
    public static Model3D sky;

    long time;
    long oldTime;
    public static Scene currentScene;
    public static final Camera camera = new Camera();


    public void init(Game activity) {
        game = activity;
        fighter = new Model3D(activity, R.raw.fighter, 0.02f);

        asteroid1 = new Model3D(activity,R.raw.asteroid1,0.4f);
        asteroid2 = new Model3D(activity,R.raw.asteroid2,0.7f);
        asteroid3 = new Model3D(activity,R.raw.asteroid3,0.8f);
        asteroid4 = new Model3D(activity,R.raw.asteroid4,0.008f);

        sky = new Model3D(activity, R.raw.sky1, 1f);

        currentScene = new Background();
        time = getTime();
        oldTime = time;
    }


    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        init(Game.activity);
        shader = new ShaderProgram(game);
        shader.applyCamera(camera,new Matrix().identity());
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        shader.updateViewPort(width, height);
        camera.update(width,height);
    }


    public long getTime() {
        return System.nanoTime() / 1000000;
    }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        time = getTime();
        currentScene.update((time-oldTime)/1000f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        currentScene.draw(shader);
        oldTime = time;
    }


}
