package pt.hidrogine.infinityedge;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import hidrogine.math.BoundingFrustum;
import hidrogine.math.Matrix;
import pt.hidrogine.infinityedge.dto.FighterDto;
import pt.hidrogine.infinityedge.dto.SkyDto;
import pt.hidrogine.infinityedge.model.Model3D;
import pt.hidrogine.infinityedge.util.Camera;
import pt.hidrogine.infinityedge.util.ShaderProgram;


public class Renderer implements GLSurfaceView.Renderer {
    private ShaderProgram shader;
    private Camera camera = new Camera();
    private Game game;
    private Model3D fighter;
    private Model3D sky;


    FighterDto obj1 = new FighterDto(0l, 0f, 0f);
    SkyDto obj2 = new SkyDto(0l, 0f, 0f);

    public void init(Game activity) {
        game = activity;

        fighter = new Model3D(activity, R.raw.fighter, 0.02f);
        sky = new Model3D(activity, R.raw.sky1, 0.3f);

        camera.lookAt(16, 4, 0, 0, 0);

    }


    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        init(Game.activity);
        shader = new ShaderProgram(game);
        shader.applyCamera(camera);
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        shader.updateViewPort(width, height);
    }


    float angle = 0;

    @Override
    public void onDrawFrame(GL10 glUnused) {


        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        shader.setIdentity();

        //BoundingFrustum frustum = new BoundingFrustum(new Matrix(camera.getViewMatrix()).multiply(camera.getProjectionMatrix()));


        camera.lookAt(0, 0, 0, (float)(4*Math.sin(angle)),1,(float)(4*Math.cos(angle)));

        shader.updateCamera(camera);


        GLES20.glDisable(GL10.GL_CULL_FACE);
        shader.disableLight();
        draw(obj2);
        shader.enableLight();
        GLES20.glEnable(GL10.GL_CULL_FACE);

        draw(obj1);

        angle += 0.01f;

    }


    public void draw(FighterDto obj) {
        shader.pushMatrix();
        shader.translate(obj.getX(), obj.getY(), obj.getZ());
        fighter.draw(shader,camera);
        shader.popMatrix();
    }

    public void draw(SkyDto obj) {
        shader.pushMatrix();
        shader.translate(obj.getX(), obj.getY(), obj.getZ());
        sky.draw(shader,camera);
        shader.popMatrix();
    }

}
