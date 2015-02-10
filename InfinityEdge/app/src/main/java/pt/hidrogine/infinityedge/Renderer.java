package pt.hidrogine.infinityedge;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import hidrogine.math.Camera;
import hidrogine.math.Matrix;
import pt.hidrogine.infinityedge.dto.AsteroidFourDto;
import pt.hidrogine.infinityedge.dto.AsteroidOneDto;
import pt.hidrogine.infinityedge.dto.AsteroidThreeDto;
import pt.hidrogine.infinityedge.dto.AsteroidTwoDto;
import pt.hidrogine.infinityedge.dto.FighterDto;
import pt.hidrogine.infinityedge.model.Model3D;
import pt.hidrogine.infinityedge.util.ShaderProgram;


public class Renderer implements GLSurfaceView.Renderer {
    private ShaderProgram shader;
    private Camera camera = new Camera();
    private Game game;
    private Model3D fighter;
    private Model3D asteroid1;
    private Model3D asteroid2;
    private Model3D asteroid3;
    private Model3D asteroid4;
    private Model3D sky;


    FighterDto obj1 = new FighterDto(0, 0, 0);
    AsteroidOneDto ast1 = new AsteroidOneDto(0,3,0);
    AsteroidTwoDto ast2 = new AsteroidTwoDto(0,0,3);
    AsteroidThreeDto ast3 = new AsteroidThreeDto(0,-3,0);
    AsteroidFourDto ast4 = new AsteroidFourDto(0,0,-3);


    public void init(Game activity) {
        game = activity;
        fighter = new Model3D(activity, R.raw.fighter, 0.02f);

        asteroid1 = new Model3D(activity,R.raw.asteroid1,0.4f);
        asteroid2 = new Model3D(activity,R.raw.asteroid2,0.7f);
        asteroid3 = new Model3D(activity,R.raw.asteroid3,0.8f);
        asteroid4 = new Model3D(activity,R.raw.asteroid4,0.008f);

        sky = new Model3D(activity, R.raw.sky1, 1f);
        camera.lookAt(4,1,4,0,0,0);
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


    float angle = 0;

    @Override
    public void onDrawFrame(GL10 glUnused) {


        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);


        //BoundingFrustum frustum = new BoundingFrustum(new Matrix(camera.getViewMatrix()).multiply(camera.getProjectionMatrix()));

        shader.applyCamera(camera,new Matrix().identity());
        camera.lookAt((float) (4 * Math.sin(angle)), 1, (float) (4 * Math.cos(angle)),0, 0, 0);
        GLES20.glDisable(GL10.GL_CULL_FACE);
        shader.disableLight();
        drawSky();
        shader.enableLight();
        GLES20.glEnable(GL10.GL_CULL_FACE);


        /* DRAW OBJECTS */

        draw(obj1);
        draw(ast1);
        draw(ast2);
        draw(ast3);
        draw(ast4);
        angle += 0.01f;

    }

    public void draw(AsteroidThreeDto obj) {
        Matrix mat = new Matrix().createTranslation(obj.getX(),obj.getY(),obj.getZ());
        shader.applyCamera(camera,mat);
        asteroid3.draw(shader, camera);
    }

    public void draw(AsteroidTwoDto obj) {
        Matrix mat = new Matrix().createTranslation(obj.getX(),obj.getY(),obj.getZ());
        shader.applyCamera(camera,mat);
        asteroid2.draw(shader, camera);
    }


    public void draw(AsteroidOneDto obj) {
        Matrix mat = new Matrix().createTranslation(obj.getX(),obj.getY(),obj.getZ());
        shader.applyCamera(camera,mat);
        asteroid1.draw(shader, camera);
    }


    public void draw(AsteroidFourDto obj) {
        Matrix mat = new Matrix().createTranslation(obj.getX(),obj.getY(),obj.getZ());
        shader.applyCamera(camera,mat);
        asteroid4.draw(shader, camera);
    }


    public void draw(FighterDto obj) {
        Matrix mat = new Matrix().createTranslation(obj.getX(),obj.getY(),obj.getZ());
        shader.applyCamera(camera,mat);
        fighter.draw(shader, camera);
    }

    public void drawSky() {
        Matrix mat = new Matrix().createTranslation(camera.getPosition());
        shader.applyCamera(camera,mat);
        sky.draw(shader, camera);
    }

}
