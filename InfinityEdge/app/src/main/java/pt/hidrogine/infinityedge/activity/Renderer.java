package pt.hidrogine.infinityedge.activity;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import hidrogine.math.Camera;
import hidrogine.math.Matrix;
import hidrogine.math.Quaternion;
import pt.hidrogine.infinityedge.R;
import pt.hidrogine.infinityedge.model.AndroidModel3D;
import pt.hidrogine.infinityedge.model.LOD3D;
import pt.hidrogine.infinityedge.scene.Background;
import pt.hidrogine.infinityedge.scene.Scene;
import pt.hidrogine.infinityedge.util.ShaderProgram;
import pt.hidrogine.infinityedge.util.TextureLoader;


public class Renderer implements GLSurfaceView.Renderer {
    private ShaderProgram shader;
    public static Game game;
    public static AndroidModel3D fighter1;
    public static AndroidModel3D fighter2;
    public static LOD3D asteroid1;
    public static LOD3D asteroid2;
    public static LOD3D asteroid3;
    public static LOD3D asteroid4;
    public static AndroidModel3D flare, flare2;
    public static AndroidModel3D[] clouds = new AndroidModel3D[7];
    public static AndroidModel3D bullet;
    public static AndroidModel3D sky;

    long time;
    long oldTime;
    public static Scene currentScene;
    public static final Camera camera = new Camera(0.1f,256);
    public static float analogX=0,analogY=0, accel;
    public static boolean fire = false;

    public void init(Game activity) {
        game = activity;
        TextureLoader loader = new TextureLoader(activity);

        //Debug.startMethodTracing("myapp");
        fighter1 = new AndroidModel3D(activity,loader, R.raw.fighter1, 0.02f);
        fighter2 = new AndroidModel3D(activity,loader, R.raw.fighter2, 1.5f);

        bullet = new AndroidModel3D(activity,loader,R.raw.bullet,0.006f, new Quaternion().createFromYawPitchRoll(0,(float)(Math.PI/2),0));
        flare = new AndroidModel3D(activity,loader,R.raw.flare,1f);
        flare2 = new AndroidModel3D(activity,loader,R.raw.flare,0.5f);

        clouds[0] = new AndroidModel3D(activity,loader,R.raw.cloud1,100f);
        clouds[1] = new AndroidModel3D(activity,loader,R.raw.cloud2,100f);
        clouds[2] = new AndroidModel3D(activity,loader,R.raw.cloud3,100f);
        clouds[3] = new AndroidModel3D(activity,loader,R.raw.cloud4,100f);
        clouds[4] = new AndroidModel3D(activity,loader,R.raw.cloud5,100f);
        clouds[5] = new AndroidModel3D(activity,loader,R.raw.cloud6,100f);
        clouds[6] = new AndroidModel3D(activity,loader,R.raw.cloud7,100f);

        asteroid1 = new LOD3D(  new AndroidModel3D(activity,loader,R.raw.asteroid1a,1.6f),
                                new AndroidModel3D(activity,loader,R.raw.asteroid1b,1.6f),
                                new AndroidModel3D(activity,loader,R.raw.asteroid1c,1.6f),
                                new AndroidModel3D(activity,loader,R.raw.asteroid1d,1.6f));
        asteroid2 = new LOD3D(  new AndroidModel3D(activity,loader,R.raw.asteroid2a,2.8f),
                                new AndroidModel3D(activity,loader,R.raw.asteroid2b,2.8f),
                                new AndroidModel3D(activity,loader,R.raw.asteroid2c,2.8f),
                                new AndroidModel3D(activity,loader,R.raw.asteroid2d,2.8f));
        asteroid3 = new LOD3D(  new AndroidModel3D(activity,loader,R.raw.asteroid3a,3.2f),
                                new AndroidModel3D(activity,loader,R.raw.asteroid3b,3.2f),
                                new AndroidModel3D(activity,loader,R.raw.asteroid3c,3.2f),
                                new AndroidModel3D(activity,loader,R.raw.asteroid3d,3.2f));
        asteroid4 = new LOD3D(  new AndroidModel3D(activity,loader,R.raw.asteroid4a,0.032f),
                                new AndroidModel3D(activity,loader,R.raw.asteroid4b,0.032f),
                                new AndroidModel3D(activity,loader,R.raw.asteroid4c,0.032f),
                                new AndroidModel3D(activity,loader,R.raw.asteroid4d,0.032f));

        sky = new AndroidModel3D(activity, loader,R.raw.sky1, 1f);

        //Debug.stopMethodTracing();

        currentScene = new Background();
        time = getTime();
        oldTime = time;
        activity.remove();
        activity.replace(new Home());
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
        GLES20.glViewport(0, 0, width, height);
        camera.update(width,height);
        camera.update();
    }


    public long getTime() {
        return System.nanoTime() / 1000000;
    }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        time = getTime();
        currentScene.update((time-oldTime)/1000f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        camera.update();
        currentScene.draw(shader);
        oldTime = time;
    }


}
