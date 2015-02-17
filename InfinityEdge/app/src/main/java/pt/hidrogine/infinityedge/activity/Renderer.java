package pt.hidrogine.infinityedge.activity;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Debug;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import hidrogine.math.Camera;
import hidrogine.math.Matrix;
import hidrogine.math.Quaternion;
import hidrogine.math.Vector3;
import pt.hidrogine.infinityedge.R;
import pt.hidrogine.infinityedge.dto.Asteroid;
import pt.hidrogine.infinityedge.model.Model3D;
import pt.hidrogine.infinityedge.scene.Background;
import pt.hidrogine.infinityedge.scene.Scene;
import pt.hidrogine.infinityedge.util.ShaderProgram;


public class Renderer implements GLSurfaceView.Renderer {
    private ShaderProgram shader;
    public static Game game;
    public static Model3D fighter;
    public static Model3D asteroid1;
    public static Model3D asteroid2;
    public static Model3D asteroid3;
    public static Model3D asteroid4;
    public static Model3D flare;
    public static Model3D smoke1;
    public static Model3D bullet;
    public static Model3D sky;

    long time;
    long oldTime;
    public static Scene currentScene;
    public static final Camera camera = new Camera();
    public static float analogX=0,analogY=0, accel;
    public static boolean fire = false;

    public void init(Game activity) {
        game = activity;

        //Debug.startMethodTracing("myapp");
        fighter = new Model3D(activity, R.raw.fighter, 0.02f);
        bullet = new Model3D(activity,R.raw.bullet,0.006f, new Quaternion().createFromYawPitchRoll(0,(float)(Math.PI/2),0));
        flare = new Model3D(activity,R.raw.flare,1f);
        smoke1 = new Model3D(activity,R.raw.smoke1,50f);

        asteroid1 = new Model3D(activity,R.raw.asteroid1,0.8f);
        asteroid2 = new Model3D(activity,R.raw.asteroid2,1.4f);
        asteroid3 = new Model3D(activity,R.raw.asteroid3,1.6f);
        asteroid4 = new Model3D(activity,R.raw.asteroid4,0.016f);

        sky = new Model3D(activity, R.raw.sky1, 1f);

        //Debug.stopMethodTracing();

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
