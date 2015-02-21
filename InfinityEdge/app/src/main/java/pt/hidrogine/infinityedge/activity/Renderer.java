package pt.hidrogine.infinityedge.activity;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import hidrogine.math.Camera;
import hidrogine.math.Matrix;
import hidrogine.math.Quaternion;
import pt.hidrogine.infinityedge.R;
import pt.hidrogine.infinityedge.model.LOD3D;
import pt.hidrogine.infinityedge.model.Model3D;
import pt.hidrogine.infinityedge.scene.Background;
import pt.hidrogine.infinityedge.scene.Scene;
import pt.hidrogine.infinityedge.util.ShaderProgram;
import pt.hidrogine.infinityedge.util.TextureLoader;


public class Renderer implements GLSurfaceView.Renderer {
    private ShaderProgram shader;
    public static Game game;
    public static Model3D fighter1;
    public static Model3D fighter2;
    public static LOD3D asteroid1;
    public static LOD3D asteroid2;
    public static LOD3D asteroid3;
    public static LOD3D asteroid4;
    public static Model3D flare, flare2;
    public static Model3D [] clouds = new Model3D[7];
    public static Model3D bullet;
    public static Model3D sky;

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
        fighter1 = new Model3D(activity,loader, R.raw.fighter1, 0.02f);
        fighter2 = new Model3D(activity,loader, R.raw.fighter2, 1.5f);

        bullet = new Model3D(activity,loader,R.raw.bullet,0.006f, new Quaternion().createFromYawPitchRoll(0,(float)(Math.PI/2),0));
        flare = new Model3D(activity,loader,R.raw.flare,1f);
        flare2 = new Model3D(activity,loader,R.raw.flare,0.5f);

        clouds[0] = new Model3D(activity,loader,R.raw.cloud1,50f);
        clouds[1] = new Model3D(activity,loader,R.raw.cloud2,50f);
        clouds[2] = new Model3D(activity,loader,R.raw.cloud3,50f);
        clouds[3] = new Model3D(activity,loader,R.raw.cloud4,50f);
        clouds[4] = new Model3D(activity,loader,R.raw.cloud5,50f);
        clouds[5] = new Model3D(activity,loader,R.raw.cloud6,50f);
        clouds[6] = new Model3D(activity,loader,R.raw.cloud7,50f);

        asteroid1 = new LOD3D(  new Model3D(activity,loader,R.raw.asteroid1a,0.8f),
                                new Model3D(activity,loader,R.raw.asteroid1b,0.8f),
                                new Model3D(activity,loader,R.raw.asteroid1c,0.8f),
                                new Model3D(activity,loader,R.raw.asteroid1d,0.8f));
        asteroid2 = new LOD3D(  new Model3D(activity,loader,R.raw.asteroid2a,1.4f),
                                new Model3D(activity,loader,R.raw.asteroid2b,1.4f),
                                new Model3D(activity,loader,R.raw.asteroid2c,1.4f),
                                new Model3D(activity,loader,R.raw.asteroid2d,1.4f));
        asteroid3 = new LOD3D(  new Model3D(activity,loader,R.raw.asteroid3a,1.6f),
                                new Model3D(activity,loader,R.raw.asteroid3b,1.6f),
                                new Model3D(activity,loader,R.raw.asteroid3c,1.6f),
                                new Model3D(activity,loader,R.raw.asteroid3d,1.6f));
        asteroid4 = new LOD3D(  new Model3D(activity,loader,R.raw.asteroid4a,0.016f),
                                new Model3D(activity,loader,R.raw.asteroid4b,0.016f),
                                new Model3D(activity,loader,R.raw.asteroid4c,0.016f),
                                new Model3D(activity,loader,R.raw.asteroid4d,0.016f));

        sky = new Model3D(activity, loader,R.raw.sky1, 1f);

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
